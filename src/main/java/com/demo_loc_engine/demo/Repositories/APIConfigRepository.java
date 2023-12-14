package com.demo_loc_engine.demo.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.demo_loc_engine.demo.Models.APIConfig;

public interface APIConfigRepository extends JpaRepository<APIConfig, Long> {

    @Query("Select i.url from APIConfig i where i.nama=:nama")
    String findIpByNama(@Param("nama") String nama);

}
