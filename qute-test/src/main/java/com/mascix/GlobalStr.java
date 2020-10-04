package com.mascix;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;

import io.quarkus.qute.TemplateExtension;

@TemplateExtension(namespace = "str")
public class GlobalStr {

    public static final String ANYTHINGFORTEMPLATE = "name2";

    static Map<String, String> myMap = new HashMap<String, String>() {
        {
            put("1", "one");
            put("2", "two");
            // etc
        }
    };

    static String Any1() {
        return ANYTHINGFORTEMPLATE;
    }

    static String g(String k) {
        System.out.println("g called:" + k + " " + myMap.get(k));
        // System.out.println("g called:"+k);
        return myMap.get(String.valueOf(k));
    }
}