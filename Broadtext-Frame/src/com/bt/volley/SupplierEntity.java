package com.bt.volley;

import java.io.Serializable;

public class SupplierEntity implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String applyUnit;
	private String submitDate; 
	private String dateString;
	private String requirementDepartment;
	private String passNum ;
	
	public String getApplyUnit() {
		return applyUnit;
	}
	public void setApplyUnit(String applyUnit) {
		this.applyUnit = applyUnit;
	}
	public String getSubmitDate() {
		return submitDate;
	}
	public void setSubmitDate(String submitDate) {
		this.submitDate = submitDate;
	}
	public String getDateString() {
		return dateString;
	}
	public void setDateString(String dateString) {
		this.dateString = dateString;
	}
	public String getRequirementDepartment() {
		return requirementDepartment;
	}
	public void setRequirementDepartment(String requirementDepartment) {
		this.requirementDepartment = requirementDepartment;
	}
	public String getPassNum() {
		return passNum;
	}
	public void setPassNum(String passNum) {
		this.passNum = passNum;
	}
	
	

}
