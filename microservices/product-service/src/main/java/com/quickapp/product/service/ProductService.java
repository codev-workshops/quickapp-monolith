package com.quickapp.product.service;

import com.quickapp.product.dto.ProductDTO;
import com.quickapp.product.model.Product;
import com.quickapp.product.model.ProductCategory;
import com.quickapp.product.repository.ProductCategoryRepository;
import com.quickapp.product.repository.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class ProductService {

    private final ProductRepository productRepository;
    private final ProductCategoryRepository categoryRepository;

    public ProductService(ProductRepository productRepository, ProductCategoryRepository categoryRepository) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
    }

    public List<ProductDTO> getAllProducts() {
        return productRepository.findAll().stream().map(this::toDTO).collect(Collectors.toList());
    }

    public ProductDTO getProductById(int id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));
        return toDTO(product);
    }

    public ProductDTO createProduct(ProductDTO dto, String userId) {
        Product product = new Product();
        product.setName(dto.getName());
        product.setDescription(dto.getDescription());
        product.setIcon(dto.getIcon());
        product.setBuyingPrice(dto.getBuyingPrice());
        product.setSellingPrice(dto.getSellingPrice());
        product.setUnitsInStock(dto.getUnitsInStock());
        product.setIsActive(dto.getIsActive());
        product.setIsDiscontinued(dto.getIsDiscontinued());
        if (dto.getProductCategoryId() > 0) {
            ProductCategory cat = categoryRepository.findById(dto.getProductCategoryId())
                    .orElseThrow(() -> new RuntimeException("Category not found"));
            product.setProductCategory(cat);
        }
        product.setCreatedBy(userId);
        product.setUpdatedBy(userId);
        product = productRepository.save(product);
        return toDTO(product);
    }

    public ProductDTO updateProduct(int id, ProductDTO dto, String userId) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));
        if (dto.getName() != null) product.setName(dto.getName());
        if (dto.getDescription() != null) product.setDescription(dto.getDescription());
        if (dto.getIcon() != null) product.setIcon(dto.getIcon());
        if (dto.getBuyingPrice() != null) product.setBuyingPrice(dto.getBuyingPrice());
        if (dto.getSellingPrice() != null) product.setSellingPrice(dto.getSellingPrice());
        product.setUnitsInStock(dto.getUnitsInStock());
        product.setIsActive(dto.getIsActive());
        product.setIsDiscontinued(dto.getIsDiscontinued());
        if (dto.getProductCategoryId() > 0) {
            ProductCategory cat = categoryRepository.findById(dto.getProductCategoryId())
                    .orElseThrow(() -> new RuntimeException("Category not found"));
            product.setProductCategory(cat);
        }
        product.setUpdatedBy(userId);
        product = productRepository.save(product);
        return toDTO(product);
    }

    public void deleteProduct(int id) {
        if (!productRepository.existsById(id)) throw new RuntimeException("Product not found");
        productRepository.deleteById(id);
    }

    private ProductDTO toDTO(Product product) {
        ProductDTO dto = new ProductDTO();
        dto.setId(product.getId());
        dto.setName(product.getName());
        dto.setDescription(product.getDescription());
        dto.setIcon(product.getIcon());
        dto.setBuyingPrice(product.getBuyingPrice());
        dto.setSellingPrice(product.getSellingPrice());
        dto.setUnitsInStock(product.getUnitsInStock());
        dto.setIsActive(product.getIsActive());
        dto.setIsDiscontinued(product.getIsDiscontinued());
        if (product.getProductCategory() != null) {
            dto.setProductCategoryId(product.getProductCategory().getId());
            dto.setProductCategoryName(product.getProductCategory().getName());
        }
        return dto;
    }
}
