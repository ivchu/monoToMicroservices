package com.epam.mentoring.dtos;

import java.util.Objects;

public class CartItemDto {
    private long productId;
    private long variantId;
    private int quantity;

    public long getProductId() {
        return productId;
    }

    public void setProductId(long productId) {
        this.productId = productId;
    }

    public long getVariantId() {
        return variantId;
    }

    public void setVariantId(long variantId) {
        this.variantId = variantId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CartItemDto cartItemDto = (CartItemDto) o;
        return productId == cartItemDto.productId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(productId);
    }
}
