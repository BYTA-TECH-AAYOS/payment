package com.bytatech.ayoos.payment.service;

import com.bytatech.ayoos.payment.service.dto.FundingInstrumentDTO;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing FundingInstrument.
 */
public interface FundingInstrumentService {

    /**
     * Save a fundingInstrument.
     *
     * @param fundingInstrumentDTO the entity to save
     * @return the persisted entity
     */
    FundingInstrumentDTO save(FundingInstrumentDTO fundingInstrumentDTO);

    /**
     * Get all the fundingInstruments.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<FundingInstrumentDTO> findAll(Pageable pageable);


    /**
     * Get the "id" fundingInstrument.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<FundingInstrumentDTO> findOne(Long id);

    /**
     * Delete the "id" fundingInstrument.
     *
     * @param id the id of the entity
     */
    void delete(Long id);

    /**
     * Search for the fundingInstrument corresponding to the query.
     *
     * @param query the query of the search
     * 
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<FundingInstrumentDTO> search(String query, Pageable pageable);
}
