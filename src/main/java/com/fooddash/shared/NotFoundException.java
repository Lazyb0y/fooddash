package com.fooddash.shared;

/** 404: the resource doesn't exist, or the caller mustn't know that it exists. */
public abstract class NotFoundException extends ApiException {

  protected NotFoundException(String type, String title, String detail) {
    super(404, type, title, detail);
  }
}
