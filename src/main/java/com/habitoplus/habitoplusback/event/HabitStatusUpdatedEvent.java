package com.habitoplus.habitoplusback.event;
import org.springframework.context.ApplicationEvent;

public class HabitStatusUpdatedEvent extends ApplicationEvent {
    private final int profileId;

    public HabitStatusUpdatedEvent(Object source, int profileId) {
        super(source);
        this.profileId = profileId;
    }

    public int getProfileId() {
        return profileId;
    }
}