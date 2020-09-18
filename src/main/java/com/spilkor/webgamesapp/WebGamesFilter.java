package com.spilkor.webgamesapp;

import com.spilkor.webgamesapp.util.dto.UserDTO;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class WebGamesFilter implements Filter {

    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
//        request.getReader().lines().collect(Collectors.joining(System.lineSeparator()));

        if (!(request instanceof HttpServletRequest && response instanceof HttpServletResponse)){
            return;
        }

        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        HttpServletResponse httpServletResponse = (HttpServletResponse) response;

        String URI = httpServletRequest.getRequestURI();

        if (URI == null || !URI.startsWith("/api/")){
            chain.doFilter(httpServletRequest, httpServletResponse);
            return;
        }

        UserDTO user = (UserDTO) httpServletRequest.getSession().getAttribute("user");

        if (user != null){
            chain.doFilter(httpServletRequest, httpServletResponse);
            return;
        }

        switch (URI){
            case "/api/user":
            case "/api/login":
            case "/api/create-account":
                chain.doFilter(httpServletRequest, httpServletResponse);
                break;
            default:
                httpServletResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        }
    }

    @Override
    public void destroy() {

    }

    @Override
    public void init(FilterConfig filterConfig) {

    }

}
