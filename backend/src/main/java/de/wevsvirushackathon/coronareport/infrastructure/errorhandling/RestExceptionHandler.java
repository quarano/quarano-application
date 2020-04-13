package de.wevsvirushackathon.coronareport.infrastructure.errorhandling;

import javax.persistence.EntityNotFoundException;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingPathVariableException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import de.wevsvirushackathon.coronareport.client.MissingClientException;
import de.wevsvirushackathon.coronareport.diary.ClientNotAuthorizedException;
import quarano.user.UserNotFoundException;

/**
 * Overrides basic Spring Exception Handling Entries to provide better error
 * responses to API users
 * 
 * @author Patrick Otto
 *
 */
@Order(Ordered.HIGHEST_PRECEDENCE)
@ControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

	@Override
	protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex,
			HttpHeaders headers, HttpStatus status, WebRequest request) {
		String error = "Malformed JSON request";
		return buildResponseEntity(new ApiError(HttpStatus.BAD_REQUEST, error, ex));
	}

	@Override
	protected ResponseEntity<Object> handleMissingPathVariable(MissingPathVariableException ex, HttpHeaders headers,
			HttpStatus status, WebRequest request) {
		String error = "Path variable is missing";
		return buildResponseEntity(new ApiError(HttpStatus.BAD_REQUEST, error, ex));
	}

	@Override
	protected ResponseEntity<Object> handleMissingServletRequestParameter(MissingServletRequestParameterException ex,
			HttpHeaders headers, HttpStatus status, WebRequest request) {
		String error = "Path variable is missing";
		return buildResponseEntity(new ApiError(HttpStatus.BAD_REQUEST, error, ex));
	}

	@Override
	protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
			HttpHeaders headers, HttpStatus status, WebRequest request) {
		String error = "A given argument was not valid";
		return buildResponseEntity(new ApiError(HttpStatus.BAD_REQUEST, error, ex));
	}

	@Override
	protected ResponseEntity<Object> handleNoHandlerFoundException(NoHandlerFoundException ex, HttpHeaders headers,
			HttpStatus status, WebRequest request) {
		String error = "An unknown error occured";
		return buildResponseEntity(new ApiError(HttpStatus.INTERNAL_SERVER_ERROR, error, ex));
	}

	private ResponseEntity<Object> buildResponseEntity(ApiError apiError) {
		return new ResponseEntity<>(apiError, apiError.getStatus());
	}

	// custom exception handlers below

	@ExceptionHandler(EntityNotFoundException.class)
	protected ResponseEntity<Object> handleEntityNotFound(EntityNotFoundException ex) {
		ApiError apiError = new ApiError(HttpStatus.NOT_FOUND, "Entity not found", ex);
		return buildResponseEntity(apiError);
	}

	@ExceptionHandler(InvalidArgumentException.class)
	protected ResponseEntity<Object> handleInvalidArgument(InvalidArgumentException ex) {
		String error = "'" + ex.getValue() + "' is no valid value for " + ex.getType() + " " + ex.getArgumentName();
		ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST, error, ex);
		return buildResponseEntity(apiError);
	}

	@ExceptionHandler(ClientNotAuthorizedException.class)
	protected ResponseEntity<Object> handleClientNotAuthorized(ClientNotAuthorizedException ex) {
		String error = "Client with client-id '" + ex.getClientCode() + " is not authorized";
		ApiError apiError = new ApiError(HttpStatus.FORBIDDEN, error, ex);
		return buildResponseEntity(apiError);
	}
	
	@ExceptionHandler(MissingClientException.class)
	protected ResponseEntity<Object> handleNotAuthorized(MissingClientException ex) {
		String error = "There is no client for given Account username";
		ApiError apiError = new ApiError(HttpStatus.INTERNAL_SERVER_ERROR, error, ex);
		return buildResponseEntity(apiError);
	}	
	
	@ExceptionHandler(UserNotFoundException.class)
	protected ResponseEntity<Object> handleUserNotFoundException(UserNotFoundException ex) {
		String error = "There is no user with the given username " + ex.getUsername() +"'";
		ApiError apiError = new ApiError(HttpStatus.INTERNAL_SERVER_ERROR, error, ex);
		return buildResponseEntity(apiError);
	}	
	
	@ExceptionHandler(InconsistentDataException.class)
	protected ResponseEntity<Object> handleInconsistentDataException(InconsistentDataException ex) {
		String error = "Internal server error";
		ApiError apiError = new ApiError(HttpStatus.INTERNAL_SERVER_ERROR, error, ex);
		return buildResponseEntity(apiError);
	}		
	
	
}
