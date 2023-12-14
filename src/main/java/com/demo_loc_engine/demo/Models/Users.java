package com.demo_loc_engine.demo.Models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "users")
public class Users {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nrik")
    private String nrik;

    @Column(name = "password")
    private String password;

    @Column(name = "is_active")
    private Boolean is_active;

    @Column(name = "role")
    private String role;

    public Long getId() {
        return id;
    }

    public Users() {
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNrik() {
        return nrik;
    }

    public void setNrik(String nrik) {
        this.nrik = nrik;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Boolean getIs_active() {
        return is_active;
    }

    public void setIs_active(Boolean is_active) {
        this.is_active = is_active;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

}
