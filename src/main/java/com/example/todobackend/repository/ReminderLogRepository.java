package com.example.todobackend.repository;

import com.example.todobackend.entity.ReminderLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ReminderLogRepository extends JpaRepository<ReminderLog, Integer> {
    List<ReminderLog> findByTodoId(Integer todoId);
}
