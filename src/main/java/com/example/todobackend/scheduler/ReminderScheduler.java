package com.example.todobackend.scheduler;

import com.example.todobackend.service.ReminderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class ReminderScheduler {
    @Autowired private ReminderService reminderService;

    @Scheduled(cron = "0 * * * * ?")
    public void checkReminders() { reminderService.checkAndSendReminders(); }

    @Scheduled(cron = "0 1 * * * ?")
    public void handleRepeat() { reminderService.checkRepeatTodos(); }
}
