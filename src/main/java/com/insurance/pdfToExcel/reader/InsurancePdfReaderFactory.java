package com.insurance.pdfToExcel.reader;

import com.insurance.pdfToExcel.reader.impl.form.BulstradKaskoPdfReader;
import com.insurance.pdfToExcel.reader.impl.text.ArmeecGoPdfReader;
import com.insurance.pdfToExcel.reader.impl.text.ArmeecKaskoPdfReader;
import com.insurance.pdfToExcel.reader.impl.text.BulinsGoPdfReader;
import com.insurance.pdfToExcel.reader.impl.text.BulinsKaskoPdfReader;
import com.insurance.pdfToExcel.reader.impl.text.BulstradGoPdfReader;
import com.insurance.pdfToExcel.reader.impl.text.DalbogGoPdfReader;
import com.insurance.pdfToExcel.reader.impl.text.DalbogKaskoPdfReader;
import com.insurance.pdfToExcel.reader.impl.text.EuroinsGoPdfReader;
import com.insurance.pdfToExcel.reader.impl.text.EuroinsKaskoPdfReader;
import com.insurance.pdfToExcel.reader.impl.text.GeneraliGoPdfReader;
import com.insurance.pdfToExcel.reader.impl.text.GeneraliZlopolukaPdfReader;
import com.insurance.pdfToExcel.reader.impl.text.LevinsGoPdfReader;
import com.insurance.pdfToExcel.reader.impl.text.LevinsKaskoPdfReader;
import com.insurance.pdfToExcel.reader.impl.text.OzkGoPdfReader;
import com.insurance.pdfToExcel.reader.impl.text.OzkKaskoPdfReader;

public class InsurancePdfReaderFactory {
	
	private static final String OZK_SEARCH_STRING = "ОЗК".replaceAll("\\s+", "");
	private static final String OZK_GO_SEARCH_STRING = "Зелена Карта".replaceAll("\\s+", "");
	
	private static final String ARMEEC_SEARCH_STRING = "Армеец".replaceAll("\\s+", "");;
	//private String armeecGoSearchString = "".replaceAll("\\s+", "");
	private static final String ARMEEC_KASKO_SEARCH_STRING = "КОМБИНИРАНА ЗАСТРАХОВАТЕЛНА ПОЛИЦА".replaceAll("\\s+", "");
	
	private static final String BULINS_GO_SEARCH_STRING = "сертификат ЗК".replaceAll("\\s+", "");
	private static final String BULINS_SEARCH_STRING = "www.bulins.com".replaceAll("\\s+", "");
	
	private static final String LEVINS_SEARCH_STRING = "www.lev-ins.com".replaceAll("\\s+", "");
	private static final String LEVINS_GO_SEARCH_STRING = "ГРАЖДАНСКА ОТГОВОРНОСТ".replaceAll("\\s+", "");
	//private static final String levinsKaskoSearchString = "".replaceAll("\\s+", "");
	
	private static final String BULSTRAD_SEARCH_STRING="WWW.BULSTRAD.BG".replaceAll("\\s+", "");
	private static final String BULSTRAD_GO_SEARCH_STRING = "ГРАЖДАНСКА ОТГОВОРНОСТ".replaceAll("\\s+", "");
	//private static final String bulstradKaskoSearchString = "".replaceAll("\\s+", "");
	
	private static final String DALBOG_GO_SEARCH_STRING = "Застраховащ/Insurer  (подпис/signature) За застраховател/Insurer  (подпис/signature)".replaceAll("\\s+", "");
	private static final String DALBOG_KASKO_SEARCH_STRING = "Застрахован (подпис) Застраховател (подпис)".replaceAll("\\s+", "");
	
	private static final String EUROINS_SEARCH_STRING = "www.euroins.bg".replaceAll("\\s+", "");
	private static final String EUROINS_GO_SEARCH_STRING = "ЗЕЛЕНА КАРТА".replaceAll("\\s+", "");
	
	private static final String GENERALI_SEARCH_STRING = "www.generali.bg".replaceAll("\\s+", "");
	private static final String GENERALI_GO_SEARCH_STRING = "Зелена карта".replaceAll("\\s+", "");
	
	public static InsurancePdfReader getPdfReader(String pdf) {
		
		pdf = pdf.replaceAll("\\s+", "").toUpperCase();
		
		if(pdf.contains(OZK_SEARCH_STRING.toUpperCase()))
		{
			if(pdf.contains(OZK_GO_SEARCH_STRING.toUpperCase()))
				return new OzkGoPdfReader();
			else
				return new OzkKaskoPdfReader();
		}
		else if(pdf.contains(ARMEEC_SEARCH_STRING.toUpperCase()))
		{
			if(pdf.contains(ARMEEC_KASKO_SEARCH_STRING.toUpperCase())) 
			{
				return new ArmeecKaskoPdfReader();
			}
			else
			{
				return new ArmeecGoPdfReader();
			}
		
		}
		else if(pdf.contains(BULINS_SEARCH_STRING.toUpperCase()))
		{
			if(pdf.contains(BULINS_GO_SEARCH_STRING.toUpperCase())) {
				return new BulinsGoPdfReader();
			}
			else {
				return new BulinsKaskoPdfReader();
			}
		}
		
		else if(pdf.contains(LEVINS_SEARCH_STRING.toUpperCase()))
		{
			if(pdf.contains(LEVINS_GO_SEARCH_STRING.toUpperCase())) 
			{
				return new LevinsGoPdfReader();
			}
			else
			{
				return new LevinsKaskoPdfReader();
			}
		}
		else if(pdf.contains(BULSTRAD_SEARCH_STRING.toUpperCase()))
		{
			if(pdf.contains(BULSTRAD_GO_SEARCH_STRING.toUpperCase())) 
			{
				return new BulstradGoPdfReader();
			}
			else 
			{
				return new BulstradKaskoPdfReader();
			}	
		}
		else if(pdf.contains(EUROINS_SEARCH_STRING.toUpperCase()))
		{
			if(pdf.contains(EUROINS_GO_SEARCH_STRING.toUpperCase())) 
				return new EuroinsGoPdfReader();
			else
				return new EuroinsKaskoPdfReader();
		}
		else if(pdf.contains(GENERALI_SEARCH_STRING.toUpperCase()))
		{
			if(pdf.contains(GENERALI_GO_SEARCH_STRING.toUpperCase())) 
				return new GeneraliGoPdfReader();
			else
				return new GeneraliZlopolukaPdfReader();
		}
				
		else if(pdf.endsWith(DALBOG_GO_SEARCH_STRING.toUpperCase()))
		{
			return new DalbogGoPdfReader();
		}
		else if(pdf.endsWith(DALBOG_KASKO_SEARCH_STRING.toUpperCase()))
		{
			return new DalbogKaskoPdfReader();
		}
				
		return null;
	}
	

}
