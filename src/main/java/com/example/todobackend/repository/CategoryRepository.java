package com.example.todobackend.repository;

import com.example.todobackend.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Integer> {
    List<Category> findByUserId(Integer userId);
    Optional<Category> findByIdAndUserId(Integer id, Integer userId);
}
