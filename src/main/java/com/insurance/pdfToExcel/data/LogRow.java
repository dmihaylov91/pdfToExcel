package com.insurance.pdfToExcel.data;

public class LogRow {
	
	public LogRow(String fileName, String importStatus, String failReason) {
		super();
		this.fileName = fileName;
		this.importStatus = importStatus;
		this.failReason = failReason;
	}
	private String fileName;
	private String importStatus;
	private String failReason;
	
	
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	public String getImportStatus() {
		return importStatus;
	}
	public void setImportStatus(String importStatus) {
		this.importStatus = importStatus;
	}
	public String getFailReason() {
		return failReason;
	}
	public void setFailReason(String failReason) {
		this.failReason = failReason;
	}

}
