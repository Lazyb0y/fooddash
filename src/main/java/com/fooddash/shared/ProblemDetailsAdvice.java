package com.fooddash.shared;

import java.net.URI;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

/**
 * One error shape for the whole API: every error becomes RFC 9457 Problem Details. Stack traces
 * stay in the server log, never in a response.
 */
@RestControllerAdvice(annotations = RestController.class)
class ProblemDetailsAdvice extends ResponseEntityExceptionHandler {

  private static final Logger log = LoggerFactory.getLogger(ProblemDetailsAdvice.class);

  @ExceptionHandler(ApiException.class)
  ProblemDetail handleApiException(ApiException ex) {
    ProblemDetail problem =
        ProblemDetail.forStatusAndDetail(HttpStatusCode.valueOf(ex.status()), ex.getMessage());
    problem.setType(ex.type());
    problem.setTitle(ex.title());
    return problem;
  }

  @ExceptionHandler(IllegalArgumentException.class)
  ProblemDetail handleBadInput(IllegalArgumentException ex) {
    return ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, ex.getMessage());
  }

  @ExceptionHandler(DataIntegrityViolationException.class)
  ProblemDetail handleIntegrityViolation(DataIntegrityViolationException ex) {
    log.warn("Request rejected by a database constraint", ex);
    ProblemDetail problem =
        ProblemDetail.forStatusAndDetail(
            HttpStatus.CONFLICT, "The request conflicts with the current data. Try again.");
    problem.setTitle("Conflict");
    return problem;
  }

  @ExceptionHandler(Exception.class)
  ProblemDetail handleUnexpected(Exception ex) {
    log.error("Unexpected error", ex);
    return ProblemDetail.forStatusAndDetail(
        HttpStatus.INTERNAL_SERVER_ERROR, "Something went wrong on our side.");
  }

  @Override
  protected ResponseEntity<Object> handleMethodArgumentNotValid(
      MethodArgumentNotValidException ex,
      HttpHeaders headers,
      HttpStatusCode status,
      WebRequest request) {
    ProblemDetail problem =
        ProblemDetail.forStatusAndDetail(
            HttpStatusCode.valueOf(422), ex.getErrorCount() + " field(s) have problems.");
    problem.setType(URI.create("https://fooddash.example/problems/validation"));
    problem.setTitle("The request is not valid");
    List<Map<String, String>> errors =
        ex.getFieldErrors().stream()
            .map(
                e ->
                    Map.of("field", e.getField(), "message", String.valueOf(e.getDefaultMessage())))
            .toList();
    problem.setProperty("errors", errors);
    return ResponseEntity.status(422).body(problem);
  }
}
