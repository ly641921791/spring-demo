package org.example;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class DbService {

    public void db() {
        log.info("db");
    }

}