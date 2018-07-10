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
import com.matruskan.databaseexamples.entities.Document;
import com.matruskan.databaseexamples.entities.User;
import com.matruskan.databaseexamples.persistence.HSQLDBServer;
import com.matruskan.databaseexamples.persistence.HibernateSession;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import org.hibernate.Session;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 */
public class UnnecessaryComputationTest {

    private static final String TEST_DB = "UnnecessaryComputationTest";
    private static HSQLDBServer hsqldbServer;
    HibernateSession hibernateSession;
    long startTimestamp;

    public UnnecessaryComputationTest() {
    }

    @BeforeClass
    public static void setUpClass() {
        hsqldbServer = new HSQLDBServer(TEST_DB);
        HibernateSession hibernateSession = new HibernateSession(TEST_DB);
        Session session = hibernateSession.openTransation();
        Author author = new Author();
        author.setName("Matruskan");
        session.save(author);
        for (int i = 0; i < 100; i++) {
            Document document = new Document();
            document.setAuthor(author);
            document.setContent("No Content Yet");
            document.setTitle("UnnecessaryComputation " + i + "nd Edition");
            document.setDate(new Date(System.currentTimeMillis() + i * 60000));
            session.save(document);
        }
        Set<Author> favoriteAuthors = new HashSet<>(Arrays.asList(author));
        for (int i = 0; i < 100; i++) {
            User user = new User();
            user.setFavoriteAuthors(favoriteAuthors);
            session.save(user);
        }
        hibernateSession.commit();
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
     * Test of dontSendNewsletterAboutRecentDocuments method, of class
     * UnnecessaryComputation.
     */
    @Test
    public void testDontSendNewsletterAboutRecentDocuments() {
        System.out.println("dontSendNewsletterAboutRecentDocuments");
        Session session = hibernateSession.openSession();
        Author author = session.get(Author.class, 1l);
        UnnecessaryComputation instance = new UnnecessaryComputation(session);
        instance.dontSendNewsletterAboutRecentDocuments(author);
    }

    /**
     * Test of doSendNewsLetterAboutRecentDocuments method, of class
     * UnnecessaryComputation.
     */
    @Test
    public void testDoSendNewsLetterAboutRecentDocuments() {
        System.out.println("doSendNewsLetterAboutRecentDocuments");
        Session session = hibernateSession.openSession();
        Author author = session.get(Author.class, 1l);
        UnnecessaryComputation instance = new UnnecessaryComputation(session);
        instance.doSendNewsLetterAboutRecentDocuments(author);
    }
}
