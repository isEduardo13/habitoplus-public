package com.habitoplus.habitoplusback.component;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.habitoplus.habitoplusback.service.StreakService;
import com.habitoplus.habitoplusback.service.ProfileService;
import com.habitoplus.habitoplusback.model.Profile;


import java.util.List;

@Component
public class StreakScheduler {

    @Autowired
    @Lazy
    private StreakService streakService;

    @Autowired
    private ProfileService profileService;

    @Scheduled(cron = "0 0 0 * * ?")
    public void checkStreaks() {
        List<Profile> profiles = profileService.getAllProfiles( );
        for (Profile profile : profiles) {
            streakService.onlyUpdateStartDate(profile.getIdProfile());
        }
    }
}