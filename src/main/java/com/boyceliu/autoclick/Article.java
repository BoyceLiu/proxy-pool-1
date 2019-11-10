package com.boyceliu.autoclick;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Article {
    private String url;
    private int count;

    public String toString() {
        return url + "," + count;
    }

    public void increment() {
        count++;
    }
}
