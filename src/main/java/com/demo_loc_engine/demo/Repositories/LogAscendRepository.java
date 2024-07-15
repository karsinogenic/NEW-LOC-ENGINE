package com.demo_loc_engine.demo.Repositories;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.demo_loc_engine.demo.Models.LogAscend;

public interface LogAscendRepository extends JpaRepository<LogAscend, Long> {

    @Query("select l from LogAscend l where l.created_at >= :start and l.created_at < :end and l.isGenerated = null and l.bic = :bic")
    List<LogAscend> findByIsNotGeneratedToday(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end,
            @Param("bic") String bic);

    @Query("select l from LogAscend l where l.isGenerated = null and l.rc='00' and l.bic = :bic ")
    List<LogAscend> findByIsNotGenerated(@Param("bic") String bic);

    @Query("select l from LogAscend l where l.isGeneratedPPMERL = null and l.rc='00' and l.bic = :bic ")
    List<LogAscend> findByIsNotGeneratedPPMERL(@Param("bic") String bic);

    @Query("select l from LogAscend l where l.isGeneratedMFTS = null and l.rc='00' and l.bic = :bic")
    List<LogAscend> findLOCTRFData(@Param("bic") String bic);

    @Query("select l from LogAscend l where l.auth_no = :auth_no")
    Optional<LogAscend> findByAuthCode(@Param("auth_no") String authNumber);

    @Query("select l from LogAscend l where l.referenceId = :ref_id")
    Optional<LogAscend> findByRefId(@Param("ref_id") String refId);

    @Query("select l from LogAscend l where l.statusTransfer = :statusTransfer and l.auth_no is not null and l.isvoided = false")
    List<LogAscend> findByStatusTransferAndAuth_noNotNull(@Param("statusTransfer") String statusTransfer);

    @Query("select l from LogAscend l where l.created_at BETWEEN :start AND :end")
    List<LogAscend> findByCreated_at(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end);

    @Query("select l from LogAscend l where l.referenceId in ?1")
    List<LogAscend> findByListRefId(List list);
}
