package com.bytatech.ayoos.payment.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.bytatech.ayoos.payment.service.FundingInstrumentService;
import com.bytatech.ayoos.payment.web.rest.errors.BadRequestAlertException;
import com.bytatech.ayoos.payment.web.rest.util.HeaderUtil;
import com.bytatech.ayoos.payment.web.rest.util.PaginationUtil;
import com.bytatech.ayoos.payment.service.dto.FundingInstrumentDTO;
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
 * REST controller for managing FundingInstrument.
 */
@RestController
@RequestMapping("/api")
public class FundingInstrumentResource {

    private final Logger log = LoggerFactory.getLogger(FundingInstrumentResource.class);

    private static final String ENTITY_NAME = "paymentFundingInstrument";

    private final FundingInstrumentService fundingInstrumentService;

    public FundingInstrumentResource(FundingInstrumentService fundingInstrumentService) {
        this.fundingInstrumentService = fundingInstrumentService;
    }

    /**
     * POST  /funding-instruments : Create a new fundingInstrument.
     *
     * @param fundingInstrumentDTO the fundingInstrumentDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new fundingInstrumentDTO, or with status 400 (Bad Request) if the fundingInstrument has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/funding-instruments")
    @Timed
    public ResponseEntity<FundingInstrumentDTO> createFundingInstrument(@RequestBody FundingInstrumentDTO fundingInstrumentDTO) throws URISyntaxException {
        log.debug("REST request to save FundingInstrument : {}", fundingInstrumentDTO);
        if (fundingInstrumentDTO.getId() != null) {
            throw new BadRequestAlertException("A new fundingInstrument cannot already have an ID", ENTITY_NAME, "idexists");
        }
        FundingInstrumentDTO result = fundingInstrumentService.save(fundingInstrumentDTO);
        return ResponseEntity.created(new URI("/api/funding-instruments/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /funding-instruments : Updates an existing fundingInstrument.
     *
     * @param fundingInstrumentDTO the fundingInstrumentDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated fundingInstrumentDTO,
     * or with status 400 (Bad Request) if the fundingInstrumentDTO is not valid,
     * or with status 500 (Internal Server Error) if the fundingInstrumentDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/funding-instruments")
    @Timed
    public ResponseEntity<FundingInstrumentDTO> updateFundingInstrument(@RequestBody FundingInstrumentDTO fundingInstrumentDTO) throws URISyntaxException {
        log.debug("REST request to update FundingInstrument : {}", fundingInstrumentDTO);
        if (fundingInstrumentDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        FundingInstrumentDTO result = fundingInstrumentService.save(fundingInstrumentDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, fundingInstrumentDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /funding-instruments : get all the fundingInstruments.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of fundingInstruments in body
     */
    @GetMapping("/funding-instruments")
    @Timed
    public ResponseEntity<List<FundingInstrumentDTO>> getAllFundingInstruments(Pageable pageable) {
        log.debug("REST request to get a page of FundingInstruments");
        Page<FundingInstrumentDTO> page = fundingInstrumentService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/funding-instruments");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /funding-instruments/:id : get the "id" fundingInstrument.
     *
     * @param id the id of the fundingInstrumentDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the fundingInstrumentDTO, or with status 404 (Not Found)
     */
    @GetMapping("/funding-instruments/{id}")
    @Timed
    public ResponseEntity<FundingInstrumentDTO> getFundingInstrument(@PathVariable Long id) {
        log.debug("REST request to get FundingInstrument : {}", id);
        Optional<FundingInstrumentDTO> fundingInstrumentDTO = fundingInstrumentService.findOne(id);
        return ResponseUtil.wrapOrNotFound(fundingInstrumentDTO);
    }

    /**
     * DELETE  /funding-instruments/:id : delete the "id" fundingInstrument.
     *
     * @param id the id of the fundingInstrumentDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/funding-instruments/{id}")
    @Timed
    public ResponseEntity<Void> deleteFundingInstrument(@PathVariable Long id) {
        log.debug("REST request to delete FundingInstrument : {}", id);
        fundingInstrumentService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/funding-instruments?query=:query : search for the fundingInstrument corresponding
     * to the query.
     *
     * @param query the query of the fundingInstrument search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/funding-instruments")
    @Timed
    public ResponseEntity<List<FundingInstrumentDTO>> searchFundingInstruments(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of FundingInstruments for query {}", query);
        Page<FundingInstrumentDTO> page = fundingInstrumentService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/funding-instruments");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

}
