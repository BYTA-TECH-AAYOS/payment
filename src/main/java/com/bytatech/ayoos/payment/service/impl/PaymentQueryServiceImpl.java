package com.bytatech.ayoos.payment.service.impl;

import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;


import java.util.Optional;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bytatech.ayoos.payment.client.paypal.api.PaymentGatewayApi;
import com.bytatech.ayoos.payment.client.paypal.model.PaymentDetails;

import com.bytatech.ayoos.payment.repository.PaymentRepository;
import com.bytatech.ayoos.payment.repository.search.PaymentSearchRepository;
import com.bytatech.ayoos.payment.service.PaymentQueryService;
import com.bytatech.ayoos.payment.service.dto.PaymentDTO;
import com.bytatech.ayoos.payment.service.mapper.PaymentMapper;
import com.bytatech.ayoos.payment.client.paypal.model.PaymentHistory;


@Service

public class PaymentQueryServiceImpl implements PaymentQueryService {

    private final Logger log = LoggerFactory.getLogger(PaymentServiceImpl.class);

	@Autowired
	PaymentGatewayApi paymentGatewayApi;
	
	private final PaymentRepository paymentRepository;

    private final PaymentMapper paymentMapper;

    private final PaymentSearchRepository paymentSearchRepository;

    public PaymentQueryServiceImpl(PaymentRepository paymentRepository, PaymentMapper paymentMapper, PaymentSearchRepository paymentSearchRepository) {
        this.paymentRepository = paymentRepository;
        this.paymentMapper = paymentMapper;
        this.paymentSearchRepository = paymentSearchRepository;
    }
	
	@Override
	public PaymentHistory getAllPayments() {
		log.info("get all payments ");
		return paymentGatewayApi.getAllPayments();
	}
	
	@Override
	public PaymentDetails getPayment(String payment_id) {
		log.info("Get the specified payment using the payment id");
		return paymentGatewayApi.getPayment(payment_id);
	}
	

						/*RDBMS Spec operations*/

	/**
     * Get all the payments.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<PaymentDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Payments");
        return paymentRepository.findAll(pageable)
            .map(paymentMapper::toDto);
    }


    /**
     * Get one payment by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<PaymentDTO> findOne(Long id) {
        log.debug("Request to get Payment : {}", id);
        return paymentRepository.findById(id)
            .map(paymentMapper::toDto);
    }
    
    /**
     * Search for the payment corresponding to the query.
     *
     * @param query the query of the search
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<PaymentDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Payments for query {}", query);
        return paymentSearchRepository.search(queryStringQuery(query), pageable)
            .map(paymentMapper::toDto);
    }


}
