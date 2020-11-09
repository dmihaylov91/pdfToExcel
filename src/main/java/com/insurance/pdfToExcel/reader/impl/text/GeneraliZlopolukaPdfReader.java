package com.insurance.pdfToExcel.reader.impl.text;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.insurance.pdfToExcel.data.PaymentRow;
import com.insurance.pdfToExcel.data.SmsSystemDataRow;

public class GeneraliZlopolukaPdfReader extends AbstractPdfTextReader {

	public GeneraliZlopolukaPdfReader() {
		super();
		
		POLICY_NUMBER_SEARCH_STRING = "ПОЛИЦА №".replaceAll("\\s+", "");
		POLICY_NUMBER_SEARCH_STRING_END = "Териториална структура".replaceAll("\\s+", "");
		
		POLICY_END_DATE_SEARCH_STRING = "До/To 23:59 ч./h на".replaceAll("\\s+", "");
		POLICY_END_DATE_SEARCH_STRING_END = "г.".replaceAll("\\s+", "");
		
		//no egn
		
		REGISTRATION_NUMBER_SEARCH_STRING = "Регистрационен/Registration №".replaceAll("\\s+", "");
		REGISTRATION_NUMBER_SEARCH_STRING_END = "Вид на МПС".replaceAll("\\s+", "");
		
		VEHICLE_BRAND_SEARCH_STRING = "Марка/Make".replaceAll("\\s+", "");
		VEHICLE_BRAND_SEARCH_STRING_END = "Модел/Model".replaceAll("\\s+", "");
		
		//no capacity
		
		VEHICLE_MODEL_SEARCH_STRING = "Модел/Model".replaceAll("\\s+", "");
		
		PAYMENT_DATES_SEARCH_STRING = "№ Дата на вноските/Date".replaceAll("\\s+", "");
		
	}

	@Override
	protected void readPdf(String pdf, SmsSystemDataRow row) {
		super.readPdf(pdf, row);
		row.setInsuranceCompany("Generali-zlopoluka");
		
		//the extracted model actually contains brand followed by model, so remove brand from model string
		String model = row.getVehicleModel();
		String brand = row.getVehicleBrand();
		if(!StringUtils.isBlank(model) && !StringUtils.isBlank(brand) && model.contains(brand)) {
			row.setVehicleModel(model.substring(model.indexOf(brand) + brand.length()));
		}
	}
	
	
	@Override
	protected String[] extractPaymentDates(String currentLine, String[] lines, int currentIndex) {
		
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
				
		return paymentRows;
	}

}
