package com.cognizant.pdfuploader.service;

import com.cognizant.pdfuploader.bean.PostAndCommentsResponse;
import com.cognizant.pdfuploader.bean.User;
import com.cognizant.pdfuploader.entity.Customer;
import com.cognizant.pdfuploader.exception.PDFApplicationException;

public interface PlaceHolderService {
	
	PostAndCommentsResponse addUserPosts(Customer customer, User user) throws PDFApplicationException;
	
	PostAndCommentsResponse getUserPosts(Customer customer, User user) throws PDFApplicationException;

}
