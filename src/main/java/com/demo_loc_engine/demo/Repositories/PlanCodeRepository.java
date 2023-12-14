package com.demo_loc_engine.demo.Repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.demo_loc_engine.demo.Models.PlanCode;

public interface PlanCodeRepository extends JpaRepository<PlanCode, Long> {

    @Query("Select e from PlanCode e where e.kode_tier = :kode_tier and e.is_active = true")
    List<PlanCode> findByKodeTier(String kode_tier);

    @Query("Select e from PlanCode e where e.kode_tier like %:kode_tier% and e.is_active = true")
    List<PlanCode> findPlanCodeByKodeTier(String kode_tier);

    @Query("Select e.plan_code from PlanCode e where e.kode_tier like %:kode_tier% and e.is_active = true")
    List<String> findPlanCodeNameByKodeTier(String kode_tier);

    @Query("Select e from PlanCode e where e.plan_code = :plan_code and e.is_active = true")
    List<PlanCode> findByPlanCode(String plan_code);

    @Query("Select e from PlanCode e where e.plan_code = :plan_code")
    List<PlanCode> findByPlanCodePPMERL(String plan_code);

    @Query("Select e.plan_code from PlanCode e")
    List<String> allPlanCode();

}
