package com.demo_loc_engine.demo.Repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.demo_loc_engine.demo.Models.Tier;

public interface TierRepository extends JpaRepository<Tier, Long> {

    @Query("select t.kode_tier from Tier t")
    List<String> allTier();

    @Query("select distinct e.kode_tier from Tier e where e.kode_tier like %:kode_channel% and e.is_active = true")
    String[] findKodeTierDistinct(@Param("kode_channel") String kode_channel);

    @Query("select e from Tier e where e.kode_tier =:kode_tier and e.is_active = true")
    List<Tier> findByKode_tier(String kode_tier);
}
