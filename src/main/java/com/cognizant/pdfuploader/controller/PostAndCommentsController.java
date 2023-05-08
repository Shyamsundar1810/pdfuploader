package com.cognizant.pdfuploader.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.cognizant.pdfuploader.bean.PostAndCommentsResponse;
import com.cognizant.pdfuploader.bean.PostResponseBean;
import com.cognizant.pdfuploader.bean.User;
import com.cognizant.pdfuploader.entity.Customer;
import com.cognizant.pdfuploader.exception.PDFApplicationException;
import com.cognizant.pdfuploader.service.PDFApplicationService;
import com.cognizant.pdfuploader.service.PlaceHolderService;
import com.cognizant.pdfuploader.util.PDFApplicationUtils;

@RestController
public class PostAndCommentsController {

	@Autowired
	PlaceHolderService placeHolderSvc;

	@Autowired
	PDFApplicationService pdfAppService;

	@Autowired
	PDFApplicationUtils utils;

	private static final String CTS_ERR_006 = "CTS_ERR_006";
	private static final String CTS_ERR_007 = "CTS_ERR_007";
	private static final String CTS_ERR_008 = "CTS_ERR_008";

	Logger logger = LoggerFactory.getLogger(PostAndCommentsController.class);

	/**
	 * @param user
	 * 
	 *             This service integrates the posts with the third-party and sends
	 *             all post and comments in the response.
	 */
	@PostMapping("/addPost")
	public PostResponseBean processUserPost(@RequestBody User user) {
		logger.info("Add Post Request received for the userID::{}", user.getUserID());
		PostResponseBean response = new PostResponseBean();
		try {
			List<Customer> customerList = pdfAppService.getDocumentID(user.getUserID(), user.getFileName());
			if (!CollectionUtils.isEmpty(customerList)) {
				PostAndCommentsResponse postFinalResponse = placeHolderSvc.addUserPosts(customerList.get(0), user);
				response.setPosts(postFinalResponse);
			} else {
				logger.error("Document Not Available to Post");
				response = utils.populatePostsErrorBean(CTS_ERR_008, "No Document available to add Post");
			}
		} catch (PDFApplicationException e) {
			response = utils.populatePostsErrorBean(CTS_ERR_007, "Service is currently not available");
		} catch (Exception e) {
			response = utils.populatePostsErrorBean(CTS_ERR_006, "System is currently down");
		}
		return response;
	}

	/**
	 * @param user
	 * 
	 *             This method fetches the posts and comments for the document.
	 */
	@PostMapping("/getPost")
	public PostResponseBean getUserPost(@RequestBody User user) {
		logger.info("Get Post Request received for the userID::{}", user.getUserID());
		PostResponseBean response = new PostResponseBean();
		try {
			List<Customer> customerList = pdfAppService.getDocumentID(user.getUserID(), user.getFileName());
			if (!CollectionUtils.isEmpty(customerList)) {
				PostAndCommentsResponse postFinalResponse = placeHolderSvc.getUserPosts(customerList.get(0), user);
				response.setPosts(postFinalResponse);
			} else {
				logger.error("Document Not Available to get Post and Comments");
				response = utils.populatePostsErrorBean(CTS_ERR_008, "Document Not Available to get Post and Comments");
			}
		} catch (PDFApplicationException e) {
			response = utils.populatePostsErrorBean(CTS_ERR_007, "Service is currently not available");
		} catch (Exception e) {
			logger.error("Exception in getUserPost()::", e);
			response = utils.populatePostsErrorBean(CTS_ERR_006, "System is currently down");
		}
		return response;
	}

}
