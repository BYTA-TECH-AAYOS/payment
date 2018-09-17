package com.bytatech.ayoos.payment.service.impl;

import com.bytatech.ayoos.payment.service.AmountService;
import com.bytatech.ayoos.payment.domain.Amount;
import com.bytatech.ayoos.payment.repository.AmountRepository;
import com.bytatech.ayoos.payment.repository.search.AmountSearchRepository;
import com.bytatech.ayoos.payment.service.dto.AmountDTO;
import com.bytatech.ayoos.payment.service.mapper.AmountMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.Optional;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing Amount.
 */
@Service
@Transactional
public class AmountServiceImpl implements AmountService {

    private final Logger log = LoggerFactory.getLogger(AmountServiceImpl.class);

    private final AmountRepository amountRepository;

    private final AmountMapper amountMapper;

    private final AmountSearchRepository amountSearchRepository;

    public AmountServiceImpl(AmountRepository amountRepository, AmountMapper amountMapper, AmountSearchRepository amountSearchRepository) {
        this.amountRepository = amountRepository;
        this.amountMapper = amountMapper;
        this.amountSearchRepository = amountSearchRepository;
    }

    /**
     * Save a amount.
     *
     * @param amountDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public AmountDTO save(AmountDTO amountDTO) {
        log.debug("Request to save Amount : {}", amountDTO);
        Amount amount = amountMapper.toEntity(amountDTO);
        amount = amountRepository.save(amount);
        AmountDTO result = amountMapper.toDto(amount);
        amountSearchRepository.save(amount);
        return result;
    }

    /**
     * Get all the amounts.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<AmountDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Amounts");
        return amountRepository.findAll(pageable)
            .map(amountMapper::toDto);
    }


    /**
     * Get one amount by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<AmountDTO> findOne(Long id) {
        log.debug("Request to get Amount : {}", id);
        return amountRepository.findById(id)
            .map(amountMapper::toDto);
    }

    /**
     * Delete the amount by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Amount : {}", id);
        amountRepository.deleteById(id);
        amountSearchRepository.deleteById(id);
    }

    /**
     * Search for the amount corresponding to the query.
     *
     * @param query the query of the search
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<AmountDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Amounts for query {}", query);
        return amountSearchRepository.search(queryStringQuery(query), pageable)
            .map(amountMapper::toDto);
    }
}
