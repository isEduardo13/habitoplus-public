package com.habitoplus.habitoplusback.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.habitoplus.habitoplusback.model.Notification;
import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Integer> {
    @Query("SELECT n FROM Notification n WHERE n.habit.idHabit = :habitId")
    List<Notification> findByHabitId(@Param("habitId") Integer habitId);

    // Ontener Notificaciones por ID de un perfil

    @Query("SELECT n FROM Notification n WHERE n.habit.profile.idProfile = :profileId")
        Page<Notification> findByProfileId(@Param("profileId") Integer profileId, Pageable pageable);

}