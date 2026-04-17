package com.example.todobackend;

import com.example.todobackend.common.Result;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ResultTest {
    @Test void testOkWithData() {
        Result<String> r = Result.ok("data");
        assertEquals(200, r.getCode());
        assertEquals("success", r.getMessage());
        assertEquals("data", r.getData());
    }
    @Test void testOkNull() {
        Result<Void> r = Result.ok();
        assertEquals(200, r.getCode());
        assertNull(r.getData());
    }
    @Test void testErrorMessage() {
        Result<Void> r = Result.error("fail");
        assertEquals(500, r.getCode());
        assertEquals("fail", r.getMessage());
    }
    @Test void testErrorCodeAndMessage() {
        Result<Void> r = Result.error(404, "not found");
        assertEquals(404, r.getCode());
        assertEquals("not found", r.getMessage());
    }
}
