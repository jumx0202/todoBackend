package com.example.todobackend;

import com.example.todobackend.entity.Todo;
import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;
import static org.junit.jupiter.api.Assertions.*;

class TodoEntityTest {
    @Test void testDefaultStatus() { assertEquals("TODO", new Todo().getStatus()); }
    @Test void testDefaultPriority() { assertEquals("MEDIUM", new Todo().getPriority()); }
    @Test void testDefaultIsReminded() { assertEquals(0, new Todo().getIsReminded()); }
    @Test void testIsExpired_pastDueAndTodo() {
        Todo t = new Todo();
        t.setDueDate(LocalDateTime.now().minusDays(1));
        t.setStatus(Todo.STATUS_TODO);
        assertTrue(t.isExpired());
    }
    @Test void testIsExpired_pastDueButDone() {
        Todo t = new Todo();
        t.setDueDate(LocalDateTime.now().minusDays(1));
        t.setStatus(Todo.STATUS_DONE);
        assertFalse(t.isExpired());
    }
    @Test void testIsExpired_futureDue() {
        Todo t = new Todo();
        t.setDueDate(LocalDateTime.now().plusDays(1));
        assertFalse(t.isExpired());
    }
    @Test void testStatusConstants() {
        assertEquals("TODO", Todo.STATUS_TODO);
        assertEquals("IN_PROGRESS", Todo.STATUS_IN_PROGRESS);
        assertEquals("DONE", Todo.STATUS_DONE);
    }
    @Test void testPriorityConstants() {
        assertEquals("HIGH", Todo.PRIORITY_HIGH);
        assertEquals("MEDIUM", Todo.PRIORITY_MEDIUM);
        assertEquals("LOW", Todo.PRIORITY_LOW);
    }
}
