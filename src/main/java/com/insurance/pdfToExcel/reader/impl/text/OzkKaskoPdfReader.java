package com.insurance.pdfToExcel.reader.impl.text;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.insurance.pdfToExcel.data.PaymentRow;
import com.insurance.pdfToExcel.data.SmsSystemDataRow;
import com.insurance.pdfToExcel.util.InsuranceConstants;

public class OzkKaskoPdfReader extends AbstractPdfTextReader{
	

public OzkKaskoPdfReader() {
	super();
	
	POLICY_NUMBER_SEARCH_STRING = "ЗАСТРАХОВАТЕЛНА ПОЛИЦА №".replaceAll("\\s+", "");
	
	POLICY_END_DATE_SEARCH_STRING = "59 часа на".replaceAll("\\s+", "");
	POLICY_END_DATE_SEARCH_STRING_END = "г.".replaceAll("\\s+", "");
	
	PERSONAL_ID_SEARCH_STRING = "ЕГН/ЕИК:".replaceAll("\\s+", "");
	
	REGISTRATION_NUMBER_SEARCH_STRING = "Рег.№:".replaceAll("\\s+", "");
	REGISTRATION_NUMBER_SEARCH_STRING_END = "Дата на първа".replaceAll("\\s+", "");
	
	VEHICLE_BRAND_SEARCH_STRING = "Марка:".replaceAll("\\s+", "");
	VEHICLE_BRAND_SEARCH_STRING_END = "Година на".replaceAll("\\s+", "");
	
	VEHICLE_CAPACITY_SEARCH_STRING = "Обем на двиг.:".replaceAll("\\s+", "");
	VEHICLE_CAPACITY_SEARCH_STRING_END = "куб.см".replaceAll("\\s+", "");
	
	VEHICLE_MODEL_SEARCH_STRING = "Модел:".replaceAll("\\s+", "");
	VEHICLE_MODEL_SEARCH_STRING_END = "Бр.места".replaceAll("\\s+", "");
	
	PAYMENT_DATES_SEARCH_STRING = "Вноски Платима до дата".replaceAll("\\s+", "");
	
}

@Override
protected void readPdf(String pdf, SmsSystemDataRow row) {
	super.readPdf(pdf, row);
	row.setInsuranceCompany("ozk-kasko");
	row.setInsuranceType(InsuranceConstants.INSURANCE_TYPE_KASKO);
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
				String paymentDate = line.substring(1, 11);
				
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