package org.exp.primeapp.controller.admin.product;

import lombok.RequiredArgsConstructor;
import org.exp.primeapp.models.dto.request.ProductReq;
import org.exp.primeapp.models.dto.responce.global.ApiResponse;
import org.exp.primeapp.models.entities.Product;
import org.exp.primeapp.service.interfaces.admin.product.AdminProductService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.exp.primeapp.utils.Const.*;

@RestController
@RequestMapping(API + V1 + ADMIN + PRODUCT)
@RequiredArgsConstructor
public class AdminProductController {

    private final AdminProductService adminProductService;

    @PostMapping
    public ResponseEntity<ApiResponse> addProduct(@RequestBody ProductReq productReq) {
        ApiResponse response = adminProductService.saveProduct(productReq);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/{productId}")
    public ResponseEntity<Product> getProduct(@PathVariable Long productId) {
        Product product = adminProductService.getProductById(productId);
        return new ResponseEntity<>(product, HttpStatus.OK);
    }

    @PutMapping("/{productId}")
    public ResponseEntity<ApiResponse> updateProduct(@PathVariable Long productId, @RequestBody ProductReq productReq) {
        ApiResponse response = adminProductService.updateProduct(productId, productReq);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PatchMapping("/toggle/{productId}")
    public ResponseEntity<Void> activateProduct(@PathVariable Long productId) {
        adminProductService.toggleProductUpdate(productId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/deactivate/{productId}")
    public ResponseEntity<ApiResponse> deactivateProduct(@PathVariable Long productId) {
        ApiResponse response = adminProductService.deactivateProduct(productId);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
