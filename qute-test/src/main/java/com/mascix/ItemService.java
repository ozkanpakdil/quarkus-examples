package com.mascix;

import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class ItemService {

    List<Item> items = new ArrayList();

    ItemService() {
        for (int i = 0; i < 100; i++) {
            int randomNum = ThreadLocalRandom.current().nextInt(100, 500 + 1);
            Item t = new Item(i + "", new BigDecimal(randomNum));
            items.add(t);
        }
    }

    // public void init( @Observes @Initialized( ApplicationScoped.class ) Object
    // init ) {
    // // perform some initialization logic
    // }
    public Object findItem(Integer id) {
        Item result = null;
        Random r = new Random();
        if (id != null)
            result = items.stream().filter(i -> id.equals(Integer.valueOf(i.name))).findFirst().get();
        if (result == null)
            result = items.stream().skip(r.nextInt(items.size() - 1)).findFirst().get();
        return result;
    }

}
