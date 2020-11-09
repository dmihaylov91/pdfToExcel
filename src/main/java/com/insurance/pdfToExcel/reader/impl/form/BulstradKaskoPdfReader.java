package com.insurance.pdfToExcel.reader.impl.form;

import org.apache.pdfbox.pdmodel.PDDocument;

import com.insurance.pdfToExcel.data.SmsSystemDataRow;
import com.insurance.pdfToExcel.util.InsuranceConstants;

public class BulstradKaskoPdfReader extends AbstractPdfFormReader {

	public BulstradKaskoPdfReader(){
		super();

		POLICY_NUMBER_FIELD_ID = "BLSTRKSPOL_001_NUM";
		
		END_DATE_DAY_FIELD_ID = "BLSTRKSPOL_010_TILLDAY";
		END_DATE_MONTH_FIELD_ID = "BLSTRKSPOL_011_TILLMONTH";
		END_DATE_YEAR_FIELD_ID = "BLSTRKSPOL_012_TILLYEAR";
		
		PERSONAL_ID_FIELD_ID = "BLSTRKSPOL_103_CID";
		VEHICLE_REGISTRATION_NUMBER_FIELD_ID = "BLSTRKSPOL_201_LP";
		VEHICLE_BRAND_FIELD_ID = "BLSTRKSPOL_205_MARKA";
		VEHICLE_MODEL_FIELD_ID = "BLSTRKSPOL_206_MODEL";
		VEHICLE_ENGINE_CAPACITY_FIELD_ID = "BLSTRKSPOL_212_VOL";
		
		PAYMENT_DATE2_FIELD_ID = "BLSTRKSPOL_251_PAY1";
		PAYMENT_DATE3_FIELD_ID = "BLSTRKSPOL_252_PAY1";
		PAYMENT_DATE4_FIELD_ID = "BLSTRKSPOL_253_PAY1";
	}
	
	@Override
	protected void readPdf(PDDocument pdfDocument, SmsSystemDataRow row){
		super.readPdf(pdfDocument, row);
		row.setInsuranceCompany("Bulstrad-kasko");
		row.setInsuranceType(InsuranceConstants.INSURANCE_TYPE_KASKO);
	}
}
