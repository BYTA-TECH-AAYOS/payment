package com.bytatech.ayoos.payment.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.bytatech.ayoos.payment.service.PayerService;
import com.bytatech.ayoos.payment.web.rest.errors.BadRequestAlertException;
import com.bytatech.ayoos.payment.web.rest.util.HeaderUtil;
import com.bytatech.ayoos.payment.web.rest.util.PaginationUtil;
import com.bytatech.ayoos.payment.service.dto.PayerDTO;
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
 * REST controller for managing Payer.
 */
@RestController
@RequestMapping("/api")
public class PayerResource {

    private final Logger log = LoggerFactory.getLogger(PayerResource.class);

    private static final String ENTITY_NAME = "paymentPayer";

    private final PayerService payerService;

    public PayerResource(PayerService payerService) {
        this.payerService = payerService;
    }

    /**
     * POST  /payers : Create a new payer.
     *
     * @param payerDTO the payerDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new payerDTO, or with status 400 (Bad Request) if the payer has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/payers")
    @Timed
    public ResponseEntity<PayerDTO> createPayer(@RequestBody PayerDTO payerDTO) throws URISyntaxException {
        log.debug("REST request to save Payer : {}", payerDTO);
        if (payerDTO.getId() != null) {
            throw new BadRequestAlertException("A new payer cannot already have an ID", ENTITY_NAME, "idexists");
        }
        PayerDTO result = payerService.save(payerDTO);
        return ResponseEntity.created(new URI("/api/payers/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /payers : Updates an existing payer.
     *
     * @param payerDTO the payerDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated payerDTO,
     * or with status 400 (Bad Request) if the payerDTO is not valid,
     * or with status 500 (Internal Server Error) if the payerDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/payers")
    @Timed
    public ResponseEntity<PayerDTO> updatePayer(@RequestBody PayerDTO payerDTO) throws URISyntaxException {
        log.debug("REST request to update Payer : {}", payerDTO);
        if (payerDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        PayerDTO result = payerService.save(payerDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, payerDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /payers : get all the payers.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of payers in body
     */
    @GetMapping("/payers")
    @Timed
    public ResponseEntity<List<PayerDTO>> getAllPayers(Pageable pageable) {
        log.debug("REST request to get a page of Payers");
        Page<PayerDTO> page = payerService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/payers");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /payers/:id : get the "id" payer.
     *
     * @param id the id of the payerDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the payerDTO, or with status 404 (Not Found)
     */
    @GetMapping("/payers/{id}")
    @Timed
    public ResponseEntity<PayerDTO> getPayer(@PathVariable Long id) {
        log.debug("REST request to get Payer : {}", id);
        Optional<PayerDTO> payerDTO = payerService.findOne(id);
        return ResponseUtil.wrapOrNotFound(payerDTO);
    }

    /**
     * DELETE  /payers/:id : delete the "id" payer.
     *
     * @param id the id of the payerDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/payers/{id}")
    @Timed
    public ResponseEntity<Void> deletePayer(@PathVariable Long id) {
        log.debug("REST request to delete Payer : {}", id);
        payerService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/payers?query=:query : search for the payer corresponding
     * to the query.
     *
     * @param query the query of the payer search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/payers")
    @Timed
    public ResponseEntity<List<PayerDTO>> searchPayers(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of Payers for query {}", query);
        Page<PayerDTO> page = payerService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/payers");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

}
