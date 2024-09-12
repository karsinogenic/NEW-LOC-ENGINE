package com.demo_loc_engine.demo.Repositories;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;

import org.springframework.data.domain.Example;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.repository.query.FluentQuery.FetchableFluentQuery;
import org.springframework.stereotype.Repository;

import com.demo_loc_engine.demo.Models.TerminalMerchant;

@Repository
public interface TerminalMerchantRepository extends JpaRepository<TerminalMerchant, Long> {

    @Query("Select u from TerminalMerchant u where u.nama = :nama ")
    Optional<TerminalMerchant> findByNama(@Param("nama") String nama);

    @Query("Select u.nama from TerminalMerchant u where u.payext = :payext ")
    String[] findByPayext(Boolean payext);
}
