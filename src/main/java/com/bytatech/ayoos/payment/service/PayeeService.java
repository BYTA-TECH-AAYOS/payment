package com.bytatech.ayoos.payment.service;

import com.bytatech.ayoos.payment.service.dto.PayeeDTO;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing Payee.
 */
public interface PayeeService {

    /**
     * Save a payee.
     *
     * @param payeeDTO the entity to save
     * @return the persisted entity
     */
    PayeeDTO save(PayeeDTO payeeDTO);

    /**
     * Get all the payees.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<PayeeDTO> findAll(Pageable pageable);


    /**
     * Get the "id" payee.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<PayeeDTO> findOne(Long id);

    /**
     * Delete the "id" payee.
     *
     * @param id the id of the entity
     */
    void delete(Long id);

    /**
     * Search for the payee corresponding to the query.
     *
     * @param query the query of the search
     * 
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<PayeeDTO> search(String query, Pageable pageable);
}
