package com.controller;

import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.http.HttpStatus;

@ResponseStatus(value=HttpStatus.NOT_FOUND, reason="The ID in the query string does not exist")
public class IDIsNullException extends RuntimeException{

}
