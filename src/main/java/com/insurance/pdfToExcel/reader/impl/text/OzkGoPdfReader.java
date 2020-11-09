package com.insurance.pdfToExcel.reader.impl.text;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.insurance.pdfToExcel.data.PaymentRow;
import com.insurance.pdfToExcel.data.SmsSystemDataRow;
import com.insurance.pdfToExcel.util.InsuranceConstants;

public class OzkGoPdfReader extends AbstractPdfTextReader{
		

	public OzkGoPdfReader() {
		super();
		
		POLICY_NUMBER_SEARCH_STRING = "INSURANCE POLICY №".replaceAll("\\s+", "");
		
		POLICY_END_DATE_SEARCH_STRING = "23:59:59 часа на".replaceAll("\\s+", "");
		
		PERSONAL_ID_SEARCH_STRING = "ЕГН | ID:".replaceAll("\\s+", "");
		
		REGISTRATION_NUMBER_SEARCH_STRING = "Номер|Reg.No:".replaceAll("\\s+", "");
		
		VEHICLE_BRAND_SEARCH_STRING = "Марка|Brand:".replaceAll("\\s+", "");
		VEHICLE_BRAND_SEARCH_STRING_END = "Обем на двигателя|Engine capacity:".replaceAll("\\s+", "");
		
		VEHICLE_CAPACITY_SEARCH_STRING = "Обем на двигателя|Engine capacity:".replaceAll("\\s+", "");
		
		VEHICLE_MODEL_SEARCH_STRING = "Модел|Model:".replaceAll("\\s+", "");
		VEHICLE_MODEL_SEARCH_STRING_END = "Макс. маса (F1):".replaceAll("\\s+", "");
		
		PAYMENT_DATES_SEARCH_STRING = "Дата на падеж".replaceAll("\\s+", "");
		
		GREEN_CARD_NUMBER_SEARCH_STRING= "Сертификат Зелена Карта №".replaceAll("\\s+", "");
		GREEN_CARD_NUMBER_SEARCH_STRING_END= "Стикер №".replaceAll("\\s+", "");
		
		STICKER_NUMBER_SEARCH_STRING= "Стикер №".replaceAll("\\s+", "");
		
	}

	@Override
	protected void readPdf(String pdf, SmsSystemDataRow row) {
		super.readPdf(pdf, row);
		row.setInsuranceCompany("ozk-go");
		row.setInsuranceType(InsuranceConstants.INSURANCE_TYPE_GO);
	}
	
	@Override
	protected String extractPersonalId(String currentLine, String[] lines, int currentIndex){
		//the end string could not be determined so just take first 10 digits after the start search string (the length of the id  is 10 digits)
		String personalId = extractStringBetween(currentLine, PERSONAL_ID_SEARCH_STRING, PERSONAL_ID_SEARCH_STRING_END);
		if(!StringUtils.isBlank(personalId) && personalId.length() >=10) {
			personalId = personalId.substring(0, 10);
		}
		return personalId;
	}
	
	
	@Override
	protected String[] extractPaymentDates(String currentLine, String[] lines, int currentIndex) {
		
		String[] paymentDates = new String[3];
		
		for(int j = 0 ; j<3; j++) {
			if (lines.length > (currentIndex+j+2) && lines[currentIndex+j+2] != null){
				String line = lines[currentIndex+j+2].replaceAll("\\s+", "");
				
				if(line.length() >= 10) {
					//the first 10 digits should contain the payment date if such exists
					String paymentDate = line.substring(0, 10);
					
					if(isDate(paymentDate))  {
						paymentDates[j] = paymentDate;
					}
					else  
					{
						//this is not a date so break out of loop
						break;
					}
				}

			}
		}
		return paymentDates;
	}
	
	protected List<PaymentRow> extractPaymentRows(String currentLine, String[] lines, int currentIndex){
		List<PaymentRow> paymentRows= new ArrayList<PaymentRow>();
		
		for(int j = 0 ; j<3; j++) {
			if (lines.length > (currentIndex+j+2) && lines[currentIndex+j+2] != null){
				String line = lines[currentIndex+j+2].replaceAll("\\s+", "");
				
				if(line.length() >= 10) {
					//the first 10 digits should contain the payment date if such exists
					String paymentDate = line.substring(0, 10);
					
					if(isDate(paymentDate))  {
						PaymentRow pr = new PaymentRow();
						paymentRows.add(pr);
						
						pr.setDate(paymentDate);
						pr.setPaymentNumber(paymentRows.size() + 1);
						
						setGoPaymenRowValues(pr, line, "BGN", 10);
					}
					else  
					{
						//this is not a date so break out of loop
						break;
					}
				}

			}
		}
		
		return paymentRows;
	}
	
}
