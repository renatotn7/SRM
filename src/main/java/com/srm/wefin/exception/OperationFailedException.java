package com.srm.wefin.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.SERVICE_UNAVAILABLE) // Ou HttpStatus.INTERNAL_SERVER_ERROR
public class OperationFailedException extends RuntimeException {

	public OperationFailedException(String message) {
		super(message);
	}

}
