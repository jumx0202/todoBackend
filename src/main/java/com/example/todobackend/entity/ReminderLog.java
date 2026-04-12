package com.example.todobackend.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "reminder_log")
public class ReminderLog {

    public static final String METHOD_BROWSER = "BROWSER";
    public static final String METHOD_EMAIL = "EMAIL";
    public static final String RESULT_SUCCESS = "SUCCESS";
    public static final String RESULT_FAILED = "FAILED";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "todo_id", nullable = false)
    private Integer todoId;

    @Column(name = "reminded_at", nullable = false)
    private LocalDateTime remindedAt;

    @Column(nullable = false, length = 20)
    private String method = METHOD_BROWSER;

    @Column(nullable = false, length = 20)
    private String result = RESULT_SUCCESS;

    @PrePersist
    protected void onCreate() { this.remindedAt = LocalDateTime.now(); }

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    public Integer getTodoId() { return todoId; }
    public void setTodoId(Integer todoId) { this.todoId = todoId; }
    public LocalDateTime getRemindedAt() { return remindedAt; }
    public void setRemindedAt(LocalDateTime remindedAt) { this.remindedAt = remindedAt; }
    public String getMethod() { return method; }
    public void setMethod(String method) { this.method = method; }
    public String getResult() { return result; }
    public void setResult(String result) { this.result = result; }
}
