package com.bytatech.ayoos.payment.web.rest;

import com.bytatech.ayoos.payment.PaymentApp;

import com.bytatech.ayoos.payment.domain.FundingInstrument;
import com.bytatech.ayoos.payment.repository.FundingInstrumentRepository;
import com.bytatech.ayoos.payment.repository.search.FundingInstrumentSearchRepository;
import com.bytatech.ayoos.payment.service.FundingInstrumentService;
import com.bytatech.ayoos.payment.service.dto.FundingInstrumentDTO;
import com.bytatech.ayoos.payment.service.mapper.FundingInstrumentMapper;
import com.bytatech.ayoos.payment.web.rest.errors.ExceptionTranslator;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.Collections;
import java.util.List;


import static com.bytatech.ayoos.payment.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the FundingInstrumentResource REST controller.
 *
 * @see FundingInstrumentResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = PaymentApp.class)
public class FundingInstrumentResourceIntTest {

    @Autowired
    private FundingInstrumentRepository fundingInstrumentRepository;

    @Autowired
    private FundingInstrumentMapper fundingInstrumentMapper;
    
    @Autowired
    private FundingInstrumentService fundingInstrumentService;

    /**
     * This repository is mocked in the com.bytatech.ayoos.payment.repository.search test package.
     *
     * @see com.bytatech.ayoos.payment.repository.search.FundingInstrumentSearchRepositoryMockConfiguration
     */
    @Autowired
    private FundingInstrumentSearchRepository mockFundingInstrumentSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restFundingInstrumentMockMvc;

    private FundingInstrument fundingInstrument;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final FundingInstrumentResource fundingInstrumentResource = new FundingInstrumentResource(fundingInstrumentService);
        this.restFundingInstrumentMockMvc = MockMvcBuilders.standaloneSetup(fundingInstrumentResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static FundingInstrument createEntity(EntityManager em) {
        FundingInstrument fundingInstrument = new FundingInstrument();
        return fundingInstrument;
    }

    @Before
    public void initTest() {
        fundingInstrument = createEntity(em);
    }

    @Test
    @Transactional
    public void createFundingInstrument() throws Exception {
        int databaseSizeBeforeCreate = fundingInstrumentRepository.findAll().size();

        // Create the FundingInstrument
        FundingInstrumentDTO fundingInstrumentDTO = fundingInstrumentMapper.toDto(fundingInstrument);
        restFundingInstrumentMockMvc.perform(post("/api/funding-instruments")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(fundingInstrumentDTO)))
            .andExpect(status().isCreated());

        // Validate the FundingInstrument in the database
        List<FundingInstrument> fundingInstrumentList = fundingInstrumentRepository.findAll();
        assertThat(fundingInstrumentList).hasSize(databaseSizeBeforeCreate + 1);
        FundingInstrument testFundingInstrument = fundingInstrumentList.get(fundingInstrumentList.size() - 1);

        // Validate the FundingInstrument in Elasticsearch
        verify(mockFundingInstrumentSearchRepository, times(1)).save(testFundingInstrument);
    }

    @Test
    @Transactional
    public void createFundingInstrumentWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = fundingInstrumentRepository.findAll().size();

        // Create the FundingInstrument with an existing ID
        fundingInstrument.setId(1L);
        FundingInstrumentDTO fundingInstrumentDTO = fundingInstrumentMapper.toDto(fundingInstrument);

