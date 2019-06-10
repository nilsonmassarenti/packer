package com.mobiquityinc.delivery_optimal.model;

public enum EnumConstants {
	MAX_PACKAGE_WEIGHT(100.0),
	MAX_ITEM_LIST(100.0),
	MAX_ITEM_WEIGHT(100.0),
	MAX_ITEM_COST(100.0);
	
	private Double value;
	
	EnumConstants(Double value) {
		this.value = value;
	}

	public Double getValue() {
		return value;
	}
}
