package com.demo_loc_engine.demo.Models;

import java.util.Date;

import org.springframework.data.annotation.CreatedDate;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "tier_kriteria")

public class TierKriteria {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;

    @Column(name = "id_kriteria", nullable = false)
    @NotNull(message = "id kriteria ga boleh kosong")
    private Long id_kriteria;

    @ManyToOne
    @JoinColumn(name = "id_kriteria", referencedColumnName = "id", insertable = false, updatable = false)
    private Kriteria kriteria;

    @Column(name = "kode_tier", nullable = false)
    @NotNull(message = "kode_tier ga boleh kosong")
    @Size(min = 5, max = 6, message = "kode_tier panjangnya antara 5-6 karakter")
    private String kode_tier;

    @ManyToOne
    @JoinColumn(name = "kode_tier", referencedColumnName = "kode_tier", insertable = false, updatable = false)
    private Tier tier;

    @Column(name = "id_operator", nullable = false)
    @NotNull(message = "id_operator ga boleh kosong")
    private Long id_operator;

    @ManyToOne
    @JoinColumn(name = "id_operator", referencedColumnName = "id", insertable = false, updatable = false)
    private Operator operator;

    @Column(name = "is_active", nullable = false)
    @NotNull(message = "is_active ga boleh kosong")
    private Boolean is_active;

    @Column(name = "created_at", updatable = false)
    private Date createdAt;

    @Column(name = "value", nullable = false)
    @NotNull(message = "value param ga boleh kosong")
    private String value;

    @Column(name = "and_or")
    private String and_or;

    public Long getId() {
        return Id;
    }

    public void setId(Long id) {
        Id = id;
    }

    public Long getId_kriteria() {
        return id_kriteria;
    }

    public void setId_kriteria(Long id_kriteria) {
        this.id_kriteria = id_kriteria;
    }

    public Kriteria getKriteria() {
        return kriteria;
    }

    public void setKriteria(Kriteria kriteria) {
        this.kriteria = kriteria;
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

    public Long getId_operator() {
        return id_operator;
    }

    public void setId_operator(Long id_operator) {
        this.id_operator = id_operator;
    }

    public Operator getOperator() {
        return operator;
    }

    public void setOperator(Operator operator) {
        this.operator = operator;
    }

    // public Integer getVal_int() {
    // return val_int;
    // }

    // public void setVal_int(Integer val_int) {
    // this.val_int = val_int;
    // }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
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

    public String getAnd_or() {
        return and_or;
    }

    public void setAnd_or(String and_or) {
        this.and_or = and_or;
    }

}
