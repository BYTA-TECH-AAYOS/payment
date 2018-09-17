package com.bytatech.ayoos.payment.service.impl;

import com.bytatech.ayoos.payment.service.AmountDetailsService;
import com.bytatech.ayoos.payment.domain.AmountDetails;
import com.bytatech.ayoos.payment.repository.AmountDetailsRepository;
import com.bytatech.ayoos.payment.repository.search.AmountDetailsSearchRepository;
import com.bytatech.ayoos.payment.service.dto.AmountDetailsDTO;
import com.bytatech.ayoos.payment.service.mapper.AmountDetailsMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.Optional;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing AmountDetails.
 */
@Service
@Transactional
public class AmountDetailsServiceImpl implements AmountDetailsService {

    private final Logger log = LoggerFactory.getLogger(AmountDetailsServiceImpl.class);

    private final AmountDetailsRepository amountDetailsRepository;

    private final AmountDetailsMapper amountDetailsMapper;

    private final AmountDetailsSearchRepository amountDetailsSearchRepository;

    public AmountDetailsServiceImpl(AmountDetailsRepository amountDetailsRepository, AmountDetailsMapper amountDetailsMapper, AmountDetailsSearchRepository amountDetailsSearchRepository) {
        this.amountDetailsRepository = amountDetailsRepository;
        this.amountDetailsMapper = amountDetailsMapper;
        this.amountDetailsSearchRepository = amountDetailsSearchRepository;
    }

    /**
     * Save a amountDetails.
     *
     * @param amountDetailsDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public AmountDetailsDTO save(AmountDetailsDTO amountDetailsDTO) {
        log.debug("Request to save AmountDetails : {}", amountDetailsDTO);
        AmountDetails amountDetails = amountDetailsMapper.toEntity(amountDetailsDTO);
        amountDetails = amountDetailsRepository.save(amountDetails);
        AmountDetailsDTO result = amountDetailsMapper.toDto(amountDetails);
        amountDetailsSearchRepository.save(amountDetails);
        return result;
    }

    /**
     * Get all the amountDetails.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<AmountDetailsDTO> findAll(Pageable pageable) {
        log.debug("Request to get all AmountDetails");
        return amountDetailsRepository.findAll(pageable)
            .map(amountDetailsMapper::toDto);
    }


    /**
     * Get one amountDetails by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<AmountDetailsDTO> findOne(Long id) {
        log.debug("Request to get AmountDetails : {}", id);
        return amountDetailsRepository.findById(id)
            .map(amountDetailsMapper::toDto);
    }

    /**
     * Delete the amountDetails by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete AmountDetails : {}", id);
        amountDetailsRepository.deleteById(id);
        amountDetailsSearchRepository.deleteById(id);
    }

    /**
     * Search for the amountDetails corresponding to the query.
     *
     * @param query the query of the search
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<AmountDetailsDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of AmountDetails for query {}", query);
        return amountDetailsSearchRepository.search(queryStringQuery(query), pageable)
            .map(amountDetailsMapper::toDto);
    }
}
