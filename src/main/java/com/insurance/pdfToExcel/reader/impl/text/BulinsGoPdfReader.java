package com.insurance.pdfToExcel.reader.impl.text;

import java.util.ArrayList;
import java.util.List;

import com.insurance.pdfToExcel.data.PaymentRow;
import com.insurance.pdfToExcel.data.SmsSystemDataRow;
import com.insurance.pdfToExcel.util.InsuranceConstants;


public class BulinsGoPdfReader extends AbstractPdfTextReader {
	
	private String PAYMENT_DATES_SEARCH_STRING_WITH_SPACES = "Брой вноски";
	
	public BulinsGoPdfReader(){
		super();
		POLICY_NUMBER_SEARCH_STRING = "№".replaceAll("\\s+", "");
		POLICY_NUMBER_SEARCH_STRING_END = "сертификат ЗК №".replaceAll("\\s+", "");
		
		POLICY_END_DATE_SEARCH_STRING = "до 2 3 5 9".replaceAll("\\s+", "");
		
		PERSONAL_ID_SEARCH_STRING = "ЕИКЕГН".replaceAll("\\s+", "");
		
		REGISTRATION_NUMBER_SEARCH_STRING = "Рег. №".replaceAll("\\s+", "");
		REGISTRATION_NUMBER_SEARCH_STRING_END = "Марка".replaceAll("\\s+", "");
		
		VEHICLE_BRAND_SEARCH_STRING = "Марка".replaceAll("\\s+", "");
		VEHICLE_BRAND_SEARCH_STRING_END = "Модификация".replaceAll("\\s+", "");

		VEHICLE_CAPACITY_SEARCH_STRING = "двигателя".replaceAll("\\s+", "");
		VEHICLE_CAPACITY_SEARCH_STRING_END= "Обща маса".replaceAll("\\s+", "");

		VEHICLE_MODEL_SEARCH_STRING = "Модел".replaceAll("\\s+", "");
		VEHICLE_MODEL_SEARCH_STRING_END = "производство".replaceAll("\\s+", "");
		
		PAYMENT_DATES_SEARCH_STRING = "Брой вноски".replaceAll("\\s+", "");//
		
	}

	@Override
	protected void readPdf(String pdf, SmsSystemDataRow row) {
		super.readPdf(pdf, row);
		row.setInsuranceCompany("Bulins-go");
		row.setInsuranceType(InsuranceConstants.INSURANCE_TYPE_GO);
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
