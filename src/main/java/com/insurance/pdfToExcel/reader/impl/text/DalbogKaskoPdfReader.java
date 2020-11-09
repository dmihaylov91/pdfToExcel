package com.insurance.pdfToExcel.reader.impl.text;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.insurance.pdfToExcel.data.PaymentRow;
import com.insurance.pdfToExcel.data.SmsSystemDataRow;
import com.insurance.pdfToExcel.util.InsuranceConstants;

public class DalbogKaskoPdfReader extends AbstractPdfTextReader {
	

	public DalbogKaskoPdfReader() {
		super();
		
		POLICY_NUMBER_SEARCH_STRING = "Застрахователна полица №".replaceAll("\\s+", "");
		POLICY_NUMBER_SEARCH_STRING_END = "валидност от".replaceAll("\\s+", "");
		
		POLICY_END_DATE_SEARCH_STRING = "до 24:00 часа на".replaceAll("\\s+", "");
		
		PERSONAL_ID_SEARCH_STRING = "n ю.л./ЕТ".replaceAll("\\s+", "");
		
		REGISTRATION_NUMBER_SEARCH_STRING = "Рег № ".replaceAll("\\s+", "");
		
		VEHICLE_BRAND_SEARCH_STRING = "Марка Модел".replaceAll("\\s+", "");
		
		VEHICLE_CAPACITY_SEARCH_STRING = "Обем на двигателя".replaceAll("\\s+", "");
		
		VEHICLE_MODEL_SEARCH_STRING = "Марка Модел".replaceAll("\\s+", "");

		
		PAYMENT_DATES_SEARCH_STRING = "Дата падеж".replaceAll("\\s+", "");
		
	}

	@Override
	protected void readPdf(String pdf, SmsSystemDataRow row) {
		super.readPdf(pdf, row);
		row.setInsuranceCompany("Dalbog-kasko");
		row.setInsuranceType(InsuranceConstants.INSURANCE_TYPE_KASKO);
		
		//the extracted model actually contains brand followed by model, so remove brand from model string
		String model = row.getVehicleModel();
		String brand = row.getVehicleBrand();
		if(!StringUtils.isBlank(model) && !StringUtils.isBlank(brand) && model.contains(brand)) {
			row.setVehicleModel(model.substring(model.indexOf(brand) + brand.length()).trim());
		}
	}
	
	@Override
	protected String extractRegistrationNumber(String currentLine, String[] lines, int currentIndex) 
	{
		//reg num is first on the next line ; lenght is 8
		String res = "";		
		if(lines.length > (currentIndex + 1) &&  lines[currentIndex + 1] != null) {
			String nextLine = lines[currentIndex + 1];
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
		if(lines.length > (currentIndex + 1) &&  lines[currentIndex + 1] != null)  {
			String nextLine = lines[currentIndex + 1];
			nextLine = nextLine.trim();
			if(nextLine.contains(" ")) {
				res = nextLine.substring(0, nextLine.indexOf(" "));
			}else {
				res = nextLine;
			}
			
		}
		
		return res;
	}
	
	@Override
	protected String extractVehicleCapacity(String currentLine, String[] lines, int currentIndex) {
		String res = "";		
		if(lines.length > (currentIndex + 1) &&  lines[currentIndex + 1] != null) {
			String nextLine = lines[currentIndex + 1];
			nextLine = nextLine.trim();
			if(nextLine.contains(" ")) {
				res = nextLine.substring(0, nextLine.indexOf(" "));
			}else {
				res = nextLine;
			}
			
		}
		
		return res;
	}
	
	@Override
	protected String extractVehicleModel(String currentLine, String[] lines, int currentIndex) {
		String res = "";		
		if(lines.length > (currentIndex + 1) &&  lines[currentIndex + 1] != null) {
			String nextLine = lines[currentIndex + 1];
			nextLine = nextLine.trim();
			if(nextLine.contains(" ")) {
				res = nextLine.substring(nextLine.indexOf(" "));
			
			}else {
				res = nextLine;
			}
			
		}
		
		return res;
	}
	
	
	@Override
	protected String[] extractPaymentDates(String currentLine, String[] lines, int currentIndex) {		
		String[] paymentDates = new String[3];		
		if(lines.length > (currentIndex + 1) &&  lines[currentIndex + 1] != null) {
			String nextLine = lines[currentIndex + 1].replaceAll("\\s+", " ");
			if(nextLine.contains(" ")) {
				String[] aLine = nextLine.split(" ");
				if(aLine != null && aLine.length > 1) {
					boolean firstPaymentFound= false;
					for(int i = 0; i<aLine.length; i++) {
						String item =aLine[i];
						if(isDate(item)) {
							if(!firstPaymentFound) {
								//we have found the first payment dates so the next dates (if such exist) are the ones we are looking for
								firstPaymentFound = true;
							}
							else {
								if(paymentDates[0] == null) {
									paymentDates[0] = item;
									
								}else if(paymentDates[1] == null) {
									paymentDates[1] = item;
								}else if(paymentDates[2] == null) {
									paymentDates[2] = item;
								}
								else {
									break;
								}
									
							}
						}
					}
				}
			}
		}
		

		return paymentDates;
	}
	
	
	protected List<PaymentRow> extractPaymentRows(String currentLine, String[] lines, int currentIndex){
		List<PaymentRow> paymentRows = new ArrayList<PaymentRow>();
		
		if(lines.length > (currentIndex + 1) &&  lines[currentIndex + 1] != null) {
			String nextLine = lines[currentIndex + 1].replaceAll("\\s+", " ");
			if(nextLine.contains(" ")) {
				String[] aLine = nextLine.split(" ");
				if(aLine != null && aLine.length > 1) {
					boolean firstPaymentFound= false;
					for(int i = 0; i<aLine.length; i++) {
						String item =aLine[i];
						if(isDate(item)) {
							if(!firstPaymentFound) {
								//we have found the first payment dates so the next dates (if such exist) are the ones we are looking for
								firstPaymentFound = true;
							}
							else {
								if(paymentRows.size() == 0) {
									PaymentRow pr = new PaymentRow();
									paymentRows.add(pr);
									
									pr.setPaymentNumber(2);
									pr.setDate(item);
								
									
								}else if(paymentRows.size() == 1) {
									PaymentRow pr = new PaymentRow();
									paymentRows.add(pr);
									
									pr.setPaymentNumber(3);
									pr.setDate(item);
								}else if(paymentRows.size() == 2) {
									PaymentRow pr = new PaymentRow();
									paymentRows.add(pr);
									
									pr.setPaymentNumber(4);
									pr.setDate(item);
								}
								else {
									break;
								}
									
							}
						}
					}
				}
			}
		}


		if (lines.length > currentIndex + 2 && lines[currentIndex + 2] != null) {

			setGoPaymentRowsPaymentValues(paymentRows, lines[currentIndex + 2], "Премия", " ", 1);
		}

		if (lines.length > currentIndex + 4 && lines[currentIndex + 4] != null) {

			setGoPaymentRowsPaymentValueTaxes(paymentRows, lines[currentIndex + 4], "ДЗП", " ", 1);
		}

		// gf and of are paid at initial payment so skip this line and go straight to
		// totals

		if (lines.length > currentIndex + 6 && lines[currentIndex + 6] != null) {

			setGoPaymentRowsPaymentTotalValues(paymentRows, lines[currentIndex + 6], "Обща сума", " ", 1);
		}

		return paymentRows;
	}
	
	
}
