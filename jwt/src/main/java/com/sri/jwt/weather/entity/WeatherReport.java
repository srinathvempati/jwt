package com.sri.jwt.weather.entity;

import java.io.Serializable;

public class WeatherReport implements Serializable {

	
	String company_name;
	String tempData;
	String icon;
	
	
	
	public WeatherReport(String company_name, String tempData, String icon) {
		super();
		this.company_name = company_name;
		this.tempData = tempData;
		this.icon = icon;
	}
	
	
	public String getCompany_name() {
		return company_name;
	}
	public void setCompany_name(String company_name) {
		this.company_name = company_name;
	}
	public String getTempData() {
		return tempData;
	}
	public void setTempData(String tempData) {
		this.tempData = tempData;
	}
	public String getIcon() {
		return icon;
	}
	public void setIcon(String icon) {
		this.icon = icon;
	}


	@Override
	public String toString() {
		return "WeatherReport [company_name=" + company_name + ", tempData=" + tempData + ", icon=" + icon + "]";
	}
	
	
	
	
}
