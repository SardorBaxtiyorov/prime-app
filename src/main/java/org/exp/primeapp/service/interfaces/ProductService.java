package org.exp.primeapp.service.interfaces;

import org.exp.primeapp.models.dto.responce.ProductRes;

import java.util.List;

public interface ProductService {
    List<ProductRes> getActiveProducts();

    ProductRes getProductById(Long id);

    List<ProductRes> getActiveProductsByCategoryId(Long categoryId);

    List<ProductRes> getInactiveProductsByCategoryId(Long categoryId);

    List<ProductRes> getInactiveProducts();

    List<ProductRes> getAllProducts();

}
