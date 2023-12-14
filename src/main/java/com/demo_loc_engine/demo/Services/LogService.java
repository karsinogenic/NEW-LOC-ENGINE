package com.demo_loc_engine.demo.Services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class LogService {
    private static final Logger logger = LoggerFactory.getLogger("loc_log");

    public void info(String msg) {
        logger.info(msg);
    }
}
