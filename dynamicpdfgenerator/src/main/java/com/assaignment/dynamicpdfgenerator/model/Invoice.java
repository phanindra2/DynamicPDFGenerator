package com.assaignment.dynamicpdfgenerator.model;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import java.util.List;

public class Invoice {
    
    @NotBlank 
    private String seller;

    @Pattern(regexp = "[0-9A-Z]{15}") 
    private String sellerGstin;

    @NotBlank 
    private String sellerAddress;
    
    @NotBlank 
    private String buyer;

    @Pattern(regexp = "[0-9A-Z]{15}") 
    private String buyerGstin;

    @NotBlank 
    private String buyerAddress;

    @NotNull 
    @Valid
    private List<Item> items;
    
    

	public Invoice() {
		super();
	}

	public String getSeller() {
		return seller;
	}

	public void setSeller(String seller) {
		this.seller = seller;
	}

	public String getSellerGstin() {
		return sellerGstin;
	}

	public void setSellerGstin(String sellerGstin) {
		this.sellerGstin = sellerGstin;
	}

	public String getSellerAddress() {
		return sellerAddress;
	}

	public void setSellerAddress(String sellerAddress) {
		this.sellerAddress = sellerAddress;
	}

	public String getBuyer() {
		return buyer;
	}

	public void setBuyer(String buyer) {
		this.buyer = buyer;
	}

	public String getBuyerGstin() {
		return buyerGstin;
	}

	public void setBuyerGstin(String buyerGstin) {
		this.buyerGstin = buyerGstin;
	}

	public String getBuyerAddress() {
		return buyerAddress;
	}

	public void setBuyerAddress(String buyerAddress) {
		this.buyerAddress = buyerAddress;
	}

	public List<Item> getItems() {
		return items;
	}

	public void setItems(List<Item> items) {
		this.items = items;
	}

	@Override
	public String toString() {
		return "Invoice [seller=" + seller + ", sellerGstin=" + sellerGstin + ", sellerAddress=" + sellerAddress
				+ ", buyer=" + buyer + ", buyerGstin=" + buyerGstin + ", buyerAddress=" + buyerAddress + ", items="
				+ items + "]";
	}

    
}
