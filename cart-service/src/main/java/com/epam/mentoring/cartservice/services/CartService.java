package com.epam.mentoring.cartservice.services;


import com.epam.mentoring.cartservice.entity.CartItem;

import java.util.Set;

public interface CartService {
    String createNewCart();

    void addProduct(String cartId, CartItem cartItem);

    void removeProduct(String cartId, String productId);

    void setProductQuantity(String cartId, String productId, int quantity);

    Set<CartItem> getItems(String cartId);
}
