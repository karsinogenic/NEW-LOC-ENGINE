package com.demo_loc_engine.demo.Repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.demo_loc_engine.demo.Models.Users;

@Repository
public interface UsersRepository extends JpaRepository<Users, Long> {

    @Query("select e from Users e where e.nrik=:username")
    List<Users> findByNrik(String username);

}
