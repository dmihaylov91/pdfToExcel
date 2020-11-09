package com.insurance.pdfToExcel.reader.impl.text;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.pdfbox.pdmodel.PDDocument;

import com.insurance.pdfToExcel.data.PaymentRow;
import com.insurance.pdfToExcel.data.SmsSystemDataRow;
import com.insurance.pdfToExcel.reader.InsurancePdfReader;

public abstract class AbstractPdfTextReader implements InsurancePdfReader {

	protected String POLICY_NUMBER_SEARCH_STRING;
	protected String POLICY_NUMBER_SEARCH_STRING_END;

	protected String POLICY_END_DATE_SEARCH_STRING;
	protected String POLICY_END_DATE_SEARCH_STRING_END;

	protected String PERSONAL_ID_SEARCH_STRING;
	protected String PERSONAL_ID_SEARCH_STRING_END;

	protected String REGISTRATION_NUMBER_SEARCH_STRING;
	protected String REGISTRATION_NUMBER_SEARCH_STRING_END;

	protected String VEHICLE_BRAND_SEARCH_STRING;
	protected String VEHICLE_BRAND_SEARCH_STRING_END;

	protected String VEHICLE_CAPACITY_SEARCH_STRING;
	protected String VEHICLE_CAPACITY_SEARCH_STRING_END;

	protected String VEHICLE_MODEL_SEARCH_STRING;
	protected String VEHICLE_MODEL_SEARCH_STRING_END;
	
	protected String PAYMENT_DATES_SEARCH_STRING;
	protected String PAYMENT_DATES_SEARCH_STRING_END;
	
	protected String GREEN_CARD_NUMBER_SEARCH_STRING;
	protected String GREEN_CARD_NUMBER_SEARCH_STRING_END;
	
	protected String STICKER_NUMBER_SEARCH_STRING;
	protected String STICKER_NUMBER_SEARCH_STRING_END;
	
	
	protected SimpleDateFormat dateFormat;
	protected String dateFormatPattern = "dd.MM.yyyy";
	
	public AbstractPdfTextReader() {
		dateFormat = new SimpleDateFormat(dateFormatPattern);	
	}
	
	public void readPdf(PDDocument pdfDocument, String pdfText, SmsSystemDataRow row) {
		readPdf(pdfText, row);
	}
	
