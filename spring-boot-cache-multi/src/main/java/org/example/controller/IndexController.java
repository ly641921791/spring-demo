package org.example.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class IndexController {

    @Cacheable(cacheNames = "IndexController", key = "'index'")
    @RequestMapping("/1")
    public String index() {
        log.info("IndexController.index");
        return "hello world";
    }

    @RequestMapping("/2")
    @CacheEvict(cacheNames = "IndexController", key = "'index'")
    public String index2() {
        log.info("IndexController.index2");
        return "hello world";
    }

}
