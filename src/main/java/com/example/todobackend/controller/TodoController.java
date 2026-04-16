package com.example.todobackend.controller;

import com.example.todobackend.common.Result;
import com.example.todobackend.dto.TodoRequest;
import com.example.todobackend.entity.Todo;
import com.example.todobackend.service.TodoService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/todos")
public class TodoController {
    @Autowired private TodoService todoService;

    private Integer uid(HttpServletRequest r) { return (Integer) r.getAttribute("userId"); }

    @GetMapping
    public Result<Page<Todo>> list(HttpServletRequest r,
            @RequestParam(defaultValue = "") String status,
            @RequestParam(required = false) Integer categoryId,
            @RequestParam(required = false) String keyword,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return todoService.getTodos(uid(r), status, categoryId, keyword,
                PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt")));
    }

    @PostMapping
    public Result<Todo> create(@Valid @RequestBody TodoRequest req, HttpServletRequest r) {
        return todoService.createTodo(uid(r), req);
    }

    @PutMapping("/{id}")
    public Result<Todo> update(@PathVariable Integer id, @RequestBody TodoRequest req, HttpServletRequest r) {
        return todoService.updateTodo(uid(r), id, req);
    }

    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Integer id, HttpServletRequest r) {
        return todoService.deleteTodo(uid(r), id);
    }

    @PutMapping("/{id}/done")
    public Result<Todo> done(@PathVariable Integer id, HttpServletRequest r) {
        return todoService.markDone(uid(r), id);
    }
}
