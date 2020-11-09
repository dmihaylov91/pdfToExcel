package com.insurance.pdfToExcel.reader.impl.form;

import org.apache.commons.lang3.StringUtils;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.interactive.form.PDAcroForm;
import  org.apache.pdfbox.pdmodel.interactive.form.PDField;

import com.insurance.pdfToExcel.data.SmsSystemDataRow;
import com.insurance.pdfToExcel.reader.InsurancePdfReader;

public abstract class AbstractPdfFormReader implements InsurancePdfReader {
	
	
	protected String POLICY_NUMBER_FIELD_ID;
	
	protected String END_DATE_DAY_FIELD_ID;
	protected String END_DATE_MONTH_FIELD_ID;
	protected String END_DATE_YEAR_FIELD_ID;
	
	protected String PERSONAL_ID_FIELD_ID;
	protected String VEHICLE_REGISTRATION_NUMBER_FIELD_ID;
	protected String VEHICLE_BRAND_FIELD_ID;
	protected String VEHICLE_MODEL_FIELD_ID;
	protected String VEHICLE_ENGINE_CAPACITY_FIELD_ID;
	
	protected String PAYMENT_DATE2_FIELD_ID;
	protected String PAYMENT_DATE3_FIELD_ID;
	protected String PAYMENT_DATE4_FIELD_ID;

	public void readPdf(PDDocument pdfDocument, String pdfText, SmsSystemDataRow row) {
		readPdf(pdfDocument, row);

	}
	
	protected void readPdf(PDDocument pdfDocument, SmsSystemDataRow row){
		PDAcroForm form = pdfDocument.getDocumentCatalog().getAcroForm();
		if(form != null) {	
			row.setPolicyNumber(getFieldValue(form, POLICY_NUMBER_FIELD_ID));
			row.setEndDate(getFieldValue(form, END_DATE_DAY_FIELD_ID) + "." + getFieldValue(form, END_DATE_MONTH_FIELD_ID)+ "."  + getFieldValue(form, END_DATE_YEAR_FIELD_ID));			
			row.setPersonalId(getFieldValue(form, PERSONAL_ID_FIELD_ID));
			row.setVehicleRegistrationNumber(getFieldValue(form, VEHICLE_REGISTRATION_NUMBER_FIELD_ID));
			row.setVehicleBrand(getFieldValue(form, VEHICLE_BRAND_FIELD_ID));
			row.setVehicleModel(getFieldValue(form, VEHICLE_MODEL_FIELD_ID));
			row.setVehicleEngineCapacity(getFieldValue(form, VEHICLE_ENGINE_CAPACITY_FIELD_ID));
        	
        	String[] dates = new String[3];
        	dates[0]= getFieldValue(form, PAYMENT_DATE2_FIELD_ID);
        	dates[0]= getFieldValue(form, PAYMENT_DATE3_FIELD_ID);
        	dates[0]= getFieldValue(form, PAYMENT_DATE4_FIELD_ID);
        	row.setPaymentDates(dates);
		}
	}
	
	
	protected String getFieldValue(PDAcroForm form, String fieldId) {
		String res = "";
		if(!StringUtils.isBlank(fieldId)) {
			PDField field = form.getField(fieldId);
			if(field != null) {
				res = field.getValueAsString();
			}
		}
		return res;
	}

}
