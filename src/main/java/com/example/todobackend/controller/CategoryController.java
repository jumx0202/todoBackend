package com.example.todobackend.controller;

import com.example.todobackend.common.Result;
import com.example.todobackend.dto.CategoryRequest;
import com.example.todobackend.entity.Category;
import com.example.todobackend.service.CategoryService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
public class CategoryController {
    @Autowired private CategoryService categoryService;

    private Integer uid(HttpServletRequest r) { return (Integer) r.getAttribute("userId"); }

    @GetMapping public Result<List<Category>> list(HttpServletRequest r) { return categoryService.getCategories(uid(r)); }
    @PostMapping public Result<Category> create(@Valid @RequestBody CategoryRequest req, HttpServletRequest r) { return categoryService.createCategory(uid(r), req); }
    @PutMapping("/{id}") public Result<Category> update(@PathVariable Integer id, @RequestBody CategoryRequest req, HttpServletRequest r) { return categoryService.updateCategory(uid(r), id, req); }
    @DeleteMapping("/{id}") public Result<Void> delete(@PathVariable Integer id, HttpServletRequest r) { return categoryService.deleteCategory(uid(r), id); }
}
