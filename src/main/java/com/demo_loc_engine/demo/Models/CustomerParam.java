package com.demo_loc_engine.demo.Models;

import groovy.transform.Generated;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "customer_param")
public class CustomerParam {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;

    private String nama_param;

    private String value;

    @Column(name = "id_operator", nullable = false)
    @NotNull(message = "id_operator ga boleh kosong")
    private Long id_operator;

    @ManyToOne
    @JoinColumn(name = "id_operator", referencedColumnName = "id", insertable = false, updatable = false)
    private Operator operator;

    @Column(name = "kode_channel", nullable = false)
    @NotNull(message = "kode_channel ga boleh kosong")
    private String kode_channel;

    @ManyToOne
    @JoinColumn(name = "kode_channel", referencedColumnName = "kode_channel", insertable = false, updatable = false)
    private Channel channel;

    @Column(name = "is_search")
    private Boolean is_search;

    @Column(name = "column_name")
    private String column_name;

    @Column(name = "or_and")
    private String or_and;

    public Long getId() {
        return Id;
    }

    public void setId(Long id) {
        Id = id;
    }

    public String getNama_param() {
        return nama_param;
    }

    public void setNama_param(String nama_param) {
        this.nama_param = nama_param;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
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

    public Boolean getIs_search() {
        return is_search;
    }

    public void setIs_search(Boolean is_search) {
        this.is_search = is_search;
    }

    public String getColumn_name() {
        return column_name;
    }

    public void setColumn_name(String column_name) {
        this.column_name = column_name;
    }

    public String getOr_and() {
        return or_and;
    }

    public void setOr_and(String or_and) {
        this.or_and = or_and;
    }

}
