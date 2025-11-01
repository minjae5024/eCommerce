package com.ecommerce.controller;

import com.ecommerce.dto.ProductCreateRequestDto;
import com.ecommerce.dto.ProductResponseDto;
import com.ecommerce.dto.ProductUpdateRequestDto;
import com.ecommerce.service.ProductService;
import jakarta.validation.Valid;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@Tag(name = "상품 API")
@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @Operation(
            summary = "상품 등록",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    required = true, content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(value = "{\"name\": \"Test Product\", \"description\": \"This is a test product.\", \"price\": 10000, \"stock\": 100}")
                    )
            )
    )
    @PostMapping
    public ResponseEntity<Long> createProduct(@Valid @RequestBody ProductCreateRequestDto requestDto) {
        Long productId = productService.createProduct(requestDto);
        return ResponseEntity.created(URI.create("/api/products/" + productId)).body(productId);
    }

    @Operation(summary = "상품 조회")
    @GetMapping("/{productId}")
    public ResponseEntity<ProductResponseDto> getProductById(@PathVariable Long productId) {
        ProductResponseDto product = productService.findProductById(productId);
        return ResponseEntity.ok(product);
    }

    @Operation(summary = "모든 상품 조회")
    @GetMapping
    public ResponseEntity<Page<ProductResponseDto>> getAllProducts(@ParameterObject Pageable pageable) {
        Page<ProductResponseDto> products = productService.findAllProducts(pageable);
        return ResponseEntity.ok(products);
    }

    @Operation(
            summary = "상품 정보 업데이트",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    required = true, content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(value = "{\"name\": \"Updated Product\", \"description\": \"This is an updated product.\", \"price\": 12000, \"stock\": 150}")
                    )
            )
    )
    @PutMapping("/{productId}")
    public ResponseEntity<Long> updateProduct(@PathVariable Long productId, @Valid @org.springframework.web.bind.annotation.RequestBody ProductUpdateRequestDto requestDto) {
        Long updatedProductId = productService.updateProduct(productId, requestDto);
        return ResponseEntity.ok(updatedProductId);
    }

    @Operation(summary = "상품 삭제")
    @DeleteMapping("/{productId}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long productId) {
        productService.deleteProduct(productId);
        return ResponseEntity.noContent().build();
    }
}