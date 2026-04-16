package com.example.todobackend.controller;

import com.example.todobackend.common.Result;
import com.example.todobackend.entity.ReminderLog;
import com.example.todobackend.service.ReminderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reminders")
public class ReminderController {
    @Autowired private ReminderService reminderService;

    @GetMapping("/logs")
    public Result<List<ReminderLog>> logs(@RequestParam Integer todoId) {
        return Result.ok(reminderService.getLogs(todoId));
    }
}
