package com.ticketer.ticketer.controller;

import com.ticketer.ticketer.model.User;
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
public class UserControllerIntegrationTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private UserRepository userRepository;

    private String baseUrl;
    private HttpHeaders headers;

    @BeforeEach
    public void setUp() {
        baseUrl = "http://localhost:" + port + "/api/users";
        headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        // Clean up database
        userRepository.deleteAll();
    }

    @Test
    public void testCreateUser() {
        // Arrange
        User newUser = TestDataUtil.createTestUser();
        HttpEntity<User> request = new HttpEntity<>(newUser, headers);

        // Act
        ResponseEntity<User> response = restTemplate.postForEntity(
                baseUrl, request, User.class);

        // Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getId()).isNotNull();
        assertThat(response.getBody().getUsername()).isEqualTo(newUser.getUsername());
        assertThat(response.getBody().getEmail()).isEqualTo(newUser.getEmail());
    }

    @Test
    public void testGetAllUsers() {
        // Arrange
        User user1 = TestDataUtil.createTestUser();
        User user2 = TestDataUtil.createTestUser();
        User user3 = TestDataUtil.createTestUser();
        
        userRepository.save(user1);
        userRepository.save(user2);
        userRepository.save(user3);

        // Act
        ResponseEntity<List<User>> response = restTemplate.exchange(
                baseUrl,
                HttpMethod.GET,
                new HttpEntity<>(headers),
                new ParameterizedTypeReference<List<User>>() {});

        // Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().size()).isEqualTo(3);
    }

    @Test
    public void testGetUserById() {
        // Arrange
        User testUser = TestDataUtil.createTestUser();
        testUser = userRepository.save(testUser);

        // Act
        ResponseEntity<User> response = restTemplate.getForEntity(
                baseUrl + "/" + testUser.getId(), User.class);

        // Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getId()).isEqualTo(testUser.getId());
        assertThat(response.getBody().getUsername()).isEqualTo(testUser.getUsername());
        assertThat(response.getBody().getEmail()).isEqualTo(testUser.getEmail());
    }

    @Test
    public void testUpdateUser() {
        // Arrange
        User testUser = TestDataUtil.createTestUser();
        testUser = userRepository.save(testUser);
        
        testUser.setFirstName("Updated");
        testUser.setLastName("Name");
        testUser.setEmail("updated" + System.currentTimeMillis() + "@example.com");
        
        HttpEntity<User> request = new HttpEntity<>(testUser, headers);

        // Act
        ResponseEntity<User> response = restTemplate.exchange(
                baseUrl + "/" + testUser.getId(),
                HttpMethod.PUT,
                request,
                User.class);

        // Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getId()).isEqualTo(testUser.getId());
        assertThat(response.getBody().getFirstName()).isEqualTo("Updated");
        assertThat(response.getBody().getLastName()).isEqualTo("Name");
        assertThat(response.getBody().getEmail()).isEqualTo(testUser.getEmail());
    }

    @Test
    public void testDeleteUser() {
        // Arrange
        User testUser = TestDataUtil.createTestUser();
        testUser = userRepository.save(testUser);

        // Act
        ResponseEntity<Void> response = restTemplate.exchange(
                baseUrl + "/" + testUser.getId(),
                HttpMethod.DELETE,
                new HttpEntity<>(headers),
                Void.class);

        // Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
        assertThat(userRepository.findById(testUser.getId()).isPresent()).isFalse();
    }

    @Test
    public void testFindUserByUsername() {
        // Arrange
        User testUser = TestDataUtil.createTestUser();
        testUser = userRepository.save(testUser);

        // Act
        ResponseEntity<User> response = restTemplate.getForEntity(
                baseUrl + "/username/" + testUser.getUsername(), User.class);

        // Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getId()).isEqualTo(testUser.getId());
        assertThat(response.getBody().getUsername()).isEqualTo(testUser.getUsername());
    }

    @Test
    public void testFindUserByEmail() {
        // Arrange
        User testUser = TestDataUtil.createTestUser();
        testUser = userRepository.save(testUser);

        // Act
        ResponseEntity<User> response = restTemplate.getForEntity(
                baseUrl + "/email/" + testUser.getEmail(), User.class);

        // Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getId()).isEqualTo(testUser.getId());
        assertThat(response.getBody().getEmail()).isEqualTo(testUser.getEmail());
    }
}