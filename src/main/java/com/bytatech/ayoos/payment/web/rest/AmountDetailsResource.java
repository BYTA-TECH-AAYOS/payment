package com.bytatech.ayoos.payment.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.bytatech.ayoos.payment.service.AmountDetailsService;
import com.bytatech.ayoos.payment.web.rest.errors.BadRequestAlertException;
import com.bytatech.ayoos.payment.web.rest.util.HeaderUtil;
import com.bytatech.ayoos.payment.web.rest.util.PaginationUtil;
import com.bytatech.ayoos.payment.service.dto.AmountDetailsDTO;
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
 * REST controller for managing AmountDetails.
 */
@RestController
@RequestMapping("/api")
public class AmountDetailsResource {

    private final Logger log = LoggerFactory.getLogger(AmountDetailsResource.class);

    private static final String ENTITY_NAME = "paymentAmountDetails";

    private final AmountDetailsService amountDetailsService;

    public AmountDetailsResource(AmountDetailsService amountDetailsService) {
        this.amountDetailsService = amountDetailsService;
    }

    /**
     * POST  /amount-details : Create a new amountDetails.
     *
     * @param amountDetailsDTO the amountDetailsDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new amountDetailsDTO, or with status 400 (Bad Request) if the amountDetails has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/amount-details")
    @Timed
    public ResponseEntity<AmountDetailsDTO> createAmountDetails(@RequestBody AmountDetailsDTO amountDetailsDTO) throws URISyntaxException {
        log.debug("REST request to save AmountDetails : {}", amountDetailsDTO);
        if (amountDetailsDTO.getId() != null) {
            throw new BadRequestAlertException("A new amountDetails cannot already have an ID", ENTITY_NAME, "idexists");
        }
        AmountDetailsDTO result = amountDetailsService.save(amountDetailsDTO);
        return ResponseEntity.created(new URI("/api/amount-details/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /amount-details : Updates an existing amountDetails.
     *
     * @param amountDetailsDTO the amountDetailsDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated amountDetailsDTO,
     * or with status 400 (Bad Request) if the amountDetailsDTO is not valid,
     * or with status 500 (Internal Server Error) if the amountDetailsDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/amount-details")
    @Timed
    public ResponseEntity<AmountDetailsDTO> updateAmountDetails(@RequestBody AmountDetailsDTO amountDetailsDTO) throws URISyntaxException {
        log.debug("REST request to update AmountDetails : {}", amountDetailsDTO);
        if (amountDetailsDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        AmountDetailsDTO result = amountDetailsService.save(amountDetailsDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, amountDetailsDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /amount-details : get all the amountDetails.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of amountDetails in body
     */
    @GetMapping("/amount-details")
    @Timed
    public ResponseEntity<List<AmountDetailsDTO>> getAllAmountDetails(Pageable pageable) {
        log.debug("REST request to get a page of AmountDetails");
        Page<AmountDetailsDTO> page = amountDetailsService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/amount-details");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /amount-details/:id : get the "id" amountDetails.
     *
     * @param id the id of the amountDetailsDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the amountDetailsDTO, or with status 404 (Not Found)
     */
    @GetMapping("/amount-details/{id}")
    @Timed
    public ResponseEntity<AmountDetailsDTO> getAmountDetails(@PathVariable Long id) {
        log.debug("REST request to get AmountDetails : {}", id);
        Optional<AmountDetailsDTO> amountDetailsDTO = amountDetailsService.findOne(id);
        return ResponseUtil.wrapOrNotFound(amountDetailsDTO);
    }

    /**
     * DELETE  /amount-details/:id : delete the "id" amountDetails.
     *
     * @param id the id of the amountDetailsDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/amount-details/{id}")
    @Timed
    public ResponseEntity<Void> deleteAmountDetails(@PathVariable Long id) {
        log.debug("REST request to delete AmountDetails : {}", id);
        amountDetailsService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/amount-details?query=:query : search for the amountDetails corresponding
     * to the query.
     *
     * @param query the query of the amountDetails search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/amount-details")
    @Timed
    public ResponseEntity<List<AmountDetailsDTO>> searchAmountDetails(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of AmountDetails for query {}", query);
        Page<AmountDetailsDTO> page = amountDetailsService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/amount-details");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

}
