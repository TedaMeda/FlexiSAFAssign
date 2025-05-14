package com.ticketer.ticketer.service;

import com.ticketer.ticketer.dto.TicketDto;
import com.ticketer.ticketer.exception.ResourceNotFoundException;
import com.ticketer.ticketer.model.Ticket;
import com.ticketer.ticketer.model.User;
import com.ticketer.ticketer.repository.TicketRepository;
import com.ticketer.ticketer.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TicketService {
    private final TicketRepository ticketRepository;
    private final UserRepository userRepository;

    @Autowired
    public TicketService(TicketRepository ticketRepository, UserRepository userRepository) {
        this.ticketRepository = ticketRepository;
        this.userRepository = userRepository;
    }

    public List<TicketDto> getAllTickets() {
        return ticketRepository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public TicketDto getTicketsById(Long id) {
        Ticket ticket = ticketRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Ticket not found with id: " + id));
        return convertToDto(ticket);
    }

    public List<TicketDto> getTicketsByUser(Long userId) {
        if (!userRepository.existsById(userId)) {
            throw new ResourceNotFoundException("User not found with id: " + userId);
        }
        return ticketRepository.findByUserId(userId).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public List<TicketDto> getTicketsByStatus(String status) {
        return ticketRepository.findByStatus(status).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public List<TicketDto> getUpcomingTickets(int days) {
        LocalDate date = LocalDate.now().plusDays(days);
        return ticketRepository.findUpcomingTickets(date).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public List<TicketDto> getOverdueTickets() {
        return ticketRepository.findOverdueTickets().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public List<TicketDto> searchTickets(String keyword) {
        return ticketRepository.searchTickets(keyword).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public TicketDto createTicket(TicketDto ticketDto) {
        User user = userRepository.findById(ticketDto.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + ticketDto.getUserId()));

        Ticket ticket = convertToEntity(ticketDto);
        Ticket savedTicket = ticketRepository.save(ticket);
        return convertToDto(savedTicket);
    }

    @Transactional
    public TicketDto updateTicket(Long id, TicketDto ticketDto) {
        Ticket existingTicket = ticketRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Ticket not found with id: " + id));

        // If user is being changed
        if (!existingTicket.getUser().getId().equals(ticketDto.getUserId())) {
            User newUser = userRepository.findById(ticketDto.getUserId())
                    .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + ticketDto.getUserId()));
            existingTicket.setUser(newUser);
        }

        existingTicket.setTitle(ticketDto.getTitle());
        existingTicket.setDescription(ticketDto.getDescription());
        existingTicket.setStatus(ticketDto.getStatus());
        existingTicket.setDueDate(ticketDto.getDueDate());
        existingTicket.setPriority(ticketDto.getPriority());

        // If ticket is being marked as completed
        if (ticketDto.isCompleted() && !existingTicket.isCompleted()) {
            existingTicket.setCompleted(true);
            existingTicket.setCompletedAt(LocalDateTime.now());
        } else if (!ticketDto.isCompleted() && existingTicket.isCompleted()) {
            existingTicket.setCompleted(false);
            existingTicket.setCompletedAt(null);
        }

        Ticket updatedTicket = ticketRepository.save(existingTicket);
        return convertToDto(updatedTicket);
    }

    @Transactional
    public void deleteTicket(Long id) {
        if (!ticketRepository.existsById(id)) {
            throw new ResourceNotFoundException("Ticket not found with id: " + id);
        }
        ticketRepository.deleteById(id);
    }

    private TicketDto convertToDto(Ticket ticket) {
        TicketDto dto = new TicketDto();
        dto.setId(ticket.getId());
        dto.setTitle(ticket.getTitle());
        dto.setDescription(ticket.getDescription());
        dto.setStatus(ticket.getStatus());
        dto.setDueDate(ticket.getDueDate());
        dto.setPriority(ticket.getPriority());
        dto.setCompleted(ticket.isCompleted());
        dto.setCompletedAt(ticket.getCompletedAt());
        dto.setUserId(ticket.getUser().getId());

        return dto;
    }

    private Ticket convertToEntity(TicketDto dto) {
        Ticket ticket = new Ticket();
        ticket.setId(dto.getId());
        ticket.setTitle(dto.getTitle());
        ticket.setDescription(dto.getDescription());
        ticket.setStatus(dto.getStatus());
        ticket.setDueDate(dto.getDueDate());
        ticket.setPriority(dto.getPriority());
        ticket.setCompleted(dto.isCompleted());
        ticket.setCompletedAt(dto.getCompletedAt());

        User user = userRepository.findById(dto.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + dto.getUserId()));
        ticket.setUser(user);

        return ticket;
    }
}
