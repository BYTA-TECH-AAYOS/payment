package com.bytatech.ayoos.payment.service.impl;

import com.bytatech.ayoos.payment.service.RelatedTransactionsService;
import com.bytatech.ayoos.payment.domain.RelatedTransactions;
import com.bytatech.ayoos.payment.repository.RelatedTransactionsRepository;
import com.bytatech.ayoos.payment.repository.search.RelatedTransactionsSearchRepository;
import com.bytatech.ayoos.payment.service.dto.RelatedTransactionsDTO;
import com.bytatech.ayoos.payment.service.mapper.RelatedTransactionsMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.Optional;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing RelatedTransactions.
 */
@Service
@Transactional
public class RelatedTransactionsServiceImpl implements RelatedTransactionsService {

    private final Logger log = LoggerFactory.getLogger(RelatedTransactionsServiceImpl.class);

    private final RelatedTransactionsRepository relatedTransactionsRepository;

    private final RelatedTransactionsMapper relatedTransactionsMapper;

    private final RelatedTransactionsSearchRepository relatedTransactionsSearchRepository;

    public RelatedTransactionsServiceImpl(RelatedTransactionsRepository relatedTransactionsRepository, RelatedTransactionsMapper relatedTransactionsMapper, RelatedTransactionsSearchRepository relatedTransactionsSearchRepository) {
        this.relatedTransactionsRepository = relatedTransactionsRepository;
        this.relatedTransactionsMapper = relatedTransactionsMapper;
        this.relatedTransactionsSearchRepository = relatedTransactionsSearchRepository;
    }

    /**
     * Save a relatedTransactions.
     *
     * @param relatedTransactionsDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public RelatedTransactionsDTO save(RelatedTransactionsDTO relatedTransactionsDTO) {
        log.debug("Request to save RelatedTransactions : {}", relatedTransactionsDTO);
        RelatedTransactions relatedTransactions = relatedTransactionsMapper.toEntity(relatedTransactionsDTO);
        relatedTransactions = relatedTransactionsRepository.save(relatedTransactions);
        RelatedTransactionsDTO result = relatedTransactionsMapper.toDto(relatedTransactions);
        relatedTransactionsSearchRepository.save(relatedTransactions);
        return result;
    }

    /**
     * Get all the relatedTransactions.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<RelatedTransactionsDTO> findAll(Pageable pageable) {
        log.debug("Request to get all RelatedTransactions");
        return relatedTransactionsRepository.findAll(pageable)
            .map(relatedTransactionsMapper::toDto);
    }


    /**
     * Get one relatedTransactions by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<RelatedTransactionsDTO> findOne(Long id) {
        log.debug("Request to get RelatedTransactions : {}", id);
        return relatedTransactionsRepository.findById(id)
            .map(relatedTransactionsMapper::toDto);
    }

    /**
     * Delete the relatedTransactions by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete RelatedTransactions : {}", id);
        relatedTransactionsRepository.deleteById(id);
        relatedTransactionsSearchRepository.deleteById(id);
    }

    /**
     * Search for the relatedTransactions corresponding to the query.
     *
     * @param query the query of the search
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<RelatedTransactionsDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of RelatedTransactions for query {}", query);
        return relatedTransactionsSearchRepository.search(queryStringQuery(query), pageable)
            .map(relatedTransactionsMapper::toDto);
    }
}
