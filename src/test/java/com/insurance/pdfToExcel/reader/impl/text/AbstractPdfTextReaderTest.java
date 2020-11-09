package com.insurance.pdfToExcel.reader.impl.text;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.encryption.InvalidPasswordException;
import org.apache.pdfbox.text.PDFTextStripper;
import org.junit.Test;

import com.insurance.pdfToExcel.data.SmsSystemDataRow;
import com.insurance.pdfToExcel.reader.InsurancePdfReader;
import com.insurance.pdfToExcel.reader.InsurancePdfReaderFactory;

public class AbstractPdfTextReaderTest {

	@Test
	public void testReadPdfPDDocumentStringSmsSystemDataRow() {
		//fail("Not yet implemented");

		File pdf = new File("C:\\Users\\DimitarMihaylov\\Desktop\\Insurance\\Nov format\\generali GO.pdf");
		// Loading an existing document
		PDDocument pdfDocument = null;
		try {
			pdfDocument = PDDocument.load(pdf);
		} catch (InvalidPasswordException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		;

		// Instantiate PDFTextStripper class
		PDFTextStripper pdfStripper = null;
		try {
			pdfStripper = new PDFTextStripper();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		pdfStripper.setSortByPosition(true);

		// Retrieving text from PDF document
		String text = null;
		try {
			text = pdfStripper.getText(pdfDocument);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println(text);

		SmsSystemDataRow dataRow = new SmsSystemDataRow();
		InsurancePdfReader pdfReader = InsurancePdfReaderFactory.getPdfReader(text);
		if (pdfReader != null) {
			System.out.println(pdfReader.getClass().getName());
			pdfReader.readPdf(pdfDocument, text, dataRow);
		}
		System.out.println(dataRow);
		
		 assertTrue( true );
	}

}
