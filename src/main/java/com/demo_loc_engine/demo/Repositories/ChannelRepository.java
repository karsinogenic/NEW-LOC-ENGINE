package com.demo_loc_engine.demo.Repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.demo_loc_engine.demo.Models.Channel;

public interface ChannelRepository extends JpaRepository<Channel, Long> {

    List<Channel> findByActiveTrue();

    @Query("select c from Channel c where c.kode_channel=:kode")
    Optional<Channel> findByKodeChannel(@Param("kode") String kode);

}
