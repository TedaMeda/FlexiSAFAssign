package com.ticketer.ticketer.repository;

import com.ticketer.ticketer.model.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TicketRepository extends JpaRepository<Ticket, String> {
}
