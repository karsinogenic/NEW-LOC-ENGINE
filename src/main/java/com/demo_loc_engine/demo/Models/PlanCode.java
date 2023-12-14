package com.demo_loc_engine.demo.Models;

import java.time.LocalDateTime;
import java.util.Date;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "plan_code")
public class PlanCode {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;

    @Column(name = "kode_tier", nullable = false)
    private String kode_tier;

    @ManyToOne
    @JoinColumn(name = "kode_tier", referencedColumnName = "kode_tier", insertable = false, updatable = false)
    private Tier tier;

    @Column(name = "plan_code", nullable = false)
    private String plan_code;

    @Column(name = "interest", nullable = false)
    private Double interest;

    @Column(name = "tenor", nullable = false)
    private Integer tenor;

    @Column(name = "interest_type", nullable = false)
    private String interest_type;

    @Column(name = "interest_channel", nullable = false)
    private String interest_channel;

    @Column(name = "is_active", nullable = false)
    private Boolean is_active;

    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private Date createdAt;

    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    public Long getId() {
        return Id;
    }

    public void setId(Long id) {
        Id = id;
    }

    public String getKode_tier() {
        return kode_tier;
    }

    public void setKode_tier(String kode_tier) {
        this.kode_tier = kode_tier;
    }

    public Tier getTier() {
        return tier;
    }

    public void setTier(Tier tier) {
        this.tier = tier;
    }

    public String getPlan_code() {
        return plan_code;
    }

    public void setPlan_code(String plan_code) {
        this.plan_code = plan_code;
    }

    public Double getInterest() {
        return interest;
    }

    public void setInterest(Double interest) {
        this.interest = interest;
    }

    public Integer getTenor() {
        return tenor;
    }

    public void setTenor(Integer tenor) {
        this.tenor = tenor;
    }

    public String getInterest_type() {
        return interest_type;
    }

    public void setInterest_type(String interest_type) {
        this.interest_type = interest_type;
    }

    public String getInterest_channel() {
        return interest_channel;
    }

    public void setInterest_channel(String interest_channel) {
        this.interest_channel = interest_channel;
    }

    public Boolean getIs_active() {
        return is_active;
    }

    public void setIs_active(Boolean is_active) {
        this.is_active = is_active;
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
