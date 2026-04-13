package com.example.todobackend.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.Set;

public class TodoRequest {
    @NotBlank @Size(max = 200) private String title;
    private String description;
    private String priority;
    private LocalDateTime dueDate;
    private LocalDateTime remindAt;
    private String repeatRule;
    private Set<Integer> categoryIds;
    public String getTitle() { return title; }
    public void setTitle(String t) { this.title = t; }
    public String getDescription() { return description; }
    public void setDescription(String d) { this.description = d; }
    public String getPriority() { return priority; }
    public void setPriority(String p) { this.priority = p; }
    public LocalDateTime getDueDate() { return dueDate; }
    public void setDueDate(LocalDateTime d) { this.dueDate = d; }
    public LocalDateTime getRemindAt() { return remindAt; }
    public void setRemindAt(LocalDateTime r) { this.remindAt = r; }
    public String getRepeatRule() { return repeatRule; }
    public void setRepeatRule(String r) { this.repeatRule = r; }
    public Set<Integer> getCategoryIds() { return categoryIds; }
    public void setCategoryIds(Set<Integer> c) { this.categoryIds = c; }
}
