package com.demo_loc_engine.demo.loc_ascend.Models;

import java.time.LocalDateTime;
import java.util.Date;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name="kategori_ascore")
public class KategoriAscore {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "namaparam", nullable = false, unique = false)
    @NotBlank(message = "namaparam belum diisi")
    private String namaparam;

    @Column(nullable = false)
    @NotBlank(message = "operator belum diisi")
    private String operator;

    @Column(name = "length", nullable = false)
    @NotNull(message = "length belum diisi")
    private Integer length;

    @Column(name = "trim_length")
    private Integer trim_length;

    @Column(name = "trim_position")
    private String trim_position;

    @Column(name = "description")
    private String description;

    @Column(name = "status_data", columnDefinition = "boolean default true")
    private Boolean statusdata;

    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private Date createdAt;

    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNamaparam() {
        return namaparam;
    }

    public void setNamaparam(String namaparam) {
        this.namaparam = namaparam;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getTrim_length() {
        return trim_length;
    }

    public void setTrim_length(Integer trim_length) {
        this.trim_length = trim_length;
    }

    public String getTrim_position() {
        return trim_position;
    }

    public void setTrim_position(String trim_position) {
        this.trim_position = trim_position;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public Integer getLength() {
        return length;
    }

    public void setLength(Integer length) {
        this.length = length;
    }

    public Boolean getStatus_data() {
        return statusdata;
    }

    public void setStatus_data(Boolean statusdata) {
        this.statusdata = statusdata;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}
