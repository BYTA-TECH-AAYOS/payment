package com.bytatech.ayoos.payment.web.rest;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.bytatech.ayoos.payment.client.paypal.model.PaymentDetails;
import com.bytatech.ayoos.payment.service.PaymentQueryService;
import com.bytatech.ayoos.payment.service.dto.PaymentDTO;
import com.bytatech.ayoos.payment.web.rest.util.PaginationUtil;
import com.codahale.metrics.annotation.Timed;

import io.github.jhipster.web.util.ResponseUtil;

import com.bytatech.ayoos.payment.client.paypal.model.PaymentHistory;

@RestController
@RequestMapping("/api/query")

public class PaymentQueryResource {
private final Logger log=LoggerFactory.getLogger(PaymentQueryResource.class);
	
	@Autowired
	private PaymentQueryService paymentQueryService;
	
	/*
	 * The method return the list of payments
	 * 
	 * @Return returns the PaymentHistory object
	 */

	@GetMapping("/paymentGateway/payments")
	public PaymentHistory getAllPaymentsFromGateway() {
		return paymentQueryService.getAllPayments();
	}
	
	/*
	 * The method return the specified payment by search using payment id
	 * 
	 * @Param the payment id of the specified payment
	 * 
	 * @Return return the payment object
	 */
	@GetMapping("/paymentGateway/payments/{payment_id}")
	public PaymentDetails getPayment(@PathVariable String payment_id) {
		return paymentQueryService.getPayment(payment_id);
	}
	
				/*RDBMS Spec Functions*/
	
	/**
     * GET  /payments : get all the payments.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of payments in body
     */
    @GetMapping("/payments")
    @Timed
    public ResponseEntity<List<PaymentDTO>> getAllPayments(Pageable pageable) {
        log.debug("REST request to get a page of Payments");
        Page<PaymentDTO> page = paymentQueryService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/payments");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /payments/:id : get the "id" payment.
     *
     * @param id the id of the paymentDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the paymentDTO, or with status 404 (Not Found)
     */
    @GetMapping("/payments/{id}")
    @Timed
    public ResponseEntity<PaymentDTO> getPayment(@PathVariable Long id) {
        log.debug("REST request to get Payment : {}", id);
        Optional<PaymentDTO> paymentDTO = paymentQueryService.findOne(id);
        return ResponseUtil.wrapOrNotFound(paymentDTO);
    }

   
    /**
     * SEARCH  /_search/payments?query=:query : search for the payment corresponding
     * to the query.
     *
     * @param query the query of the payment search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/payments")
    @Timed
    public ResponseEntity<List<PaymentDTO>> searchPayments(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of Payments for query {}", query);
        Page<PaymentDTO> page = paymentQueryService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/payments");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

	
}
