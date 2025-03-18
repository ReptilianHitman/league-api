package com.reptilian;

import java.util.Map;

public class Testing {
    public static void main(String[] args) {
        Map<Integer, Item> items = Item.getItems();

        for (Item item : items.values()) {
            System.out.println(item.getName());
        }
    }
}
