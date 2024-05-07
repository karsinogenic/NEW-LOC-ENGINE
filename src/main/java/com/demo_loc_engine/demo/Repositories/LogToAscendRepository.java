package com.demo_loc_engine.demo.Repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.demo_loc_engine.demo.Models.LogToAscend;

public interface LogToAscendRepository extends JpaRepository<LogToAscend, Long> {

    @Query("Select l From LogToAscend l where l.referenceId = :refId order by l.id DESC")
    public List<LogToAscend> findByRefId(@Param("refId") String refId);

    @Query("Select l From LogToAscend l where l.referenceId = :refId order by l.id DESC")
    public List<LogToAscend> findByRefIdNew(@Param("refId") String refId);
}
