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
import com.matruskan.databaseexamples.persistence.HSQLDBServer;
import com.matruskan.databaseexamples.persistence.HibernateSession;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.Map;
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
public class InefficientDataAccessingTest {

    private static final String TEST_DB = "InefficientComputationTest";
    private static final int NUMBER_OF_AUTHORS = 100;
    private static final int NUMBER_OF_DOCUMENTS = 100;
    private static HSQLDBServer hsqldbServer;
    HibernateSession hibernateSession;
    long startTimestamp;

    public InefficientDataAccessingTest() {
    }

    @BeforeClass
    public static void setUpClass() {
        hsqldbServer = new HSQLDBServer(TEST_DB);
        HibernateSession hibernateSession = new HibernateSession(TEST_DB);
        Session session = hibernateSession.openTransation();
        for (int i = 0; i < NUMBER_OF_AUTHORS; i++) {
            createAuthor("Matruskan " + i, NUMBER_OF_DOCUMENTS, "Inefficient Data Accessing " + i, session);
        }
        hibernateSession.commit();
        hibernateSession.closeSession();
    }

    private static void createAuthor(String name, int numberOfDocuments, String documentTitle, Session session) {
        Author author = new Author();
        author.setName(name);
        session.save(author);
        for (int i = 0; i < numberOfDocuments; i++) {
            Document document = new Document();
            document.setAuthor(author);
            document.setContent("No Content Yet");
            document.setTitle(documentTitle + " " + i + "nd Edition");
            document.setDate(new Date(System.currentTimeMillis() + i * 60000));
            session.save(document);
        }
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
     * Test of buildTitle method, of class InefficientDataAccessing.
     */
    @Test
    public void testBuildTitle() {
        System.out.println("buildTitle");
        Document document = new Document();
        document.setTitle("BuildTitle Test");
        document.setDate(Date.from(LocalDate.of(2018, 7, 14).atStartOfDay(ZoneId.systemDefault()).toInstant()));
        Author author = new Author();
        author.setName("Matruskan");
        String expected = "BuildTitle Test (created by Matruskan on 2018-07-14)";
        String result = InefficientDataAccessing.buildTitle(document, author);
        assertEquals(expected, result);
    }

    /**
     * Test of dontListTitles method, of class InefficientDataAccessing.
     */
    @Test
    public void testDontListTitles() {
        System.out.println("dontListTitles");
        InefficientDataAccessing instance = new InefficientDataAccessing(hibernateSession.openSession());
        List<String> result = instance.dontListTitles();
        assertEquals(NUMBER_OF_AUTHORS * NUMBER_OF_DOCUMENTS, result.size());
    }

    /**
     * Test of doListTitles method, of class InefficientDataAccessing.
     */
    @Test
    public void testDoListTitles() {
        System.out.println("doListTitles");
        InefficientDataAccessing instance = new InefficientDataAccessing(hibernateSession.openSession());
        List<String> result = instance.doListTitles();
        assertEquals(NUMBER_OF_AUTHORS * NUMBER_OF_DOCUMENTS, result.size());
    }

    /**
     * Test of dontCreateHashForDocumentsContent method, of class
     * InefficientDataAccessing.
     */
    @Test
    public void testDontCreateHashForDocumentsContent() {
        System.out.println("dontCreateHashForDocumentsContent");
        InefficientDataAccessing instance = new InefficientDataAccessing(hibernateSession.openSession());
        Map<Long, Integer> result = instance.dontCreateHashForDocumentsContent();
        assertEquals(NUMBER_OF_AUTHORS * NUMBER_OF_DOCUMENTS, result.size());
    }

    /**
     * Test of doCreateHashForDocumentsContent method, of class
     * InefficientDataAccessing.
     */
    @Test
    public void testDoCreateHashForDocumentsContent() {
        System.out.println("doCreateHashForDocumentsContent");
        InefficientDataAccessing instance = new InefficientDataAccessing(hibernateSession.openSession());
        Map<Long, Integer> result = instance.doCreateHashForDocumentsContent();
        assertEquals(NUMBER_OF_AUTHORS * NUMBER_OF_DOCUMENTS, result.size());
    }

}