	protected void readPdf(String pdf, SmsSystemDataRow row) {

		String[] lines = pdf.split("\n");
		for (int i = 0; i < lines.length; i++) {
			String line = lines[i].replaceAll("\\s+", "");
			
			if (StringUtils.isBlank(row.getPolicyNumber()) && POLICY_NUMBER_SEARCH_STRING!= null && line.toUpperCase().contains(POLICY_NUMBER_SEARCH_STRING.toUpperCase()) && (POLICY_NUMBER_SEARCH_STRING_END == null || line.toUpperCase().contains(POLICY_NUMBER_SEARCH_STRING_END.toUpperCase())))
			{
				String policyNumber = extractPolicyNumber(line, lines, i);
				row.setPolicyNumber(policyNumber);
			}
			
			if (StringUtils.isBlank(row.getEndDate()) && POLICY_END_DATE_SEARCH_STRING != null && line.toUpperCase().contains(POLICY_END_DATE_SEARCH_STRING.toUpperCase()) && (POLICY_END_DATE_SEARCH_STRING_END == null || line.toUpperCase().contains(POLICY_END_DATE_SEARCH_STRING_END.toUpperCase()))) 
			{								
				String endDate = extractPolicyEndDate(line, lines, i);
				endDate = convertDateFormat(endDate);
				row.setEndDate(endDate);			
			}
			
			if (StringUtils.isBlank(row.getPersonalId())&&  PERSONAL_ID_SEARCH_STRING != null && line.toUpperCase().contains(PERSONAL_ID_SEARCH_STRING.toUpperCase())&& (PERSONAL_ID_SEARCH_STRING_END == null || line.toUpperCase().contains(PERSONAL_ID_SEARCH_STRING_END.toUpperCase()))) 
			{				
				String personalId = extractPersonalId(line, lines, i);				
				row.setPersonalId(personalId);
			}
			
			if (StringUtils.isBlank(row.getVehicleRegistrationNumber()) && REGISTRATION_NUMBER_SEARCH_STRING != null && line.toUpperCase().contains(REGISTRATION_NUMBER_SEARCH_STRING.toUpperCase())&& (REGISTRATION_NUMBER_SEARCH_STRING_END == null || line.toUpperCase().contains(REGISTRATION_NUMBER_SEARCH_STRING_END.toUpperCase()))) 
			{				
				String registrationNumber = extractRegistrationNumber(line, lines, i);	
				row.setVehicleRegistrationNumber(registrationNumber);
			}
		
			if (StringUtils.isBlank(row.getVehicleBrand()) && VEHICLE_BRAND_SEARCH_STRING != null && line.toUpperCase().contains(VEHICLE_BRAND_SEARCH_STRING.toUpperCase())&& (VEHICLE_BRAND_SEARCH_STRING_END == null || line.toUpperCase().contains(VEHICLE_BRAND_SEARCH_STRING_END.toUpperCase()))) 
			{				
				String brand = extractVehicleBrand(line, lines, i);			
				row.setVehicleBrand(brand);
			}
			
			if (StringUtils.isBlank(row.getVehicleEngineCapacity()) && VEHICLE_CAPACITY_SEARCH_STRING != null && line.toUpperCase().contains(VEHICLE_CAPACITY_SEARCH_STRING.toUpperCase())&& (VEHICLE_CAPACITY_SEARCH_STRING_END == null || line.toUpperCase().contains(VEHICLE_CAPACITY_SEARCH_STRING_END.toUpperCase())))
			{				
				String capacity = extractVehicleCapacity(line, lines, i);			
				row.setVehicleEngineCapacity(capacity);								
			}
			
			if (StringUtils.isBlank(row.getVehicleModel()) && VEHICLE_MODEL_SEARCH_STRING != null && line.toUpperCase().contains(VEHICLE_MODEL_SEARCH_STRING.toUpperCase())&& (VEHICLE_MODEL_SEARCH_STRING_END == null || line.toUpperCase().contains(VEHICLE_MODEL_SEARCH_STRING_END.toUpperCase()))) 
			{				
				String model = extractVehicleModel(line, lines, i);
				row.setVehicleModel(model);				
			}
			
			if (ArrayUtils.isEmpty(row.getPaymentDates()) && PAYMENT_DATES_SEARCH_STRING != null &&line.toUpperCase().contains(PAYMENT_DATES_SEARCH_STRING.toUpperCase())&& (PAYMENT_DATES_SEARCH_STRING_END == null || line.toUpperCase().contains(PAYMENT_DATES_SEARCH_STRING_END.toUpperCase())))
			{								
				String[] paymentDates = extractPaymentDates(line, lines, i);
				for(int j=0; j<paymentDates.length; j++) {
					paymentDates[j] = convertDateFormat(paymentDates[j]);
				}
				row.setPaymentDates(paymentDates);
				
				//List<PaymentRow> paymentRows = extractPaymentRows(line, lines, i);
				//row.setPaymentRows(paymentRows);
			}
			
//			if (StringUtils.isBlank(row.getGreenCardNumber()) && GREEN_CARD_NUMBER_SEARCH_STRING != null && line.toUpperCase().contains(GREEN_CARD_NUMBER_SEARCH_STRING.toUpperCase())&& (GREEN_CARD_NUMBER_SEARCH_STRING_END == null || line.toUpperCase().contains(GREEN_CARD_NUMBER_SEARCH_STRING_END.toUpperCase()))) 
//			{				
//				String greenCardNumber = extractGreenCardNumber(line, lines, i);
//				row.setGreenCardNumber(greenCardNumber);				
//			}
//			
//			if (StringUtils.isBlank(row.getStickerNumber()) && STICKER_NUMBER_SEARCH_STRING != null && line.toUpperCase().contains(STICKER_NUMBER_SEARCH_STRING.toUpperCase())&& (STICKER_NUMBER_SEARCH_STRING_END == null || line.toUpperCase().contains(STICKER_NUMBER_SEARCH_STRING_END.toUpperCase()))) 
//			{				
//				String stickerNumber = extractStickerNumber(line, lines, i);
//				row.setStickerNumber(stickerNumber);				
//			}
		}
	}
	

