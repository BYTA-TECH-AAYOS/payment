package com.bytatech.ayoos.payment.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.bytatech.ayoos.payment.service.PayeeService;
import com.bytatech.ayoos.payment.web.rest.errors.BadRequestAlertException;
import com.bytatech.ayoos.payment.web.rest.util.HeaderUtil;
import com.bytatech.ayoos.payment.web.rest.util.PaginationUtil;
import com.bytatech.ayoos.payment.service.dto.PayeeDTO;
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
 * REST controller for managing Payee.
 */
@RestController
@RequestMapping("/api")
public class PayeeResource {

    private final Logger log = LoggerFactory.getLogger(PayeeResource.class);

    private static final String ENTITY_NAME = "paymentPayee";

    private final PayeeService payeeService;

    public PayeeResource(PayeeService payeeService) {
        this.payeeService = payeeService;
    }

    /**
     * POST  /payees : Create a new payee.
     *
     * @param payeeDTO the payeeDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new payeeDTO, or with status 400 (Bad Request) if the payee has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/payees")
    @Timed
    public ResponseEntity<PayeeDTO> createPayee(@RequestBody PayeeDTO payeeDTO) throws URISyntaxException {
        log.debug("REST request to save Payee : {}", payeeDTO);
        if (payeeDTO.getId() != null) {
            throw new BadRequestAlertException("A new payee cannot already have an ID", ENTITY_NAME, "idexists");
        }
        PayeeDTO result = payeeService.save(payeeDTO);
        return ResponseEntity.created(new URI("/api/payees/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /payees : Updates an existing payee.
     *
     * @param payeeDTO the payeeDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated payeeDTO,
     * or with status 400 (Bad Request) if the payeeDTO is not valid,
     * or with status 500 (Internal Server Error) if the payeeDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/payees")
    @Timed
    public ResponseEntity<PayeeDTO> updatePayee(@RequestBody PayeeDTO payeeDTO) throws URISyntaxException {
        log.debug("REST request to update Payee : {}", payeeDTO);
        if (payeeDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        PayeeDTO result = payeeService.save(payeeDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, payeeDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /payees : get all the payees.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of payees in body
     */
    @GetMapping("/payees")
    @Timed
    public ResponseEntity<List<PayeeDTO>> getAllPayees(Pageable pageable) {
        log.debug("REST request to get a page of Payees");
        Page<PayeeDTO> page = payeeService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/payees");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /payees/:id : get the "id" payee.
     *
     * @param id the id of the payeeDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the payeeDTO, or with status 404 (Not Found)
     */
    @GetMapping("/payees/{id}")
    @Timed
    public ResponseEntity<PayeeDTO> getPayee(@PathVariable Long id) {
        log.debug("REST request to get Payee : {}", id);
        Optional<PayeeDTO> payeeDTO = payeeService.findOne(id);
        return ResponseUtil.wrapOrNotFound(payeeDTO);
    }

    /**
     * DELETE  /payees/:id : delete the "id" payee.
     *
     * @param id the id of the payeeDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/payees/{id}")
    @Timed
    public ResponseEntity<Void> deletePayee(@PathVariable Long id) {
        log.debug("REST request to delete Payee : {}", id);
        payeeService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/payees?query=:query : search for the payee corresponding
     * to the query.
     *
     * @param query the query of the payee search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/payees")
    @Timed
    public ResponseEntity<List<PayeeDTO>> searchPayees(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of Payees for query {}", query);
        Page<PayeeDTO> page = payeeService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/payees");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

}
