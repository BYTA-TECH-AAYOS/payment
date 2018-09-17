package com.bytatech.ayoos.payment.service;

import com.bytatech.ayoos.payment.service.dto.TransactionInfoDTO;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing TransactionInfo.
 */
public interface TransactionInfoService {

    /**
     * Save a transactionInfo.
     *
     * @param transactionInfoDTO the entity to save
     * @return the persisted entity
     */
    TransactionInfoDTO save(TransactionInfoDTO transactionInfoDTO);

    /**
     * Get all the transactionInfos.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<TransactionInfoDTO> findAll(Pageable pageable);


    /**
     * Get the "id" transactionInfo.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<TransactionInfoDTO> findOne(Long id);

    /**
     * Delete the "id" transactionInfo.
     *
     * @param id the id of the entity
     */
    void delete(Long id);

    /**
     * Search for the transactionInfo corresponding to the query.
     *
     * @param query the query of the search
     * 
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<TransactionInfoDTO> search(String query, Pageable pageable);
}
