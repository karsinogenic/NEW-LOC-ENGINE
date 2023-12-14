package com.demo_loc_engine.demo.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.demo_loc_engine.demo.Models.NewAESComponent;
import com.demo_loc_engine.demo.Services.AESComponent;

public interface AESComponentRepository extends JpaRepository<NewAESComponent, Long> {

    @Query("select n from NewAESComponent n where n.nama = :nama")
    NewAESComponent findByNama(@Param("nama") String nama);

}
