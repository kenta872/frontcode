package com.front.error;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class Errors {

	@Autowired
	ErrorMessage errorMessage;
	
    public ApplicationException errorUrl() {
    	return new ApplicationException(errorMessage.ERROR_EXCEPTION_URL);
    }
    
    public ApplicationException errorIllegal() {
    	return new ApplicationException(errorMessage.ERROR_EXCEPTION_ILLEGAL);
    }
    
    public ApplicationException errorDbIllegal() {
    	return new ApplicationException(errorMessage.ERROR_EXCEPTION_DBILLEGAL);
    }
	
    
    public String notHttp() {
    	return errorMessage.ERROR_MESSAGE_NOTHTTP;
    }
	
    public String notHtmlScript() {
    	return errorMessage.ERROR_MESSAGE_NOTHTMLSCRIPT;
    }
    
    public String notCssScript() {
    	return errorMessage.ERROR_MESSAGE_NOTCSSSCRIPT;
    }

}
