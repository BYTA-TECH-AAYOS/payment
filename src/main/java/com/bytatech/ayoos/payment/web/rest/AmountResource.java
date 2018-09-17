package com.bytatech.ayoos.payment.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.bytatech.ayoos.payment.service.AmountService;
import com.bytatech.ayoos.payment.web.rest.errors.BadRequestAlertException;
import com.bytatech.ayoos.payment.web.rest.util.HeaderUtil;
import com.bytatech.ayoos.payment.web.rest.util.PaginationUtil;
import com.bytatech.ayoos.payment.service.dto.AmountDTO;
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
 * REST controller for managing Amount.
 */
@RestController
@RequestMapping("/api")
public class AmountResource {

    private final Logger log = LoggerFactory.getLogger(AmountResource.class);

    private static final String ENTITY_NAME = "paymentAmount";

    private final AmountService amountService;

    public AmountResource(AmountService amountService) {
        this.amountService = amountService;
    }

    /**
     * POST  /amounts : Create a new amount.
     *
     * @param amountDTO the amountDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new amountDTO, or with status 400 (Bad Request) if the amount has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/amounts")
    @Timed
    public ResponseEntity<AmountDTO> createAmount(@RequestBody AmountDTO amountDTO) throws URISyntaxException {
        log.debug("REST request to save Amount : {}", amountDTO);
        if (amountDTO.getId() != null) {
            throw new BadRequestAlertException("A new amount cannot already have an ID", ENTITY_NAME, "idexists");
        }
        AmountDTO result = amountService.save(amountDTO);
        return ResponseEntity.created(new URI("/api/amounts/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /amounts : Updates an existing amount.
     *
     * @param amountDTO the amountDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated amountDTO,
     * or with status 400 (Bad Request) if the amountDTO is not valid,
     * or with status 500 (Internal Server Error) if the amountDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/amounts")
    @Timed
    public ResponseEntity<AmountDTO> updateAmount(@RequestBody AmountDTO amountDTO) throws URISyntaxException {
        log.debug("REST request to update Amount : {}", amountDTO);
        if (amountDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        AmountDTO result = amountService.save(amountDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, amountDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /amounts : get all the amounts.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of amounts in body
     */
    @GetMapping("/amounts")
    @Timed
    public ResponseEntity<List<AmountDTO>> getAllAmounts(Pageable pageable) {
        log.debug("REST request to get a page of Amounts");
        Page<AmountDTO> page = amountService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/amounts");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /amounts/:id : get the "id" amount.
     *
     * @param id the id of the amountDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the amountDTO, or with status 404 (Not Found)
     */
    @GetMapping("/amounts/{id}")
    @Timed
    public ResponseEntity<AmountDTO> getAmount(@PathVariable Long id) {
        log.debug("REST request to get Amount : {}", id);
        Optional<AmountDTO> amountDTO = amountService.findOne(id);
        return ResponseUtil.wrapOrNotFound(amountDTO);
    }

    /**
     * DELETE  /amounts/:id : delete the "id" amount.
     *
     * @param id the id of the amountDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/amounts/{id}")
    @Timed
    public ResponseEntity<Void> deleteAmount(@PathVariable Long id) {
        log.debug("REST request to delete Amount : {}", id);
        amountService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/amounts?query=:query : search for the amount corresponding
     * to the query.
     *
     * @param query the query of the amount search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/amounts")
    @Timed
    public ResponseEntity<List<AmountDTO>> searchAmounts(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of Amounts for query {}", query);
        Page<AmountDTO> page = amountService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/amounts");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

}