	protected String convertDateFormat(String originalDate) {
		String formattedDate = originalDate;
		
		if(formattedDate != null) {
			//date format is "dd-mm-yyyy" or "dd.mm.yyyy"
			if(formattedDate.length() == 10) {
				formattedDate = formattedDate.replaceAll("-", ".");
			}
			else if(formattedDate.length() == 8) {
				formattedDate = formattedDate.substring(0, 2) + "." + formattedDate.substring(2, 4)  + "." + formattedDate.substring(4, 8);
			}
		}
		else {
			formattedDate = "";
		}
		
		return formattedDate;
	}
	
	
	protected String extractStringBetween(String line, String startString, String endString) {
		String res="";
		
		try 
		{
			int startIndex = line.indexOf(startString) + startString.length();
			if (endString != null) {
				int endIndex = line.lastIndexOf(endString);
				res = line.substring(startIndex, endIndex);
			} else {
				res = line.substring(startIndex);
			}
		} catch (Exception ex) {
			// ;
		}

		return res;
	}
	
	protected boolean isDate(String date) {
		try {
			dateFormat.parse(date);
			//date parsed successfully so set first payment date
			return true;
		}
		catch(Exception e) {
			//;		
		}
		return false;
	}	
	
	protected String extractPolicyNumber(String currentLine, String[] lines, int currentIndex) {
		return extractStringBetween(currentLine, POLICY_NUMBER_SEARCH_STRING, POLICY_NUMBER_SEARCH_STRING_END);
	}
	
	protected String extractPolicyEndDate(String currentLine, String[] lines, int currentIndex) {
		return extractStringBetween(currentLine, POLICY_END_DATE_SEARCH_STRING, POLICY_END_DATE_SEARCH_STRING_END);
	}
	
	protected String extractPersonalId(String currentLine, String[] lines, int currentIndex) 
	{
		return extractStringBetween(currentLine, PERSONAL_ID_SEARCH_STRING, PERSONAL_ID_SEARCH_STRING_END);
	}
	
	protected String extractRegistrationNumber(String currentLine, String[] lines, int currentIndex) 
	{
		return extractStringBetween(currentLine, REGISTRATION_NUMBER_SEARCH_STRING, REGISTRATION_NUMBER_SEARCH_STRING_END);
	}
		
	protected String extractVehicleBrand(String currentLine, String[] lines, int currentIndex) {
		return extractStringBetween(currentLine, VEHICLE_BRAND_SEARCH_STRING, VEHICLE_BRAND_SEARCH_STRING_END);
	}
	
	protected String extractVehicleModel(String currentLine, String[] lines, int currentIndex) {
		 return extractStringBetween(currentLine, VEHICLE_MODEL_SEARCH_STRING, VEHICLE_MODEL_SEARCH_STRING_END);	
	}
	
	protected String extractVehicleCapacity(String currentLine, String[] lines, int currentIndex) {
		 return extractStringBetween(currentLine, VEHICLE_CAPACITY_SEARCH_STRING, VEHICLE_CAPACITY_SEARCH_STRING_END);	
	}
	
	protected String extractGreenCardNumber(String currentLine, String[] lines, int currentIndex) {
		 return extractStringBetween(currentLine, GREEN_CARD_NUMBER_SEARCH_STRING, GREEN_CARD_NUMBER_SEARCH_STRING_END);	
	}
	
	protected String extractStickerNumber(String currentLine, String[] lines, int currentIndex) {
		 return extractStringBetween(currentLine, STICKER_NUMBER_SEARCH_STRING, STICKER_NUMBER_SEARCH_STRING_END);	
	}
	
	abstract String[] extractPaymentDates(String currentLine, String[] lines, int currentIndex);
	
	
	protected List<PaymentRow> extractPaymentRows(String currentLine, String[] lines, int currentIndex){
		List<PaymentRow> paymentRows= new ArrayList<PaymentRow>();
		
		return paymentRows;
	}
	
	
	//Дата на падеж Вноска Данък 2% върху вноска Вноска за ГФ и Такса стикер Общо дължима сума
	//23.08.2018 131.28 BGN 2.63 BGN 12.90 BGN 146.81 BGN
	//23.02.2019 131.00 BGN 2.62 BGN 1.40 BGN 135.02 BGN
	
