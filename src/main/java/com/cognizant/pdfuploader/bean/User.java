package com.cognizant.pdfuploader.bean;

import javax.validation.constraints.NotBlank;

public class User {
	@NotBlank(message = "Invalid UserID")
	private String userID;
	@NotBlank(message = "Name is mandatory")
	private String name;
	@NotBlank(message = "FileName is mandatory")
	private String fileName;

	private String postTitle;
	private String postBody;

	public String getUserID() {
		return userID;
	}

	public void setUserID(String userID) {
		this.userID = userID;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getPostTitle() {
		return postTitle;
	}

	public void setPostTitle(String postTitle) {
		this.postTitle = postTitle;
	}

	public String getPostBody() {
		return postBody;
	}

	public void setPostBody(String postBody) {
		this.postBody = postBody;
	}

}
