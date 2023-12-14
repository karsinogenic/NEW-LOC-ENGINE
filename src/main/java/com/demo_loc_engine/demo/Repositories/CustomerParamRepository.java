package com.demo_loc_engine.demo.Repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.demo_loc_engine.demo.Models.CustomerParam;

public interface CustomerParamRepository extends JpaRepository<CustomerParam, Long> {

    @Query("select c from CustomerParam c where c.kode_channel = :kode_channel")
    List<CustomerParam> findByKodeChannel(@Param("kode_channel") String kode_channel);

}
