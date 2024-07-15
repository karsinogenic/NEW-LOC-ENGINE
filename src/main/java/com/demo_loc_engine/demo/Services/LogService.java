package com.demo_loc_engine.demo.Services;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class LogService {
    private static final Logger logger = LoggerFactory.getLogger("loc_log");

    public void info(String msg) {
        logger.info(msg);
    }

    public void info2(String extId, String intId, String type, String destinationIp, String sourceIp, String engine,
            String service, String ket, String rc, String rd, JSONObject json) {
        logger.info(extId + "|" + destinationIp + "|" + sourceIp + "|" + intId + "|" + type + "|"
                + engine + "|" + service + "|" + ket + "|" + rc + "|" + rd + "|" + json.toString());
    }
}