	//used by: ArmeecGo, ArmeecKasko, EuroinsKasko, OzkGo
	protected void setGoPaymenRowValues(PaymentRow row, String line, String splitString, int paymentValueStartIndex) {
		String[] aLine = line.split(splitString);
		if(aLine != null && aLine.length > 0) {
			//first 10 digits are for the payment date
			String paymentValue = aLine[0].substring(paymentValueStartIndex);
			row.setValue(paymentValue);
			
			if(aLine.length > 1) {
				String paymentValueTax = aLine[1];
				row.setValueTax(paymentValueTax);
			}
			if(aLine.length > 2) {
				String gf = aLine[2];
				row.setGfAndStickerValue(gf);
			}
			if(aLine.length > 3) {
				String total = aLine[3];
				row.setTotalValue(total);
			}
		}
		
	}
	
	//used by: LevinsGo
	protected void setGoPaymenRowValuesWithSpaces(PaymentRow row, String line, String splitString, int startIndex) {
		line = line.replaceAll("\\s+", " ");	
		String[] aLine = line.split(splitString);
		if(aLine != null && aLine.length > startIndex) {
			//first 10 digits are for the payment date
			String paymentValue = aLine[startIndex];
			row.setValue(paymentValue);
			
			if(aLine.length > startIndex+1) {
				String paymentValueTax = aLine[startIndex+1];
				row.setValueTax(paymentValueTax);
			}
			
			//no gf
			if(aLine.length > startIndex+2) {
				String total = aLine[startIndex+2];
				row.setTotalValue(total);
			}
		}
		
	}	
	
	//ВНОСКА ДАТА НА ПАДЕЖ ПРЕМИЯ ДАНЪК 2.00% ДЪЛЖИМА СУМА
	//1 23.10.2017 г. 484.50 BGN 9.69 BGN 494.19 BGN
	//2 23.04.2018 г. 484.50 BGN 9.69 BGN 494.19 BGN
	
	//used by: ArmeecGo, ArmeecKasko, EuroinsKasko, OzkGo
	protected void setKaskoPaymenRowValues(PaymentRow row, String line, String splitString, int paymentValueStartIndex) {
		String[] aLine = line.split(splitString);
		if(aLine != null && aLine.length > 0) {
			//first 10 digits are for the payment date
			String paymentValue = aLine[0].substring(paymentValueStartIndex);
			row.setValue(paymentValue);
			
			if(aLine.length > 1) {
				String paymentValueTax = aLine[1];
				row.setValueTax(paymentValueTax);
			}
			if(aLine.length > 2) {
				String total = aLine[2];
				row.setTotalValue(total);
			}
		}
		
	}
	
	
	//				Вноска 1 	Вноска 2 	Вноска 3 	Вноска 4
	//Дата падеж	17.08.2018 18.11.2018 	18.02.2019 	18.05.2019
	//Премия		178,00 	 	178,00 		178,00 		178,00
	//2% ДЗП		3,56 		3,56 		3,56 		3,56
	//Обща сума		181,56 		181,56 		181,56 		181,56
	
