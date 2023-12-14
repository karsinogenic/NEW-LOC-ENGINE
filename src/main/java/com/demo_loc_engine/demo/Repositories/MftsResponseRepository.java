package com.demo_loc_engine.demo.Repositories;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.demo_loc_engine.demo.Models.MftsResponse;

public interface MftsResponseRepository extends JpaRepository<MftsResponse, Long> {

    @Query("Select m from MftsResponse m where m.nama_file = :nama")
    Optional<MftsResponse> findByNamaFile(@Param("nama") String nama_file);

    @Query("Select m from MftsResponse m where m.is_send = null")
    List<MftsResponse> findFileNotSent();

    @Query("Select m from MftsResponse m where m.is_send = true and (m.is_receive is null or m.is_receive = false)")
    List<MftsResponse> findMFTSFile();

    @Query("Select m from MftsResponse m where m.is_read is null and m.is_receive = true and m.isi_file_receive is not null")
    List<MftsResponse> findMFTSFileNotRead();

    @Query("select m from MftsResponse m where m.date_create Between :start and :end ")
    List<MftsResponse> findSequenceData(@Param(value = "start") LocalDateTime start_date,
            @Param(value = "end") LocalDateTime end_date);
}
