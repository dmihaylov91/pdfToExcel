package com.insurance.pdfToExcel.reader.impl.text;

import java.util.ArrayList;
import java.util.List;

import com.insurance.pdfToExcel.data.PaymentRow;
import com.insurance.pdfToExcel.data.SmsSystemDataRow;
import com.insurance.pdfToExcel.util.InsuranceConstants;

public class ArmeecKaskoPdfReader extends AbstractPdfTextReader {

	public ArmeecKaskoPdfReader(){
		super();
		POLICY_NUMBER_SEARCH_STRING = "ПОЛИЦА №:".replaceAll("\\s+", "");
		
		POLICY_END_DATE_SEARCH_STRING = "Край".replaceAll("\\s+", "");
		POLICY_END_DATE_SEARCH_STRING_END = "23:59:59".replaceAll("\\s+", "");
		
		PERSONAL_ID_SEARCH_STRING = "ЕГН/ЕИК по Булстат".replaceAll("\\s+", "");
		
		REGISTRATION_NUMBER_SEARCH_STRING = "Рег. №".replaceAll("\\s+", "");
		REGISTRATION_NUMBER_SEARCH_STRING_END = "Рама №".replaceAll("\\s+", "");
		
		VEHICLE_BRAND_SEARCH_STRING = "Марка".replaceAll("\\s+", "");
		VEHICLE_BRAND_SEARCH_STRING_END = "Вид".replaceAll("\\s+", "");

		VEHICLE_CAPACITY_SEARCH_STRING = "Обем на двигател".replaceAll("\\s+", "");
		VEHICLE_CAPACITY_SEARCH_STRING_END= "см".replaceAll("\\s+", "");

		VEHICLE_MODEL_SEARCH_STRING = "Модел".replaceAll("\\s+", "");
		VEHICLE_MODEL_SEARCH_STRING_END = "Цвят".replaceAll("\\s+", "");
		
		PAYMENT_DATES_SEARCH_STRING = "Дата/Падеж Премия".replaceAll("\\s+", "");//
		
	}
	
	@Override
	protected void readPdf(String pdf, SmsSystemDataRow row) {
		super.readPdf(pdf, row);
		row.setInsuranceCompany("Armeec-kasko");
		row.setInsuranceType(InsuranceConstants.INSURANCE_TYPE_KASKO);
	}
	
	
	@Override
	String[] extractPaymentDates(String currentLine, String[] lines, int currentIndex) {
		// TODO Auto-generated method stub
		String[] res = new String[3];
		//payment dates are in the lines below
		for(int i=1; i <= 10; i++) {
			if(lines.length > (currentIndex + i) && lines[currentIndex + i] != null) {
				String line = lines[currentIndex + i].replaceAll("\\s+", "");
				if(line.length() >= 13) {
					if(line.startsWith("II") && !line.startsWith("III")) {
						String date2 = line.substring(2, 12);
						res[0] = date2;
					}
					else if(line.startsWith("III")) {
						String date3 = line.substring(3, 13);
						res[1] = date3;
					}
					else if(line.startsWith("IV")) {
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
		
		//payment dates are in the lines below
		for(int i=1; i <= 10; i++) {
			if(lines.length > (currentIndex + i) && lines[currentIndex + i] != null) {
				String line = lines[currentIndex + i].replaceAll("\\s+", "");
				if(line.length() >= 13) {
					if(line.startsWith("II") && !line.startsWith("III")) {
						String date2 = line.substring(2, 12);
						
						PaymentRow pr = new PaymentRow();
						paymentRows.add(pr);
						
						pr.setDate(date2);
						pr.setPaymentNumber(2);
						
						setKaskoPaymenRowValues(pr, line, "лв.", 12);
					}
					else if(line.startsWith("III")) {
						String date3 = line.substring(3, 13);
						
						PaymentRow pr = new PaymentRow();
						paymentRows.add(pr);
						
						pr.setDate(date3);
						pr.setPaymentNumber(3);
						
						setKaskoPaymenRowValues(pr, line, "лв.", 13);
					}
					else if(line.startsWith("IV")) {
						String date4 = line.substring(2, 12);
						
						PaymentRow pr = new PaymentRow();
						paymentRows.add(pr);
						
						pr.setDate(date4);
						pr.setPaymentNumber(4);
						
						setKaskoPaymenRowValues(pr, line, "лв.", 12);
						
						break;
					}
				}
			}
		}
		
		return paymentRows;
	}
	
	@Override
	protected String extractVehicleBrand(String currentLine, String[] lines, int currentIndex) {
		return getLineAbove(lines, currentIndex);
	}
	
	@Override
	protected String extractVehicleModel(String currentLine, String[] lines, int currentIndex) {
		return getLineAbove(lines, currentIndex);
	}
	
	private String getLineAbove(String[] lines, int currentIndex) {
		String res="";
		if(lines[currentIndex - 1] != null) {
			res = lines[currentIndex - 1].replaceAll("\\s+", "");
		}
		return res;
	}
	

}
