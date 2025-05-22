package com.ecommerce.project.exception;

public class ResourceNotFoundException extends RuntimeException {

	String resouceName;
	String fieldName;
	String field;
	Long fieldId;

	public ResourceNotFoundException(String resourceName, String fieldName, String field) {
		super(String.format("%s not found with %s:%s",resourceName,fieldName,field));
		this.resouceName = resouceName;
		this.fieldName = fieldName;
		this.field = field;
	}

	public ResourceNotFoundException(String resourceName, String fieldName, Long fieldId) {
		super(String.format("%s not found with %s:%s",resourceName,fieldName,fieldId));
		this.resouceName = resouceName;
		this.fieldName = fieldName;
		this.fieldId = fieldId;
	}

	public ResourceNotFoundException() {
		super();
	}

}
