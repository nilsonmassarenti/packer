package com.mobiquityinc.delivery_optimal.model;

public class Pack {
	
	private Integer index;
	private Double weight;
	private Double cost;
	private String currency;
	
	public Pack(Integer index, Double weight, Double cost, String currency) {
		super();
		this.index = index;
		this.weight = weight;
		this.cost = cost;
		this.currency = currency;
	}

	public Integer getIndex() {
		return index;
	}

	public Double getWeight() {
		return weight;
	}

	public Double getCost() {
		return cost;
	}

	public String getCurrency() {
		return currency;
	}
	
	

}
