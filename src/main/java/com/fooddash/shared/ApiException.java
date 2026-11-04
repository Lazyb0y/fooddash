package com.fooddash.shared;

import java.net.URI;

/**
 * A failure the client can act on. {@link ProblemDetailsAdvice} turns it into an RFC 9457 Problem
 * Details response with this status, type and title.
 */
public abstract class ApiException extends RuntimeException {

  private static final String PROBLEM_TYPES = "https://fooddash.example/problems/";

  private final int status;
  private final String type;
  private final String title;

  protected ApiException(int status, String type, String title, String detail) {
    super(detail);
    this.status = status;
    this.type = type;
    this.title = title;
  }

  public int status() {
    return status;
  }

  public URI type() {
    return URI.create(PROBLEM_TYPES + type);
  }

  public String title() {
    return title;
  }
}
