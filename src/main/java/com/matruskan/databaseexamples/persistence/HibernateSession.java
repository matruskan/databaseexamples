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
package com.matruskan.databaseexamples.persistence;

import com.matruskan.databaseexamples.entities.Account;
import com.matruskan.databaseexamples.entities.Author;
import com.matruskan.databaseexamples.entities.Document;
import com.matruskan.databaseexamples.entities.User;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.AvailableSettings;
import org.hibernate.cfg.Configuration;

/**
 *
 */
public class HibernateSession {

    private final SessionFactory sessionFactory;

    public HibernateSession() {
        Configuration cfg = buildConfiguration();
        sessionFactory = cfg.buildSessionFactory();
    }

    public HibernateSession(String databaseName) {
        Configuration cfg = buildConfiguration();
        cfg.setProperty(AvailableSettings.URL, "jdbc:hsqldb:hsql://localhost/" + databaseName);
        sessionFactory = cfg.buildSessionFactory();
    }

    public Session openSession() {
        return sessionFactory.openSession();
    }

    public void closeSession() {
        if (sessionFactory.getCurrentSession().isOpen()) {
            sessionFactory.getCurrentSession().close();
        }
    }

    public void close() {
        if (sessionFactory.isOpen()) {
            sessionFactory.close();
        }
    }

    public Session openTransation() {
        Session currentSession = sessionFactory.getCurrentSession();
        if (currentSession == null) {
            currentSession = openSession();
        }
        currentSession.beginTransaction();
        return currentSession;
    }

    public void commit() {
        sessionFactory.getCurrentSession().getTransaction().commit();
    }

    public void rollback() {
        sessionFactory.getCurrentSession().getTransaction().rollback();
    }

    private Configuration buildConfiguration() throws HibernateException {
        return new Configuration()
                .addAnnotatedClass(Account.class)
                .addAnnotatedClass(Author.class)
                .addAnnotatedClass(Document.class)
                .addAnnotatedClass(User.class)
                .configure();
    }
}
