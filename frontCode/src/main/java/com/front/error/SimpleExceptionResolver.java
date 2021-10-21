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
            // JSPに表示するメッセージをセットします。
            mav.addObject("errorMessage", "予期せぬエラーが発生しました。" + " 詳細：【" + ex + "】");
            // 遷移先のJSPを指定します。(error.jspに遷移します。)      	
        }
        mav.setViewName("error"); 
        
        return mav;
    }
}
