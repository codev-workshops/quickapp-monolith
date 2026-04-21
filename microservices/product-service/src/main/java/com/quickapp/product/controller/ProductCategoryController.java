package com.quickapp.product.controller;

import com.quickapp.product.dto.ProductCategoryDTO;
import com.quickapp.product.service.ProductCategoryService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/product-categories")
public class ProductCategoryController {

    private final ProductCategoryService categoryService;

    public ProductCategoryController(ProductCategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping
    public ResponseEntity<List<ProductCategoryDTO>> getAllCategories() {
        return ResponseEntity.ok(categoryService.getAllCategories());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductCategoryDTO> getCategoryById(@PathVariable int id) {
        return ResponseEntity.ok(categoryService.getCategoryById(id));
    }

    @PostMapping
    public ResponseEntity<ProductCategoryDTO> createCategory(@RequestBody ProductCategoryDTO dto, Authentication auth) {
        return ResponseEntity.ok(categoryService.createCategory(dto, auth.getName()));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductCategoryDTO> updateCategory(@PathVariable int id, @RequestBody ProductCategoryDTO dto,
                                                              Authentication auth) {
        return ResponseEntity.ok(categoryService.updateCategory(id, dto, auth.getName()));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCategory(@PathVariable int id) {
        categoryService.deleteCategory(id);
        return ResponseEntity.noContent().build();
    }
}
