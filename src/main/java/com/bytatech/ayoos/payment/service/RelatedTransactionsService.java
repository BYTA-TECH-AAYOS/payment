package com.bytatech.ayoos.payment.service;

import com.bytatech.ayoos.payment.service.dto.RelatedTransactionsDTO;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing RelatedTransactions.
 */
public interface RelatedTransactionsService {

    /**
     * Save a relatedTransactions.
     *
     * @param relatedTransactionsDTO the entity to save
     * @return the persisted entity
     */
    RelatedTransactionsDTO save(RelatedTransactionsDTO relatedTransactionsDTO);

    /**
     * Get all the relatedTransactions.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<RelatedTransactionsDTO> findAll(Pageable pageable);


    /**
     * Get the "id" relatedTransactions.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<RelatedTransactionsDTO> findOne(Long id);

    /**
     * Delete the "id" relatedTransactions.
     *
     * @param id the id of the entity
     */
    void delete(Long id);

    /**
     * Search for the relatedTransactions corresponding to the query.
     *
     * @param query the query of the search
     * 
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<RelatedTransactionsDTO> search(String query, Pageable pageable);
}
