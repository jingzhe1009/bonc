package com.ljz.model;

import java.io.Serializable;

public class DataRvsdRecordTmp implements Serializable {
	
	private static final long serialVersionUID = 1L;

	private String importType;

	private String batchNo;

	private String needVrsnNbr;

    private String dataDrcAbbr;
    
    private String chgPsn;

	private String exctPsn;

	private String corrIntfStdVrsn;

	private String intfDscr;

	private String sDate;

	private String eDate;

	public String getImportType() {
		return importType;
	}

	public void setImportType(String importType) {
		this.importType = importType;
	}

	public String getBatchNo() {
		return batchNo;
	}

	public void setBatchNo(String batchNo) {
		this.batchNo = batchNo;
	}

	public String getNeedVrsnNbr() {
		return needVrsnNbr;
	}

	public void setNeedVrsnNbr(String needVrsnNbr) {
		this.needVrsnNbr = needVrsnNbr;
	}

	public String getDataDrcAbbr() {
		return dataDrcAbbr;
	}

	public void setDataDrcAbbr(String dataDrcAbbr) {
		this.dataDrcAbbr = dataDrcAbbr;
	}

	public String getChgPsn() {
		return chgPsn;
	}

	public void setChgPsn(String chgPsn) {
		this.chgPsn = chgPsn;
	}

	public String getExctPsn() {
		return exctPsn;
	}

	public void setExctPsn(String exctPsn) {
		this.exctPsn = exctPsn;
	}

	public String getCorrIntfStdVrsn() {
		return corrIntfStdVrsn;
	}

	public void setCorrIntfStdVrsn(String corrIntfStdVrsn) {
		this.corrIntfStdVrsn = corrIntfStdVrsn;
	}

	public String getIntfDscr() {
		return intfDscr;
	}

	public void setIntfDscr(String intfDscr) {
		this.intfDscr = intfDscr;
	}

	public String getsDate() {
		return sDate;
	}

	public void setsDate(String sDate) {
		this.sDate = sDate;
	}

	public String geteDate() {
		return eDate;
	}

	public void seteDate(String eDate) {
		this.eDate = eDate;
	}

	@Override
	public String toString() {
		return "DataRvsdRecordTmp{" +
				"importType='" + importType + '\'' +
				", batchNo='" + batchNo + '\'' +
				", needVrsnNbr='" + needVrsnNbr + '\'' +
				", dataDrcAbbr='" + dataDrcAbbr + '\'' +
				", chgPsn='" + chgPsn + '\'' +
				", exctPsn='" + exctPsn + '\'' +
				", corrIntfStdVrsn='" + corrIntfStdVrsn + '\'' +
				", intfDscr='" + intfDscr + '\'' +
				", sDate='" + sDate + '\'' +
				", eDate='" + eDate + '\'' +
				'}';
	}

}