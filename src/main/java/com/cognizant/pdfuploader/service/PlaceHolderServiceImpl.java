package com.cognizant.pdfuploader.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import com.cognizant.pdfuploader.bean.PostAndCommentsResponse;
import com.cognizant.pdfuploader.bean.Posts;
import com.cognizant.pdfuploader.bean.User;
import com.cognizant.pdfuploader.entity.Customer;
import com.cognizant.pdfuploader.exception.PDFApplicationException;

@Service
public class PlaceHolderServiceImpl implements PlaceHolderService {

	@Autowired
	HystrixService hystrixService;

	private static final String CTS_ERR_007 = "CTS_ERR_007";

	/**
	 *
	 */
	@Override
	public PostAndCommentsResponse addUserPosts(Customer customer, User user) throws PDFApplicationException {
		PostAndCommentsResponse postFinalResponse = new PostAndCommentsResponse();
		Posts posts = populateRequest(customer, user);
		Posts postResponse = hystrixService.callAddPostService(posts);
		if (!ObjectUtils.isEmpty(postResponse)) {
			postFinalResponse = hystrixService.getPostsAndComments(posts);
			if (ObjectUtils.isEmpty(postFinalResponse)) {
				throw new PDFApplicationException(CTS_ERR_007, "Post/Comments service is currently down");
			}
		} else {
			throw new PDFApplicationException(CTS_ERR_007, "Post/Comments service is currently down");
		}

		return postFinalResponse;
	}

	/**
	 * @throws PDFApplicationException
	 *
	 */
	@Override
	public PostAndCommentsResponse getUserPosts(Customer customer, User user) throws PDFApplicationException {
		Posts posts = populateRequest(customer, user);
		PostAndCommentsResponse postncomments = hystrixService.getPostsAndComments(posts);
		if (ObjectUtils.isEmpty(postncomments)) {
			throw new PDFApplicationException(CTS_ERR_007, "Post/Comments service is currently down");
		}
		return postncomments;
	}

	/**
	 * @param customer
	 * @param user
	 * @return
	 * 
	 *         This method populates the request for the third-party API.
	 */
	private Posts populateRequest(Customer customer, User user) {
		Posts post = new Posts();
		post.setUserId((int) customer.getId());
		post.setTitle(user.getPostTitle());
		post.setBody(user.getPostBody());
		return post;
	}

}
