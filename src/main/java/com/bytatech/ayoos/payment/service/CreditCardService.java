package com.bytatech.ayoos.payment.service;

import com.bytatech.ayoos.payment.service.dto.CreditCardDTO;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing CreditCard.
 */
public interface CreditCardService {

    /**
     * Save a creditCard.
     *
     * @param creditCardDTO the entity to save
     * @return the persisted entity
     */
    CreditCardDTO save(CreditCardDTO creditCardDTO);

    /**
     * Get all the creditCards.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<CreditCardDTO> findAll(Pageable pageable);


    /**
     * Get the "id" creditCard.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<CreditCardDTO> findOne(Long id);

    /**
     * Delete the "id" creditCard.
     *
     * @param id the id of the entity
     */
    void delete(Long id);

    /**
     * Search for the creditCard corresponding to the query.
     *
     * @param query the query of the search
     * 
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<CreditCardDTO> search(String query, Pageable pageable);
}
