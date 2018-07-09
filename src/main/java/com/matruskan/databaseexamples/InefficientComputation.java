package com.matruskan.databaseexamples;

import com.matruskan.databaseexamples.entities.Author;
import com.matruskan.databaseexamples.entities.Document;
import java.util.List;
import org.hibernate.Session;

/**
 * Example 1: Inneficient Computation
 *
 * "The poorly performing code conducts useful computation but inefficiently"
 *
 * "Inefficient queries. The same operation on persistent data can be
 * implemented via different ORM calls. However, the performance of the
 * generated queries can be drastically different."
 *
 * "Moving computation to the DBMS. As the ORM framework hides the details of
 * query generation, developers often write code that results in multiple
 * queries being generated."
 *
 * "Moving computation to the server. Interestingly, there are cases where the
 * computation should be moved to the server from the DBMS."
 *
 * This class shows an example of **moving computation to the DBMS**.
 */
public class InefficientComputation {

    private final Session session;

    public InefficientComputation(Session session) {
        this.session = session;
    }

    public boolean dontDocumentExists(Author author) {
        String query = "select d from Document d"
                + " where d.author = :author";
        List<Document> documents = session
                .createQuery(query, Document.class)
                .setParameter("author", author)
                .getResultList();
        boolean documentExists = !documents.isEmpty();
        return documentExists;
    }

    public boolean doDocumentExists(Author author) {
        String query = "select count(d) from Document d where d.author = :author";
        Long count = session.createQuery(query, Long.class)
                .setParameter("author", author)
                .uniqueResult();
        boolean documentExists = count > 0;
        return documentExists;
    }

    public Document dontMostRecentDocument(Author author) {
        String query = "select d from Document d"
                + " where d.author = :author";
        List<Document> documents = session
                .createQuery(query, Document.class)
                .setParameter("author", author)
                .getResultList();
        documents.sort((d1, d2) -> d2.getDate().compareTo(d1.getDate()));
        Document recentDocument = documents.get(0);
        return recentDocument;
    }

    public Document doMostRecentDocument(Author author) {
        String query = "select d from Document d"
                + " where d.author = :author"
                + " order by d.date desc";
        Document recentDocument = session
                .createQuery(query, Document.class)
                .setParameter("author", author)
                .setMaxResults(1)
                .uniqueResult();
        return recentDocument;
    }
}
