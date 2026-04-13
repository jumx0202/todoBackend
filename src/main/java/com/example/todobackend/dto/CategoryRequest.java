package com.example.todobackend.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class CategoryRequest {
    @NotBlank @Size(max = 50) private String name;
    private String color;
    public String getName() { return name; }
    public void setName(String n) { this.name = n; }
    public String getColor() { return color; }
    public void setColor(String c) { this.color = c; }
}
