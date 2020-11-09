package com.insurance.pdfToExcel.reader.impl.text;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.insurance.pdfToExcel.data.PaymentRow;
import com.insurance.pdfToExcel.data.SmsSystemDataRow;
import com.insurance.pdfToExcel.util.InsuranceConstants;

public class ArmeecGoPdfReader extends AbstractPdfTextReader {
	
	
	public ArmeecGoPdfReader(){
		super();
		POLICY_NUMBER_SEARCH_STRING = "№:".replaceAll("\\s+", "");
		
		POLICY_END_DATE_SEARCH_STRING = "КРАЙ:".replaceAll("\\s+", "");
		POLICY_END_DATE_SEARCH_STRING_END = "23:59".replaceAll("\\s+", "");
		
		PERSONAL_ID_SEARCH_STRING = "ЕГН / ЕИК".replaceAll("\\s+", "");
		
		REGISTRATION_NUMBER_SEARCH_STRING = "Регистрационен №".replaceAll("\\s+", "");
		REGISTRATION_NUMBER_SEARCH_STRING_END = "Рама №".replaceAll("\\s+", "");
		
		VEHICLE_BRAND_SEARCH_STRING = "Марка".replaceAll("\\s+", "");

		VEHICLE_CAPACITY_SEARCH_STRING = "Обем на двигателя".replaceAll("\\s+", "");
		VEHICLE_CAPACITY_SEARCH_STRING_END= "(см".replaceAll("\\s+", "");

		VEHICLE_MODEL_SEARCH_STRING = "Марка и модел".replaceAll("\\s+", "");
		VEHICLE_MODEL_SEARCH_STRING_END = "Цвят".replaceAll("\\s+", "");
		
		PAYMENT_DATES_SEARCH_STRING = "ДАТА НА ПАДЕЖ".replaceAll("\\s+", "");
		
		GREEN_CARD_NUMBER_SEARCH_STRING= "Зелена карта №:".replaceAll("\\s+", "");
		GREEN_CARD_NUMBER_SEARCH_STRING_END= "на бланка".replaceAll("\\s+", "");
		
		STICKER_NUMBER_SEARCH_STRING= "с номер:".replaceAll("\\s+", "");
		
	}
	
	@Override
	protected void readPdf(String pdf, SmsSystemDataRow row) {
		super.readPdf(pdf, row);
		row.setInsuranceCompany("Armeec-go");
		row.setInsuranceType(InsuranceConstants.INSURANCE_TYPE_GO);
		
		//the extracted model actually contains brand followed by model, so remove brand from model string
		String model = row.getVehicleModel();
		String brand = row.getVehicleBrand();
		if(!StringUtils.isBlank(model) && !StringUtils.isBlank(brand) && model.contains(brand)) {
			row.setVehicleModel(model.substring(model.indexOf(brand) + brand.length()));
		}
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
						
						setGoPaymenRowValues(pr, line, "лв.", 11);
						
					} else if (line.startsWith("3")) {
						String date3 = line.substring(1, 11);
						PaymentRow pr = new PaymentRow();
						paymentRows.add(pr);
						
						pr.setDate(date3);
						pr.setPaymentNumber(3);
						
						setGoPaymenRowValues(pr, line, "лв.", 11);
					} else if (line.startsWith("4")) {
						String date4 = line.substring(1, 11);
						
						PaymentRow pr = new PaymentRow();
						paymentRows.add(pr);
						
						pr.setDate(date4);
						pr.setPaymentNumber(4);
						
						setGoPaymenRowValues(pr, line, "лв.", 11);
						break;
					}
				}
			}
		}
		
		return paymentRows;
	}

}
