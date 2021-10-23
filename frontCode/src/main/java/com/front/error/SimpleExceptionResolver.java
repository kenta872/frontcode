package com.front.error;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

@Component
public class SimpleExceptionResolver implements HandlerExceptionResolver {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object handler,
            Exception ex) {
        logger.error("例外をキャッチしました。", ex);
        ModelAndView mav = new ModelAndView();
        if(ex instanceof ApplicationException) {
        	ApplicationException appException = (ApplicationException) ex;
        	String errorMessage = appException.getErrorMessage();
        	mav.addObject("errorMessage",errorMessage);
        } else {
        	logger.error("不明なシステムエラーが発生しました。", ex);
            String textMessage = ex.getMessage();
            mav.addObject("errorMessage", textMessage);
            // JSPに表示するメッセージをセットします。 	
        }
        
        mav.setViewName("error"); 
        
        return mav;
    }
}
