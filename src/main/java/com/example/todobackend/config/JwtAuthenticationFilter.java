package com.example.todobackend.config;

import com.example.todobackend.common.Result;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;
import java.util.List;

public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final JwtUtil jwtUtil;
    private final ObjectMapper om = new ObjectMapper();

    public JwtAuthenticationFilter(JwtUtil jwtUtil) { this.jwtUtil = jwtUtil; }

    private static final List<String> WHITE = List.of("/api/auth/register","/api/auth/login");

    @Override
    protected void doFilterInternal(HttpServletRequest req, HttpServletResponse resp,
                                    FilterChain chain) throws ServletException, IOException {
        if (WHITE.stream().anyMatch(req.getRequestURI()::startsWith)) { chain.doFilter(req, resp); return; }
        String auth = req.getHeader("Authorization");
        if (auth == null || !auth.startsWith("Bearer ")) { fail(resp, "未提供认证令牌"); return; }
        String token = auth.substring(7);
        if (!jwtUtil.validateToken(token)) { fail(resp, "令牌无效或已过期"); return; }
        req.setAttribute("userId", jwtUtil.getUserIdFromToken(token));
        chain.doFilter(req, resp);
    }

    private void fail(HttpServletResponse r, String msg) throws IOException {
        r.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        r.setContentType("application/json;charset=UTF-8");
        om.writeValue(r.getWriter(), Result.error(401, msg));
    }
}
