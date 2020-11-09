package com.insurance.pdfToExcel.data;

import java.util.ArrayList;

public class LogData {
	
	private ArrayList<LogRow> logRows;
	
	public LogData() {
		logRows = new ArrayList<LogRow>();
	}

	public ArrayList<LogRow> getLogRows() {
		return logRows;
	}

	public void setLogRows(ArrayList<LogRow> logRows) {
		this.logRows = logRows;
	}
	
	public void addLogRow(LogRow logRow) {
		logRows.add(logRow);
	}
	
	public void addLogMessage(String fileName, String importStatus, String failReason) {
		logRows.add(new LogRow(fileName, importStatus, failReason));
	}

}
