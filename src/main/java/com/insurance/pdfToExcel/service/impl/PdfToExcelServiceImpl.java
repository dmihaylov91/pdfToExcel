package com.insurance.pdfToExcel.service.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.insurance.pdfToExcel.data.LogData;
import com.insurance.pdfToExcel.data.SmsSystemDataRow;
import com.insurance.pdfToExcel.reader.InsurancePdfReader;
import com.insurance.pdfToExcel.reader.InsurancePdfReaderFactory;
import com.insurance.pdfToExcel.service.PdfToExcelService;
import com.insurance.pdfToExcel.writer.SmsSystemExcelWriter;


public class PdfToExcelServiceImpl implements PdfToExcelService
{

    
	public void convertPdfToExcel(List<File> pdfFiles, File smsSystemExcelFile, LogData logData) throws Exception
    {
  
        for(int i=0; i<pdfFiles.size(); i++) {              
        	File file = pdfFiles.get(i);
        	if(file != null) {
        		convertPdfToExcel(file, smsSystemExcelFile, logData);
        	}
        }
    }
	
	public void convertPdfToExcel(File pdfFile, File smsSystemExcelFile, LogData logData) throws Exception
	{
		try {

			// Loading an existing document
			PDDocument pdfDocument = PDDocument.load(pdfFile);;
	
		
			// Instantiate PDFTextStripper class
			PDFTextStripper pdfStripper = new PDFTextStripper();
	
			pdfStripper.setSortByPosition(true);
	
			// Retrieving text from PDF document
			String text = null;
			text = pdfStripper.getText(pdfDocument);
	
			SmsSystemDataRow dataRow = new SmsSystemDataRow();
			dataRow.setFileName(pdfFile.getAbsolutePath());
			InsurancePdfReader pdfReader = InsurancePdfReaderFactory.getPdfReader(text);
			if (pdfReader != null) {
				pdfReader.readPdf(pdfDocument, text, dataRow);
				
				// Closing the pdf document
				pdfDocument.close();
						
				System.out.println(dataRow);
				
			
				if(!StringUtils.isBlank(dataRow.getPolicyNumber())) {
					// Write data to excel
					InputStream inp = new FileInputStream(smsSystemExcelFile);
					XSSFWorkbook wb = new XSSFWorkbook(inp);
		
				
					SmsSystemExcelWriter smsSystemWriter = new SmsSystemExcelWriter();
					boolean success = smsSystemWriter.writeDataRowtoSmsSystemWorkbook(dataRow, wb, logData);
		
					// Write the output to a file
					OutputStream fileOut = new FileOutputStream(smsSystemExcelFile);
					wb.write(fileOut);	
					
					
					if(success) {
		
						logData.addLogMessage(dataRow.getFileName(), "УСПЕХ", "Нова полица / регистрационен номер: " + StringUtils.defaultString(dataRow.getPolicyNumber()) + " / " + StringUtils.defaultString(dataRow.getVehicleRegistrationNumber()));	
					}
				}
				else {
					logData.addLogMessage(dataRow.getFileName(), "ГРЕШКА", "Не е намерена полица" );
				}
			}
			else {	
				logData.addLogMessage(dataRow.getFileName(), "ГРЕШКА", "Файлът не е разпознат" );		
			}
		}
		catch(Exception e) {
			logData.addLogMessage(pdfFile.getAbsolutePath(), "ГРЕШКА", e.getMessage() );	
		}
	}
    
    
}
