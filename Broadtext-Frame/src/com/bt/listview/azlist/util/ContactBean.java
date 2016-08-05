package com.bt.listview.azlist.util;


/**
 * 联系人实体
 * @author XJ
 *
 */
public class ContactBean implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String contactName = null;//姓名
	public  String firstCharName = "";//首字母
	private String contactPhone;//手机
	private String contactHomePhone;//电话

	public String getContactName() {
		return contactName;
	}

	public void setContactName(String contactName) {
		this.contactName = contactName;
	}

	public String getContactPhone() {
		return contactPhone;
	}

	public void setContactPhone(String contactPhone) {
		this.contactPhone = contactPhone;
	}

	public String getContactHomePhone() {
		return contactHomePhone;
	}

	public void setContactHomePhone(String contactHomePhone) {
		this.contactHomePhone = contactHomePhone;
	}
}
