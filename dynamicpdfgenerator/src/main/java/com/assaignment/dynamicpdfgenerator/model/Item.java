package com.assaignment.dynamicpdfgenerator.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class Item {
    @NotBlank 
    @NotNull
    private String name;

    @NotBlank 
    @NotNull
    private String quantity;

    @NotNull
    private Double rate;

    @NotNull 
    private Double amount;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getQuantity() {
		return quantity;
	}

	public void setQuantity(String quantity) {
		this.quantity = quantity;
	}

	public Double getRate() {
		return rate;
	}

	public void setRate(Double rate) {
		this.rate = rate;
	}

	public Double getAmount() {
		return amount;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
	}

	@Override
	public String toString() {
		return "Item [name=" + name + ", quantity=" + quantity + ", rate=" + rate + ", amount=" + amount + "]";
	}

    
}
