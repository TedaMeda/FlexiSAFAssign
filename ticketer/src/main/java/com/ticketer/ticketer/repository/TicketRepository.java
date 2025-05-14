package com.ticketer.ticketer.repository;

import com.ticketer.ticketer.model.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface TicketRepository extends JpaRepository<Ticket, Long> {
    List<Ticket> findByUserId(Long userId);
    List<Ticket> findByUserIdAndCompleted(Long userId, boolean completed);
    List<Ticket> findByStatus(String status);

    @Query("SELECT t FROM Ticket t WHERE t.dueDate <= :date AND t.completed = false")
    List<Ticket> findUpcomingTickets(@Param("date") LocalDate date);

    @Query("SELECT t FROM Ticket t WHERE t.dueDate < CURRENT_DATE AND t.completed = false")
    List<Ticket> findOverdueTickets();

    @Query("SELECT t FROM Ticket t WHERE LOWER(t.title) LIKE LOWER(CONCAT('%', :keyword, '%')) OR LOWER(t.description) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    List<Ticket> searchTickets(@Param("keyword") String keyword);
}
