package com.cognizant.pdfuploader.service;

import java.text.MessageFormat;

import org.apache.commons.lang3.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.cognizant.pdfuploader.bean.PostAndCommentsResponse;
import com.cognizant.pdfuploader.bean.Posts;
import com.cognizant.pdfuploader.bean.PostsAndComments;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;

@Service
public class HystrixService {

	@Autowired
	private RestTemplate restTemplate;

	@Value("${postAPIURL}")
	private String postAPIURL;

	@Value("${postNCommentsAPIURL}")
	private String postNCommentsAPIURL;

	Logger logger = LoggerFactory.getLogger(HystrixService.class);

	/**
	 * @param posts
	 * @return
	 * 
	 *         This method calls Post API.
	 */
	@HystrixCommand(fallbackMethod = "addPostFallback", commandProperties = {
			@HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "5000"),
			@HystrixProperty(name = "circuitBreaker.requestVolumeThreshold", value = "10"),
			@HystrixProperty(name = "circuitBreaker.sleepWindowInMilliseconds", value = "1000") })
	public Posts callAddPostService(Posts posts) {
		Posts result = restTemplate.postForObject(postAPIURL, posts, Posts.class);
		return result;
	}

	/**
	 * @param posts
	 * @return
	 */
	public Posts addPostFallback(Posts posts, Throwable e) {
		logger.error("Reached addPostFallback !!!", e);
		return null;
	}

	/**
	 * @param posts
	 * @return
	 * 
	 *         This method calls the post and comments API.
	 */
	@HystrixCommand(fallbackMethod = "getPostFallback", commandProperties = {
			@HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "5000"),
			@HystrixProperty(name = "circuitBreaker.requestVolumeThreshold", value = "10"),
			@HystrixProperty(name = "circuitBreaker.sleepWindowInMilliseconds", value = "1000") })
	public PostAndCommentsResponse getPostsAndComments(Posts posts) {
		String postnCommentsURL = MessageFormat.format(postNCommentsAPIURL, posts.getUserId());
		PostsAndComments[] respObj = restTemplate.getForObject(postnCommentsURL, PostsAndComments[].class);
		if (ArrayUtils.isNotEmpty(respObj)) {
			PostAndCommentsResponse response = new PostAndCommentsResponse();
			response.setPostnCommentsResponse(respObj);
			return response;
		}
		return null;

	}

	/**
	 * @param posts
	 * @return
	 */
	public PostAndCommentsResponse getPostFallback(Posts posts, Throwable e) {
		logger.error("Reached getPostFallback !!!", e);
		return null;
	}

}
