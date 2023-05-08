package com.cognizant.pdfuploader.service;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cognizant.pdfuploader.entity.Customer;
import com.cognizant.pdfuploader.repository.PDFApplicationRepository;

@Service
@Transactional
public class PDFApplicationServiceImpl implements PDFApplicationService {

	@Autowired
	PDFApplicationRepository pdfAppRepository;

	@Override
	public List<Customer> savePDFToDB(Customer customer) {
		pdfAppRepository.save(customer);
		return findByCustID(customer.getCustID());

	}

	@Override
	public List<Customer> findByCustID(String custID) {
		return pdfAppRepository.findByCustID(custID);
	}

	@Override
	public List<Customer> removePDF(String fileName, String custID) {
		pdfAppRepository.deleteByFileNameAndCustID(fileName, custID);
		return findByCustID(custID);
	}

	@Override
	public List<Customer> getDocumentID(String custID, String fileName) {
		return pdfAppRepository.findByCustIDAndFileName(custID, fileName);
	}

}
