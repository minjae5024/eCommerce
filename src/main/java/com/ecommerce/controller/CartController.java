package com.ecommerce.controller;

import com.ecommerce.dto.CartItemRequestDto;
import com.ecommerce.dto.CartResponseDto;
import com.ecommerce.service.CartService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Map;

@Tag(name = "장바구니 API")
@RestController
@RequestMapping("/api/cart")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;

    @Operation(
            summary = "장바구니 상품 추가",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    required = true, content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(value = "{\"productId\": 1, \"quantity\": 1}")
                    )
            )
    )
    @PostMapping("/items")
    public ResponseEntity<Long> addItemToCart(@Valid @RequestBody CartItemRequestDto requestDto, Principal principal) {
        Long cartItemId = cartService.addItem(principal.getName(), requestDto);
        return ResponseEntity.ok(cartItemId);
    }

    @Operation(summary = "장바구니 조회")
    @GetMapping
    public ResponseEntity<CartResponseDto> getCart(Principal principal) {
        CartResponseDto cart = cartService.getCart(principal.getName());
        return ResponseEntity.ok(cart);
    }

    @Operation(
            summary = "장바구니 상품 수량 업데이트",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    required = true,
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = "{\"quantity\": 2}")
                    )
            )
    )
    @PatchMapping("/items/{cartItemId}")
    public ResponseEntity<Void> updateCartItemQuantity(@PathVariable Long cartItemId,
                                                       @RequestBody Map<String, Integer> requestBody,
                                                       Principal principal) {
        int quantity = requestBody.get("quantity");
        cartService.updateCartItemQuantity(principal.getName(), cartItemId, quantity);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "장바구니 상품 삭제")
    @DeleteMapping("/items/{cartItemId}")
    public ResponseEntity<Void> removeCartItem(@PathVariable Long cartItemId, Principal principal) {
        cartService.removeCartItem(principal.getName(), cartItemId);
        return ResponseEntity.noContent().build();
    }
}
