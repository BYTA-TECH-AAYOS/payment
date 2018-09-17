package com.bytatech.ayoos.payment.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.bytatech.ayoos.payment.service.RelatedTransactionsService;
import com.bytatech.ayoos.payment.web.rest.errors.BadRequestAlertException;
import com.bytatech.ayoos.payment.web.rest.util.HeaderUtil;
import com.bytatech.ayoos.payment.web.rest.util.PaginationUtil;
import com.bytatech.ayoos.payment.service.dto.RelatedTransactionsDTO;
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
 * REST controller for managing RelatedTransactions.
 */
@RestController
@RequestMapping("/api")
public class RelatedTransactionsResource {

    private final Logger log = LoggerFactory.getLogger(RelatedTransactionsResource.class);

    private static final String ENTITY_NAME = "paymentRelatedTransactions";

    private final RelatedTransactionsService relatedTransactionsService;

    public RelatedTransactionsResource(RelatedTransactionsService relatedTransactionsService) {
        this.relatedTransactionsService = relatedTransactionsService;
    }

    /**
     * POST  /related-transactions : Create a new relatedTransactions.
     *
     * @param relatedTransactionsDTO the relatedTransactionsDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new relatedTransactionsDTO, or with status 400 (Bad Request) if the relatedTransactions has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/related-transactions")
    @Timed
    public ResponseEntity<RelatedTransactionsDTO> createRelatedTransactions(@RequestBody RelatedTransactionsDTO relatedTransactionsDTO) throws URISyntaxException {
        log.debug("REST request to save RelatedTransactions : {}", relatedTransactionsDTO);
        if (relatedTransactionsDTO.getId() != null) {
            throw new BadRequestAlertException("A new relatedTransactions cannot already have an ID", ENTITY_NAME, "idexists");
        }
        RelatedTransactionsDTO result = relatedTransactionsService.save(relatedTransactionsDTO);
        return ResponseEntity.created(new URI("/api/related-transactions/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /related-transactions : Updates an existing relatedTransactions.
     *
     * @param relatedTransactionsDTO the relatedTransactionsDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated relatedTransactionsDTO,
     * or with status 400 (Bad Request) if the relatedTransactionsDTO is not valid,
     * or with status 500 (Internal Server Error) if the relatedTransactionsDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/related-transactions")
    @Timed
    public ResponseEntity<RelatedTransactionsDTO> updateRelatedTransactions(@RequestBody RelatedTransactionsDTO relatedTransactionsDTO) throws URISyntaxException {
        log.debug("REST request to update RelatedTransactions : {}", relatedTransactionsDTO);
        if (relatedTransactionsDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        RelatedTransactionsDTO result = relatedTransactionsService.save(relatedTransactionsDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, relatedTransactionsDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /related-transactions : get all the relatedTransactions.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of relatedTransactions in body
     */
    @GetMapping("/related-transactions")
    @Timed
    public ResponseEntity<List<RelatedTransactionsDTO>> getAllRelatedTransactions(Pageable pageable) {
        log.debug("REST request to get a page of RelatedTransactions");
        Page<RelatedTransactionsDTO> page = relatedTransactionsService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/related-transactions");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /related-transactions/:id : get the "id" relatedTransactions.
     *
     * @param id the id of the relatedTransactionsDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the relatedTransactionsDTO, or with status 404 (Not Found)
     */
    @GetMapping("/related-transactions/{id}")
    @Timed
    public ResponseEntity<RelatedTransactionsDTO> getRelatedTransactions(@PathVariable Long id) {
        log.debug("REST request to get RelatedTransactions : {}", id);
        Optional<RelatedTransactionsDTO> relatedTransactionsDTO = relatedTransactionsService.findOne(id);
        return ResponseUtil.wrapOrNotFound(relatedTransactionsDTO);
    }

    /**
     * DELETE  /related-transactions/:id : delete the "id" relatedTransactions.
     *
     * @param id the id of the relatedTransactionsDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/related-transactions/{id}")
    @Timed
    public ResponseEntity<Void> deleteRelatedTransactions(@PathVariable Long id) {
        log.debug("REST request to delete RelatedTransactions : {}", id);
        relatedTransactionsService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/related-transactions?query=:query : search for the relatedTransactions corresponding
     * to the query.
     *
     * @param query the query of the relatedTransactions search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/related-transactions")
    @Timed
    public ResponseEntity<List<RelatedTransactionsDTO>> searchRelatedTransactions(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of RelatedTransactions for query {}", query);
        Page<RelatedTransactionsDTO> page = relatedTransactionsService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/related-transactions");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

}
