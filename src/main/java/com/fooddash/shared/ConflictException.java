package com.fooddash.shared;

/** 409: the resource's current state doesn't allow the request. */
public abstract class ConflictException extends ApiException {

  protected ConflictException(String type, String title, String detail) {
    super(409, type, title, detail);
  }
}
