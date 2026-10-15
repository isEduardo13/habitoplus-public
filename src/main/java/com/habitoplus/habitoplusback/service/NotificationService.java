package com.habitoplus.habitoplusback.service;

import java.util.List;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import com.habitoplus.habitoplusback.dto.NotificationDTO;
import com.habitoplus.habitoplusback.model.Habit;
import com.habitoplus.habitoplusback.model.Notification;
import com.habitoplus.habitoplusback.repository.HabitRepository;
import com.habitoplus.habitoplusback.repository.NotificationRepository;
import com.habitoplus.habitoplusback.exception.UserNotFoundException;
import com.habitoplus.habitoplusback.exception.GroupEmptyException;

@Service
@Transactional
public class NotificationService {

    @Autowired
    private NotificationRepository notificationRepository;

    @Autowired
    private HabitRepository habitRepository;

    public Page<Notification> getNotificationsByProfileId(Integer profileId, int page, int pageSize) {
        PageRequest pageRequest = PageRequest.of(page, pageSize);
        return notificationRepository.findByProfileId(profileId, pageRequest);
    }

    public void createNotification(NotificationDTO notificationDTO) throws UserNotFoundException {
        Notification notification = new Notification();

        notification.setMessage(notificationDTO.getMessage());
        notification.setType(notificationDTO.getType());
        notification.setDateNotification(notificationDTO.getDateNotification());
        notification.setIsRead(notificationDTO.getIsRead());

        Habit habit = habitRepository.findById(notificationDTO.getIdHabit())
                .orElseThrow(() -> new UserNotFoundException("Habit ID not found: " + notificationDTO.getIdHabit()));
        notification.setHabit(habit);
        notificationRepository.save(notification);
    }

    public List<Notification> getNotificationsByHabit(Habit habit) {
        if (habit == null) {
            throw new GroupEmptyException("Habit cannot be null");
        }
        return notificationRepository.findByHabitId(habit.getIdHabit());
    }

    public void deleteNotification(Integer id) throws UserNotFoundException {
        if (!notificationRepository.existsById(id)) {
            throw new UserNotFoundException("Notification ID not found: " + id);
        }
        notificationRepository.deleteById(id);
    }

    @Transactional
    public void markAsReadAndDeleteReadNotifications(Boolean isRead, Integer habitId) {
        List<Notification> notifications = notificationRepository.findByHabitId(habitId);
        if (notifications.isEmpty()) {
            throw new GroupEmptyException("No notifications found for habit ID: " + habitId);
        }
        if (isRead) {
            for (Notification notification : notifications) {
                notificationRepository.delete(notification);
            }
        }
    }
}
