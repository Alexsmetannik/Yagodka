package org.yagodka.configs;

import org.yagodka.services.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;


import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Configuration
public class SecurityConfig implements HandlerInterceptor  {


    @Autowired
    private AuthService authService;

    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new HandlerInterceptor() {
            @Override
            public boolean preHandle(HttpServletRequest request,
                                     HttpServletResponse response,
                                     Object handler) throws Exception {
                // Skip security for public endpoints
                if (request.getRequestURI().startsWith("/public/register") ||
                        request.getRequestURI().startsWith("/public/login")) {
                    return true;
                }

                String token = request.getHeader("Authorization");
                if (token != null && authService.validateToken(token)) {
                    return true;
                }

                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                return false;
            }
        }).addPathPatterns("/public/**");
    }
}
