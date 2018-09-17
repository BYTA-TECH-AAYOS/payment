package com.bytatech.ayoos.payment.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.bytatech.ayoos.payment.service.TransactionInfoService;
import com.bytatech.ayoos.payment.web.rest.errors.BadRequestAlertException;
import com.bytatech.ayoos.payment.web.rest.util.HeaderUtil;
import com.bytatech.ayoos.payment.web.rest.util.PaginationUtil;
import com.bytatech.ayoos.payment.service.dto.TransactionInfoDTO;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing TransactionInfo.
 */
@RestController
@RequestMapping("/api")
public class TransactionInfoResource {

    private final Logger log = LoggerFactory.getLogger(TransactionInfoResource.class);

    private static final String ENTITY_NAME = "paymentTransactionInfo";

    private final TransactionInfoService transactionInfoService;

    public TransactionInfoResource(TransactionInfoService transactionInfoService) {
        this.transactionInfoService = transactionInfoService;
    }

    /**
     * POST  /transaction-infos : Create a new transactionInfo.
     *
     * @param transactionInfoDTO the transactionInfoDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new transactionInfoDTO, or with status 400 (Bad Request) if the transactionInfo has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/transaction-infos")
    @Timed
    public ResponseEntity<TransactionInfoDTO> createTransactionInfo(@RequestBody TransactionInfoDTO transactionInfoDTO) throws URISyntaxException {
        log.debug("REST request to save TransactionInfo : {}", transactionInfoDTO);
        if (transactionInfoDTO.getId() != null) {
            throw new BadRequestAlertException("A new transactionInfo cannot already have an ID", ENTITY_NAME, "idexists");
        }
        TransactionInfoDTO result = transactionInfoService.save(transactionInfoDTO);
        return ResponseEntity.created(new URI("/api/transaction-infos/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /transaction-infos : Updates an existing transactionInfo.
     *
     * @param transactionInfoDTO the transactionInfoDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated transactionInfoDTO,
     * or with status 400 (Bad Request) if the transactionInfoDTO is not valid,
     * or with status 500 (Internal Server Error) if the transactionInfoDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/transaction-infos")
    @Timed
    public ResponseEntity<TransactionInfoDTO> updateTransactionInfo(@RequestBody TransactionInfoDTO transactionInfoDTO) throws URISyntaxException {
        log.debug("REST request to update TransactionInfo : {}", transactionInfoDTO);
        if (transactionInfoDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        TransactionInfoDTO result = transactionInfoService.save(transactionInfoDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, transactionInfoDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /transaction-infos : get all the transactionInfos.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of transactionInfos in body
     */
    @GetMapping("/transaction-infos")
    @Timed
    public ResponseEntity<List<TransactionInfoDTO>> getAllTransactionInfos(Pageable pageable) {
        log.debug("REST request to get a page of TransactionInfos");
        Page<TransactionInfoDTO> page = transactionInfoService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/transaction-infos");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /transaction-infos/:id : get the "id" transactionInfo.
     *
     * @param id the id of the transactionInfoDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the transactionInfoDTO, or with status 404 (Not Found)
     */
    @GetMapping("/transaction-infos/{id}")
    @Timed
    public ResponseEntity<TransactionInfoDTO> getTransactionInfo(@PathVariable Long id) {
        log.debug("REST request to get TransactionInfo : {}", id);
        Optional<TransactionInfoDTO> transactionInfoDTO = transactionInfoService.findOne(id);
        return ResponseUtil.wrapOrNotFound(transactionInfoDTO);
    }

    /**
     * DELETE  /transaction-infos/:id : delete the "id" transactionInfo.
     *
     * @param id the id of the transactionInfoDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/transaction-infos/{id}")
    @Timed
    public ResponseEntity<Void> deleteTransactionInfo(@PathVariable Long id) {
        log.debug("REST request to delete TransactionInfo : {}", id);
        transactionInfoService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/transaction-infos?query=:query : search for the transactionInfo corresponding
     * to the query.
     *
     * @param query the query of the transactionInfo search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/transaction-infos")
    @Timed
    public ResponseEntity<List<TransactionInfoDTO>> searchTransactionInfos(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of TransactionInfos for query {}", query);
        Page<TransactionInfoDTO> page = transactionInfoService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/transaction-infos");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

}
