package com.example.todobackend.service;

import com.example.todobackend.common.BusinessException;
import com.example.todobackend.common.Result;
import com.example.todobackend.dto.CategoryRequest;
import com.example.todobackend.entity.Category;
import com.example.todobackend.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class CategoryService {
    @Autowired private CategoryRepository categoryRepository;

    public Result<List<Category>> getCategories(Integer userId) {
        return Result.ok(userId == null ? categoryRepository.findAll() : categoryRepository.findByUserId(userId));
    }

    public Result<Category> createCategory(Integer userId, CategoryRequest req) {
        Category c = new Category();
        c.setUserId(userId);
        c.setName(req.getName());
        c.setColor(req.getColor() != null ? req.getColor() : "#1890FF");
        return Result.ok(categoryRepository.save(c));
    }

    public Result<Category> updateCategory(Integer userId, Integer id, CategoryRequest req) {
        Category cat = userId != null
                ? categoryRepository.findByIdAndUserId(id, userId).orElseThrow(() -> new BusinessException(404, "分类不存在"))
                : categoryRepository.findById(id).orElseThrow(() -> new BusinessException(404, "分类不存在"));
        if (req.getName() != null) cat.setName(req.getName());
        if (req.getColor() != null) cat.setColor(req.getColor());
        return Result.ok(categoryRepository.save(cat));
    }

    public Result<Void> deleteCategory(Integer userId, Integer id) {
        if (userId != null) {
            categoryRepository.findByIdAndUserId(id, userId)
                    .orElseThrow(() -> new BusinessException(404, "分类不存在"));
        } else {
            categoryRepository.findById(id)
                    .orElseThrow(() -> new BusinessException(404, "分类不存在"));
        }
        categoryRepository.deleteById(id);
        return Result.ok();
    }
}
