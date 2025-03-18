package com.reptilian;

import java.util.Map;

public class Testing {
    public static void main(String[] args) {
        Map<Integer, Item> items = Item.getItems();

        for (int i = 0; i <= items.keySet().stream().max(Integer::compareTo).get(); i++) {
            System.out.println(i + ": " + (items.get(i) != null ? items.get(i).getName() : "null"));
        }
    }
}
