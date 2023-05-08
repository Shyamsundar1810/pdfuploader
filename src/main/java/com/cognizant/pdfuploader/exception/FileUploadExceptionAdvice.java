package com.cognizant.pdfuploader.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.cognizant.pdfuploader.bean.PDFAppResponseBean;

@ControllerAdvice
public class FileUploadExceptionAdvice extends ResponseEntityExceptionHandler {

	private static final String CTS_ERR_001 = "CTS_ERR_001";

	@ExceptionHandler(MaxUploadSizeExceededException.class)
	public ResponseEntity<PDFAppResponseBean> handleMaxSizeException(MaxUploadSizeExceededException exc) {
		PDFAppResponseBean response = new PDFAppResponseBean();
		response.setErrorCode(CTS_ERR_001);
		response.setErrorMessage("File too large!");
		return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(response);
	}

}
