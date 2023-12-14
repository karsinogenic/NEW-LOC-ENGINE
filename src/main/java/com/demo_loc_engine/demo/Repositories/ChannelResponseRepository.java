package com.demo_loc_engine.demo.Repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.demo_loc_engine.demo.Models.ChannelResponse;

@Repository
public interface ChannelResponseRepository extends JpaRepository<ChannelResponse, Long> {

    @Query("select cr from ChannelResponse cr where cr.referenceId like :refId")
    Optional<ChannelResponse> getByReferenceId(@Param("refId") String refId);

}
