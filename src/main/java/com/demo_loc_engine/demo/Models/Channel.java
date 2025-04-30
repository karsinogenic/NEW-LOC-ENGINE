package com.demo_loc_engine.demo.Models;

import java.util.Date;
import java.time.LocalDateTime;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import jakarta.persistence.*;

@Entity
@Table(name = "channel")
public class Channel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nama_channel", nullable = false, unique = true)
    private String nama_channel;

    @Column(name = "kode_channel", nullable = false, unique = true)
    private String kode_channel;

    @Column(name = "active", nullable = false)
    private Boolean active;

    @CreatedDate
    @Column(name = "created_at", updatable = false)
    private Date createdAt;

    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "ovb_start")
    private Integer ovbStart;
    @Column(name = "ovb_end")
    private Integer ovbEnd;
    @Column(name = "skn_start")
    private Integer sknStart;
    @Column(name = "skn_end")
    private Integer sknEnd;

    public Integer getOvbStart() {
        return ovbStart;
    }

    public void setOvbStart(Integer ovbStart) {
        this.ovbStart = ovbStart;
    }

    public Integer getOvbEnd() {
        return ovbEnd;
    }

    public void setOvbEnd(Integer ovbEnd) {
        this.ovbEnd = ovbEnd;
    }

    public Integer getSknStart() {
        return sknStart;
    }

    public void setSknStart(Integer sknStart) {
        this.sknStart = sknStart;
    }

    public Integer getSknEnd() {
        return sknEnd;
    }

    public void setSknEnd(Integer sknEnd) {
        this.sknEnd = sknEnd;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNama_channel() {
        return nama_channel;
    }

    public void setNama_channel(String nama_channel) {
        this.nama_channel = nama_channel;
    }

    public String getKode_channel() {
        return kode_channel;
    }

    public void setKode_channel(String kode_channel) {
        this.kode_channel = kode_channel;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
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
