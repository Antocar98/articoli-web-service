package com.xantrix.webapp.exceptions;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
@RestController
public class RestExceptionHandler extends  ResponseEntityExceptionHandler
{
	@ExceptionHandler(NotFoundException.class)
	public final ResponseEntity<ErrorResponse> exceptionNotFoundHandler(Exception ex)
	{
		ErrorResponse errore = new ErrorResponse();

		errore.setCode(HttpStatus.NOT_FOUND.value());
		errore.setMessage(ex.getMessage());

		return new ResponseEntity<ErrorResponse>(errore, new HttpHeaders(), HttpStatus.NOT_FOUND);
	}

	@ExceptionHandler(BindingException.class)
	public ResponseEntity<ErrorResponse> exceptionBindingHandler(Exception ex)
	{
		ErrorResponse errore = new ErrorResponse();

		errore.setCode(HttpStatus.BAD_REQUEST.value());
		errore.setMessage(ex.getMessage());

		return new ResponseEntity<ErrorResponse>(errore, new HttpHeaders(), HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(DuplicateException.class)
	public ResponseEntity<ErrorResponse> exceptionDeplicateRecordHandler(Exception ex)
	{
		ErrorResponse errore = new ErrorResponse();

		errore.setCode(HttpStatus.NOT_ACCEPTABLE.value());
		errore.setMessage(ex.getMessage());

		return new ResponseEntity<ErrorResponse>(errore, HttpStatus.NOT_ACCEPTABLE);
	}
}
