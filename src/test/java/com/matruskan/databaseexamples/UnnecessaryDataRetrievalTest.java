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
package com.matruskan.databaseexamples;

import com.matruskan.databaseexamples.entities.Author;
import com.matruskan.databaseexamples.persistence.HSQLDBServer;
import com.matruskan.databaseexamples.persistence.HibernateSession;
import java.util.List;
import org.hibernate.Session;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Matheus
 */
public class UnnecessaryDataRetrievalTest {
    
    private static final String TEST_DB = "UnnecessaryDataRetrievalTest";
    private static final int NUMBER_OF_AUTHORS = 10000;
    private static HSQLDBServer hsqldbServer;
    HibernateSession hibernateSession;
    long startTimestamp;

    public UnnecessaryDataRetrievalTest() {
    }

    @BeforeClass
    public static void setUpClass() {
        hsqldbServer = new HSQLDBServer(TEST_DB);
        HibernateSession hibernateSession = new HibernateSession(TEST_DB);
        Session session = hibernateSession.openTransation();
        for (int i = 0; i < NUMBER_OF_AUTHORS; i++) {
            Author author = new Author();
            author.setName("Matruskan " + i);
            session.save(author);
        }
        hibernateSession.commit();
        hibernateSession.closeSession();
    }

    @AfterClass
    public static void tearDownClass() {
        hsqldbServer.stop();
        hsqldbServer.delete();
    }

    @Before
    public void setUp() {
        hibernateSession = new HibernateSession(TEST_DB);
        startTimestamp = System.currentTimeMillis();
    }

    @After
    public void tearDown() {
        long timeElapsed = System.currentTimeMillis() - startTimestamp;
        System.out.println("Time elapsed: " + timeElapsed);
        hibernateSession.closeSession();
        hibernateSession.close();
    }

    /**
     * Test of dontGetAuthorNames method, of class UnnecessaryDataRetrieval.
     */
    @Test
    public void testDontGetAuthorNames() {
        System.out.println("dontGetAuthorNames");
        UnnecessaryDataRetrieval instance = new UnnecessaryDataRetrieval(hibernateSession.openSession());
        List<String> result = instance.dontGetAuthorNames();
        assertEquals(NUMBER_OF_AUTHORS, result.size());
    }

    /**
     * Test of doGetAuthorNames method, of class UnnecessaryDataRetrieval.
     */
    @Test
    public void testDoGetAuthorNames() {
        System.out.println("doGetAuthorNames");
        UnnecessaryDataRetrieval instance = new UnnecessaryDataRetrieval(hibernateSession.openSession());
        List<String> result = instance.doGetAuthorNames();
        assertEquals(NUMBER_OF_AUTHORS, result.size());
    }
}
