package com.xantrix.webapp.security;

import java.io.IOException;


import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.java.Log;

@Log
public class CustomAccessDeniedHandler  implements AccessDeniedHandler
{
	private ObjectMapper objectMapper = new ObjectMapper();

	@Override
	public void handle(HttpServletRequest request, HttpServletResponse response,
			AccessDeniedException exception) throws IOException, ServletException 
	{
		 String errMsg = "Priviligi Insufficenti. Impossibile Proseguire.";
		
		 HttpStatus httpStatus = HttpStatus.FORBIDDEN; // 403
		 response.setStatus(httpStatus.value());
		 
		 log.warning(errMsg);
		
		 //Map<String, Object> data = new HashMap<>();
		 
		 response
         .getOutputStream()
         .println(objectMapper.writeValueAsString(errMsg));
		
	}

}
