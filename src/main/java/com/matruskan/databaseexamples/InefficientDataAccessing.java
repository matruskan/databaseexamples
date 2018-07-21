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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import org.hibernate.Session;

/**
 * Example 3: Inefficient Data Accessing
 *
 * "Problems under this category suffer from data transfer slow downs, including
 * not batching data transfers (e.g., the well-known “N+1” problem) or batching
 * too much data into one transfer."
 *
 * "**Inefficient lazy loading**. [...] when a set of objects O in table T1 are
 * requested, objects stored in table T2 associated with T1 and O can be loaded
 * together through eager loading. If lazy loading is chosen instead, one query
 * will be issued to load N objects from T1, and then N separate queries have to
 * be issued to load associations of each such object from T2. This is known as
 * the “N+1” query problem."
 *
 * "**Inefficient eager loading**. However, always loading data eagerly can also
 * cause problems. Specifically, when the associated objects are too large,
 * loading them all at once will create huge memory pressure and even make the
 * application unresponsive."
 *
 * "**Inefficient updating**. Like the “N+1” problem, developers would issue N
 * queries to update N records separately rather than merging them into one
 * update."
 *
 * This class shows an example of **inefficient lazy loading**, and an example
 * of *inefficient eager loading**.
 */
public class InefficientDataAccessing {

    private final Session session;

    public InefficientDataAccessing(Session session) {
        this.session = session;
    }

    public List<String> dontListTitles() {
        List<String> titles = new ArrayList<>();
        String query = "select a from Author a";
        List<Author> authors = session
                .createQuery(query, Author.class)
                .getResultList();
        for (Author author : authors) {
            Set<Document> documents = author.getDocuments();
            for (Document document : documents) {
                String title = buildTitle(document, author);
                titles.add(title);
            }
        }
        return titles;
    }

    public List<String> doListTitles() {
        List<String> titles = new ArrayList<>();
        String query = "select distinct a from Author a join fetch a.documents d";
        List<Author> authors = session
                .createQuery(query, Author.class)
                .getResultList();
        for (Author author : authors) {
            Set<Document> documents = author.getDocuments();
            for (Document document : documents) {
                String title = buildTitle(document, author);
                titles.add(title);
            }
        }
        return titles;
    }

    public Map<Long, Integer> dontCreateHashForDocumentsContent() {
        Map<Long, Integer> hashes = new HashMap<>();
        String query = "select d from Document d";
        List<Document> documents = session
                .createQuery(query, Document.class)
                .getResultList();
        for (Document document : documents) {
            int hashCodeForContent = Objects.hashCode(document.getContent());
            hashes.put(document.getId(), hashCodeForContent);
        }
        return hashes;
    }

    public Map<Long, Integer> doCreateHashForDocumentsContent() {
        Map<Long, Integer> hashes = new HashMap<>();
        String query = "select d.id, d.content from Document d";
        List<Object[]> objects = session
                .createQuery(query, Object[].class)
                .getResultList();
        for (Object[] object : objects) {
            int hashCodeForContent = Objects.hashCode(object[1]);
            hashes.put((Long) object[0], hashCodeForContent);
        }
        return hashes;
    }

    public static String buildTitle(Document document, Author author) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("YYYY-MM-dd");
        String title = new StringBuilder()
                .append(document.getTitle())
                .append(" (created by ")
                .append(author.getName())
                .append(" on ")
                .append(dateFormat.format(document.getDate()))
                .append(")")
                .toString();
        return title;
    }
}
