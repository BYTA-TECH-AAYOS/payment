package com.bytatech.ayoos.payment.service;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.bytatech.ayoos.payment.client.paypal.model.PaymentDetails;
import com.bytatech.ayoos.payment.client.paypal.model.PaymentHistory;
import com.bytatech.ayoos.payment.service.dto.PaymentDTO;

public interface PaymentQueryService {

	PaymentHistory getAllPayments();
	
	PaymentDetails getPayment(String payment_id );
	
			/*RDBMS Spec operations*/
	
	 /**
     * Get all the payments.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<PaymentDTO> findAll(Pageable pageable);


    /**
     * Get the "id" payment.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<PaymentDTO> findOne(Long id);




    /**
     * Search for the payment corresponding to the query.
     *
     * @param query the query of the search
     * 
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<PaymentDTO> search(String query, Pageable pageable);
}
