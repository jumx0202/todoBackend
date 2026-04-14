package com.example.todobackend.service;

import com.example.todobackend.common.BusinessException;
import com.example.todobackend.common.Result;
import com.example.todobackend.dto.TodoRequest;
import com.example.todobackend.entity.Category;
import com.example.todobackend.entity.Todo;
import com.example.todobackend.repository.CategoryRepository;
import com.example.todobackend.repository.TodoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.HashSet;
import java.util.Set;

@Service
public class TodoService {
    @Autowired private TodoRepository todoRepository;
    @Autowired private CategoryRepository categoryRepository;

    public Result<Page<Todo>> getTodos(Integer userId, String status, Integer categoryId,
                                       String keyword, Pageable pageable) {
        Page<Todo> page;
        if (keyword != null && !keyword.isBlank())
            page = todoRepository.findByUserIdAndTitleContaining(userId, keyword, pageable);
        else if (categoryId != null)
            page = todoRepository.findByUserIdAndCategoryId(userId, categoryId, pageable);
        else if (status != null && !status.isBlank())
            page = todoRepository.findByUserIdAndStatus(userId, status, pageable);
        else
            page = todoRepository.findByUserId(userId, pageable);
        return Result.ok(page);
    }

    @Transactional
    public Result<Todo> createTodo(Integer userId, TodoRequest req) {
        Todo t = new Todo();
        t.setUserId(userId);
        copyRequestToTodo(t, req);
        t.setCategories(fetchCategories(userId, req.getCategoryIds()));
        return Result.ok(todoRepository.save(t));
    }

    @Transactional
    public Result<Todo> updateTodo(Integer userId, Integer todoId, TodoRequest req) {
        Todo t = findByIdAndUser(todoId, userId);
        copyRequestToTodo(t, req);
        t.setCategories(fetchCategories(userId, req.getCategoryIds()));
        return Result.ok(todoRepository.save(t));
    }

    @Transactional
    public Result<Void> deleteTodo(Integer userId, Integer todoId) {
        todoRepository.delete(findByIdAndUser(todoId, userId));
        return Result.ok();
    }

    @Transactional
    public Result<Todo> markDone(Integer userId, Integer todoId) {
        Todo t = findByIdAndUser(todoId, userId);
        t.setStatus(Todo.STATUS_DONE);
        return Result.ok(todoRepository.save(t));
    }

    private Todo findByIdAndUser(Integer todoId, Integer userId) {
        Todo t = todoRepository.findById(todoId)
                .orElseThrow(() -> new BusinessException(404, "待办不存在"));
        if (!t.getUserId().equals(userId))
            throw new BusinessException(403, "无权访问此待办");
        return t;
    }

    private void copyRequestToTodo(Todo t, TodoRequest req) {
        if (req.getTitle() != null) t.setTitle(req.getTitle());
        if (req.getDescription() != null) t.setDescription(req.getDescription());
        if (req.getPriority() != null) t.setPriority(req.getPriority());
        if (req.getDueDate() != null) t.setDueDate(req.getDueDate());
        if (req.getRemindAt() != null) t.setRemindAt(req.getRemindAt());
        if (req.getRepeatRule() != null) t.setRepeatRule(req.getRepeatRule());
    }

    private Set<Category> fetchCategories(Integer userId, Set<Integer> ids) {
        if (ids == null || ids.isEmpty()) return new HashSet<>();
        Set<Category> cats = new HashSet<>();
        for (Integer cid : ids) {
            cats.add(categoryRepository.findByIdAndUserId(cid, userId)
                    .orElseThrow(() -> new BusinessException("分类不存在：id=" + cid)));
        }
        return cats;
    }
}
