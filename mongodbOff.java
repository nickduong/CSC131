package com.example;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class mongodbOff {
    private static final Logger logger = LoggerFactory.getLogger(mongodbOff.class);

    void logOff() {
        // Set the logging level for the MongoDB driver to OFF
        ch.qos.logback.classic.Logger mongoLogger = (ch.qos.logback.classic.Logger) LoggerFactory.getLogger("org.mongodb.driver");
        mongoLogger.setLevel(ch.qos.logback.classic.Level.OFF);

        logger.info("This is a test message");
    }
}
