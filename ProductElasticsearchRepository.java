package com.ecommerce.product.repository;

import com.ecommerce.product.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;

@Repository
public interface ProductElasticsearchRepository extends ElasticsearchRepository<Product, Long> {
    
    Page<Product> findByNameContainingOrDescriptionContaining(String name, String description, Pageable pageable);
    
    Page<Product> findByCategory(String category, Pageable pageable);
    
    Page<Product> findByBrand(String brand, Pageable pageable);
    
    Page<Product> findByTagsContaining(String tag, Pageable pageable);
    
    @Query("{\"bool\": {\"must\": [{\"range\": {\"price\": {\"gte\": ?0, \"lte\": ?1}}}]}}")
    Page<Product> findByPriceRange(BigDecimal minPrice, BigDecimal maxPrice, Pageable pageable);
    
    @Query("{\"bool\": {\"must\": [{\"match\": {\"category\": \"?0\"}}, {\"range\": {\"price\": {\"gte\": ?1, \"lte\": ?2}}}]}}")
    Page<Product> findByCategoryAndPriceRange(String category, BigDecimal minPrice, BigDecimal maxPrice, Pageable pageable);
    
    @Query("{\"multi_match\": {\"query\": \"?0\", \"fields\": [\"name^2\", \"description\", \"brand\", \"tags\"]}}")
    Page<Product> searchProducts(String query, Pageable pageable);
    
    @Query("{\"bool\": {\"must\": [{\"multi_match\": {\"query\": \"?0\", \"fields\": [\"name^2\", \"description\", \"brand\", \"tags\"]}}, {\"term\": {\"category\": \"?1\"}}]}}")
    Page<Product> searchProductsByCategory(String query, String category, Pageable pageable);
    
    Page<Product> findByFeaturedTrue(Pageable pageable);
    
    @Query("{\"bool\": {\"must\": [{\"range\": {\"rating\": {\"gte\": ?0}}}]}}")
    Page<Product> findByRatingGreaterThan(Double rating, Pageable pageable);
} 