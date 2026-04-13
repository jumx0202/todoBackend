package com.example.todobackend.repository;

import com.example.todobackend.entity.Todo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface TodoRepository extends JpaRepository<Todo, Integer> {
    Page<Todo> findByUserId(Integer userId, Pageable pageable);
    Page<Todo> findByUserIdAndStatus(Integer userId, String status, Pageable pageable);
    @Query("SELECT t FROM Todo t WHERE t.userId = :userId AND t.categories.id = :categoryId")
    Page<Todo> findByUserIdAndCategoryId(@Param("userId") Integer userId,
                                          @Param("categoryId") Integer categoryId, Pageable pageable);
    Page<Todo> findByUserIdAndTitleContaining(Integer userId, String keyword, Pageable pageable);
    @Query("SELECT t FROM Todo t WHERE t.remindAt <= :now AND t.isReminded = 0")
    List<Todo> findRemindersDue(@Param("now") LocalDateTime now);
    @Query("SELECT t FROM Todo t WHERE t.repeatRule IS NOT NULL")
    List<Todo> findRepeatTodos();
}
