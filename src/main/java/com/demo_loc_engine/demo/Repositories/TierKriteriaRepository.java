package com.demo_loc_engine.demo.Repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.demo_loc_engine.demo.Models.TierKriteria;

@Repository
public interface TierKriteriaRepository extends JpaRepository<TierKriteria, Long> {

    @Query("select e from TierKriteria e where e.kode_tier = :kode and e.is_active = true")
    List<TierKriteria> findByKodeTier(@Param("kode") String kode);

    @Query("select distinct e.kode_tier from TierKriteria e where e.kode_tier like %:kode_channel% and e.is_active = true")
    String[] findKodeTierDistinct(@Param("kode_channel") String kode_channel);
}
