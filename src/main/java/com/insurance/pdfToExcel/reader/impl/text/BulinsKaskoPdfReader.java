package com.insurance.pdfToExcel.reader.impl.text;

import java.util.ArrayList;
import java.util.List;

import com.insurance.pdfToExcel.data.PaymentRow;
import com.insurance.pdfToExcel.data.SmsSystemDataRow;
import com.insurance.pdfToExcel.util.InsuranceConstants;

public class BulinsKaskoPdfReader extends AbstractPdfTextReader {
	
	private String PAYMENT_DATES_SEARCH_STRING_WITH_SPACES = "Брой вноски";
	
	public BulinsKaskoPdfReader(){
		super();
		POLICY_NUMBER_SEARCH_STRING = "писмена декларация №".replaceAll("\\s+", "");
		//POLICY_NUMBER_SEARCH_STRING_END = "сертификат ЗК №".replaceAll("\\s+", "");
		
		POLICY_END_DATE_SEARCH_STRING = "до 2  4  :0  0 часа на".replaceAll("\\s+", "");
		POLICY_END_DATE_SEARCH_STRING_END = "г.".replaceAll("\\s+", "");
		
		PERSONAL_ID_SEARCH_STRING = "ЕГН/ЕИК:".replaceAll("\\s+", "");
		
		REGISTRATION_NUMBER_SEARCH_STRING = "ДАННИ ЗА ЗАСТРАХОВАНОТО МПС:".replaceAll("\\s+", "");
		
		VEHICLE_BRAND_SEARCH_STRING = "Марка:".replaceAll("\\s+", "");
		//VEHICLE_BRAND_SEARCH_STRING_END = "носимост:".replaceAll("\\s+", "");

		VEHICLE_CAPACITY_SEARCH_STRING = "Марка:".replaceAll("\\s+", "");
		//VEHICLE_CAPACITY_SEARCH_STRING_END= "Обща маса".replaceAll("\\s+", "");

		VEHICLE_MODEL_SEARCH_STRING = "Модел:".replaceAll("\\s+", "");
		VEHICLE_MODEL_SEARCH_STRING_END = "носимост".replaceAll("\\s+", "");
		
		PAYMENT_DATES_SEARCH_STRING = "Брой вноски".replaceAll("\\s+", "");//
		
	}

	@Override
	protected void readPdf(String pdf, SmsSystemDataRow row) {
		super.readPdf(pdf, row);
		row.setInsuranceCompany("Bulins-kasko");
		row.setInsuranceType(InsuranceConstants.INSURANCE_TYPE_KASKO);
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
	protected String extractVehicleBrand(String currentLine, String[] lines, int currentIndex) {
		String brand = "";
		//need to use spaces here because brand and model are on the same line separated by a space
		String line = lines[currentIndex];
		if(line != null && line.length() > VEHICLE_BRAND_SEARCH_STRING.length()) {
			line = line.replaceAll("\\s+", " ");
			String sbrandLine= extractStringBetween(line, VEHICLE_BRAND_SEARCH_STRING, null);
			if(brand != null) {
				String[] abran = sbrandLine.trim().split(" ");
				if(abran != null && abran.length > 0)
				{
					return abran[0];
				}
			}
		}
		return brand;
	}
	
	@Override
	protected String extractVehicleCapacity(String currentLine, String[] lines, int currentIndex) {
		String brand = "";
		//need to use spaces here because brand and model are on the same line separated by a space
		String line = lines[currentIndex];
		if(line != null && line.length() > VEHICLE_BRAND_SEARCH_STRING.length()) {
			line = line.replaceAll("\\s+", " ");
			String sbrandLine= extractStringBetween(line, VEHICLE_BRAND_SEARCH_STRING, null);
			if(brand != null) {
				String[] abran = sbrandLine.trim().split(" ");
				if(abran != null && abran.length > 1)
				{
					return abran[1];
				}
			}
		}
		return brand;
	}
	
	@Override
	protected String[] extractPaymentDates(String currentLine, String[] lines, int currentIndex) {
		
		String[] paymentDates = new String[3];
		
		String sPaymentDays = extractStringBetween(currentLine, PAYMENT_DATES_SEARCH_STRING, PAYMENT_DATES_SEARCH_STRING_END);
		
		String[] aPaymentDays = sPaymentDays.split("г.");
		if(aPaymentDays != null && aPaymentDays.length > 1) {
			for(int i = 1; (i<aPaymentDays.length && i<4); i++) {
				paymentDates[i-1] = aPaymentDays[i];
			}
		}
		return paymentDates;
	}
	
	protected List<PaymentRow> extractPaymentRows(String currentLine, String[] lines, int currentIndex){
		List<PaymentRow> paymentRows= new ArrayList<PaymentRow>();
				
		if(lines.length > currentIndex && lines[currentIndex] != null) {
			this.setGoPaymentRowsPaymentDates(paymentRows, lines[currentIndex], PAYMENT_DATES_SEARCH_STRING_WITH_SPACES, "г.", 1);
		}

		if(lines.length > currentIndex + 1 && lines[currentIndex + 1] != null) {
		
			setGoPaymentRowsPaymentValues(paymentRows, lines[currentIndex + 1], "BGN", " ", 2);
		}
		
		if(lines.length > currentIndex + 2 && lines[currentIndex + 2] != null) {
			
			setGoPaymentRowsPaymentValueTaxes(paymentRows, lines[currentIndex + 2], "BGN", " ", 1);
		}
				
		//gf and of are paid at initial payment so skip this line and go straight to totals
		
		if(lines.length > currentIndex + 4 && lines[currentIndex + 4] != null) {
			
			setGoPaymentRowsPaymentTotalValues(paymentRows, lines[currentIndex + 4], "BGN", " ", 1);
		}
				
		return paymentRows;
	}
	
	

}