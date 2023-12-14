package com.demo_loc_engine.demo.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.demo_loc_engine.demo.Models.FirebaseConfig;

public interface FirebaseConfigRepository extends JpaRepository<FirebaseConfig, Long> {

    @Query("select f from FirebaseConfig f where f.notif_type=:status")
    FirebaseConfig findNotifLoc(@Param("status") String status);

}
