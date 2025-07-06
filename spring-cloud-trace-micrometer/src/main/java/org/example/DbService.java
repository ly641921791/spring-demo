package org.example;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class DbService {

//    @NewSpan
//    @Observed
//    @NewSpan(name = "db2")
//    @Observed(name = "db1")
    public void db() {
        log.info("db");
    }

}
