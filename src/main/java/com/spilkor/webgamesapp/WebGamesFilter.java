package com.spilkor.webgamesapp;

import com.spilkor.webgamesapp.util.dto.UserDTO;
import org.apache.catalina.connector.RequestFacade;
import org.apache.catalina.connector.ResponseFacade;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class WebGamesFilter implements Filter {

    private final List<String> allowedOrigins = Arrays.asList("http://localhost:3000");

    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
//        request.getReader().lines().collect(Collectors.joining(System.lineSeparator()));
        RequestFacade requestFacade = (RequestFacade) request;
        String URI = requestFacade.getRequestURI();
        UserDTO user = (UserDTO) requestFacade.getSession().getAttribute("user");

        if (request instanceof HttpServletRequest && response instanceof HttpServletResponse) {
            addHeaders((HttpServletRequest) request, (HttpServletResponse) response);
        }

        if (user != null){
            chain.doFilter(request, response);
            return;
        }

        switch (URI){
            case "/user":
            case "/login":
            case "/logout":
            case "/create-account":
                chain.doFilter(request, response);
                break;
            default:
                ((ResponseFacade) response).setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        }
    }

    private void addHeaders(HttpServletRequest request, HttpServletResponse response) {
        String origin = request.getHeader("Origin");
        response.setHeader("Access-Control-Allow-Origin", allowedOrigins.contains(origin) ? origin : "");
        response.setHeader("Vary", "Origin");
        response.setHeader("Access-Control-Max-Age", "3600");
        response.setHeader("Access-Control-Allow-Credentials", "true");
        response.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, DELETE, PUT");
        response.setHeader("Access-Control-Allow-Headers", "Origin, X-Requested-With, Content-Type, Accept, " + "X-CSRF-TOKEN");
    }

    @Override
    public void destroy() {

    }

    @Override
    public void init(FilterConfig filterConfig) {

    }
}
