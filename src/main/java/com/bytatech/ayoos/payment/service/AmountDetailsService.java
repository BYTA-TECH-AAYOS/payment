package com.bytatech.ayoos.payment.service;

import com.bytatech.ayoos.payment.service.dto.AmountDetailsDTO;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing AmountDetails.
 */
public interface AmountDetailsService {

    /**
     * Save a amountDetails.
     *
     * @param amountDetailsDTO the entity to save
     * @return the persisted entity
     */
    AmountDetailsDTO save(AmountDetailsDTO amountDetailsDTO);

    /**
     * Get all the amountDetails.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<AmountDetailsDTO> findAll(Pageable pageable);


    /**
     * Get the "id" amountDetails.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<AmountDetailsDTO> findOne(Long id);

    /**
     * Delete the "id" amountDetails.
     *
     * @param id the id of the entity
     */
    void delete(Long id);

    /**
     * Search for the amountDetails corresponding to the query.
     *
     * @param query the query of the search
     * 
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<AmountDetailsDTO> search(String query, Pageable pageable);
}
