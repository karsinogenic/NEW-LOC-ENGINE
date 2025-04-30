package com.demo_loc_engine.demo.Models;

import java.time.LocalDate;

import org.hibernate.validator.constraints.UniqueElements;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table
@Data
@AllArgsConstructor
@NoArgsConstructor
public class HolidayDate {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;

    @NotNull
    @Column(unique = true)
    // @UniqueElements
    private LocalDate holidaydate;

    @NotNull
    private String description;

    @Column(name = "is_active",columnDefinition = "Boolean DEFAULT true",nullable = false)
    private Boolean is_active;
}
