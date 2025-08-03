package com.srm.wefin.handler;

import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.PessimisticLockingFailureException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.retry.ExhaustedRetryException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import com.fasterxml.jackson.databind.exc.MismatchedInputException;
import com.srm.wefin.dto.ErroResponse; // <--- Importe a nova classe de ErroResponse
import com.srm.wefin.exception.DuplicateResourceException;
import com.srm.wefin.exception.OperationFailedException;
import com.srm.wefin.exception.ResourceNotFoundException;

import jakarta.servlet.http.HttpServletRequest; // Importe HttpServletRequest

@ControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(DuplicateResourceException.class)
	public ResponseEntity<ErroResponse> handleDuplicateResourceException(DuplicateResourceException ex, HttpServletRequest request) {
		ErroResponse error = new ErroResponse(LocalDateTime.now(), HttpStatus.CONFLICT.value(), HttpStatus.CONFLICT.getReasonPhrase(), // "Conflict"
				ex.getMessage(), request.getRequestURI());
		return new ResponseEntity<>(error, HttpStatus.CONFLICT);
	}

	@ExceptionHandler(ResourceNotFoundException.class)
	public ResponseEntity<ErroResponse> handleResourceNotFoundException(ResourceNotFoundException ex, HttpServletRequest request) {
		ErroResponse error = new ErroResponse(LocalDateTime.now(), HttpStatus.NOT_FOUND.value(), HttpStatus.NOT_FOUND.getReasonPhrase(), // "Not Found"
				ex.getMessage(), request.getRequestURI());
		return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
	}

	@ExceptionHandler(OperationFailedException.class)
	public ResponseEntity<ErroResponse> handleOperationFailedException(OperationFailedException ex, HttpServletRequest request) {
		ErroResponse error = new ErroResponse(LocalDateTime.now(), HttpStatus.SERVICE_UNAVAILABLE.value(), HttpStatus.SERVICE_UNAVAILABLE.getReasonPhrase(), // "Service Unavailable"
				ex.getMessage(), request.getRequestURI());
		return new ResponseEntity<>(error, HttpStatus.SERVICE_UNAVAILABLE);
	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<ErroResponse> handleValidationExceptions(MethodArgumentNotValidException ex, HttpServletRequest request) {
		List<String> errors = ex.getBindingResult().getFieldErrors().stream().map(err -> err.getField() + ": " + err.getDefaultMessage()).collect(Collectors.toList());

		ErroResponse error = new ErroResponse(LocalDateTime.now(), HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST.getReasonPhrase(), // "Bad Request"
				"Um ou mais campos da requisição são inválidos.", errors, //
				request.getRequestURI());
		return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(PessimisticLockingFailureException.class)
	public ResponseEntity<ErroResponse> handlePessimisticLockingFailureException(PessimisticLockingFailureException ex, HttpServletRequest request) {
		ErroResponse error = new ErroResponse(LocalDateTime.now(), HttpStatus.SERVICE_UNAVAILABLE.value(), //
				HttpStatus.SERVICE_UNAVAILABLE.getReasonPhrase(), // 
				"A operação não pôde ser concluída devido a um problema de concorrência. Por favor, tente novamente.", request.getRequestURI());
		return new ResponseEntity<>(error, HttpStatus.SERVICE_UNAVAILABLE);
	}

	// Catch-all para qualquer outra exceção inesperada
	@ExceptionHandler(Exception.class)
	public ResponseEntity<ErroResponse> handleGenericException(Exception ex, HttpServletRequest request) {
		ErroResponse error = new ErroResponse(LocalDateTime.now(), HttpStatus.INTERNAL_SERVER_ERROR.value(), HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(), // "Internal Server Error"
				"Ocorreu um erro inesperado no servidor. Por favor, tente novamente mais tarde.", request.getRequestURI());
		return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@ExceptionHandler(ExhaustedRetryException.class)
	public ResponseEntity<ErroResponse> handleExhaustedRetryException(ExhaustedRetryException ex, HttpServletRequest request) {
		Throwable cause = ex.getCause(); // Pega a exceção original que levou à exaustão das retentativas

		if (cause instanceof DuplicateKeyException) {
			// Se a causa for uma PSQLException, delegamos ao handler específico de PSQLException
			return handleDuplicateKeyException((DuplicateKeyException) cause, request);
		} else if (cause instanceof DataIntegrityViolationException) {
			// Se não for DuplicateKeyException, mas ainda uma violação de integridade (ex: Foreign Key)
			return handleDataIntegrityViolation((DataIntegrityViolationException) cause, request);
		} else if (cause instanceof DuplicateResourceException) {
			return handleDuplicateResourceException((DuplicateResourceException) cause, request);
		} else {
			ErroResponse errorResponse = new ErroResponse(LocalDateTime.now(), HttpStatus.INTERNAL_SERVER_ERROR.value(), HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(),
					"A operação falhou após várias tentativas. Detalhes: " + (cause != null ? cause.getLocalizedMessage() : "Erro desconhecido."), null, request.getRequestURI());
			return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@ExceptionHandler(DuplicateKeyException.class)
	public ResponseEntity<ErroResponse> handleDuplicateKeyException(DuplicateKeyException ex, HttpServletRequest request) {
		String errorMessage = "Já existe um registro com o mesmo valor para um campo que deve ser único.";
		List<String> details = null;

		// Tenta extrair detalhes específicos da mensagem da exceção subjacente
		String specificDetail = extractDuplicateKeyDetails(ex.getLocalizedMessage());
		if (specificDetail != null && !specificDetail.isEmpty()) {
			errorMessage += " Detalhe: " + specificDetail;
			details = Collections.singletonList("Campo duplicado: " + specificDetail);
		}

		ErroResponse errorResponse = new ErroResponse(LocalDateTime.now(), HttpStatus.CONFLICT.value(), HttpStatus.CONFLICT.getReasonPhrase(), errorMessage, details, request.getRequestURI());
		return new ResponseEntity<>(errorResponse, HttpStatus.CONFLICT);
	}

	@ExceptionHandler(DataIntegrityViolationException.class)
	public ResponseEntity<ErroResponse> handleDataIntegrityViolation(DataIntegrityViolationException ex, HttpServletRequest request) {
		String errorMessage = "Violação de integridade de dados.";
		List<String> details = null;
		HttpStatus status = HttpStatus.BAD_REQUEST; // Geralmente BAD_REQUEST ou CONFLICT para outros tipos

		// Tenta identificar o tipo de violação a partir da causa raiz, se não for DuplicateKeyException
		Throwable cause = ex.getCause();
		if (cause instanceof java.sql.SQLException) {
			java.sql.SQLException sqlEx = (java.sql.SQLException) cause;
			String sqlState = sqlEx.getSQLState();
			String localizedMessage = sqlEx.getLocalizedMessage();

			// Exemplo: SQLState para FOREIGN KEY VIOLATION (PostgreSQL: '23503', MySQL: '23000' + 'Cannot add or update a child row: a foreign key constraint fails')
			if ("23503".equals(sqlState) || (localizedMessage != null && localizedMessage.contains("foreign key constraint fails"))) {
				errorMessage = "Violação de chave estrangeira. Um recurso referenciado não existe ou não pode ser excluído.";
				details = Collections.singletonList(localizedMessage);
				status = HttpStatus.BAD_REQUEST; // 400 Bad Request
			} else {
				details = Collections.singletonList(localizedMessage); // Fallback para o detalhe da mensagem original
				status = HttpStatus.INTERNAL_SERVER_ERROR; // Outros tipos de violação de integridade podem ser 500
			}
		} else {
			details = Collections.singletonList(ex.getLocalizedMessage());
			status = HttpStatus.INTERNAL_SERVER_ERROR;
		}

		ErroResponse errorResponse = new ErroResponse(LocalDateTime.now(), status.value(), status.getReasonPhrase(), errorMessage, details, request.getRequestURI());
		return new ResponseEntity<>(errorResponse, status);
	}

	// Método auxiliar para extrair detalhes da mensagem de DuplicateKeyException

	private String extractDuplicateKeyDetails(String errorMessage) {

		// Tentativa de extrair para PostgreSQL
		int keyIndex = errorMessage.indexOf("Key (");
		if (keyIndex != -1) {
			int endKeyIndex = errorMessage.indexOf(")", keyIndex);
			if (endKeyIndex != -1) {
				String fullDetail = errorMessage.substring(keyIndex + 5, endKeyIndex).trim();
				return fullDetail.replace(")=(", " = ");
			}
		}

		// Tentativa de extrair para MySQL
		int entryIndex = errorMessage.indexOf("Duplicate entry '");
		if (entryIndex != -1) {
			int endEntryIndex = errorMessage.indexOf("'", entryIndex + "Duplicate entry '".length());
			if (endEntryIndex != -1) {
				String value = errorMessage.substring(entryIndex + "Duplicate entry '".length(), endEntryIndex);
				int forKeyIndex = errorMessage.indexOf("for key '", endEntryIndex);
				if (forKeyIndex != -1) {
					int endKeyNameIndex = errorMessage.indexOf("'", forKeyIndex + "for key '".length());
					if (endKeyNameIndex != -1) {
						String keyName = errorMessage.substring(forKeyIndex + "for key '".length(), endKeyNameIndex);
						return keyName + " = " + value;
					}
				}
			}
		}

		// Fallback: Retorna a mensagem original se não conseguir parsear
		return errorMessage;
	}

	@ExceptionHandler(HttpMessageNotReadableException.class)
	public ResponseEntity<ErroResponse> handleHttpMessageNotReadableException(HttpMessageNotReadableException ex, HttpServletRequest request) {
		String errorMessage = "Corpo da requisição JSON malformado ou ilegível.";
		List<String> details = Collections.singletonList(ex.getLocalizedMessage()); // Mensagem de erro mais técnica

		// Tenta extrair uma mensagem mais amigável para JSON inválido
		if (ex.getCause() instanceof com.fasterxml.jackson.core.JsonParseException) {
			errorMessage = "Erro de sintaxe no JSON da requisição. Verifique se o JSON está bem formatado.";
			details = Collections.singletonList("JSON inválido: " + ex.getCause().getLocalizedMessage());
		} else if (ex.getCause() instanceof MismatchedInputException) {
			MismatchedInputException mismatchedEx = (MismatchedInputException) ex.getCause();
			errorMessage = "Tipo de dado inválido para um campo no JSON da requisição.";
			if (mismatchedEx.getPath() != null && !mismatchedEx.getPath().isEmpty()) {
				String fieldName = mismatchedEx.getPath().get(mismatchedEx.getPath().size() - 1).getFieldName();
				errorMessage += " Campo: '" + fieldName + "'.";
				details = Collections.singletonList("Esperado: " + mismatchedEx.getTargetType().getSimpleName() + ". Recebido: " + mismatchedEx.getLocalizedMessage());
			} else {
				details = Collections.singletonList(mismatchedEx.getLocalizedMessage());
			}
		}

		ErroResponse errorResponse = new ErroResponse(LocalDateTime.now(), HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST.getReasonPhrase(), errorMessage, details, request.getRequestURI());
		return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(MethodArgumentTypeMismatchException.class)
	public ResponseEntity<ErroResponse> handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException ex, HttpServletRequest request) {
		String errorMessage = "Tipo de argumento inválido.";
		List<String> details = null;

		// Se for um erro de formato de data/hora
		if (ex.getCause() instanceof DateTimeParseException) {
			DateTimeParseException dtEx = (DateTimeParseException) ex.getCause();
			errorMessage = "Formato de data/hora inválido para o parâmetro '" + ex.getName() + "'.";
			details = Collections.singletonList("Valor recebido: '" + dtEx.getParsedString() + "'. Formato esperado: " + ex.getRequiredType().getSimpleName() + " no padrão ISO 8601.");
		} else {
			// Para outros tipos de mismatch (ex: string para Long)
			errorMessage = "O valor '" + ex.getValue() + "' fornecido para o parâmetro '" + ex.getName() + "' não pode ser convertido para o tipo esperado.";
			details = Collections.singletonList("Tipo esperado: " + ex.getRequiredType().getSimpleName() + ".");
		}

		ErroResponse errorResponse = new ErroResponse(LocalDateTime.now(), HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST.getReasonPhrase(), errorMessage, details, request.getRequestURI());
		return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
	}

}