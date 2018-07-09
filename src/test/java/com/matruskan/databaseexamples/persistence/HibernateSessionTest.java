package com.matruskan.databaseexamples.persistence;

import com.matruskan.databaseexamples.entities.Author;
import com.matruskan.databaseexamples.entities.Document;
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
public class HibernateSessionTest {
    private static final String TEST_DB = "HibernateSessionTest";
    private static HSQLDBServer hsqldbServer;
    HibernateSession instance;
    
    public HibernateSessionTest() {
    }

    @BeforeClass
    public static void setUpClass() {
        hsqldbServer = new HSQLDBServer(TEST_DB);
    }

    @AfterClass
    public static void tearDownClass() {
        hsqldbServer.stop();
        hsqldbServer.delete();
    }

    @Before
    public void setUp() {
        instance = new HibernateSession(TEST_DB);
    }

    @After
    public void tearDown() {
        instance.closeSession();
        instance.close();
    }

    /**
     * Test of openSession method, of class HibernateSession.
     */
    @Test
    public void testOpenSession() {
        System.out.println("openSession");
        Session session = instance.openSession();
        Document d = (Document) session.get(Document.class, 1l);
    }

    /**
     * Test of openTransation method, of class HibernateSession.
     */
    @Test
    public void testOpenTransation() {
        System.out.println("openTransation");
        Session session = instance.openTransation();
        try {
            assertTrue(session.getTransaction().isActive());
        } finally {
            instance.rollback();
        }
    }

    /**
     * Test of commit method, of class HibernateSession.
     */
    @Test
    public void testCommitWithoutOpenTransactionFails() {
        System.out.println("commitWithoutOpenTransactionFails");
        try {
            instance.commit();
            fail("Can't commit with no open transaction");
        } catch (IllegalStateException ex) {
            System.err.println("Expected exception:");
            ex.printStackTrace();
        }
    }

    @Test
    public void testCommit() {
        System.out.println("commit");
        Session session = instance.openTransation();
        Author author = new Author();
        String authorName = "Matruskan";
        author.setName(authorName);
        session.save(author);
        instance.commit();
        session = instance.openSession();
        List<Author> result = session
                .createQuery("select a from Author a where a.name = :name", Author.class)
                .setParameter("name", authorName)
                .list();
        assertTrue(result.size() > 0);
    }

    /**
     * Test of rollback method, of class HibernateSession.
     */
    @Test
    public void testRollback() {
        System.out.println("rollback");
        Session session = instance.openTransation();
        Author author = new Author();
        String authorName = "Matruskan2";
        author.setName(authorName);
        session.save(author);
        instance.rollback();
        session = instance.openSession();
        Author result = session
                .createQuery("select a from Author a where a.name = :name", Author.class)
                .setParameter("name", authorName)
                .uniqueResult();
        assertNull(result);
    }

}
