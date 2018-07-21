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
import java.util.List;
import java.util.stream.Collectors;
import org.hibernate.Session;

/**
 * Example 4: Unnecessary Data Retrieval
 *
 * "Unnecessary data retrieval happens when software retrieves persistent data
 * that is not used later."
 *
 * This class shows an example of that.
 */
public class UnnecessaryDataRetrieval {

    private final Session session;

    public UnnecessaryDataRetrieval(Session session) {
        this.session = session;
    }

    public List<String> dontGetAuthorNames() {
        String query = "select a from Author a";
        List<Author> authors = session
                .createQuery(query, Author.class)
                .getResultList();
        List<String> names = authors.stream()
                .map(Author::getName)
                .collect(Collectors.toList());
        return names;
    }

    public List<String> doGetAuthorNames() {
        String query = "select a.name from Author a";
        List<String> names = session
                .createQuery(query, String.class)
                .getResultList();
        return names;
    }
}
