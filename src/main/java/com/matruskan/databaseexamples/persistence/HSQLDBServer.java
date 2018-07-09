/*
 * The MIT License
 *
 * Copyright 2018 matruskan
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
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
