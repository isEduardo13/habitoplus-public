package com.habitoplus.habitoplusback.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.habitoplus.habitoplusback.enums.Priority;
import com.habitoplus.habitoplusback.model.Habit;

@Repository
public interface HabitRepository extends JpaRepository<Habit, Integer> {
    @Query("SELECT h FROM Habit h WHERE h.profile.idProfile = :idProfile AND h.category.idCategory = :idCategory")
    List<Habit> findHabitsByCategory(@Param("idProfile") Integer idProfile, @Param("idCategory") Integer idCategory);

    @Query("SELECT h FROM Habit h WHERE h.profile.idProfile = :idProfile AND h.priority = :priority")
    List<Habit> findHabitsByPriority(@Param("idProfile") Integer idProfile, @Param("priority") Priority priority);

    @Query("SELECT h FROM Habit h WHERE h.profile.idProfile = :idProfile")
    List<Habit> findAllHabitsOfAProfile(@Param("idProfile") Integer idProfile);

    @Query("SELECT h FROM Habit h WHERE  h.idHabit = :idHabit")
    Habit findHabitById(@Param("idHabit") Integer idHabit);

    @Query("SELECT h FROM Habit h WHERE h.profile.idProfile = :idProfile")
    Page<Habit> findAllHabitsOfAProfile(@Param("idProfile") Integer idProfile, PageRequest pageable);
}