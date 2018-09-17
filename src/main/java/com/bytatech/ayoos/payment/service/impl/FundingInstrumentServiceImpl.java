package com.bytatech.ayoos.payment.service.impl;

import com.bytatech.ayoos.payment.service.FundingInstrumentService;
import com.bytatech.ayoos.payment.domain.FundingInstrument;
import com.bytatech.ayoos.payment.repository.FundingInstrumentRepository;
import com.bytatech.ayoos.payment.repository.search.FundingInstrumentSearchRepository;
import com.bytatech.ayoos.payment.service.dto.FundingInstrumentDTO;
import com.bytatech.ayoos.payment.service.mapper.FundingInstrumentMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.Optional;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing FundingInstrument.
 */
@Service
@Transactional
public class FundingInstrumentServiceImpl implements FundingInstrumentService {

    private final Logger log = LoggerFactory.getLogger(FundingInstrumentServiceImpl.class);

    private final FundingInstrumentRepository fundingInstrumentRepository;

    private final FundingInstrumentMapper fundingInstrumentMapper;

    private final FundingInstrumentSearchRepository fundingInstrumentSearchRepository;

    public FundingInstrumentServiceImpl(FundingInstrumentRepository fundingInstrumentRepository, FundingInstrumentMapper fundingInstrumentMapper, FundingInstrumentSearchRepository fundingInstrumentSearchRepository) {
        this.fundingInstrumentRepository = fundingInstrumentRepository;
        this.fundingInstrumentMapper = fundingInstrumentMapper;
        this.fundingInstrumentSearchRepository = fundingInstrumentSearchRepository;
    }

    /**
     * Save a fundingInstrument.
     *
     * @param fundingInstrumentDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public FundingInstrumentDTO save(FundingInstrumentDTO fundingInstrumentDTO) {
        log.debug("Request to save FundingInstrument : {}", fundingInstrumentDTO);
        FundingInstrument fundingInstrument = fundingInstrumentMapper.toEntity(fundingInstrumentDTO);
        fundingInstrument = fundingInstrumentRepository.save(fundingInstrument);
        FundingInstrumentDTO result = fundingInstrumentMapper.toDto(fundingInstrument);
        fundingInstrumentSearchRepository.save(fundingInstrument);
        return result;
    }

    /**
     * Get all the fundingInstruments.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<FundingInstrumentDTO> findAll(Pageable pageable) {
        log.debug("Request to get all FundingInstruments");
        return fundingInstrumentRepository.findAll(pageable)
            .map(fundingInstrumentMapper::toDto);
    }


    /**
     * Get one fundingInstrument by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<FundingInstrumentDTO> findOne(Long id) {
        log.debug("Request to get FundingInstrument : {}", id);
        return fundingInstrumentRepository.findById(id)
            .map(fundingInstrumentMapper::toDto);
    }

    /**
     * Delete the fundingInstrument by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete FundingInstrument : {}", id);
        fundingInstrumentRepository.deleteById(id);
        fundingInstrumentSearchRepository.deleteById(id);
    }

    /**
     * Search for the fundingInstrument corresponding to the query.
     *
     * @param query the query of the search
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<FundingInstrumentDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of FundingInstruments for query {}", query);
        return fundingInstrumentSearchRepository.search(queryStringQuery(query), pageable)
            .map(fundingInstrumentMapper::toDto);
    }
}
