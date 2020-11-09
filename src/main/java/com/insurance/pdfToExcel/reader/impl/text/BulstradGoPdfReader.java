package com.insurance.pdfToExcel.reader.impl.text;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.insurance.pdfToExcel.data.PaymentRow;
import com.insurance.pdfToExcel.data.SmsSystemDataRow;
import com.insurance.pdfToExcel.util.InsuranceConstants;

public class BulstradGoPdfReader extends AbstractPdfTextReader {
	

	protected String PAYMENT_DATE_TABLE_SEARCH_STRING_WITH_SPACES = "Дата на падеж *";
	protected String VEHICLE_BRAND_SEARCH_STRING_WITH_SPACES = "Make and model:";
	
	public BulstradGoPdfReader(){
		super();
		
		POLICY_NUMBER_SEARCH_STRING = "г. №".replaceAll("\\s+", "");
		POLICY_NUMBER_SEARCH_STRING_END = "ЗАСТРАХОВАНИЯ".replaceAll("\\s+", "");
		
		POLICY_END_DATE_SEARCH_STRING = "до/till: 23:59 |".replaceAll("\\s+", "");
		
		//no personal id?
		
		REGISTRATION_NUMBER_SEARCH_STRING = "МПС Рег.№ / Vehicle Reg.№".replaceAll("\\s+", "");
		REGISTRATION_NUMBER_SEARCH_STRING_END = "Рама № / Chassis".replaceAll("\\s+", "");
		
		VEHICLE_BRAND_SEARCH_STRING = "Марка и модел / Make and model:".replaceAll("\\s+", "");

		VEHICLE_MODEL_SEARCH_STRING = "Марка и модел / Make and model:".replaceAll("\\s+", "");
		
		//no vehicle capacity?
		
		PAYMENT_DATES_SEARCH_STRING = "Дата на падеж *".replaceAll("\\s+", "");
		
		GREEN_CARD_NUMBER_SEARCH_STRING= "ЗЕЛЕНА КАРТА / GREEN CARD".replaceAll("\\s+", "");
			
		//no sticker
		
	}
	
	
	@Override
	protected void readPdf(String pdf, SmsSystemDataRow row) {
		super.readPdf(pdf, row);
		row.setInsuranceCompany("Bulstrad-go");
		row.setInsuranceType(InsuranceConstants.INSURANCE_TYPE_GO);
		
		String brand = row.getVehicleBrand();
		//brand should be contained in model
		if(brand != null && brand.contains(" ")) {
			String[] a = brand.split(" ");
			if(a != null && a.length > 1) {
				row.setVehicleBrand(a[0]);
				row.setVehicleModel(a[1]);
			}
		}
	}
	
	@Override
	protected String extractVehicleBrand(String currentLine, String[] lines, int currentIndex) {
		String brand = "";
		//need to use spaces here because brand and model are on the same line separated by a space
		String line = lines[currentIndex];
		if(line != null && line.length() > VEHICLE_BRAND_SEARCH_STRING_WITH_SPACES.length()) {
			String sbrand= line.substring(line.indexOf(VEHICLE_BRAND_SEARCH_STRING_WITH_SPACES) + VEHICLE_BRAND_SEARCH_STRING_WITH_SPACES.length()).trim();
			if(brand != null) {
				return brand = sbrand;
			}
		}
		return brand;
	}
	
	@Override
	protected String extractVehicleModel(String currentLine, String[] lines, int currentIndex) {
		//brand is contained in model 
		return "";	
	}

	@Override
	String[] extractPaymentDates(String currentLine, String[] lines, int currentIndex) {
		String[] res = new String[3];
		
		String line = lines[currentIndex];
		if(line.length() > PAYMENT_DATE_TABLE_SEARCH_STRING_WITH_SPACES.length()) {
			String sdates = line.substring(line.indexOf(PAYMENT_DATE_TABLE_SEARCH_STRING_WITH_SPACES) + PAYMENT_DATE_TABLE_SEARCH_STRING_WITH_SPACES.length()).trim();
			if(!StringUtils.isBlank(sdates)) {
				String[] aDates = sdates.split(" ");
				if(aDates != null && aDates.length > 0) {
					for(int i=0; i<aDates.length && i<3; i++) {
						res[i] = aDates[i];
					}
				}
			}
		}
		
		return res;
	}
	
	protected List<PaymentRow> extractPaymentRows(String currentLine, String[] lines, int currentIndex){
		List<PaymentRow> paymentRows= new ArrayList<PaymentRow>();
		
		if (lines.length > currentIndex && lines[currentIndex] != null) {
			this.setGoPaymentRowsPaymentDates(paymentRows, lines[currentIndex], PAYMENT_DATE_TABLE_SEARCH_STRING_WITH_SPACES, " ", 1);
		}

		if (lines.length > currentIndex + 1 && lines[currentIndex + 1] != null) {

			setGoPaymentRowsPaymentValues(paymentRows, lines[currentIndex + 1].replaceAll("\\s+", " "),"Застрахователна", " ", 1);
		}

		if (lines.length > currentIndex + 3 && lines[currentIndex + 3] != null) {

			setGoPaymentRowsPaymentValueTaxes(paymentRows, lines[currentIndex + 3].replaceAll("\\s+", " "), "ДЗП", " ", 1);
		}

		// gf and of are paid at initial payment so skip this line and go straight to
		// totals

		if (lines.length > currentIndex + 5 && lines[currentIndex + 5] != null) {

			setGoPaymentRowsPaymentTotalValues(paymentRows, lines[currentIndex + 5].replaceAll("\\s+", " "), "Общо дължима сума", " ", 1);
		}
			
		
		
		return paymentRows;
	}

}
