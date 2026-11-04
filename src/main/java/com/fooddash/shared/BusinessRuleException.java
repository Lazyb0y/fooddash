package com.fooddash.shared;

/** 422: the request is well formed but breaks a business rule. */
public abstract class BusinessRuleException extends ApiException {

  protected BusinessRuleException(String type, String title, String detail) {
    super(422, type, title, detail);
  }
}
