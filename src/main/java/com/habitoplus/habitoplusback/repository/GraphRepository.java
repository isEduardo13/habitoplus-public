package com.habitoplus.habitoplusback.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.habitoplus.habitoplusback.model.Graph;
import com.habitoplus.habitoplusback.model.Pixela;

@Repository
public interface GraphRepository extends JpaRepository<Graph, Integer>{
    Optional<Graph> findByGraphId(String graphId);
    Optional<Graph> findByPixelaAndGraphId(Pixela pixela, String graphId);
    Optional<Graph> findByPixela_IdPixela(Integer idPixela);
    @Query("SELECT g FROM Graph g JOIN g.pixela p WHERE p.username = :username AND g.graphId = :graphId")
    Graph findGraphByUsernameAndGraphId(@Param("username") String username, @Param("graphId") String graphId);
}
