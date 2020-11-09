package com.insurance.pdfToExcel.reader.impl.text;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.insurance.pdfToExcel.data.PaymentRow;
import com.insurance.pdfToExcel.data.SmsSystemDataRow;
import com.insurance.pdfToExcel.util.InsuranceConstants;

public class LevinsGoPdfReader extends AbstractPdfTextReader {

	public LevinsGoPdfReader(){
		super();
		POLICY_NUMBER_SEARCH_STRING = "Insurance policy №".replaceAll("\\s+", "");
		
		POLICY_END_DATE_SEARCH_STRING = "Till:".replaceAll("\\s+", "");
		POLICY_END_DATE_SEARCH_STRING_END = "23:59".replaceAll("\\s+", "");
		
		//no EGN/EIK in document???
		
		REGISTRATION_NUMBER_SEARCH_STRING = "Регистр. No".replaceAll("\\s+", "");

		
		VEHICLE_BRAND_SEARCH_STRING = "Марка:".replaceAll("\\s+", "");
		VEHICLE_BRAND_SEARCH_STRING_END = "Модел:".replaceAll("\\s+", "");

		//no capacity???

		VEHICLE_MODEL_SEARCH_STRING = "Модел:".replaceAll("\\s+", "");
		VEHICLE_MODEL_SEARCH_STRING_END = "Registration".replaceAll("\\s+", "");
		
		PAYMENT_DATES_SEARCH_STRING = "No Дата Основна сума".replaceAll("\\s+", "");
		
		STICKER_NUMBER_SEARCH_STRING= "Застрахован:".replaceAll("\\s+", "");
		STICKER_NUMBER_SEARCH_STRING_END= "От името".replaceAll("\\s+", "");
		
	}
	
	@Override
	protected void readPdf(String pdf, SmsSystemDataRow row) {
		super.readPdf(pdf, row);
		row.setInsuranceCompany("Levins-go");
		row.setInsuranceType(InsuranceConstants.INSURANCE_TYPE_GO);
	}
	
	@Override
	protected String extractPolicyNumber(String currentLine, String[] lines, int currentIndex) {
		//policy num contains start day as well ('/xx.xx.xxxx'), so need to remove this
		String policyNum = super.extractPolicyNumber(currentLine, lines, currentIndex);
		if(!StringUtils.isBlank(policyNum) && policyNum.length() > 11) {
			policyNum = policyNum.substring(0, policyNum.length()-11);
		}
		return policyNum;
	}
	
	@Override
	protected String extractRegistrationNumber(String currentLine, String[] lines, int currentIndex) 
	{
		//the end string could not be determined so just take first 8 digits after the start search string (the length of the registration number is 8 digits)
		String regNum = super.extractRegistrationNumber(currentLine, lines, currentIndex);
		if(!StringUtils.isBlank(regNum) && regNum.length() >=0) {
			regNum = regNum.substring(0, 8);
		}
		return regNum;
	}
	
	
	@Override
	String[] extractPaymentDates(String currentLine, String[] lines, int currentIndex) {
		String[] res = new String[3];
		// payment dates are in the lines below
		for (int i = 1; i <= 10; i++) {
			if (lines.length > (currentIndex + i) && lines[currentIndex + i] != null) {
				String line = lines[currentIndex + i].replaceAll("\\s+", "");
				if(line.length() >= 13) {
					if (line.startsWith("II") && !line.startsWith("III")) {
						String date2 = line.substring(2, 12);
						res[0] = date2;
					} else if (line.startsWith("III")) {
						String date3 = line.substring(3, 13);
						res[1] = date3;
					} else if (line.startsWith("IV")) {
						String date4 = line.substring(2, 12);
						res[2] = date4;
						break;
					}
				}
			}
		}

		return res;

	}
	
	protected List<PaymentRow> extractPaymentRows(String currentLine, String[] lines, int currentIndex){
		List<PaymentRow> paymentRows= new ArrayList<PaymentRow>();
		
		for (int i = 1; i <= 10; i++) {
			if (lines.length > (currentIndex + i) && lines[currentIndex + i] != null) {
				String line = lines[currentIndex + i].replaceAll("\\s+", " ");
				if(line.length() >= 13) {
					if (line.startsWith("II") && !line.startsWith("III")) {
						String date2 = line.substring(3, 13);
						//res[0] = date2;
						
						PaymentRow pr = new PaymentRow();
						paymentRows.add(pr);
						
						pr.setDate(date2);
						pr.setPaymentNumber(2);
						
						setGoPaymenRowValuesWithSpaces(pr, line, " ", 2);
					} else if (line.startsWith("III")) {
						String date3 = line.substring(4, 14);
						//res[1] = date3;
						
						PaymentRow pr = new PaymentRow();
						paymentRows.add(pr);
						
						pr.setDate(date3);
						pr.setPaymentNumber(3);
						
						setGoPaymenRowValuesWithSpaces(pr, line, " ", 2);
					} else if (line.startsWith("IV")) {
						String date4 = line.substring(3, 13);
						//res[2] = date4;
						
						PaymentRow pr = new PaymentRow();
						paymentRows.add(pr);
						
						pr.setDate(date4);
						pr.setPaymentNumber(4);
						
						setGoPaymenRowValuesWithSpaces(pr, line, " ", 2);
						
						break;
					}
				}
			}
		}
		
		
		return paymentRows;
	}

}
