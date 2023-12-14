package com.demo_loc_engine.demo.Models;

import java.util.Date;
import java.time.LocalDateTime;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import jakarta.persistence.*;

@Entity
@Table(name = "tier")

public class Tier {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;

    @Column(name = "nama_tier", nullable = false)
    private String nama_tier;

    @Column(name = "kode_tier", nullable = false, unique = true)
    private String kode_tier;

    @Column(name = "kode_channel", nullable = false)
    private String kode_channel;

    @ManyToOne
    @JoinColumn(name = "kode_channel", referencedColumnName = "kode_channel", insertable = false, updatable = false)
    private Channel channel;

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

    public String getNama_tier() {
        return nama_tier;
    }

    public void setNama_tier(String nama_tier) {
        this.nama_tier = nama_tier;
    }

    public String getKode_tier() {
        return kode_tier;
    }

    public void setKode_tier(String kode_tier) {
        this.kode_tier = kode_tier;
    }

    public String getKode_channel() {
        return kode_channel;
    }

    public void setKode_channel(String kode_channel) {
        this.kode_channel = kode_channel;
    }

    public Channel getChannel() {
        return channel;
    }

    public void setChannel(Channel channel) {
        this.channel = channel;
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
