package com.bytatech.ayoos.payment.service.impl;

import com.bytatech.ayoos.payment.service.PayerService;
import com.bytatech.ayoos.payment.domain.Payer;
import com.bytatech.ayoos.payment.repository.PayerRepository;
import com.bytatech.ayoos.payment.repository.search.PayerSearchRepository;
import com.bytatech.ayoos.payment.service.dto.PayerDTO;
import com.bytatech.ayoos.payment.service.mapper.PayerMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.Optional;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing Payer.
 */
@Service
@Transactional
public class PayerServiceImpl implements PayerService {

    private final Logger log = LoggerFactory.getLogger(PayerServiceImpl.class);

    private final PayerRepository payerRepository;

    private final PayerMapper payerMapper;

    private final PayerSearchRepository payerSearchRepository;

    public PayerServiceImpl(PayerRepository payerRepository, PayerMapper payerMapper, PayerSearchRepository payerSearchRepository) {
        this.payerRepository = payerRepository;
        this.payerMapper = payerMapper;
        this.payerSearchRepository = payerSearchRepository;
    }

    /**
     * Save a payer.
     *
     * @param payerDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public PayerDTO save(PayerDTO payerDTO) {
        log.debug("Request to save Payer : {}", payerDTO);
        Payer payer = payerMapper.toEntity(payerDTO);
        payer = payerRepository.save(payer);
        PayerDTO result = payerMapper.toDto(payer);
        payerSearchRepository.save(payer);
        return result;
    }

    /**
     * Get all the payers.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<PayerDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Payers");
        return payerRepository.findAll(pageable)
            .map(payerMapper::toDto);
    }


    /**
     * Get one payer by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<PayerDTO> findOne(Long id) {
        log.debug("Request to get Payer : {}", id);
        return payerRepository.findById(id)
            .map(payerMapper::toDto);
    }

    /**
     * Delete the payer by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Payer : {}", id);
        payerRepository.deleteById(id);
        payerSearchRepository.deleteById(id);
    }

    /**
     * Search for the payer corresponding to the query.
     *
     * @param query the query of the search
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<PayerDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Payers for query {}", query);
        return payerSearchRepository.search(queryStringQuery(query), pageable)
            .map(payerMapper::toDto);
    }
}
