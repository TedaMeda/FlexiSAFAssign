package com.ticketer.ticketer.controller;

import com.ticketer.ticketer.model.Ticket;
import com.ticketer.ticketer.model.User;
import com.ticketer.ticketer.model.TicketStatus;
import com.ticketer.ticketer.repository.TicketRepository;
import com.ticketer.ticketer.repository.UserRepository;
import com.ticketer.ticketer.util.TestDataUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class TicketControllerIntegrationTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private TicketRepository ticketRepository;

    @Autowired
    private UserRepository userRepository;

    private String baseUrl;
    private User testUser;
    private HttpHeaders headers;

    @BeforeEach
    public void setUp() {
        baseUrl = "http://localhost:" + port + "/api/tickets";
        headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        // Clean up database
        ticketRepository.deleteAll();
        userRepository.deleteAll();

        // Create test user
        testUser = TestDataUtil.createTestUser();
        testUser = userRepository.save(testUser);
    }

    @Test
    public void testCreateTicket() {
        // Arrange
        Ticket newTicket = TestDataUtil.createTestTicket(testUser);
        HttpEntity<Ticket> request = new HttpEntity<>(newTicket, headers);

        // Act
        ResponseEntity<Ticket> response = restTemplate.postForEntity(
                baseUrl, request, Ticket.class);

        // Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getId()).isNotNull();
        assertThat(response.getBody().getTitle()).isEqualTo(newTicket.getTitle());
        assertThat(response.getBody().getDescription()).isEqualTo(newTicket.getDescription());
        assertThat(response.getBody().getStatus()).isEqualTo(TicketStatus.OPEN);
        assertThat(response.getBody().getAssignedTo().getId()).isEqualTo(testUser.getId());
    }

    @Test
    public void testGetAllTickets() {
        // Arrange
        List<Ticket> testTickets = TestDataUtil.createTestTickets(testUser, 3);
        testTickets.forEach(ticket -> ticketRepository.save(ticket));

        // Act
        ResponseEntity<List<Ticket>> response = restTemplate.exchange(
                baseUrl,
                HttpMethod.GET,
                new HttpEntity<>(headers),
                new ParameterizedTypeReference<List<Ticket>>() {});

        // Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().size()).isEqualTo(3);
    }

    @Test
    public void testGetTicketById() {
        // Arrange
        Ticket testTicket = TestDataUtil.createTestTicket(testUser);
        testTicket = ticketRepository.save(testTicket);

        // Act
        ResponseEntity<Ticket> response = restTemplate.getForEntity(
                baseUrl + "/" + testTicket.getId(), Ticket.class);

        // Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getId()).isEqualTo(testTicket.getId());
        assertThat(response.getBody().getTitle()).isEqualTo(testTicket.getTitle());
    }

    @Test
    public void testUpdateTicket() {
        // Arrange
        Ticket testTicket = TestDataUtil.createTestTicket(testUser);
        testTicket = ticketRepository.save(testTicket);
        
        testTicket.setTitle("Updated Title");
        testTicket.setDescription("Updated Description");
        testTicket.setStatus(TicketStatus.IN_PROGRESS);
        
        HttpEntity<Ticket> request = new HttpEntity<>(testTicket, headers);

        // Act
        ResponseEntity<Ticket> response = restTemplate.exchange(
                baseUrl + "/" + testTicket.getId(),
                HttpMethod.PUT,
                request,
                Ticket.class);

        // Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getId()).isEqualTo(testTicket.getId());
        assertThat(response.getBody().getTitle()).isEqualTo("Updated Title");
        assertThat(response.getBody().getDescription()).isEqualTo("Updated Description");
        assertThat(response.getBody().getStatus()).isEqualTo(TicketStatus.IN_PROGRESS);
    }

    @Test
    public void testDeleteTicket() {
        // Arrange
        Ticket testTicket = TestDataUtil.createTestTicket(testUser);
        testTicket = ticketRepository.save(testTicket);

        // Act
        ResponseEntity<Void> response = restTemplate.exchange(
                baseUrl + "/" + testTicket.getId(),
                HttpMethod.DELETE,
                new HttpEntity<>(headers),
                Void.class);

        // Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
        assertThat(ticketRepository.findById(testTicket.getId()).isPresent()).isFalse();
    }

    @Test
    public void testGetTicketsByStatus() {
        // Arrange
        List<Ticket> testTickets = TestDataUtil.createTestTickets(testUser, 5);
        testTickets.get(0).setStatus(TicketStatus.OPEN);
        testTickets.get(1).setStatus(TicketStatus.IN_PROGRESS);
        testTickets.get(2).setStatus(TicketStatus.IN_PROGRESS);
        testTickets.get(3).setStatus(TicketStatus.RESOLVED);
        testTickets.get(4).setStatus(TicketStatus.CLOSED);
        testTickets.forEach(ticket -> ticketRepository.save(ticket));

        // Act
        ResponseEntity<List<Ticket>> response = restTemplate.exchange(
                baseUrl + "/status/IN_PROGRESS",
                HttpMethod.GET,
                new HttpEntity<>(headers),
                new ParameterizedTypeReference<List<Ticket>>() {});

        // Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().size()).isEqualTo(2);
        assertThat(response.getBody().stream().allMatch(t -> t.getStatus() == TicketStatus.IN_PROGRESS)).isTrue();
    }

    @Test
    public void testGetTicketsByAssignee() {
        // Arrange
        User anotherUser = TestDataUtil.createTestUser();
        anotherUser = userRepository.save(anotherUser);
        
        List<Ticket> testTickets = TestDataUtil.createTestTickets(testUser, 3);
        List<Ticket> otherTickets = TestDataUtil.createTestTickets(anotherUser, 2);
        
        testTickets.forEach(ticket -> ticketRepository.save(ticket));
        otherTickets.forEach(ticket -> ticketRepository.save(ticket));

        // Act
        ResponseEntity<List<Ticket>> response = restTemplate.exchange(
                baseUrl + "/assignee/" + testUser.getId(),
                HttpMethod.GET,
                new HttpEntity<>(headers),
                new ParameterizedTypeReference<List<Ticket>>() {});

        // Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().size()).isEqualTo(3);
        assertThat(response.getBody().stream().allMatch(t -> t.getAssignedTo().getId().equals(testUser.getId()))).isTrue();
    }
}