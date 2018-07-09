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
        String query = "select count(d) from Document d"
                + " where d.author = :author";
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
