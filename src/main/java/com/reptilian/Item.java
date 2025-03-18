package com.reptilian;

import java.util.HashMap;
import java.util.Map;

public class Item {
    private static Map<Integer, Item> items = new HashMap<>();
    private final int value;
    private final String name;

    private Item(int value, String name) {
        this.value = value;
        this.name = name;
    }

    public int getValue() {
        return value;
    }

    public String getName() {
        return name;
    }

    public static Map<Integer, Item> getItems() {
        items.put(-1, new Item(-1, "Eye of the Herald"));
        items.put(1001, new Item(1001, "Boots"));
        items.put(1004, new Item(1004, "Faerie Charm"));
        items.put(1018, new Item(1018, "Cloak of Agility"));
        items.put(1026, new Item(1026, "Blasting Wand"));
        items.put(1029, new Item(1029, "Cloth Armor"));
        items.put(1036, new Item(1036, "Long Sword"));
        items.put(1038, new Item(1038, "B. F. Sword"));
        items.put(1042, new Item(1042, "Dagger"));
        items.put(1052, new Item(1052, "Amplifying Tome"));
        items.put(1054, new Item(1054, "Doran's Shield"));
        items.put(1055, new Item(1055, "Doran's Blade"));
        items.put(1056, new Item(1056, "Doran's Ring"));
        items.put(1083, new Item(1083, "Cull"));
        items.put(1082, new Item(1082, "Dark Seal"));
        items.put(1101, new Item(1101, "Scorchclaw Pup"));
        items.put(1102, new Item(1102, "Gustwalker Hatchling"));
        items.put(1103, new Item(1103, "Mosstomper Seedling"));
        items.put(2003, new Item(2003, "Health Potion"));
        items.put(2010, new Item(2010, "Total Biscuit of Everlasting Will"));
        items.put(2022, new Item(2022, "Glowing Mote"));
        items.put(2031, new Item(2031, "Refillable Potion"));
        items.put(2051, new Item(2051, "Guardian's Horn"));
        items.put(2052, new Item(2052, "Poro-Snax"));
        items.put(2055, new Item(2055, "Control Ward"));
        items.put(2138, new Item(2138, "Elixir of Iron"));
        items.put(2139, new Item(2139, "Elixir of Sorcery"));
        items.put(2140, new Item(2140, "Elixir of Wrath"));
        items.put(2141, new Item(2141, "Cappa Juice"));
        items.put(2150, new Item(2150, "Elixir of Skill"));
        items.put(2151, new Item(2151, "Elixir of Avarice"));
        items.put(2152, new Item(2152, "Elixir of Force"));
        items.put(2422, new Item(2422, "Slightly Magical Boots"));
        items.put(3006, new Item(3006, "Berserker's Greaves"));
        items.put(3009, new Item(3009, "Boots of Swiftness"));
        items.put(3010, new Item(3010, "Symbiotic Soles"));
        items.put(3013, new Item(3013, "Synchronized Souls"));
        items.put(3020, new Item(3020, "Sorcerer's Shoes"));
        items.put(3047, new Item(3047, "Plated Steelcaps"));
        items.put(3070, new Item(3070, "Tear of the Goddess"));
        items.put(3111, new Item(3111, "Mercury's Treads"));
        items.put(3112, new Item(3112, "Guardian's Orb"));
        items.put(3158, new Item(3158, "Ionian Boots of Lucidity"));
        items.put(3172, new Item(3172, "Zephyr"));
        items.put(3177, new Item(3177, "Guardian's Blade"));
        items.put(3184, new Item(3184, "Guardian's Hammer"));
        items.put(3340, new Item(3340, "Stealth Ward"));
        items.put(3363, new Item(3363, "Farsight Alteration"));
        items.put(3364, new Item(3364, "Oracle Lens"));
        items.put(3400, new Item(3400, "Your Cut"));
        items.put(3865, new Item(3865, "World Atlas"));

        return items;
    }
}
