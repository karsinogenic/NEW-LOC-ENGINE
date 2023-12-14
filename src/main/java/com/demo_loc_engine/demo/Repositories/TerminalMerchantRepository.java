package com.demo_loc_engine.demo.Repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.demo_loc_engine.demo.Models.TerminalMerchant;

@Repository
public interface TerminalMerchantRepository extends JpaRepository<TerminalMerchant, Long> {

    @Query("Select u from TerminalMerchant u where u.nama = :nama ")
    Optional<TerminalMerchant> findByNama(@Param("nama") String nama);

}