        // An entity with an existing ID cannot be created, so this API call must fail
        restFundingInstrumentMockMvc.perform(post("/api/funding-instruments")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(fundingInstrumentDTO)))
            .andExpect(status().isBadRequest());

        // Validate the FundingInstrument in the database
        List<FundingInstrument> fundingInstrumentList = fundingInstrumentRepository.findAll();
        assertThat(fundingInstrumentList).hasSize(databaseSizeBeforeCreate);

        // Validate the FundingInstrument in Elasticsearch
        verify(mockFundingInstrumentSearchRepository, times(0)).save(fundingInstrument);
    }

    @Test
    @Transactional
    public void getAllFundingInstruments() throws Exception {
        // Initialize the database
        fundingInstrumentRepository.saveAndFlush(fundingInstrument);

        // Get all the fundingInstrumentList
        restFundingInstrumentMockMvc.perform(get("/api/funding-instruments?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(fundingInstrument.getId().intValue())));
    }
    
    @Test
    @Transactional
    public void getFundingInstrument() throws Exception {
        // Initialize the database
        fundingInstrumentRepository.saveAndFlush(fundingInstrument);

        // Get the fundingInstrument
        restFundingInstrumentMockMvc.perform(get("/api/funding-instruments/{id}", fundingInstrument.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(fundingInstrument.getId().intValue()));
    }

    @Test
    @Transactional
    public void getNonExistingFundingInstrument() throws Exception {
        // Get the fundingInstrument
        restFundingInstrumentMockMvc.perform(get("/api/funding-instruments/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateFundingInstrument() throws Exception {
        // Initialize the database
        fundingInstrumentRepository.saveAndFlush(fundingInstrument);

        int databaseSizeBeforeUpdate = fundingInstrumentRepository.findAll().size();

        // Update the fundingInstrument
        FundingInstrument updatedFundingInstrument = fundingInstrumentRepository.findById(fundingInstrument.getId()).get();
        // Disconnect from session so that the updates on updatedFundingInstrument are not directly saved in db
        em.detach(updatedFundingInstrument);
        FundingInstrumentDTO fundingInstrumentDTO = fundingInstrumentMapper.toDto(updatedFundingInstrument);

        restFundingInstrumentMockMvc.perform(put("/api/funding-instruments")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(fundingInstrumentDTO)))
            .andExpect(status().isOk());

        // Validate the FundingInstrument in the database
        List<FundingInstrument> fundingInstrumentList = fundingInstrumentRepository.findAll();
        assertThat(fundingInstrumentList).hasSize(databaseSizeBeforeUpdate);
        FundingInstrument testFundingInstrument = fundingInstrumentList.get(fundingInstrumentList.size() - 1);

        // Validate the FundingInstrument in Elasticsearch
        verify(mockFundingInstrumentSearchRepository, times(1)).save(testFundingInstrument);
    }

    @Test
    @Transactional
    public void updateNonExistingFundingInstrument() throws Exception {
        int databaseSizeBeforeUpdate = fundingInstrumentRepository.findAll().size();

        // Create the FundingInstrument
        FundingInstrumentDTO fundingInstrumentDTO = fundingInstrumentMapper.toDto(fundingInstrument);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFundingInstrumentMockMvc.perform(put("/api/funding-instruments")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(fundingInstrumentDTO)))
            .andExpect(status().isBadRequest());

        // Validate the FundingInstrument in the database
        List<FundingInstrument> fundingInstrumentList = fundingInstrumentRepository.findAll();
        assertThat(fundingInstrumentList).hasSize(databaseSizeBeforeUpdate);

        // Validate the FundingInstrument in Elasticsearch
        verify(mockFundingInstrumentSearchRepository, times(0)).save(fundingInstrument);
    }

    @Test
    @Transactional
    public void deleteFundingInstrument() throws Exception {
        // Initialize the database
        fundingInstrumentRepository.saveAndFlush(fundingInstrument);

        int databaseSizeBeforeDelete = fundingInstrumentRepository.findAll().size();

        // Get the fundingInstrument
        restFundingInstrumentMockMvc.perform(delete("/api/funding-instruments/{id}", fundingInstrument.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<FundingInstrument> fundingInstrumentList = fundingInstrumentRepository.findAll();
        assertThat(fundingInstrumentList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the FundingInstrument in Elasticsearch
        verify(mockFundingInstrumentSearchRepository, times(1)).deleteById(fundingInstrument.getId());
    }

    @Test
    @Transactional
    public void searchFundingInstrument() throws Exception {
        // Initialize the database
        fundingInstrumentRepository.saveAndFlush(fundingInstrument);
        when(mockFundingInstrumentSearchRepository.search(queryStringQuery("id:" + fundingInstrument.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(fundingInstrument), PageRequest.of(0, 1), 1));
        // Search the fundingInstrument
        restFundingInstrumentMockMvc.perform(get("/api/_search/funding-instruments?query=id:" + fundingInstrument.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(fundingInstrument.getId().intValue())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(FundingInstrument.class);
        FundingInstrument fundingInstrument1 = new FundingInstrument();
        fundingInstrument1.setId(1L);
        FundingInstrument fundingInstrument2 = new FundingInstrument();
        fundingInstrument2.setId(fundingInstrument1.getId());
        assertThat(fundingInstrument1).isEqualTo(fundingInstrument2);
        fundingInstrument2.setId(2L);
        assertThat(fundingInstrument1).isNotEqualTo(fundingInstrument2);
        fundingInstrument1.setId(null);
        assertThat(fundingInstrument1).isNotEqualTo(fundingInstrument2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(FundingInstrumentDTO.class);
        FundingInstrumentDTO fundingInstrumentDTO1 = new FundingInstrumentDTO();
        fundingInstrumentDTO1.setId(1L);
        FundingInstrumentDTO fundingInstrumentDTO2 = new FundingInstrumentDTO();
        assertThat(fundingInstrumentDTO1).isNotEqualTo(fundingInstrumentDTO2);
        fundingInstrumentDTO2.setId(fundingInstrumentDTO1.getId());
        assertThat(fundingInstrumentDTO1).isEqualTo(fundingInstrumentDTO2);
        fundingInstrumentDTO2.setId(2L);
        assertThat(fundingInstrumentDTO1).isNotEqualTo(fundingInstrumentDTO2);
        fundingInstrumentDTO1.setId(null);
        assertThat(fundingInstrumentDTO1).isNotEqualTo(fundingInstrumentDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(fundingInstrumentMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(fundingInstrumentMapper.fromId(null)).isNull();
    }
}
