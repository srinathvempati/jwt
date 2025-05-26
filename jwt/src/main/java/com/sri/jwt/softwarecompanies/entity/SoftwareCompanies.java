package com.sri.jwt.softwarecompanies.entity;

import java.time.LocalDate;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;

@Entity
@Table(name = "software_companies")
public class SoftwareCompanies {
	
	@Id
	@GeneratedValue
	private Integer Id;
	private String company_name;
	private String industry;
	private String location;
	private String state;
	private Integer employees;
	private String comments;
	private String country;
	private LocalDate localDate;
	private boolean done; 
	
	public SoftwareCompanies() {
		
	}

	

	public SoftwareCompanies(Integer id, String company_name, String industry, String location, String state,
			Integer employees, String comments, String country, LocalDate localDate, boolean done) {
		super();
		Id = id;
		this.company_name = company_name;
		this.industry = industry;
		this.location = location;
		this.state = state;
		this.employees = employees;
		this.comments = comments;
		this.country = country;
		this.localDate = localDate;
		this.done = done;
	}

	public LocalDate getLocalDate() {
		return localDate;
	}

	public void setLocalDate(LocalDate localDate) {
		this.localDate = localDate;
	}

	public boolean isDone() {
		return done;
	}

	public void setDone(boolean done) {
		this.done = done;
	}

	public Integer getId() {
		return Id;
	}

	public void setId(Integer id) {
		Id = id;
	}

	public String getCompany_name() {
		return company_name;
	}

	public void setCompany_name(String company_name) {
		this.company_name = company_name;
	}

	public String getIndustry() {
		return industry;
	}

	public void setIndustry(String industry) {
		this.industry = industry;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public Integer getEmployees() {
		return employees;
	}

	public void setEmployees(Integer employees) {
		this.employees = employees;
	}

	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}



	@Override
	public String toString() {
		return "SoftwareCompanies [Id=" + Id + ", company_name=" + company_name + ", industry=" + industry
				+ ", location=" + location + ", state=" + state + ", employees=" + employees + ", comments=" + comments
				+ ", country=" + country + ", localDate=" + localDate + ", done=" + done + "]";
	}

	
	
	
	
	
	
	
	
	

}
