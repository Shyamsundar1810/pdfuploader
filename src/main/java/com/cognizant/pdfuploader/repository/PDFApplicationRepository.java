package com.cognizant.pdfuploader.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.cognizant.pdfuploader.entity.Customer;

@Repository
public interface PDFApplicationRepository extends JpaRepository<Customer, String>{
	
	List<Customer> findByCustID(@Param("CUSTID") String custID);
	
	List<Customer> deleteByFileNameAndCustID(@Param("FILE_NAME") String fileName, @Param("CUSTID") String custID);
	
	List<Customer> findByCustIDAndFileName(@Param("CUSTID") String custID, @Param("FILE_NAME") String fileName);

}
