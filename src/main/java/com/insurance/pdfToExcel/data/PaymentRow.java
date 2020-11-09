package com.insurance.pdfToExcel.data;

public class PaymentRow {
	
	
	public int paymentNumber;
	public String date;
	public String value;
	public String valueTax;
	public String gfAndStickerValue;
	public String totalValue;
	
	public int getPaymentNumber() {
		return paymentNumber;
	}
	public void setPaymentNumber(int paymentNumber) {
		this.paymentNumber = paymentNumber;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public String getValueTax() {
		return valueTax;
	}
	public void setValueTax(String valueTax) {
		this.valueTax = valueTax;
	}
	public String getGfAndStickerValue() {
		return gfAndStickerValue;
	}
	public void setGfAndStickerValue(String gfAndStickerValue) {
		this.gfAndStickerValue = gfAndStickerValue;
	}
	public String getTotalValue() {
		return totalValue;
	}
	public void setTotalValue(String totalValue) {
		this.totalValue = totalValue;
	}


}
