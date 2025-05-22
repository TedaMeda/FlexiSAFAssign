package com.ticketer.ticketer.util;

import com.ticketer.ticketer.model.Ticket;
import com.ticketer.ticketer.model.User;
import com.ticketer.ticketer.model.TicketStatus;
import com.ticketer.ticketer.model.TicketPriority;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class TestDataUtil {

    public static User createTestUser() {
        User user = new User();
        user.setUsername("testuser" + UUID.randomUUID().toString().substring(0, 8));
        user.setEmail("test" + UUID.randomUUID().toString().substring(0, 8) + "@example.com");
        user.setFirstName("Test");
        user.setLastName("User");
        user.setPassword("password123");
        return user;
    }

    public static Ticket createTestTicket(User assignedTo) {
        Ticket ticket = new Ticket();
        ticket.setTitle("Test Ticket " + UUID.randomUUID().toString().substring(0, 8));
        ticket.setDescription("This is a test ticket description");
        ticket.setStatus(TicketStatus.OPEN);
        ticket.setPriority(TicketPriority.MEDIUM);
        ticket.setCreatedBy(assignedTo);
        ticket.setAssignedTo(assignedTo);
        ticket.setCreatedAt(LocalDateTime.now());
        ticket.setUpdatedAt(LocalDateTime.now());
        return ticket;
    }

    public static List<Ticket> createTestTickets(User assignedTo, int count) {
        List<Ticket> tickets = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            tickets.add(createTestTicket(assignedTo));
        }
        return tickets;
    }
}