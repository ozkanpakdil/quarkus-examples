package com.mascix;

import lombok.Data;

@Data
public class ExampleObject {
    public String name;
    public String developer;

    public ExampleObject(String name, String dev) {
        this.name = name;
        this.developer = dev;
    }
}