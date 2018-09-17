package com.bytatech.ayoos.payment.service;

import java.util.ArrayList;

import com.bytatech.ayoos.payment.client.paypal.model.PaymentInitiateRequest;
import com.bytatech.ayoos.payment.client.paypal.model.PaymentInitiateResponse;
import com.bytatech.ayoos.payment.client.paypal.model.RefundSaleRequest;
import com.bytatech.ayoos.payment.domain.Payment;
import com.bytatech.ayoos.payment.client.paypal.model.PaymentExecutionRequest;
import com.paypal.api.payments.Patch;

public interface PaymentCommandService {

	PaymentInitiateResponse initiatePayment(PaymentInitiateRequest paymentInitiateRequest);

	void updatePayment(String payment_id, ArrayList<Patch> patchRequest);

	void executePayment(String payment_id, PaymentExecutionRequest paymentExecution);

	void refundSale(String sale_id, RefundSaleRequest refundSaleRequest);

	/*RDBMS Spec functions*/
	
    /**
     * Save a payment.
     *
     * @param paymentDTO the entity to save
     * @return the persisted entity
     */
    void save(Payment paymentDTO);
    
    /**
     * Delete the "id" payment.
     *
     * @param id the id of the entity
     */
    void delete(Long id);
}
