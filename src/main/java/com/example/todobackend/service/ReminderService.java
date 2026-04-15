package com.example.todobackend.service;

import com.example.todobackend.entity.ReminderLog;
import com.example.todobackend.entity.Todo;
import com.example.todobackend.repository.ReminderLogRepository;
import com.example.todobackend.repository.TodoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class ReminderService {
    @Autowired private TodoRepository todoRepository;
    @Autowired private ReminderLogRepository reminderLogRepository;

    @Transactional
    public void checkAndSendReminders() {
        List<Todo> due = todoRepository.findRemindersDue(LocalDateTime.now());
        for (Todo t : due) {
            boolean ok = sendReminder(t);
            saveLog(t, ok);
            t.setIsReminded(1);
            todoRepository.save(t);
        }
    }

    @Transactional
    public void checkRepeatTodos() {
        List<Todo> repeats = todoRepository.findRepeatTodos();
        for (Todo t : repeats) {
            LocalDateTime next = calcNext(t);
            t.setRemindAt(next);
            t.setIsReminded(0);
            todoRepository.save(t);
        }
    }

    private boolean sendReminder(Todo t) {
        System.out.println("[提醒] 待办ID=" + t.getId() + " 标题=" + t.getTitle());
        return true;
    }

    private void saveLog(Todo t, boolean ok) {
        ReminderLog log = new ReminderLog();
        log.setTodoId(t.getId());
        log.setMethod(ReminderLog.METHOD_BROWSER);
        log.setResult(ok ? ReminderLog.RESULT_SUCCESS : ReminderLog.RESULT_FAILED);
        reminderLogRepository.save(log);
    }

    private LocalDateTime calcNext(Todo t) {
        if (t.getRemindAt() == null) return null;
        return switch (t.getRepeatRule()) {
            case "DAILY" -> t.getRemindAt().plusDays(1);
            case "WEEKLY" -> t.getRemindAt().plusWeeks(1);
            case "MONTHLY" -> t.getRemindAt().plusMonths(1);
            default -> null;
        };
    }

    public List<ReminderLog> getLogs(Integer todoId) {
        return reminderLogRepository.findByTodoId(todoId);
    }
}
