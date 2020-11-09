package com.insurance.pdfToExcel.reader.impl.text;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.insurance.pdfToExcel.data.PaymentRow;
import com.insurance.pdfToExcel.data.SmsSystemDataRow;
import com.insurance.pdfToExcel.util.InsuranceConstants;

public class LevinsKaskoPdfReader extends AbstractPdfTextReader {
	
	private String PAYMENT_DATES_SEARCH_STRING_WITH_SPACES= "Дължима до:";
	
	
	public LevinsKaskoPdfReader(){
		super();
		POLICY_NUMBER_SEARCH_STRING = "факс:".replaceAll("\\s+", "");
		
		POLICY_END_DATE_SEARCH_STRING = "24:00 ч. на:".replaceAll("\\s+", "");
		POLICY_END_DATE_SEARCH_STRING_END = "г.".replaceAll("\\s+", "");
		
		PERSONAL_ID_SEARCH_STRING = "ЕГН/ЕИК".replaceAll("\\s+", "");
		PERSONAL_ID_SEARCH_STRING_END = "Данъчен".replaceAll("\\s+", "");
		
		REGISTRATION_NUMBER_SEARCH_STRING = "Рег. №".replaceAll("\\s+", "");
		
		VEHICLE_BRAND_SEARCH_STRING = "Марка:".replaceAll("\\s+", "");

		VEHICLE_CAPACITY_SEARCH_STRING = "Обем:".replaceAll("\\s+", "");

		VEHICLE_MODEL_SEARCH_STRING = "Модел:".replaceAll("\\s+", "");
		
		PAYMENT_DATES_SEARCH_STRING = "Дължима до:".replaceAll("\\s+", "");
		
	}
	
	
	@Override
	protected void readPdf(String pdf, SmsSystemDataRow row) {
		super.readPdf(pdf, row);
		row.setInsuranceCompany("Levins-kasko");
		row.setInsuranceType(InsuranceConstants.INSURANCE_TYPE_KASKO);
	}
	
	@Override
	protected String extractPolicyNumber(String currentLine, String[] lines, int currentIndex) {
		
		String policyNumber="";
		//the policy number is on the next line after the one containing the search string
		if(lines.length > (currentIndex + 1) && lines[currentIndex + 1] != null) {
			String line = lines[currentIndex + 1].replaceAll("\\s+", "");
			//the first character should be just the sign for number '№'
			if(line.length() > 1) {
				policyNumber = line.substring(1);
			}
		}
		return policyNumber;
	}

	@Override
	String[] extractPaymentDates(String currentLine, String[] lines, int currentIndex) {
		String[] res = new String[3];
		//dates should be on the current line of the search string in format like this 'Дължима до: 18.09.2018 18.12.2018 18.03.2019 18.06.2019'
		//need to use spaces so retrieve currentLine with spaces as they are removed by default
		String line = lines[currentIndex];
		String dates[] = line.split(" ");
		//we are interested in the last 3 dates (the first three entries in array should be 'дължима', 'до:', '18.09.2018')
		if(dates.length >= 4) {
			for(int i=3; i<dates.length && i<6; i++) {
				res[i-3] = dates[i];
			}
		}
		
		return res;
	}
	
	protected List<PaymentRow> extractPaymentRows(String currentLine, String[] lines, int currentIndex){
		List<PaymentRow> paymentRows= new ArrayList<PaymentRow>();
		
		String sPaymentDaysLine = extractStringBetween(lines[currentIndex], PAYMENT_DATES_SEARCH_STRING_WITH_SPACES, null);
		
		if(!StringUtils.isBlank(sPaymentDaysLine)) {
			this.setGoPaymentRowsPaymentDates(paymentRows, sPaymentDaysLine, " ", " ", 1);
		}

		if(lines.length > currentIndex + -1 && lines[currentIndex - 1] != null) {
		
			setGoPaymentRowsPaymentValues(paymentRows, lines[currentIndex - 1], "размер на:", " ", 1);
			setGoPaymentRowsPaymentTotalValues(paymentRows, lines[currentIndex - 1], "размер на:", " ", 1);
		}
		
				
		return paymentRows;
	}

}
