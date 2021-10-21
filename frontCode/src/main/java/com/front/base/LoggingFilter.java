package com.front.base;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class LoggingFilter implements Filter {
	
	private  Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    	logger.debug("アプリケーション開始");
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        if (request instanceof HttpServletRequest) {
            HttpServletRequest httpReq = (HttpServletRequest) request;
            logger.debug("RequestURL : {}",httpReq.getRequestURI());
        }
        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {
    	logger.debug("アプリケーション終了");
    }
}