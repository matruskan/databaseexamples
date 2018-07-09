package com.matruskan.databaseexamples;

import com.matruskan.databaseexamples.entities.Author;
import com.matruskan.databaseexamples.entities.Document;
import com.matruskan.databaseexamples.persistence.HSQLDBServer;
import com.matruskan.databaseexamples.persistence.HibernateSession;
import java.util.Date;
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
public class InefficientComputationTest {

    private static final String TEST_DB = "InefficientComputationTest";
    private static HSQLDBServer hsqldbServer;
    HibernateSession hibernateSession;
    long startTimestamp;

    public InefficientComputationTest() {
    }

    @BeforeClass
    public static void setUpClass() {
        hsqldbServer = new HSQLDBServer(TEST_DB);
        HibernateSession hibernateSession = new HibernateSession(TEST_DB);
        Session session = hibernateSession.openTransation();
        Author author = new Author();
        author.setName("Matruskan");
        session.save(author);
        for (int i = 0; i < 10000; i++) {
            Document document = new Document();
            document.setAuthor(author);
            document.setContent("No Content Yet");
            document.setTitle("InneficientComputation " + i + "nd Edition");
            document.setDate(new Date(System.currentTimeMillis() + i * 60000));
            session.save(document);
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
     * Test of documentExists method, of class InefficientComputation.
     */
    @Test
    public void testDocumentExists() {
        System.out.println("dontDocumentExists");
        Session session = hibernateSession.openSession();
        Author author = session.get(Author.class, 1l);
        InefficientComputation instance = new InefficientComputation(session);
        boolean expResult = true;
        boolean result = instance.dontDocumentExists(author);
        assertEquals(expResult, result);
    }

    @Test
    public void testDoDocumentExists() {
        System.out.println("doDocumentExists");
        Session session = hibernateSession.openSession();
        Author author = session.get(Author.class, 1l);
        InefficientComputation instance = new InefficientComputation(session);
        boolean expResult = true;
        boolean result = instance.doDocumentExists(author);
        assertEquals(expResult, result);
    }

    @Test
    public void testDontMostRecentDocument() {
        System.out.println("dontMostRecentDocument");
        Session session = hibernateSession.openSession();
        Author author = session.get(Author.class, 1l);
        InefficientComputation instance = new InefficientComputation(session);
        Document result = instance.dontMostRecentDocument(author);
        assertEquals("InneficientComputation 9999nd Edition", result.getTitle());
    }
    @Test
    public void testDoMostRecentDocument() {
        System.out.println("doMostRecentDocument");
        Session session = hibernateSession.openSession();
        Author author = session.get(Author.class, 1l);
        InefficientComputation instance = new InefficientComputation(session);
        Document result = instance.doMostRecentDocument(author);
        assertEquals("InneficientComputation 9999nd Edition", result.getTitle());
    }
}
