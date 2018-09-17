package com.bytatech.ayoos.payment.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.bytatech.ayoos.payment.service.CreditCardService;
import com.bytatech.ayoos.payment.web.rest.errors.BadRequestAlertException;
import com.bytatech.ayoos.payment.web.rest.util.HeaderUtil;
import com.bytatech.ayoos.payment.web.rest.util.PaginationUtil;
import com.bytatech.ayoos.payment.service.dto.CreditCardDTO;
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
 * REST controller for managing CreditCard.
 */
@RestController
@RequestMapping("/api")
public class CreditCardResource {

    private final Logger log = LoggerFactory.getLogger(CreditCardResource.class);

    private static final String ENTITY_NAME = "paymentCreditCard";

    private final CreditCardService creditCardService;

    public CreditCardResource(CreditCardService creditCardService) {
        this.creditCardService = creditCardService;
    }

    /**
     * POST  /credit-cards : Create a new creditCard.
     *
     * @param creditCardDTO the creditCardDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new creditCardDTO, or with status 400 (Bad Request) if the creditCard has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/credit-cards")
    @Timed
    public ResponseEntity<CreditCardDTO> createCreditCard(@RequestBody CreditCardDTO creditCardDTO) throws URISyntaxException {
        log.debug("REST request to save CreditCard : {}", creditCardDTO);
        if (creditCardDTO.getId() != null) {
            throw new BadRequestAlertException("A new creditCard cannot already have an ID", ENTITY_NAME, "idexists");
        }
        CreditCardDTO result = creditCardService.save(creditCardDTO);
        return ResponseEntity.created(new URI("/api/credit-cards/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /credit-cards : Updates an existing creditCard.
     *
     * @param creditCardDTO the creditCardDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated creditCardDTO,
     * or with status 400 (Bad Request) if the creditCardDTO is not valid,
     * or with status 500 (Internal Server Error) if the creditCardDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/credit-cards")
    @Timed
    public ResponseEntity<CreditCardDTO> updateCreditCard(@RequestBody CreditCardDTO creditCardDTO) throws URISyntaxException {
        log.debug("REST request to update CreditCard : {}", creditCardDTO);
        if (creditCardDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        CreditCardDTO result = creditCardService.save(creditCardDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, creditCardDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /credit-cards : get all the creditCards.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of creditCards in body
     */
    @GetMapping("/credit-cards")
    @Timed
    public ResponseEntity<List<CreditCardDTO>> getAllCreditCards(Pageable pageable) {
        log.debug("REST request to get a page of CreditCards");
        Page<CreditCardDTO> page = creditCardService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/credit-cards");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /credit-cards/:id : get the "id" creditCard.
     *
     * @param id the id of the creditCardDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the creditCardDTO, or with status 404 (Not Found)
     */
    @GetMapping("/credit-cards/{id}")
    @Timed
    public ResponseEntity<CreditCardDTO> getCreditCard(@PathVariable Long id) {
        log.debug("REST request to get CreditCard : {}", id);
        Optional<CreditCardDTO> creditCardDTO = creditCardService.findOne(id);
        return ResponseUtil.wrapOrNotFound(creditCardDTO);
    }

    /**
     * DELETE  /credit-cards/:id : delete the "id" creditCard.
     *
     * @param id the id of the creditCardDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/credit-cards/{id}")
    @Timed
    public ResponseEntity<Void> deleteCreditCard(@PathVariable Long id) {
        log.debug("REST request to delete CreditCard : {}", id);
        creditCardService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/credit-cards?query=:query : search for the creditCard corresponding
     * to the query.
     *
     * @param query the query of the creditCard search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/credit-cards")
    @Timed
    public ResponseEntity<List<CreditCardDTO>> searchCreditCards(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of CreditCards for query {}", query);
        Page<CreditCardDTO> page = creditCardService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/credit-cards");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

}
