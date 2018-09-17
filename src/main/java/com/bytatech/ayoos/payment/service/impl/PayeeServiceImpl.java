package com.bytatech.ayoos.payment.service.impl;

import com.bytatech.ayoos.payment.service.PayeeService;
import com.bytatech.ayoos.payment.domain.Payee;
import com.bytatech.ayoos.payment.repository.PayeeRepository;
import com.bytatech.ayoos.payment.repository.search.PayeeSearchRepository;
import com.bytatech.ayoos.payment.service.dto.PayeeDTO;
import com.bytatech.ayoos.payment.service.mapper.PayeeMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.Optional;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing Payee.
 */
@Service
@Transactional
public class PayeeServiceImpl implements PayeeService {

    private final Logger log = LoggerFactory.getLogger(PayeeServiceImpl.class);

    private final PayeeRepository payeeRepository;

    private final PayeeMapper payeeMapper;

    private final PayeeSearchRepository payeeSearchRepository;

    public PayeeServiceImpl(PayeeRepository payeeRepository, PayeeMapper payeeMapper, PayeeSearchRepository payeeSearchRepository) {
        this.payeeRepository = payeeRepository;
        this.payeeMapper = payeeMapper;
        this.payeeSearchRepository = payeeSearchRepository;
    }

    /**
     * Save a payee.
     *
     * @param payeeDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public PayeeDTO save(PayeeDTO payeeDTO) {
        log.debug("Request to save Payee : {}", payeeDTO);
        Payee payee = payeeMapper.toEntity(payeeDTO);
        payee = payeeRepository.save(payee);
        PayeeDTO result = payeeMapper.toDto(payee);
        payeeSearchRepository.save(payee);
        return result;
    }

    /**
     * Get all the payees.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<PayeeDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Payees");
        return payeeRepository.findAll(pageable)
            .map(payeeMapper::toDto);
    }


    /**
     * Get one payee by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<PayeeDTO> findOne(Long id) {
        log.debug("Request to get Payee : {}", id);
        return payeeRepository.findById(id)
            .map(payeeMapper::toDto);
    }

    /**
     * Delete the payee by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Payee : {}", id);
        payeeRepository.deleteById(id);
        payeeSearchRepository.deleteById(id);
    }

    /**
     * Search for the payee corresponding to the query.
     *
     * @param query the query of the search
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<PayeeDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Payees for query {}", query);
        return payeeSearchRepository.search(queryStringQuery(query), pageable)
            .map(payeeMapper::toDto);
    }
}
