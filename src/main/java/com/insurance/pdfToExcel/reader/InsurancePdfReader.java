package com.insurance.pdfToExcel.reader;

import org.apache.pdfbox.pdmodel.PDDocument;

import com.insurance.pdfToExcel.data.SmsSystemDataRow;

public interface InsurancePdfReader {
	
	void readPdf(PDDocument pdfDocument, String pdfText, SmsSystemDataRow row);

}
