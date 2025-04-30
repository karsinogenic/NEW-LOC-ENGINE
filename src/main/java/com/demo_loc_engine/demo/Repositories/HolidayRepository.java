package com.demo_loc_engine.demo.Repositories;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.demo_loc_engine.demo.Models.HolidayDate;

public interface HolidayRepository extends JpaRepository<HolidayDate,Long>{
    
    @Query("select h from HolidayDate h where h.holidaydate between ?1 and ?2")
    List<HolidayDate> findHolidayBetween(LocalDate ldt1,LocalDate ldt2);
    
    @Query("select h from HolidayDate h where h.holidaydate = ?1")
    List<HolidayDate> findByHolidayDate(LocalDate ldt1);

    @Query("select h from HolidayDate h where h.holidaydate = ?1 and h.is_active = true")
    Optional<HolidayDate> findByHolidayDateActive(LocalDate ldt1);

}
