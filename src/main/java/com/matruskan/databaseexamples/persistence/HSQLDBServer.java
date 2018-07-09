package com.matruskan.databaseexamples.persistence;

import java.io.File;
import org.hsqldb.server.Server;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
/**
 *
 */
public class HSQLDBServer {
    private static final Logger LOGGER = LoggerFactory.getLogger(HSQLDBServer.class);
    private final String databaseName;
    private final Server server;

    public HSQLDBServer(String databaseName) {
        this.databaseName = databaseName;
        server = new Server();
        server.setDatabaseName(0, databaseName);
        server.setDatabasePath(0, "db/" + databaseName);
        server.setSilent(true);
        server.start();
        LOGGER.info("HSQLDB Server started");
    }

    public void stop() {
        server.stop();
    }

    public void delete() {
        new File("db/" + databaseName).delete();
        new File("db/" + databaseName + ".tmp").delete();
        new File("db/" + databaseName + ".lck").delete();
        new File("db/" + databaseName + ".properties").delete();
        new File("db/" + databaseName + ".script").delete();
        new File("db/" + databaseName + ".log").delete();
    }
}
