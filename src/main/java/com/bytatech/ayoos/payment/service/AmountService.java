package com.bytatech.ayoos.payment.service;

import com.bytatech.ayoos.payment.service.dto.AmountDTO;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing Amount.
 */
public interface AmountService {

    /**
     * Save a amount.
     *
     * @param amountDTO the entity to save
     * @return the persisted entity
     */
    AmountDTO save(AmountDTO amountDTO);

    /**
     * Get all the amounts.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<AmountDTO> findAll(Pageable pageable);


    /**
     * Get the "id" amount.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<AmountDTO> findOne(Long id);

    /**
     * Delete the "id" amount.
     *
     * @param id the id of the entity
     */
    void delete(Long id);

    /**
     * Search for the amount corresponding to the query.
     *
     * @param query the query of the search
     * 
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<AmountDTO> search(String query, Pageable pageable);
}
