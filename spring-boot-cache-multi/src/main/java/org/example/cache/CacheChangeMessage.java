package org.example.cache;

import lombok.Data;

@Data
public class CacheChangeMessage {

    private String publisher;
    private String cacheName;
    private String key;
    private Boolean clear;

}
