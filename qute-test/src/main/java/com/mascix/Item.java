package com.mascix;

import java.math.BigDecimal;

import io.quarkus.qute.TemplateExtension;

public class Item {
    public Item(String name, BigDecimal price) {
        super();
        this.name = name;
        this.price = price;
    }

    public String name;
    public BigDecimal price;
}

@TemplateExtension
class MyExtensions {
    public static final String ANYTHINGFORTEMPLATE = "name1";

    static String someVar(Item item) {
        return ANYTHINGFORTEMPLATE;
    }
}
