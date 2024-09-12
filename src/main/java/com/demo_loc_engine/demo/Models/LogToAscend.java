package com.demo_loc_engine.demo.Models;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "log_to_ascend")
public class LogToAscend {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;

    @Column(name = "reference_id", nullable = false,unique = true)
    private String referenceId;

    @Column(columnDefinition = "TEXT")
    private String input;

    @Column(columnDefinition = "TEXT")
    private String output;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "redial_at")
    private LocalDateTime redialAt;

    public Long getId() {
        return Id;
    }

    public LocalDateTime getRedialAt() {
        return redialAt;
    }

    public void setRedialAt(LocalDateTime redialAt) {
        this.redialAt = redialAt;
    }

    public void setId(Long id) {
        Id = id;
    }

    public String getInput() {
        return input;
    }

    public void setInput(String input) {
        this.input = input;
    }

    public String getOutput() {
        return output;
    }

    public void setOutput(String output) {
        this.output = output;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public String getReferenceId() {
        return referenceId;
    }

    public void setReferenceId(String referenceId) {
        this.referenceId = referenceId;
    }

}
