package com.insurance.pdfToExcel.service;

import java.io.File;
import java.util.List;

import com.insurance.pdfToExcel.data.LogData;

public interface PdfToExcelService {
	
	void convertPdfToExcel(List<File> pdfFiles, File smsSystemExcelFile, LogData logData) throws Exception;
	
	void convertPdfToExcel(File pdfFile, File smsSystemExcelFile, LogData logData) throws Exception;

}
