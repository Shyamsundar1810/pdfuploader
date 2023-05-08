package com.cognizant.pdfuploader.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.cognizant.pdfuploader.bean.PDFAppResponseBean;
import com.cognizant.pdfuploader.bean.User;
import com.cognizant.pdfuploader.entity.Customer;
import com.cognizant.pdfuploader.service.PDFApplicationService;
import com.cognizant.pdfuploader.util.PDFApplicationUtils;
import com.fasterxml.jackson.databind.ObjectMapper;

@RestController
public class PDFApplicationController {

	@Autowired
	PDFApplicationService pdfAppService;

	@Autowired
	PDFApplicationUtils utils;

	@Autowired
	ObjectMapper objectMapper;

	private static final String CTS_ERR_002 = "CTS_ERR_002";
	private static final String CTS_ERR_003 = "CTS_ERR_003";
	private static final String CTS_ERR_004 = "CTS_ERR_004";
	private static final String CTS_ERR_005 = "CTS_ERR_005";

	Logger logger = LoggerFactory.getLogger(PDFApplicationController.class);

	/**
	 * @param file
	 * @param user
	 * @return
	 * 
	 *         This service adds the pdf into DB and send response to display the
	 *         uploaded pdf.
	 */
	@PostMapping("/uploadandviewpdf")
	public ResponseEntity<List<PDFAppResponseBean>> uploadAndViewPDF(@RequestParam("file") MultipartFile file,
			@Valid @RequestParam("user") String userStr) {
		List<PDFAppResponseBean> response = new ArrayList<>();
		try {
			User user = objectMapper.readValue(userStr, User.class);
			logger.info("Upload Request received for the userID::{}", user.getUserID());
			Set<ConstraintViolation<User>> violations = utils.reqValidator(user);
			if (!violations.isEmpty()) {
				return ResponseEntity.badRequest().build();
			}
			if (utils.validateFileExtension(user.getFileName())) {
				List<Customer> customer = pdfAppService.savePDFToDB(utils.populateCustomer(user, file));
				getResponseList(response, customer);
			} else {
				logger.error("Invalid File Extension");
				response.add(utils.populateErrorResponseBean(CTS_ERR_002, "Invalid File Extension"));
			}
		} catch (Exception e) {
			logger.error("Exception in uploadAndViewPDF()::", e);
			response.add(utils.populateErrorResponseBean(CTS_ERR_005, "System is currently down"));
		}
		return ResponseEntity.status(HttpStatus.OK).body(response);
	}

	/**
	 * @param user
	 * @return
	 * 
	 *         This service return the available pdfs for the customer.
	 */
	@PostMapping("/viewpdf")
	public ResponseEntity<List<PDFAppResponseBean>> viewPDF(@RequestBody User user) {
		logger.info("View Request received for the userID::{}", user.getUserID());
		List<PDFAppResponseBean> response = new ArrayList<>();
		try {
			Set<ConstraintViolation<User>> violations = utils.reqValidator(user);
			if (!violations.isEmpty()) {
				return ResponseEntity.badRequest().build();
			}
			List<Customer> customerDataList = pdfAppService.findByCustID(user.getUserID());
			if (!CollectionUtils.isEmpty(customerDataList)) {
				getResponseList(response, customerDataList);
			} else {
				response.add(utils.populateErrorResponseBean(CTS_ERR_003, "Please upload files"));
			}

		} catch (Exception e) {
			logger.error("Exception in viewPDF():: ", e);
			response.add(utils.populateErrorResponseBean(CTS_ERR_005, "System is currently down"));
		}
		return ResponseEntity.status(HttpStatus.OK).body(response);
	}

	/**
	 * @param user
	 * @return
	 * 
	 *         This service deletes the pdf and send the available pdfs as response.
	 */
	@PostMapping("/removeandviewpdf")
	public ResponseEntity<List<PDFAppResponseBean>> removeAndViewPDF(@RequestBody User user) {
		logger.info("Remove Request received for the userID::{}", user.getUserID());
		List<PDFAppResponseBean> response = new ArrayList<>();
		try {
			Set<ConstraintViolation<User>> violations = utils.reqValidator(user);
			if (!violations.isEmpty()) {
				return ResponseEntity.badRequest().build();
			}
			List<Customer> customerDataList = pdfAppService.removePDF(user.getFileName(), user.getUserID());
			if (!CollectionUtils.isEmpty(customerDataList)) {
				getResponseList(response, customerDataList);
			} else {
				response.add(utils.populateErrorResponseBean(CTS_ERR_004, "No Files Available to remove"));
			}

		} catch (Exception e) {
			logger.error("Error in viewPDF():: ", e);
			response.add(utils.populateErrorResponseBean(CTS_ERR_005, "System is currently down"));
		}
		return ResponseEntity.status(HttpStatus.OK).body(response);
	}

	/**
	 * @param response
	 * @param customer
	 */
	private void getResponseList(List<PDFAppResponseBean> response, List<Customer> customer) {
		customer.stream().forEach(cust -> {
			PDFAppResponseBean bean = new PDFAppResponseBean();
			bean.setFileName(cust.getFileName());
			bean.setName(cust.getName());
			bean.setFileData(cust.getFileData());
			response.add(bean);
		});
	}

}
