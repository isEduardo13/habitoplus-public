package com.habitoplus.habitoplusback.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.habitoplus.habitoplusback.model.Pixela;

@Repository
public interface PixelaRepository extends JpaRepository<Pixela, Integer>{
    Optional<Pixela> findByUsername(String username);
    @Query("SELECT p FROM Pixela p WHERE p.account.idAccount = :idAccount")
    Optional<Pixela> findByAccountId(@Param("idAccount") Integer idAccount);
}
