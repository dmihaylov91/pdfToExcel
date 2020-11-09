package com.insurance.pdfToExcel.writer;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.insurance.pdfToExcel.data.LogData;
import com.insurance.pdfToExcel.data.SmsSystemDataRow;

public class SmsSystemExcelWriter {
	
	//private static final String DATA_TABLE_NAME = "Table1";
	
	private static final int VEHICLE_REGISTRATION_NUMBER_COLUMN = 0;
	private static final int VEHICLE_BRAND_MODEL_COLUMN = 1;
	private static final int VEHICLE_ENGINE_CAPACITY_COLUMN = 3;
	private static final int VEHICLE_PERSONAL_ID_COLUMN = 4;
	private static final int PAYMENT_DATE_2_COLUMN = 5;
	private static final int PAYMENT_DATE_3_COLUMN = 6;
	private static final int PAYMENT_DATE_4_COLUMN = 7;	
	private static final int POLICY_END_DATE_COLUMN = 8;
	private static final int INSURANCE_COMPANY_COLUMN = 9;
	private static final int POLICY_NUMBER_COLUMN = 10;
	
	private static final int GSM_NUMBER_COLUMN = 11;
	//private static final int STICKER_NUMBER = 12;
	
	private static final int LAST_COLUMN_INDEX = 12;

	public void writeDataToSmsSystemWorkbook(List<SmsSystemDataRow> dataRows, XSSFWorkbook workbook, LogData logData) {
		for(SmsSystemDataRow dataRow: dataRows) {
			writeDataRowtoSmsSystemWorkbook(dataRow, workbook, logData);
		}
	}
	
	public boolean writeDataRowtoSmsSystemWorkbook(SmsSystemDataRow dataRow, XSSFWorkbook workbook, LogData logData) {

		XSSFSheet sheet = workbook.getSheetAt(0);
		// populate data row
		if (validateRow(sheet, dataRow)) {

//		// retrieve the data table containing all customer/vehicle info
//		XSSFTable dataTable = workbook.getTable(DATA_TABLE_NAME);
//		dataTable.getCTTable().setInsertRow(true);
//		dataTable.getCTTable().setInsertRowShift(true);

			// create a new row at the end of the sheet
			int lastRow = getLastRowNumber(sheet);
			XSSFRow prevRow = sheet.getRow(lastRow);
			
			int newRowIndex = lastRow + 1;
			XSSFRow row = sheet.createRow(newRowIndex);

			// create individual cells for the new row
			for (int i = 0; i <= LAST_COLUMN_INDEX; i++) {
				XSSFCell cell = row.createCell(i);
				
				//set cell style
				if(prevRow != null) {
					XSSFCell prevCell = prevRow.getCell(i);
					if(prevCell != null) {
						cell.setCellStyle(prevCell.getCellStyle());
					}
				}
				
			}
			
			replaceOldRow(sheet, dataRow, logData);

			// populate data row
			populateRow(row, dataRow);
			
			return true;

//		//expand table area to cover the new row
//		AreaReference areaRef = workbook.getCreationHelper().createAreaReference(dataTable.getStartCellReference(), 
//				new CellReference(newRowIndex, dataTable.getEndColIndex()));
//
//		dataTable.setCellReferences(areaRef);

		}
		else {			
			logData.addLogMessage(dataRow.getFileName(), "ГРЕШКА", "Дублирана полица: " + dataRow.getPolicyNumber() );		
			return false;
		}
	}
	
	private void replaceOldRow(XSSFSheet sheet , SmsSystemDataRow dataRow,  LogData logData) {
		if(!StringUtils.isBlank(dataRow.getVehicleRegistrationNumber()) && !StringUtils.isBlank(dataRow.getPolicyNumber())) {
			int lastRow = sheet.getLastRowNum();
			for(int i = lastRow; i>=0; i--) {
				XSSFRow currentRow = sheet.getRow(i);
				if(currentRow != null && currentRow.getCell(POLICY_NUMBER_COLUMN) != null && currentRow.getCell(VEHICLE_REGISTRATION_NUMBER_COLUMN) != null) {
					String policyNumber = this.getCellValueAsString(currentRow.getCell(POLICY_NUMBER_COLUMN));
					String regNumber = this.getCellValueAsString(currentRow.getCell(VEHICLE_REGISTRATION_NUMBER_COLUMN));
					if(!StringUtils.isBlank(policyNumber) && !StringUtils.isBlank(regNumber)) {
						regNumber = regNumber.replaceAll("\\s+", " ").trim();
						if(regNumber.equals(dataRow.getVehicleRegistrationNumber())){
							if((policyNumber.startsWith("BG/") && dataRow.getPolicyNumber().startsWith("BG/")) || (!policyNumber.startsWith("BG/") && !dataRow.getPolicyNumber().startsWith("BG/")) ) {
								//we have found the correct row to delete 
								//copy GSM and Egn No to new row and delete row
								
								XSSFCell egnCell = currentRow.getCell(VEHICLE_PERSONAL_ID_COLUMN);
								XSSFCell gsmCell =currentRow.getCell(GSM_NUMBER_COLUMN);
								
								if(StringUtils.isBlank(dataRow.getPersonalId()) && egnCell != null) {
									dataRow.setPersonalId(getCellValueAsString(egnCell));
								}
								if(gsmCell != null) {
									dataRow.setGsmNumber(getCellValueAsString(gsmCell));
								}
								
								
								//delete row
								sheet.removeRow(currentRow);
								
								if ( i < lastRow) {
									sheet.shiftRows(i+1, lastRow, -1);
								}
								
								logData.addLogMessage(dataRow.getFileName(), "TРИЕНЕ НА СТАР РЕД", "Стара полица / стар регистрационен номер: " + policyNumber + " / " + regNumber);
								
							}
							
						}
					}
				}
			}
		}
	}
	
