package com.ticketer.ticketer.dto;

import jakarta.validation.constraints.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class TicketDto {
    private Long id;

    @NotBlank(message = "Title is required")
    @Size(max = 100, message = "Title cannot exceed 100 characters")
    private String title;

    @Size(max = 1000, message = "Description cannot exceed 1000 characters")
    private String description;

    @NotBlank(message = "Status is required")
    @Pattern(regexp = "^(TODO|IN_PROGRESS|DONE)$", message = "Status must be TODO, IN_PROGRESS, or DONE")
    private String status;

    @FutureOrPresent(message = "Due date must be in the present or future")
    private LocalDate dueDate;

    @Min(value = 1, message = "Priority must be at least 1")
    @Max(value = 5, message = "Priority cannot exceed 5")
    private int priority;

    private boolean completed = false;

    private LocalDateTime completedAt;

    @NotNull(message = "User ID is required")
    private Long userId;

    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

    public LocalDateTime getCompletedAt() {
        return completedAt;
    }

    public void setCompletedAt(LocalDateTime completedAt) {
        this.completedAt = completedAt;
    }

    public @Size(max = 1000, message = "Description cannot exceed 1000 characters") String getDescription() {
        return description;
    }

    public void setDescription(@Size(max = 1000, message = "Description cannot exceed 1000 characters") String description) {
        this.description = description;
    }

    public @FutureOrPresent(message = "Due date must be in the present or future") LocalDate getDueDate() {
        return dueDate;
    }

    public void setDueDate(@FutureOrPresent(message = "Due date must be in the present or future") LocalDate dueDate) {
        this.dueDate = dueDate;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Min(value = 1, message = "Priority must be at least 1")
    @Max(value = 5, message = "Priority cannot exceed 5")
    public int getPriority() {
        return priority;
    }

    public void setPriority(@Min(value = 1, message = "Priority must be at least 1") @Max(value = 5, message = "Priority cannot exceed 5") int priority) {
        this.priority = priority;
    }

    public @NotBlank(message = "Status is required") @Pattern(regexp = "^(TODO|IN_PROGRESS|DONE)$", message = "Status must be TODO, IN_PROGRESS, or DONE") String getStatus() {
        return status;
    }

    public void setStatus(@NotBlank(message = "Status is required") @Pattern(regexp = "^(TODO|IN_PROGRESS|DONE)$", message = "Status must be TODO, IN_PROGRESS, or DONE") String status) {
        this.status = status;
    }

    public @NotBlank(message = "Title is required") @Size(max = 100, message = "Title cannot exceed 100 characters") String getTitle() {
        return title;
    }

    public void setTitle(@NotBlank(message = "Title is required") @Size(max = 100, message = "Title cannot exceed 100 characters") String title) {
        this.title = title;
    }

    public @NotNull(message = "User ID is required") Long getUserId() {
        return userId;
    }

    public void setUserId(@NotNull(message = "User ID is required") Long userId) {
        this.userId = userId;
    }
}
