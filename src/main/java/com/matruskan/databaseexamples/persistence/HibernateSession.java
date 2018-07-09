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
