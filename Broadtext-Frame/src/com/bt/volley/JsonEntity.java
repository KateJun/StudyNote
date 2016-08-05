package com.bt.volley;

import java.util.ArrayList;


public class JsonEntity {

//	{
//		  "headers": {
//		    "Accept": "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8",
//		    "X-Request-Id": "fda9a705-d667-4c2c-89bf-e86122c441e3",
//		    "Accept-Encoding": "gzip,deflate,sdch",
//		    "Connection": "close",
//		    "Accept-Charset": "GBK,utf-8;q=0.7,*;q=0.3",
//		    "Host": "httpbin.org",
//		    "User-Agent": "Mozilla/5.0 (Windows NT 6.1) AppleWebKit/537.1 (KHTML, like Gecko) Chrome/21.0.1180.89 Safari/537.1",
//		    "Accept-Language": "zh-CN,zh;q=0.8"
//		  },
//		  "origin": "112.64.137.58",
//		  "url": "http://httpbin.org/get?param1=hello",
//		  "args": {
//		    "param1": "hello"
//		  }
//		}

	 private String contextRoot;
	 private String value; //登录 flag
	 private String name;
	 private String department;
	 private String supplierCount;
	
	 private ArrayList<OutPlaceEntity> inOutPlaceList; //出入地
	 private ArrayList<SupplierEntity> supplierList; //主列表
//	 result\.inOutPlaceList\[\d+\]\.name, 
//	 result\.inOutPlaceList\[\d+\]\.code,
	
//	result\.supplierList\[\d+\]\.applyUnit,
//	result\.supplierList\[\d+\]\.submitDate, 
//	result\.supplierList\[\d+\]\.dateString,
//	result\.supplierList\[\d+\]\.requirementDepartment, 
//	result\.supplierList\[\d+\]\.passNum 
	 
	 
	 public String getContextRoot() {
		return contextRoot;
	}
	public void setContextRoot(String contextRoot) {
		this.contextRoot = contextRoot;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDepartment() {
		return department;
	}
	public void setDepartment(String department) {
		this.department = department;
	}
	public String getSupplierCount() {
		return supplierCount;
	}
	public void setSupplierCount(String supplierCount) {
		this.supplierCount = supplierCount;
	}
	public ArrayList<OutPlaceEntity> getInOutPlaceList() {
		return inOutPlaceList;
	}
	public void setInOutPlaceList(ArrayList<OutPlaceEntity> inOutPlaceList) {
		this.inOutPlaceList = inOutPlaceList;
	}
	public ArrayList<SupplierEntity> getSupplierList() {
		return supplierList;
	}
	public void setSupplierList(ArrayList<SupplierEntity> supplierList) {
		this.supplierList = supplierList;
	}
}
