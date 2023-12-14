package com.demo_loc_engine.demo.Models;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.Table;

@Entity
@Table(name = "mfts_response")
public class MftsResponse {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;

    @Column(name = "nama_file", unique = true)
    private String nama_file;

    private String rd_send;

    private String rc_send;

    private Boolean is_send;

    private Boolean is_read;

    private LocalDateTime date_send;

    @Column(name = "status_send")
    private String status_send;

    @Lob
    @Column(name = "isi_file_send", columnDefinition = "text")
    private String isi_file_send;

    private String rd_receive;

    private String rc_receive;

    private Boolean is_receive;

    private LocalDateTime date_receive;

    private LocalDateTime date_create;

    @Column(name = "status_receive")
    private String status_receive;

    @Lob
    @Column(name = "isi_file_receive", columnDefinition = "text")
    private String isi_file_receive;

    public Long getId() {
        return Id;
    }

    public void setId(Long id) {
        Id = id;
    }

    public String getNama_file() {
        return nama_file;
    }

    public void setNama_file(String nama_file) {
        this.nama_file = nama_file;
    }

    public String getRd_send() {
        return rd_send;
    }

    public void setRd_send(String rd_send) {
        this.rd_send = rd_send;
    }

    public String getRc_send() {
        return rc_send;
    }

    public void setRc_send(String rc_send) {
        this.rc_send = rc_send;
    }

    public Boolean getIs_send() {
        return is_send;
    }

    public void setIs_send(Boolean is_send) {
        this.is_send = is_send;
    }

    public LocalDateTime getDate_send() {
        return date_send;
    }

    public void setDate_send(LocalDateTime date_send) {
        this.date_send = date_send;
    }

    public String getStatus_send() {
        return status_send;
    }

    public void setStatus_send(String status_send) {
        this.status_send = status_send;
    }

    public String getIsi_file_send() {
        return isi_file_send;
    }

    public void setIsi_file_send(String isi_file_send) {
        this.isi_file_send = isi_file_send;
    }

    public Boolean getIs_receive() {
        return is_receive;
    }

    public void setIs_receive(Boolean is_receive) {
        this.is_receive = is_receive;
    }

    public LocalDateTime getDate_receive() {
        return date_receive;
    }

    public void setDate_receive(LocalDateTime date_receive) {
        this.date_receive = date_receive;
    }

    public String getStatus_receive() {
        return status_receive;
    }

    public void setStatus_receive(String status_receive) {
        this.status_receive = status_receive;
    }

    public String getIsi_file_receive() {
        return isi_file_receive;
    }

    public void setIsi_file_receive(String isi_file_receive) {
        this.isi_file_receive = isi_file_receive;
    }

    public String getRd_receive() {
        return rd_receive;
    }

    public void setRd_receive(String rd_receive) {
        this.rd_receive = rd_receive;
    }

    public String getRc_receive() {
        return rc_receive;
    }

    public void setRc_receive(String rc_receive) {
        this.rc_receive = rc_receive;
    }

    public Boolean getIs_read() {
        return is_read;
    }

    public void setIs_read(Boolean is_read) {
        this.is_read = is_read;
    }

    public LocalDateTime getDate_create() {
        return date_create;
    }

    public void setDate_create(LocalDateTime date_create) {
        this.date_create = date_create;
    }

}
