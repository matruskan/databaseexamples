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
 *
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
