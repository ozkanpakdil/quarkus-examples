package com.mascix;

import java.math.BigDecimal;

public class Item {
    public Item(String name, BigDecimal price) {
        super();
        this.name = name;
        this.price = price;
    }

    public String name;
    public BigDecimal price;
}