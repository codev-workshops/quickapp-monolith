package com.quickapp.product.service;

import com.quickapp.product.dto.ProductCategoryDTO;
import com.quickapp.product.model.ProductCategory;
import com.quickapp.product.repository.ProductCategoryRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class ProductCategoryService {

    private final ProductCategoryRepository categoryRepository;

    public ProductCategoryService(ProductCategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public List<ProductCategoryDTO> getAllCategories() {
        return categoryRepository.findAll().stream().map(this::toDTO).collect(Collectors.toList());
    }

    public ProductCategoryDTO getCategoryById(int id) {
        ProductCategory cat = categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Category not found"));
        return toDTO(cat);
    }

    public ProductCategoryDTO createCategory(ProductCategoryDTO dto, String userId) {
        ProductCategory cat = new ProductCategory();
        cat.setName(dto.getName());
        cat.setDescription(dto.getDescription());
        cat.setIcon(dto.getIcon());
        cat.setCreatedBy(userId);
        cat.setUpdatedBy(userId);
        cat = categoryRepository.save(cat);
        return toDTO(cat);
    }

    public ProductCategoryDTO updateCategory(int id, ProductCategoryDTO dto, String userId) {
        ProductCategory cat = categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Category not found"));
        if (dto.getName() != null) cat.setName(dto.getName());
        if (dto.getDescription() != null) cat.setDescription(dto.getDescription());
        if (dto.getIcon() != null) cat.setIcon(dto.getIcon());
        cat.setUpdatedBy(userId);
        cat = categoryRepository.save(cat);
        return toDTO(cat);
    }

    public void deleteCategory(int id) {
        if (!categoryRepository.existsById(id)) throw new RuntimeException("Category not found");
        categoryRepository.deleteById(id);
    }

    private ProductCategoryDTO toDTO(ProductCategory cat) {
        ProductCategoryDTO dto = new ProductCategoryDTO();
        dto.setId(cat.getId());
        dto.setName(cat.getName());
        dto.setDescription(cat.getDescription());
        dto.setIcon(cat.getIcon());
        return dto;
    }
}
