package com.cognizant.pdfuploader.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "WWW_CUSTOMER")
@SequenceGenerator(name="seq", initialValue=1, allocationSize=1)
public class Customer {
	
	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="seq")
    private long id;
	
	private String custID;
	
	private String name;
	
	private String fileName;
	
	@Lob
	private byte[] fileData;

	public String getCustID() {
		return custID;
	}

	public void setCustID(String custID) {
		this.custID = custID;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public byte[] getFileData() {
		return fileData;
	}

	public void setFileData(byte[] fileData) {
		this.fileData = fileData;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

}
