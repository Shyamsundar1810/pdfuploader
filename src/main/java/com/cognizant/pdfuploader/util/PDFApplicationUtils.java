package com.cognizant.pdfuploader.util;

import java.io.IOException;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import com.cognizant.pdfuploader.bean.PDFAppResponseBean;
import com.cognizant.pdfuploader.bean.PostResponseBean;
import com.cognizant.pdfuploader.bean.User;
import com.cognizant.pdfuploader.entity.Customer;

@Component
public class PDFApplicationUtils {

	private static final String PDF = "pdf";
	Logger logger = LoggerFactory.getLogger(PDFApplicationUtils.class);

	/**
	 * @param user
	 * @param file
	 * @return
	 * @throws IOException
	 * 
	 *                     This method populates the Customer entity.
	 */
	public Customer populateCustomer(User user, MultipartFile file) throws IOException {
		Customer customer = new Customer();
		customer.setCustID(user.getUserID());
		customer.setName(user.getName());
		customer.setFileName(user.getFileName());
		customer.setFileData(IOUtils.toByteArray(file.getInputStream()));
		return customer;

	}

	/**
	 * @param fileName
	 * @return
	 * 
	 *         This method validates the file extension
	 */
	public boolean validateFileExtension(String fileName) {
		return PDF.equalsIgnoreCase(FilenameUtils.getExtension(fileName));
	}

	/**
	 * @param errorCode
	 * @param errorMessage
	 * @return
	 * 
	 *         This method populates the error response for PDF services.
	 */
	public PDFAppResponseBean populateErrorResponseBean(String errorCode, String errorMessage) {
		PDFAppResponseBean bean = new PDFAppResponseBean();
		bean.setErrorCode(errorCode);
		bean.setErrorMessage(errorMessage);
		return bean;
	}

	/**
	 * @param errorCode
	 * @param errorMessage
	 * @return
	 * 
	 *         This method populates the error response for Post/Comment services.
	 */
	public PostResponseBean populatePostsErrorBean(String errorCode, String errorMessage) {
		PostResponseBean bean = new PostResponseBean();
		bean.setErrorCode(errorCode);
		bean.setErrorMessage(errorMessage);
		return bean;
	}

	/**
	 * @param user
	 * @return
	 * 
	 *         This method validates the incoming request
	 */
	public Set<ConstraintViolation<User>> reqValidator(User user) {
		ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
		Validator validator = factory.getValidator();
		Set<ConstraintViolation<User>> violations = validator.validate(user);
		violations.forEach(reason -> logger.error("Bad Request::".concat(reason.getMessage())));
		return violations;
	}

}
