package com.habitoplus.habitoplusback.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.habitoplus.habitoplusback.event.HabitStatusUpdatedEvent;
import com.habitoplus.habitoplusback.exception.StreakNotFoundException;
import com.habitoplus.habitoplusback.model.Habit;
import com.habitoplus.habitoplusback.model.Profile;
import com.habitoplus.habitoplusback.model.Streak;
import com.habitoplus.habitoplusback.repository.StreakRepository;

@Service
@Transactional
public class StreakService {

    @Autowired
    private StreakRepository streakRepository;

    @Autowired
    @Lazy
    private HabitService habitService;

    @Transactional
    public void createStreak(Profile profile, Streak streak) {
        streakRepository.save(streak);
    }

    @EventListener
    @Transactional
    public void handleHabitStatusUpdatedEvent(HabitStatusUpdatedEvent event) {
        updateStreak(event.getProfileId());
    }

    @Transactional
    public void updateStreak(int idProfile) {
        List<Habit> habits = habitService.getHabitsByProfile(idProfile);
        Streak streak = streakRepository.findAllStreakOfAProfile(idProfile);

        if (streak == null) {
            throw new StreakNotFoundException("Streak not found for profile id: " + idProfile);
        }

        streak.setCompleteDays(habits.size());
        int contador = 0;

        for (Habit habit : habits) {
            if (habit.getStatus()) {
                contador++;
            }
        }

        if (contador == streak.getCompleteDays()) {
            streak.setConsecutiveDays(streak.getConsecutiveDays() + 1);
        } else {
            streak.setConsecutiveDays(0);
        }

        streakRepository.save(streak);
    }

    public void onlyUpdateStartDate(int idProfile) {
        Streak streak = streakRepository.findAllStreakOfAProfile(idProfile);
        if (streak.getConsecutiveDays() == 0) {
            Date currentDate = new Date();
            streak.setStartDate(currentDate);
            streakRepository.save(streak);
        }
    }

    public Streak getByStreakOfAProfile(Integer idProfile) {
        Streak streak = streakRepository.findAllStreakOfAProfile(idProfile);
        if (streak == null) {
            throw new StreakNotFoundException("No streak found for profile with ID " + idProfile);
        }
        return streak;
    }
    
}