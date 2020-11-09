/**
 * 
 */
package com.insurance.pdfToExcel.reader.impl.text;

import com.insurance.pdfToExcel.data.SmsSystemDataRow;
import com.insurance.pdfToExcel.util.InsuranceConstants;

/**
 * @author DimitarMihaylov
 *
 */
public class DalbogGoPdfReader extends AbstractPdfTextReader {

	
	//protected String POLICY_END_DATE_SEARCH_STRING_ACTUAL = "до: 23:59 на:".replaceAll("\\s+", "");
	protected String PAYMENT_DATES_SEARCH_STRING_WITH_SPACES = "Дата/ Date";
			
	public DalbogGoPdfReader() {
		super();
		
		POLICY_NUMBER_SEARCH_STRING = "на автомобилистите №".replaceAll("\\s+", "");
		
		POLICY_END_DATE_SEARCH_STRING = "до: 23:59 на:".replaceAll("\\s+", "");
		
		PERSONAL_ID_SEARCH_STRING = " n ю.л./ЕТ/".replaceAll("\\s+", "");
		
		//this is line above
		REGISTRATION_NUMBER_SEARCH_STRING = "Рег No/Reg".replaceAll("\\s+", "");
		
		VEHICLE_BRAND_SEARCH_STRING = "Марка/Мake".replaceAll("\\s+", "");
		
		//no capacity
		
		//no model
		
		PAYMENT_DATES_SEARCH_STRING = "Дата/ Date".replaceAll("\\s+", "");
		
	}

	@Override
	protected void readPdf(String pdf, SmsSystemDataRow row) {
		super.readPdf(pdf, row);
		row.setInsuranceCompany("Dalbog-go");
		row.setInsuranceType(InsuranceConstants.INSURANCE_TYPE_GO);
	}
	
//	@Override
//	protected String extractPolicyEndDate(String currentLine, String[] lines, int currentIndex) {
//		String res = "";
//		
//		String nextLine = lines[currentIndex + 1];
//		if(nextLine != null) {
//			nextLine = nextLine.replaceAll("\\s+", "");
//			if(nextLine.length() > POLICY_END_DATE_SEARCH_STRING_ACTUAL.length()) {
//				res = extractStringBetween(nextLine, POLICY_END_DATE_SEARCH_STRING_ACTUAL, null);
//			}
//		}
//		return res;
//	}
	
	
	protected String extractRegistrationNumber(String currentLine, String[] lines, int currentIndex) 
	{
		//reg num is first on the next line ; lenght is 8
		String res = "";
		String nextLine = lines[currentIndex + 1];
		if(nextLine != null) {
			nextLine = nextLine.replaceAll("\\s+", "");
			if(nextLine.length() >=8) {
				res = nextLine.substring(0,  7);
			}
		}
		
		return res;
	}
	
	@Override
	protected String extractVehicleBrand(String currentLine, String[] lines, int currentIndex) {
		String res = "";		
		if(lines.length > (currentIndex + 1) && lines[currentIndex + 1] != null) {
			String nextLine = lines[currentIndex + 1];
			if(nextLine.contains(" ")) {
				res = nextLine.substring(0, nextLine.indexOf(" "));
			}else {
				res = nextLine;
			}
			
		}
		
		return res;
	}
	
	
	@Override
	protected String[] extractPaymentDates(String currentLine, String[] lines, int currentIndex) {		
		String[] paymentDates = new String[3];
		
		//need spaces, dates are separated by spaces
		String line = lines[currentIndex];
		if(line != null && line.length() > PAYMENT_DATES_SEARCH_STRING_WITH_SPACES.length()) {
			String sDates = extractStringBetween(line, PAYMENT_DATES_SEARCH_STRING_WITH_SPACES, null).trim();
			if(sDates != null && sDates.contains(" ")){
				String[] aDates = sDates.split(" ");
				if(aDates != null && aDates.length > 1) {
					for(int i=1; i<aDates.length && i<4; i++) {
						paymentDates[i-1] = aDates[i];
					}
				}
			}
		}
		
		return paymentDates;
	}

}
