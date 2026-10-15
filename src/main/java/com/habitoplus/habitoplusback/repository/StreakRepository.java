package com.habitoplus.habitoplusback.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


import com.habitoplus.habitoplusback.model.Streak;

@Repository
public interface StreakRepository extends JpaRepository<Streak, Integer> {

   @Query("SELECT  s FROM Streak s WHERE s.profile.idProfile = :idProfile")
   Streak findAllStreakOfAProfile(@Param("idProfile") Integer idProfile);

}
