package org.example;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.sleuth.annotation.NewSpan;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class DbService {

    @NewSpan
    public void db() {
        log.info("db");
    }

}