package com.bytatech.ayoos.payment.web.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bytatech.ayoos.payment.client.paypal.model.PaymentInitiateRequest;
import com.bytatech.ayoos.payment.client.paypal.model.PaymentInitiateResponse;
import com.bytatech.ayoos.payment.client.paypal.model.RefundSaleRequest;
import com.bytatech.ayoos.payment.domain.Payment;
import com.bytatech.ayoos.payment.client.paypal.model.PaymentExecutionRequest;
import com.bytatech.ayoos.payment.service.PaymentCommandService;
import com.bytatech.ayoos.payment.web.rest.errors.BadRequestAlertException;
import com.bytatech.ayoos.payment.web.rest.util.HeaderUtil;
import com.codahale.metrics.annotation.Timed;
import com.paypal.api.payments.Patch;



@RestController
@RequestMapping("/api/command")

public class PaymentCommandResource {

	private final Logger log=LoggerFactory.getLogger(PaymentCommandResource.class);
	
	private PaymentCommandService paymentCommandService;

	/*
	 * The method is used to Initiate a payment using the transaction details
	 * 
	 * @Parm The Payment object to create a simple payment
	 * 
	 * @Return The return object is the PaymenetInitiateResponse that is created
	 */

	public PaymentCommandResource(PaymentCommandService paymentCommandService) {

		this.paymentCommandService=paymentCommandService;
		
	}

	@PostMapping("/paymentGateway/payments/initiate")
	public PaymentInitiateResponse initiatePayment(@RequestBody PaymentInitiateRequest paymentInitiateRequest) {
		return paymentCommandService.initiatePayment(paymentInitiateRequest);
	}

	/*
	 * The method used to execute the created payment
	 * 
	 * @Param payment id of the payment and the payment execution request
	 * 
	 * @Return return type void after updated
	 */
	@PostMapping("paymentGateway/payments/{payment_id}/execute")
	public void executePayment(@PathVariable String payment_id,
			@RequestBody(required = false) PaymentExecutionRequest paymentExecutionRequest) {
		paymentCommandService.executePayment(payment_id, paymentExecutionRequest);
	}

	/*
	 * The method used to update the created payment
	 * 
	 * @Param payment id of the payment and the patch request
	 * 
	 * @Return return type void after updated
	 */
	@PatchMapping("/paymentGateway/payments/{payment_id}")
	public void updatePayment(@PathVariable String payment_id, @RequestBody ArrayList<Patch> patchRequest) {
		paymentCommandService.updatePayment(payment_id, patchRequest);
	}

	@PostMapping("/paymentGateway/payments/sale/{sale_id}/refund")
	public void refundSale(@PathVariable String sale_id, @RequestBody RefundSaleRequest refundSaleRequest) {
		paymentCommandService.refundSale(sale_id, refundSaleRequest);
	}

	/* RDBMS Spec functions */

	/**
	 * POST /payments : Create a new payment.
	 *
	 * @param paymentDTO
	 *            the paymentDTO to create
	 * @return the ResponseEntity with status 201 (Created) and with body the new
	 *         paymentDTO, or with status 400 (Bad Request) if the payment has
	 *         already an ID
	 * @throws URISyntaxException
	 *             if the Location URI syntax is incorrect
	 */
	@PostMapping("/payments")
	@Timed
	public void createPayment(@RequestBody Payment payment) throws URISyntaxException {
		log.debug("REST request to save Payment : {}", payment);
		if (payment.getId() != null) {
			throw new BadRequestAlertException("A new payment cannot already have an ID", "payment", "idexists");
		}
		paymentCommandService.save(payment);

	}

	/**
	 * PUT /payments : Updates an existing payment.
	 *
	 * @param paymentDTO
	 *            the paymentDTO to update
	 * @return the ResponseEntity with status 200 (OK) and with body the updated
	 *         paymentDTO, or with status 400 (Bad Request) if the paymentDTO is not
	 *         valid, or with status 500 (Internal Server Error) if the paymentDTO
	 *         couldn't be updated
	 * @throws URISyntaxException
	 *             if the Location URI syntax is incorrect
	 */
	@PutMapping("/payments")
	@Timed
	public void updatePayment(@RequestBody Payment payment) throws URISyntaxException {
		log.debug("REST request to update Payment : {}", payment);
		if (payment.getId() == null) {
			createPayment(payment);
		} else {
			paymentCommandService.save(payment);
		}
	}

	/**
	 * DELETE /payments/:id : delete the "id" payment.
	 *
	 * @param id
	 *            the id of the paymentDTO to delete
	 * @return the ResponseEntity with status 200 (OK)
	 */
	@DeleteMapping("/payments/{id}")
	@Timed
	public ResponseEntity<Void> deletePayment(@PathVariable Long id) {
		log.debug("REST request to delete Payment : {}", id);
		paymentCommandService.delete(id);
		return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("payment", id.toString())).build();
	}

}
