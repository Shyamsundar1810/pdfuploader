package com.cognizant.pdfuploader.service;

import java.util.List;

import com.cognizant.pdfuploader.entity.Customer;

public interface PDFApplicationService {
	
	List<Customer> savePDFToDB(Customer customer);
	
	List<Customer> findByCustID(String custID);
	
	List<Customer> removePDF(String fileName, String custID);
	
	List<Customer> getDocumentID(String custID, String fileName);

}
