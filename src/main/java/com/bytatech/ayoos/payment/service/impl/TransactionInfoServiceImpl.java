package com.bytatech.ayoos.payment.service.impl;

import com.bytatech.ayoos.payment.service.TransactionInfoService;
import com.bytatech.ayoos.payment.domain.TransactionInfo;
import com.bytatech.ayoos.payment.repository.TransactionInfoRepository;
import com.bytatech.ayoos.payment.repository.search.TransactionInfoSearchRepository;
import com.bytatech.ayoos.payment.service.dto.TransactionInfoDTO;
import com.bytatech.ayoos.payment.service.mapper.TransactionInfoMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.Optional;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing TransactionInfo.
 */
@Service
@Transactional
public class TransactionInfoServiceImpl implements TransactionInfoService {

    private final Logger log = LoggerFactory.getLogger(TransactionInfoServiceImpl.class);

    private final TransactionInfoRepository transactionInfoRepository;

    private final TransactionInfoMapper transactionInfoMapper;

    private final TransactionInfoSearchRepository transactionInfoSearchRepository;

    public TransactionInfoServiceImpl(TransactionInfoRepository transactionInfoRepository, TransactionInfoMapper transactionInfoMapper, TransactionInfoSearchRepository transactionInfoSearchRepository) {
        this.transactionInfoRepository = transactionInfoRepository;
        this.transactionInfoMapper = transactionInfoMapper;
        this.transactionInfoSearchRepository = transactionInfoSearchRepository;
    }

    /**
     * Save a transactionInfo.
     *
     * @param transactionInfoDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public TransactionInfoDTO save(TransactionInfoDTO transactionInfoDTO) {
        log.debug("Request to save TransactionInfo : {}", transactionInfoDTO);
        TransactionInfo transactionInfo = transactionInfoMapper.toEntity(transactionInfoDTO);
        transactionInfo = transactionInfoRepository.save(transactionInfo);
        TransactionInfoDTO result = transactionInfoMapper.toDto(transactionInfo);
        transactionInfoSearchRepository.save(transactionInfo);
        return result;
    }

    /**
     * Get all the transactionInfos.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<TransactionInfoDTO> findAll(Pageable pageable) {
        log.debug("Request to get all TransactionInfos");
        return transactionInfoRepository.findAll(pageable)
            .map(transactionInfoMapper::toDto);
    }


    /**
     * Get one transactionInfo by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<TransactionInfoDTO> findOne(Long id) {
        log.debug("Request to get TransactionInfo : {}", id);
        return transactionInfoRepository.findById(id)
            .map(transactionInfoMapper::toDto);
    }

    /**
     * Delete the transactionInfo by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete TransactionInfo : {}", id);
        transactionInfoRepository.deleteById(id);
        transactionInfoSearchRepository.deleteById(id);
    }

    /**
     * Search for the transactionInfo corresponding to the query.
     *
     * @param query the query of the search
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<TransactionInfoDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of TransactionInfos for query {}", query);
        return transactionInfoSearchRepository.search(queryStringQuery(query), pageable)
            .map(transactionInfoMapper::toDto);
    }
}
