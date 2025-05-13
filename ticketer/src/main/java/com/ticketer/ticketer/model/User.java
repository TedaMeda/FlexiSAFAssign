package com.ticketer.ticketer.model;

import jakarta.persistence.*;

import java.util.Set;

@Entity
@Table(name = "user")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private String id;
    @Column(nullable = false)
    private String name;
    @Column(nullable = false, unique = true)
    private String email;

    @OneToMany(mappedBy = "userId", cascade = CascadeType.REMOVE)
    private Set<Ticket> ticket;

    public User() {
    }

    public User(String email, String id, String name, Set<Ticket> ticket) {
        this.email = email;
        this.id = id;
        this.name = name;
        this.ticket = ticket;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<Ticket> getTicket() {
        return ticket;
    }

    public void setTicket(Set<Ticket> ticket) {
        this.ticket = ticket;
    }
}
