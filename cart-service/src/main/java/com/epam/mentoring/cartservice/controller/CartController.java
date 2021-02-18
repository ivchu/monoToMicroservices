package com.epam.mentoring.cartservice.controller;

import com.epam.mentoring.cartservice.entity.CartItem;
import com.epam.mentoring.cartservice.services.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;

@RestController
@RequestMapping("/cart")
public class CartController {

    @Autowired
    CartService cartService;

    @PostMapping
    public String create() {
        return cartService.createNewCart();
    }

    @PostMapping("/{id}")
    public ResponseEntity<String> addProduct(@PathVariable("id") String cartId, @RequestBody CartItem cartItem) {
        cartService.addProduct(cartId, cartItem);
        return ResponseEntity.ok(cartId);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Set<CartItem>> getCartItems(@PathVariable("id") String cartId) {
        return ResponseEntity.ok(cartService.getItems(cartId));
    }

    @DeleteMapping("{id}/{product_id}")
    public ResponseEntity<String> removeItem(@PathVariable("id") String cartId, @PathVariable("product_id") String productId) {
        cartService.removeProduct(cartId, productId);
        return ResponseEntity.ok(cartId);
    }

    @PostMapping("{id}/quantity")
    public ResponseEntity<String> setProductQuantity(@PathVariable("id") String cartId, @RequestBody CartItem cartItem) {
        String productId = Long.toString(cartItem.getProductId());
        cartService.setProductQuantity(cartId, productId, cartItem.getQuantity());
        return ResponseEntity.ok(cartId);
    }
}