	//used by: BulinsGo, BulstradGo, DalBogKasko, LevinsKasko
	protected void setGoPaymentRowsPaymentDates(List<PaymentRow> paymentRows, String line, String searchString, String splitString, int firstValueIndex) {
		line = line.replaceAll("\\s+", " ");
		String aLine[] = line.split(searchString);	
		if(aLine != null && aLine.length > 1) {	
			String[] aPaymentDays = aLine[1].trim().split(splitString);
			if(aPaymentDays != null && aPaymentDays.length > firstValueIndex) {
				for(int i = firstValueIndex; (i<aPaymentDays.length && i<3+firstValueIndex); i++) {
					PaymentRow pr = new PaymentRow();
					paymentRows.add(pr);
					
					pr.setPaymentNumber(paymentRows.size());
					pr.setDate(aPaymentDays[i].trim());
				}
			}	
		}
	}
	
	
	//				Вноска 1 	Вноска 2 	Вноска 3 	Вноска 4
	//Дата падеж	17.08.2018 18.11.2018 	18.02.2019 	18.05.2019
	//Премия		178,00 	 	178,00 		178,00 		178,00
	//2% ДЗП		3,56 		3,56 		3,56 		3,56
	//Обща сума		181,56 		181,56 		181,56 		181,56
	protected void setGoPaymentRowsPaymentValues(List<PaymentRow> paymentRows, String line, String searchString, String splitString, int firstValueIndex) {
	    line = line.replaceAll("\\s+", " ");
		String aLine[] = line.split(searchString);
		if(aLine != null && aLine.length > 1) {
			String[] values = aLine[1].trim().split(splitString);
			if(values != null && values.length > firstValueIndex) {
				//index 0 is usually for the initial payment
				for(int i = firstValueIndex; (i<values.length && i<paymentRows.size()+firstValueIndex); i++) {
					if(paymentRows.size() > (i-firstValueIndex)){
						paymentRows.get(i-firstValueIndex).setValue(values[i].trim());
					}
				}
			}
		}	
	}
	
	
	//				Вноска 1 	Вноска 2 	Вноска 3 	Вноска 4
	//Дата падеж	17.08.2018 18.11.2018 	18.02.2019 	18.05.2019
	//Премия		178,00 	 	178,00 		178,00 		178,00
	//2% ДЗП		3,56 		3,56 		3,56 		3,56
	//Обща сума		181,56 		181,56 		181,56 		181,56	
	protected void setGoPaymentRowsPaymentValueTaxes(List<PaymentRow> paymentRows, String line, String searchString, String splitString, int firstValueIndex) {
		line = line.replaceAll("\\s+", " ");
		String aLine[] = line.split(searchString);
		if(aLine != null && aLine.length > 1) {
			String[] values = aLine[1].trim().split(splitString);
			if(values != null && values.length > firstValueIndex) {
				//index 0 is usually for the initial payment
				for(int i = firstValueIndex; (i<values.length && i<paymentRows.size()+firstValueIndex); i++) {
					if(paymentRows.size() > (i-firstValueIndex)){
						paymentRows.get(i-firstValueIndex).setValueTax(values[i].trim());
					}
				}
			}
		}	
	}
	
	
	//				Вноска 1 	Вноска 2 	Вноска 3 	Вноска 4
	//Дата падеж	17.08.2018 18.11.2018 	18.02.2019 	18.05.2019
	//Премия		178,00 	 	178,00 		178,00 		178,00
	//2% ДЗП		3,56 		3,56 		3,56 		3,56
	//Обща сума		181,56 		181,56 		181,56 		181,56	
	protected void setGoPaymentRowsPaymentTotalValues(List<PaymentRow> paymentRows, String line, String searchString, String splitString, int firstValueIndex) {
		line = line.replaceAll("\\s+", " ");
		String aLine[] = line.split(searchString);
		if(aLine != null && aLine.length > 1) {
			String[] values = aLine[1].trim().split(splitString);
			if(values != null && values.length > firstValueIndex) {
				//index 0 is usually for the initial payment
				for(int i = firstValueIndex; (i<values.length && i<paymentRows.size()+firstValueIndex); i++) {
					if(paymentRows.size() > (i-firstValueIndex)){
						paymentRows.get(i-firstValueIndex).setTotalValue(values[i].trim());
					}
				}
			}
		}	
	}
	
	
	//				Вноска 1 	Вноска 2 	Вноска 3 	Вноска 4
	//Дата падеж	17.08.2018 18.11.2018 	18.02.2019 	18.05.2019
	//Премия		178,00 	 	178,00 		178,00 		178,00
	//2% ДЗП		3,56 		3,56 		3,56 		3,56
	//Обща сума		181,56 		181,56 		181,56 		181,56	
	protected void setGoPaymentRowsPaymentGf(List<PaymentRow> paymentRows, String line, String searchString, String splitString, int firstValueIndex) {
		line = line.replaceAll("\\s+", " ");
		String aLine[] = line.split(searchString);
		if(aLine != null && aLine.length > 1) {
			String[] values = aLine[1].trim().split(splitString);
			if(values != null && values.length > firstValueIndex) {
				//index 0 is usually for the initial payment
				for(int i = firstValueIndex; (i<values.length && i<paymentRows.size()+firstValueIndex); i++) {
					if(paymentRows.size() > (i-firstValueIndex)){
						paymentRows.get(i-firstValueIndex).setGfAndStickerValue(values[i].trim());
					}
				}
			}
		}	
	}
		
}