	private void populateRow(XSSFRow row, SmsSystemDataRow data) {
		row.getCell(VEHICLE_REGISTRATION_NUMBER_COLUMN).setCellValue(data.getVehicleRegistrationNumber());
		row.getCell(VEHICLE_BRAND_MODEL_COLUMN).setCellValue(StringUtils.defaultString(data.getVehicleBrand()) + " "+ StringUtils.defaultString(data.getVehicleModel()));
		row.getCell(VEHICLE_ENGINE_CAPACITY_COLUMN).setCellValue(data.getVehicleEngineCapacity());
		row.getCell(VEHICLE_PERSONAL_ID_COLUMN).setCellValue(data.getPersonalId());
		row.getCell(POLICY_END_DATE_COLUMN).setCellValue(data.getEndDate());
		row.getCell(INSURANCE_COMPANY_COLUMN).setCellValue(data.getInsuranceCompany());
		row.getCell(POLICY_NUMBER_COLUMN).setCellValue(data.getPolicyNumber());
		row.getCell(GSM_NUMBER_COLUMN).setCellValue(data.getGsmNumber());

		
		if(data.getPaymentDates() != null)
		{	
			String[] paymentDates = data.getPaymentDates();
			row.getCell(PAYMENT_DATE_2_COLUMN).setCellValue(paymentDates.length > 0 ? paymentDates[0] : "");
			row.getCell(PAYMENT_DATE_3_COLUMN).setCellValue(paymentDates.length > 1 ? paymentDates[1] : "");
			row.getCell(PAYMENT_DATE_4_COLUMN).setCellValue(paymentDates.length > 2 ? paymentDates[2] : "");
		}
	}
	
	private boolean validateRow(XSSFSheet sheet, SmsSystemDataRow dataRow) {
		int lastRow = sheet.getLastRowNum();
		for(int i = lastRow; i>=0; i--) {
			XSSFRow currentRow = sheet.getRow(i);
			if(currentRow != null && currentRow.getCell(POLICY_NUMBER_COLUMN) != null) {
				String policyNumber = getCellValueAsString(currentRow.getCell(POLICY_NUMBER_COLUMN));
				if(policyNumber != null && policyNumber.equals(dataRow.getPolicyNumber())) {
					return false;
				}
			}
		}
		return true;
	}
	
	
	private int getLastRowNumber(XSSFSheet sheet) {
		int lastRow = sheet.getLastRowNum();
		for(int i = lastRow; i>=0; i--) {
			XSSFRow currentRow = sheet.getRow(i);
			if(currentRow != null) {
				for(int j = 0; j<= LAST_COLUMN_INDEX; j++) {
					XSSFCell cell = currentRow.getCell(j);
					if(cell != null && !StringUtils.isBlank(getCellValueAsString(cell))) {
						return i;
					}
				}
			}
			
		}
		return 0;
	}
	
	private String getCellValueAsString(XSSFCell cell) {
		String res = "";
		if(cell != null) {
			 if (cell.getCellTypeEnum() == CellType.STRING)
	         {
				 res += cell.getStringCellValue();
	         }
	         else if(cell.getCellTypeEnum() == CellType.NUMERIC)
	         {
	        	 res += ((int)cell.getNumericCellValue());
	         }
	         else if(cell.getCellTypeEnum() == CellType.BOOLEAN)	        	 
	         {
	        	 res += cell.getBooleanCellValue();
	         }
		}
		
		return res;
     }
	
	
}
