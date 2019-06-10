package com.mobiquityinc.delivery_optimal.model;

public enum EnumException {
	FILE_NOT_FOUND("File not found"),
	FILE_EMPTY("File is empty"),
	FILE_ERROR_READ("The file presented some error"),
	MAX_PACKAGE_WEIGHT("The max weight package exceed {{weight}}"),
	MAX_ITEMS("The max items exceed {{weight}}"),
	MAX_ITEM_WEIGHT(("The item exceed {{weight}}")),
	MAX_ITEM_COST(("The item cost exceed {{cost}}"));
	
	private String message;
	
	EnumException(String message) {
		this.message = message;
	}
	
	public String getMessage() {
		return this.message;
	}
}
