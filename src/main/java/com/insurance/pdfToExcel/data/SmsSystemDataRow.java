package com.insurance.pdfToExcel.data;

import java.util.ArrayList;
import java.util.List;

public class SmsSystemDataRow {
	
	private String fileName;
	private String insuranceCompany;
	private String policyNumber;
	private String personalId;
	private String vehicleRegistrationNumber;
	private String vehicleBrand;
	private String vehicleModel;
	private String vehicleEngineCapacity;
	private String endDate;
	private String[] paymentDates;
	
	//************
	private String insuranceType;

	private String greenCardNumber;
	private String stickerNumber;
	
	private String gsmNumber;
	


	private List<PaymentRow> paymentRows;
	
	public SmsSystemDataRow() {
		paymentRows = new ArrayList<PaymentRow>();
	}
	
	public String toString() {
		String res = insuranceCompany + "; " + policyNumber  + "; " + endDate + "; "+ personalId + "; " + vehicleRegistrationNumber + "; " + vehicleBrand + "; " +vehicleModel + "; " + vehicleEngineCapacity;// + "; " + insuranceType+ "; " + greenCardNumber+ "; " + stickerNumber;
		if(paymentDates != null) {
			for(int i= 0; i<paymentDates.length; i++) {
				res += "; " + paymentDates[i];
			}
		}
		
		//for(PaymentRow pr: paymentRows) {
			//res += "\n" + pr.getDate() + "; " + pr.getPaymentNumber()+ "; " + pr.getValue()+ "; " + pr.getValueTax()+ "; " + pr.getGfAndStickerValue()+ "; " + pr.getTotalValue();
		//}
		
		return res;
	}
	
	
	public String getInsuranceType() {
		return insuranceType;
	}

	public void setInsuranceType(String insuranceType) {
		this.insuranceType = insuranceType;
	}

	public String getGreenCardNumber() {
		return greenCardNumber;
	}

	public void setGreenCardNumber(String greenCardNumber) {
		this.greenCardNumber = greenCardNumber;
	}

	public String getStickerNumber() {
		return stickerNumber;
	}

	public void setStickerNumber(String stickerNumber) {
		this.stickerNumber = stickerNumber;
	}

	public List<PaymentRow> getPaymentRows() {
		return paymentRows;
	}

	public void setPaymentRows(List<PaymentRow> paymentRows) {
		this.paymentRows = paymentRows;
	}
	
	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}
	
	public String getInsuranceCompany() {
		return insuranceCompany;
	}
	public void setInsuranceCompany(String insuranceCompany) {
		this.insuranceCompany = insuranceCompany;
	}
	public String getPolicyNumber() {
		return policyNumber;
	}
	public void setPolicyNumber(String policyNumber) {
		this.policyNumber = policyNumber;
	}
	public String getPersonalId() {
		return personalId;
	}
	public void setPersonalId(String personalId) {
		this.personalId = personalId;
	}
	public String getVehicleRegistrationNumber() {
		return vehicleRegistrationNumber;
	}
	public void setVehicleRegistrationNumber(String registrationNumber) {
		this.vehicleRegistrationNumber = registrationNumber;
	}
	public String getVehicleBrand() {
		return vehicleBrand;
	}
	public void setVehicleBrand(String vehicleBrand) {
		this.vehicleBrand = vehicleBrand;
	}
	public String getVehicleModel() {
		return vehicleModel;
	}
	public void setVehicleModel(String vehicleModel) {
		this.vehicleModel = vehicleModel;
	}
	public String getVehicleEngineCapacity() {
		return vehicleEngineCapacity;
	}
	public void setVehicleEngineCapacity(String vehicleCapacity) {
		this.vehicleEngineCapacity = vehicleCapacity;
	}
	public String[] getPaymentDates() {
		return paymentDates;
	}
	public void setPaymentDates(String[] paymentDates) {
		this.paymentDates = paymentDates;
	}
	
	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	
	public String getGsmNumber() {
		return gsmNumber;
	}

	public void setGsmNumber(String gsmNumber) {
		this.gsmNumber = gsmNumber;
	}
	


}
