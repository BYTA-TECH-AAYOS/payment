package com.bytatech.ayoos.payment.service;

import com.bytatech.ayoos.payment.service.dto.PayerDTO;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing Payer.
 */
public interface PayerService {

    /**
     * Save a payer.
     *
     * @param payerDTO the entity to save
     * @return the persisted entity
     */
    PayerDTO save(PayerDTO payerDTO);

    /**
     * Get all the payers.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<PayerDTO> findAll(Pageable pageable);


    /**
     * Get the "id" payer.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<PayerDTO> findOne(Long id);

    /**
     * Delete the "id" payer.
     *
     * @param id the id of the entity
     */
    void delete(Long id);

    /**
     * Search for the payer corresponding to the query.
     *
     * @param query the query of the search
     * 
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<PayerDTO> search(String query, Pageable pageable);
}
