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
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "test_ascore")
public class TestAscore {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nama_param", nullable = false)
    @NotBlank(message = "namaparam belum diisi")
    private String namaparam;

    @ManyToOne
    @JoinColumn(name = "nama_param", referencedColumnName = "namaparam", insertable = false, updatable = false)
    private KategoriAscore kategoriAscore;

    // @NotBlank
    @Column(name = "value_str")
    private String valuestr;

    // @NotNull
    // @Pattern(regexp="[\\d]",message = "value1 harus angka")
    private Long value1;
    // @NotNull
    // @Pattern(regexp="[\\d]",message = "value2 harus angka")
    private Long value2;

    @Column(name = "is_slik")
    @NotNull(message = "is_slik belum diisi")
    private Boolean isslik;

    @Column(nullable = false)
    @NotNull(message = "score belum diisi")
    private Integer score;


    // @Column(nullable = false)
    // @NotBlank(message = "operator belum diisi")
    // private String operator;

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

    public String getValue_str() {
        return valuestr;
    }

    public void setValue_str(String value_str) {
        this.valuestr = value_str;
    }

    public Long getValue1() {
        return value1;
    }

    public void setValue1(Long value1) {
        this.value1 = value1;
    }

    public Long getValue2() {
        return value2;
    }

    public void setValue2(Long value2) {
        this.value2 = value2;
    }

    public Integer getScore() {
        return score;
    }

    public void setScore(Integer score) {
        this.score = score;
    }

    public Boolean getIs_slik() {
        return isslik;
    }

    public void setIs_slik(Boolean is_slik) {
        this.isslik = is_slik;
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

    public Boolean getStatus_data() {
        return statusdata;
    }

    public void setStatus_data(Boolean statusdata) {
        this.statusdata = statusdata;
    }

    public KategoriAscore getKategoriAscore() {
        return kategoriAscore;
    }

    public void setKategoriAscore(KategoriAscore kategoriAscore) {
        this.kategoriAscore = kategoriAscore;
    }
}
