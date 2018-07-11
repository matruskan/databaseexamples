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

import com.matruskan.databaseexamples.control.Newsletter;
import com.matruskan.databaseexamples.entities.Author;
import com.matruskan.databaseexamples.entities.Document;
import com.matruskan.databaseexamples.entities.User;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import org.hibernate.Session;

/**
 * Example 2: Unnecessary Computation
 *
 * "More than 10% of the performance issues are caused by (mis)using ORM APIs
 * that lead to unnecessary queries being issued."
 *
 * "**Loop-invariant queries**. Sometimes, queries are repeatedly issued to load
 * the same database contents and hence are unnecessary."
 *
 * "**Dead-store queries**. In such cases, queries are repeatedly issued to load
 * different database contents into the same memory object while the object has
 * not been used between the reloads.
 *
 * "**Queries with known results**. A number of issues are due to issuing
 * queries whose results are already known, hence incurring unnecessary network
 * round trips and query processing time.
 *
 * This class shows an example of **loop-invariant queries**.
 */
public class UnnecessaryComputation {

    private final Session session;

    public UnnecessaryComputation(Session session) {
        this.session = session;
    }

    public void dontSendNewsletterAboutRecentDocuments(Author author) {
        List<Document> yesterdayDocuments = loadYesterdayDocuments(author);
        yesterdayDocuments.forEach(this::sendNewsletter);
    }

    private void sendNewsletter(Document document) {
        Author author = document.getAuthor();
        List<User> usersThatFavoritedAuthor = loadUsersWhoFavoritedAuthor(author);
        new Newsletter()
                .withTitle("New Document from " + author.getName() + ": " + document.getTitle())
                .addHeader(author.getImgUrl(), author.getName() + " created a new Document: " + document.getTitle())
                .addContent(document.getContent())
                .addLink("Open Document", document)
                .sendTo(usersThatFavoritedAuthor);
    }

    public void doSendNewsLetterAboutRecentDocuments(Author author) {
        List<Document> yesterdayDocuments = loadYesterdayDocuments(author);
        List<User> usersThatFavoritedAuthor = loadUsersWhoFavoritedAuthor(author);
        yesterdayDocuments.forEach(document -> this.sendNewsletter(document, usersThatFavoritedAuthor));
    }

    private void sendNewsletter(Document document, List<User> users) {
        Author author = document.getAuthor();
        new Newsletter()
                .withTitle("New Document from " + author.getName() + ": " + document.getTitle())
                .addHeader(author.getImgUrl(), author.getName() + " created a new Document: " + document.getTitle())
                .addContent(document.getContent())
                .addLink("Open Document", document)
                .sendTo(users);
    }

    private List<Document> loadYesterdayDocuments(Author author) {
        String query = "select d from Document d"
                + " where d.author = :author"
                + " and d.date > :yesterday";
        List<Document> documentsOfTheWeek = session
                .createQuery(query, Document.class)
                .setParameter("author", author)
                .setParameter("yesterday", getYesterday())
                .getResultList();
        return documentsOfTheWeek;
    }

    private List<User> loadUsersWhoFavoritedAuthor(Author author) {
        String query = "select u from User u join u.favoriteAuthors a"
                + " where a = :author";
        List<User> users = session.createQuery(query, User.class)
                .setParameter("author", author)
                .getResultList();
        return users;
    }

    private Date getYesterday() {
         return Date.from(
                 LocalDate.now()
                         .minusDays(1)
                         .atStartOfDay(ZoneId.systemDefault())
                         .toInstant());
    }
}
