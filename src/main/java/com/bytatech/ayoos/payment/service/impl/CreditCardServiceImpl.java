package com.bytatech.ayoos.payment.service.impl;

import com.bytatech.ayoos.payment.service.CreditCardService;
import com.bytatech.ayoos.payment.domain.CreditCard;
import com.bytatech.ayoos.payment.repository.CreditCardRepository;
import com.bytatech.ayoos.payment.repository.search.CreditCardSearchRepository;
import com.bytatech.ayoos.payment.service.dto.CreditCardDTO;
import com.bytatech.ayoos.payment.service.mapper.CreditCardMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.Optional;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing CreditCard.
 */
@Service
@Transactional
public class CreditCardServiceImpl implements CreditCardService {

    private final Logger log = LoggerFactory.getLogger(CreditCardServiceImpl.class);

    private final CreditCardRepository creditCardRepository;

    private final CreditCardMapper creditCardMapper;

    private final CreditCardSearchRepository creditCardSearchRepository;

    public CreditCardServiceImpl(CreditCardRepository creditCardRepository, CreditCardMapper creditCardMapper, CreditCardSearchRepository creditCardSearchRepository) {
        this.creditCardRepository = creditCardRepository;
        this.creditCardMapper = creditCardMapper;
        this.creditCardSearchRepository = creditCardSearchRepository;
    }

    /**
     * Save a creditCard.
     *
     * @param creditCardDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public CreditCardDTO save(CreditCardDTO creditCardDTO) {
        log.debug("Request to save CreditCard : {}", creditCardDTO);
        CreditCard creditCard = creditCardMapper.toEntity(creditCardDTO);
        creditCard = creditCardRepository.save(creditCard);
        CreditCardDTO result = creditCardMapper.toDto(creditCard);
        creditCardSearchRepository.save(creditCard);
        return result;
    }

    /**
     * Get all the creditCards.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<CreditCardDTO> findAll(Pageable pageable) {
        log.debug("Request to get all CreditCards");
        return creditCardRepository.findAll(pageable)
            .map(creditCardMapper::toDto);
    }


    /**
     * Get one creditCard by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<CreditCardDTO> findOne(Long id) {
        log.debug("Request to get CreditCard : {}", id);
        return creditCardRepository.findById(id)
            .map(creditCardMapper::toDto);
    }

    /**
     * Delete the creditCard by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete CreditCard : {}", id);
        creditCardRepository.deleteById(id);
        creditCardSearchRepository.deleteById(id);
    }

    /**
     * Search for the creditCard corresponding to the query.
     *
     * @param query the query of the search
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<CreditCardDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of CreditCards for query {}", query);
        return creditCardSearchRepository.search(queryStringQuery(query), pageable)
            .map(creditCardMapper::toDto);
    }
}
