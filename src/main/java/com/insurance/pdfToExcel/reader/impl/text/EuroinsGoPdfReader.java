package com.insurance.pdfToExcel.reader.impl.text;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.insurance.pdfToExcel.data.PaymentRow;
import com.insurance.pdfToExcel.data.SmsSystemDataRow;
import com.insurance.pdfToExcel.util.InsuranceConstants;

public class EuroinsGoPdfReader extends AbstractPdfTextReader {

	public EuroinsGoPdfReader() {
		super();
		
		POLICY_NUMBER_SEARCH_STRING = "ЗАСТРАХОВАТЕЛНА ПОЛИЦА №".replaceAll("\\s+", "");
		
		POLICY_END_DATE_SEARCH_STRING = "КРАЙ:23:59:00 ч.".replaceAll("\\s+", "");
		POLICY_END_DATE_SEARCH_STRING_END = "г.".replaceAll("\\s+", "");
		
		PERSONAL_ID_SEARCH_STRING = "ЕГН / ЕИК".replaceAll("\\s+", "");
		
		REGISTRATION_NUMBER_SEARCH_STRING = "Регистрационен № Рама №".replaceAll("\\s+", "");
		
		VEHICLE_BRAND_SEARCH_STRING = "Марка и модел Брой места".replaceAll("\\s+", "");
		
		VEHICLE_CAPACITY_SEARCH_STRING = "Регистрационен № Рама №".replaceAll("\\s+", "");
		
		//VEHICLE_MODEL_SEARCH_STRING = "Марка и модел Брой места".replaceAll("\\s+", "");
		
		PAYMENT_DATES_SEARCH_STRING = "ВНОСКА ДАТА НА ПАДЕЖ".replaceAll("\\s+", "");
		
	}

	@Override
	protected void readPdf(String pdf, SmsSystemDataRow row) {
		super.readPdf(pdf, row);
		row.setInsuranceCompany("Euroins-go");
		row.setInsuranceType(InsuranceConstants.INSURANCE_TYPE_GO);
	}
	
	@Override
	protected String extractPersonalId(String currentLine, String[] lines, int currentIndex) {
		String res = "";
		if(lines.length > currentIndex + 1 && lines[currentIndex + 1] != null) {
			String line = lines[currentIndex + 1];
			line = line.replaceAll("\\s+", " ").trim();
			String[] aLine = line.split(" ");
			if(aLine != null && aLine.length > 0) {
				res= aLine[0];
			}
		}
		return res;
	}
	
	@Override
	protected String extractRegistrationNumber(String currentLine, String[] lines, int currentIndex) {
		String res = "";
		if(lines.length > currentIndex + 1 && lines[currentIndex + 1] != null) {
			String line = lines[currentIndex + 1];
			line = line.replaceAll("\\s+", " ").trim();
			String[] aLine = line.split(" ");
			if(aLine != null && aLine.length > 0) {
				res= aLine[0];
			}
		}
		return res;
	}
	
	@Override
	protected String extractVehicleCapacity(String currentLine, String[] lines, int currentIndex) {
		String res = "";
		if(lines.length > currentIndex + 1 && lines[currentIndex + 1] != null) {
			String line = lines[currentIndex + 1];
			line = line.replaceAll("\\s+", " ").trim();
			String[] aLine = line.split(" ");
			if(aLine != null && aLine.length > 2) {
				res= aLine[2];
			}
		}
		return res;
	}
	
	@Override
	protected String extractVehicleBrand(String currentLine, String[] lines, int currentIndex) {
		String res = "";
		if(lines.length > currentIndex + 1 && lines[currentIndex + 1] != null) {
			String line = lines[currentIndex + 1];
			line = line.replaceAll("\\s+", " ").trim();
			String[] aLine = line.split(" ");
			if(aLine != null && aLine.length > 0) {
				res= aLine[0];
				if(aLine.length > 1) {
					res += aLine[1];
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
