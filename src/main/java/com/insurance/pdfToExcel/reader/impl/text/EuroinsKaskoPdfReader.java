package com.insurance.pdfToExcel.reader.impl.text;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.insurance.pdfToExcel.data.PaymentRow;
import com.insurance.pdfToExcel.data.SmsSystemDataRow;
import com.insurance.pdfToExcel.util.InsuranceConstants;

public class EuroinsKaskoPdfReader extends AbstractPdfTextReader {

	public EuroinsKaskoPdfReader() {
		super();
		
		POLICY_NUMBER_SEARCH_STRING = "КАСКО на МПС №".replaceAll("\\s+", "");
		
		POLICY_END_DATE_SEARCH_STRING = "НАЧАЛО КРАЙ СТОЙНОСТ СУМА".replaceAll("\\s+", "");
		
		PERSONAL_ID_SEARCH_STRING = "ЕГН / ЕИК".replaceAll("\\s+", "");
		
		REGISTRATION_NUMBER_SEARCH_STRING = "Регистрационен №".replaceAll("\\s+", "");
		REGISTRATION_NUMBER_SEARCH_STRING_END = "Марка".replaceAll("\\s+", "");
		
		VEHICLE_BRAND_SEARCH_STRING = "Марка".replaceAll("\\s+", "");
		VEHICLE_BRAND_SEARCH_STRING_END = "Обем двг. (cm3)".replaceAll("\\s+", "");
		
		VEHICLE_CAPACITY_SEARCH_STRING = "Обем двг. (cm3)".replaceAll("\\s+", "");
		
		VEHICLE_MODEL_SEARCH_STRING = "Модел".replaceAll("\\s+", "");
		VEHICLE_MODEL_SEARCH_STRING_END = "Общо тегло".replaceAll("\\s+", "");
		
		PAYMENT_DATES_SEARCH_STRING = "ВНОСКА ДАТА НА ПАДЕЖ".replaceAll("\\s+", "");
		
	}

	@Override
	protected void readPdf(String pdf, SmsSystemDataRow row) {
		super.readPdf(pdf, row);
		row.setInsuranceCompany("Euroins-kasko");
		row.setInsuranceType(InsuranceConstants.INSURANCE_TYPE_KASKO);
	}
	
	
	@Override
	protected String extractPolicyEndDate(String currentLine, String[] lines, int currentIndex) {
		String res="";
		if(lines.length > (currentIndex + 1) &&  lines[currentIndex + 1] != null)  {
			String nextLine = lines[currentIndex + 1].replaceAll("\\s+", "");
			//10 is the length of a formatted date
			if(nextLine != null && nextLine.length() > 10) {
				String startString = "ч.";
				String endString = "г.";
				String date = nextLine.substring(nextLine.lastIndexOf(startString) +startString.length(), nextLine.lastIndexOf(endString));
				if(date != null) {
					res = date;
				}
			}
			
		}
		
		return res;
	}
	
	@Override
	String[] extractPaymentDates(String currentLine, String[] lines, int currentIndex) {
		String[] res = new String[3];

		for (int i = 1; i <= 10; i++) {
			if (lines.length > (currentIndex + i) && lines[currentIndex + i] != null) {
				String line = lines[currentIndex + i].replaceAll("\\s+", "");				
				//the first three digits should be numeric 2/3/4 followed by the day of the month
				if (line.length() > 11 && StringUtils.isNumeric(line.substring(0, 3))) {
					if (line.startsWith("2")) {
						String date2 = line.substring(1, 11);
						res[0] = date2;
					} else if (line.startsWith("3")) {
						String date3 = line.substring(1, 11);
						res[1] = date3;
					} else if (line.startsWith("4")) {
						String date4 = line.substring(1, 11);
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
				String line = lines[currentIndex + i].replaceAll("\\s+", "");				
				//the first three digits should be numeric 2/3/4 followed by the day of the month
				if (line.length() > 11 && StringUtils.isNumeric(line.substring(0, 3))) {
					if (line.startsWith("2")) {
						String date2 = line.substring(1, 11);
						PaymentRow pr = new PaymentRow();
						paymentRows.add(pr);
						
						pr.setDate(date2);
						pr.setPaymentNumber(2);
						
						setKaskoPaymenRowValues(pr, line, "BGN", 13);
					} else if (line.startsWith("3")) {
						String date3 = line.substring(1, 11);
						PaymentRow pr = new PaymentRow();
						paymentRows.add(pr);
						
						pr.setDate(date3);
						pr.setPaymentNumber(3);
						
						setKaskoPaymenRowValues(pr, line, "BGN", 13);
					} else if (line.startsWith("4")) {
						String date4 = line.substring(1, 11);
						
						PaymentRow pr = new PaymentRow();
						paymentRows.add(pr);
						
						pr.setDate(date4);
						pr.setPaymentNumber(4);
						
						setKaskoPaymenRowValues(pr, line, "BGN", 13);
						break;
					}
				}
			}
		}
		
		return paymentRows;
	}

}
