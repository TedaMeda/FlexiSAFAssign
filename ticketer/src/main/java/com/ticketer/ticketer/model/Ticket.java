package com.ticketer.ticketer.model;

import jakarta.persistence.*;

@Entity
@Table(name = "ticket")
public class Ticket {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private String id;
    @Column(nullable = false)
    private String task;
    private Status status;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User userId;

    public Ticket() {
    }

    public Ticket(String id, Status status, String task, User userId) {
        this.id = id;
        this.status = status;
        this.task = task;
        this.userId = userId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public String getTask() {
        return task;
    }

    public void setTask(String task) {
        this.task = task;
    }

    public User getUserId() {
        return userId;
    }

    public void setUserId(User userId) {
        this.userId = userId;
    }

    public enum Status{
        TODO,
        PENDING,
        COMPLETED
    }
}
