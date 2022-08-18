package com.yaroslav.lobur.controller.filter;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.annotation.WebInitParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

@WebFilter(filterName = "EncodingFilter",
        urlPatterns = "/*",
        initParams = {@WebInitParam(name = "encoding", value = "UTF-8")})
public class EncodingFilter implements Filter {

    private final Logger logger = LoggerFactory.getLogger(EncodingFilter.class);

    private String encoding;

    @Override
    public void init(FilterConfig filterConfig) {
        encoding = filterConfig.getInitParameter("encoding");
        logger.debug("Encoding filter param is {}", encoding);
    }

    public void doFilter(ServletRequest request, ServletResponse response,
                         FilterChain chain) throws IOException, ServletException {
        String requestEncoding = request.getCharacterEncoding();
        String responseEncoding = response.getCharacterEncoding();
        if (encoding != null &&
                (!encoding.equalsIgnoreCase(requestEncoding) || !encoding.equalsIgnoreCase(responseEncoding))) {
            request.setCharacterEncoding(encoding);
            response.setCharacterEncoding(encoding);
            logger.debug("Setting encoding {}", encoding);
        }
        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {
        encoding = null;
    }
}