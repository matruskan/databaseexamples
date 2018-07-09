package com.matruskan.databaseexamples.entities;

import java.io.Serializable;
import java.util.Set;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToOne;

/**
 *
 * @author Matheus
 */
@Entity
public class User implements Serializable {

    @Id
    @GeneratedValue
    private Long id;
    @OneToOne @JoinColumn
    private Account account;
    @ManyToMany
    @JoinTable(
            name = "User_FavoriteAuthors",
            joinColumns = {@JoinColumn(name = "user_id")},
            inverseJoinColumns = {@JoinColumn(name = "author_id")})
    private Set<Author> favoriteAuthors;

    public User() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public Set<Author> getFavoriteAuthors() {
        return favoriteAuthors;
    }

    public void setFavoriteAuthors(Set<Author> favoriteAuthors) {
        this.favoriteAuthors = favoriteAuthors;
    }

}
