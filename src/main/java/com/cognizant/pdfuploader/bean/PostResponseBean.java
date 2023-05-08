package com.cognizant.pdfuploader.bean;

public class PostResponseBean {

	private PostAndCommentsResponse posts;

	private String errorCode;

	private String errorMessage;

	public PostAndCommentsResponse getPosts() {
		return posts;
	}

	public void setPosts(PostAndCommentsResponse posts) {
		this.posts = posts;
	}

	public String getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

}
