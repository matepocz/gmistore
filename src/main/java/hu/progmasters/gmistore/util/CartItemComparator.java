package hu.progmasters.gmistore.util;

import hu.progmasters.gmistore.dto.CartItemDto;

import java.util.Comparator;

public class CartItemComparator implements Comparator<CartItemDto> {

    @Override
    public int compare(CartItemDto cartItemDtoFirst, CartItemDto cartItemDtoSecond) {
        return Long.compare(cartItemDtoFirst.getId(), cartItemDtoSecond.getId());
    }
}
