package com.ecommerce.product.service;

import com.ecommerce.product.entity.Product;
import com.ecommerce.product.repository.ProductRepository;
import com.ecommerce.product.repository.ProductElasticsearchRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ProductElasticsearchRepository elasticsearchRepository;

    @Autowired
    private KafkaTemplate<String, Object> kafkaTemplate;

    @Cacheable(value = "products", key = "#id")
    public Optional<Product> getProductById(Long id) {
        return productRepository.findById(id);
    }

    @Cacheable(value = "productsByCategory", key = "#category + '_' + #pageable.pageNumber + '_' + #pageable.pageSize")
    public Page<Product> getProductsByCategory(String category, Pageable pageable) {
        return productRepository.findByCategory(category, pageable);
    }

    @Cacheable(value = "productsByBrand", key = "#brand + '_' + #pageable.pageNumber + '_' + #pageable.pageSize")
    public Page<Product> getProductsByBrand(String brand, Pageable pageable) {
        return productRepository.findByBrand(brand, pageable);
    }

    @Cacheable(value = "featuredProducts", key = "#pageable.pageNumber + '_' + #pageable.pageSize")
    public Page<Product> getFeaturedProducts(Pageable pageable) {
        return productRepository.findByFeatured(true, pageable);
    }

    public Page<Product> getProductsByPriceRange(BigDecimal minPrice, BigDecimal maxPrice, Pageable pageable) {
        return productRepository.findByPriceRange(minPrice, maxPrice, pageable);
    }

    public Page<Product> getProductsByCategoryAndPriceRange(String category, BigDecimal minPrice, BigDecimal maxPrice, Pageable pageable) {
        return productRepository.findByCategoryAndPriceRange(category, minPrice, maxPrice, pageable);
    }

    public Page<Product> getAllProducts(Pageable pageable) {
        return productRepository.findAll(pageable);
    }

    // Elasticsearch search methods
    public Page<Product> searchProducts(String query, Pageable pageable) {
        return elasticsearchRepository.searchProducts(query, pageable);
    }

    public Page<Product> searchProductsByCategory(String query, String category, Pageable pageable) {
        return elasticsearchRepository.searchProductsByCategory(query, category, pageable);
    }

    public Page<Product> searchProductsByTag(String tag, Pageable pageable) {
        return elasticsearchRepository.findByTagsContaining(tag, pageable);
    }

    public Page<Product> searchProductsByPriceRange(BigDecimal minPrice, BigDecimal maxPrice, Pageable pageable) {
        return elasticsearchRepository.findByPriceRange(minPrice, maxPrice, pageable);
    }

    @CacheEvict(value = {"products", "productsByCategory", "productsByBrand", "featuredProducts"}, allEntries = true)
    public Product createProduct(Product product) {
        Product savedProduct = productRepository.save(product);
        
        // Index in Elasticsearch
        elasticsearchRepository.save(savedProduct);
        
        // Publish product creation event
        publishProductEvent("product.created", savedProduct);
        
        return savedProduct;
    }

    @CacheEvict(value = {"products", "productsByCategory", "productsByBrand", "featuredProducts"}, allEntries = true)
    public Product updateProduct(Long id, Product productDetails) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        product.setName(productDetails.getName());
        product.setDescription(productDetails.getDescription());
        product.setPrice(productDetails.getPrice());
        product.setStockQuantity(productDetails.getStockQuantity());
        product.setCategory(productDetails.getCategory());
        product.setBrand(productDetails.getBrand());
        product.setTags(productDetails.getTags());
        product.setImageUrls(productDetails.getImageUrls());
        product.setWeight(productDetails.getWeight());
        product.setDimensions(productDetails.getDimensions());
        product.setFeatured(productDetails.getFeatured());

        Product updatedProduct = productRepository.save(product);
        
        // Update in Elasticsearch
        elasticsearchRepository.save(updatedProduct);
        
        // Publish product update event
        publishProductEvent("product.updated", updatedProduct);
        
        return updatedProduct;
    }

    @CacheEvict(value = {"products", "productsByCategory", "productsByBrand", "featuredProducts"}, allEntries = true)
    public void deleteProduct(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        product.setStatus(Product.ProductStatus.DISCONTINUED);
        productRepository.save(product);
        
        // Remove from Elasticsearch
        elasticsearchRepository.deleteById(id);
        
        // Publish product deletion event
        publishProductEvent("product.deleted", product);
    }

    @CacheEvict(value = "products", key = "#id")
    public Product updateStock(Long id, Integer quantity) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        product.setStockQuantity(quantity);
        if (quantity == 0) {
            product.setStatus(Product.ProductStatus.OUT_OF_STOCK);
        } else if (product.getStatus() == Product.ProductStatus.OUT_OF_STOCK) {
            product.setStatus(Product.ProductStatus.ACTIVE);
        }

        Product updatedProduct = productRepository.save(product);
        
        // Update in Elasticsearch
        elasticsearchRepository.save(updatedProduct);
        
        // Publish stock update event
        publishProductEvent("product.stock.updated", updatedProduct);
        
        return updatedProduct;
    }

    @CacheEvict(value = "products", key = "#id")
    public Product updateRating(Long id, Double rating, Integer reviewCount) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        product.setRating(rating);
        product.setReviewCount(reviewCount);

        Product updatedProduct = productRepository.save(product);
        
        // Update in Elasticsearch
        elasticsearchRepository.save(updatedProduct);
        
        return updatedProduct;
    }

    @Cacheable(value = "categories")
    public List<String> getAllCategories() {
        return productRepository.findAllActiveCategories();
    }

    @Cacheable(value = "brands")
    public List<String> getAllBrands() {
        return productRepository.findAllActiveBrands();
    }

    public List<Product> getLowStockProducts(Integer threshold) {
        return productRepository.findLowStockProducts(threshold);
    }

    private void publishProductEvent(String eventType, Product product) {
        ProductEvent event = new ProductEvent(
            eventType, 
            product.getId(), 
            product.getName(), 
            product.getCategory(), 
            product.getPrice(), 
            product.getStockQuantity()
        );
        kafkaTemplate.send("product-events", event);
    }

    // Inner class for product events
    public static class ProductEvent {
        private String eventType;
        private Long productId;
        private String productName;
        private String category;
        private BigDecimal price;
        private Integer stockQuantity;

        public ProductEvent(String eventType, Long productId, String productName, String category, BigDecimal price, Integer stockQuantity) {
            this.eventType = eventType;
            this.productId = productId;
            this.productName = productName;
            this.category = category;
            this.price = price;
            this.stockQuantity = stockQuantity;
        }

        // Getters and Setters
        public String getEventType() { return eventType; }
        public void setEventType(String eventType) { this.eventType = eventType; }

        public Long getProductId() { return productId; }
        public void setProductId(Long productId) { this.productId = productId; }

        public String getProductName() { return productName; }
        public void setProductName(String productName) { this.productName = productName; }

        public String getCategory() { return category; }
        public void setCategory(String category) { this.category = category; }

        public BigDecimal getPrice() { return price; }
        public void setPrice(BigDecimal price) { this.price = price; }

        public Integer getStockQuantity() { return stockQuantity; }
        public void setStockQuantity(Integer stockQuantity) { this.stockQuantity = stockQuantity; }
    }
} 