package com.example.todobackend;

import com.example.todobackend.common.BusinessException;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class BusinessExceptionTest {
    @Test void testDefaultCode() {
        BusinessException e = new BusinessException("msg");
        assertEquals(400, e.getCode());
        assertEquals("msg", e.getMessage());
    }
    @Test void testCustomCode() {
        BusinessException e = new BusinessException(404, "not found");
        assertEquals(404, e.getCode());
        assertEquals("not found", e.getMessage());
    }
}
