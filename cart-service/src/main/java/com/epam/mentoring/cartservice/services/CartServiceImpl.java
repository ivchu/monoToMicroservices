package com.epam.mentoring.cartservice.services;

import com.epam.mentoring.cartservice.entity.Cart;
import com.epam.mentoring.cartservice.entity.CartItem;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Service
public class CartServiceImpl implements CartService {

    private final ConcurrentMap<String, Cart> cache = new ConcurrentHashMap<>();
//    @Autowired
//    private EcommerceService ecommerceService;

    @Override
    public String createNewCart() {
        return UUID.randomUUID().toString();
    }

    @Override
    public void addProduct(String cartId, CartItem cartItem) {
        Cart cart = cache.getOrDefault(cartId, new Cart());
        if (cart.getItems() == null) {
            cart.setItems(new ArrayList<>());
        }
        cart.getItems().add(cartItem);
        cache.putIfAbsent(cartId, cart);
    }

    @Override
    public void removeProduct(String cartId, String productId) {
        CartItem itemToRemove = new CartItem();
        itemToRemove.setProductId(Long.parseLong(productId));
        Cart cart = cache.get(cartId);
        if (cart != null) {
            cart.getItems().remove(itemToRemove);
        }
    }

    @Override
    public void setProductQuantity(String cartId, String productId, int quantity) {
        Cart cart = cache.get(cartId);
        if (cart != null) {
            cart.getItems()
                    .forEach(
                            cartItem -> {
                                if (cartItem.getProductId() == Long.parseLong(productId)) {
                                    cartItem.setQuantity(quantity);
                                }
                            });
        }
    }

    @Override
    public Set<CartItem> getItems(String cartId) {
        Cart cart = cache.get(cartId);
        if (cart != null) {
            return new HashSet<>(cart.getItems());
        }
        return Collections.emptySet();
    }

}
