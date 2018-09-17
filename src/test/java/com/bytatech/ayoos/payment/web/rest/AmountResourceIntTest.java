package com.bytatech.ayoos.payment.web.rest;

import com.bytatech.ayoos.payment.PaymentApp;

import com.bytatech.ayoos.payment.domain.Amount;
import com.bytatech.ayoos.payment.repository.AmountRepository;
import com.bytatech.ayoos.payment.repository.search.AmountSearchRepository;
import com.bytatech.ayoos.payment.service.AmountService;
import com.bytatech.ayoos.payment.service.dto.AmountDTO;
import com.bytatech.ayoos.payment.service.mapper.AmountMapper;
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
 * Test class for the AmountResource REST controller.
 *
 * @see AmountResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = PaymentApp.class)
public class AmountResourceIntTest {

    private static final Double DEFAULT_TOTAL = 1D;
    private static final Double UPDATED_TOTAL = 2D;

    private static final String DEFAULT_CURRENCY = "AAAAAAAAAA";
    private static final String UPDATED_CURRENCY = "BBBBBBBBBB";

    @Autowired
    private AmountRepository amountRepository;

    @Autowired
    private AmountMapper amountMapper;
    
    @Autowired
    private AmountService amountService;

    /**
     * This repository is mocked in the com.bytatech.ayoos.payment.repository.search test package.
     *
     * @see com.bytatech.ayoos.payment.repository.search.AmountSearchRepositoryMockConfiguration
     */
    @Autowired
    private AmountSearchRepository mockAmountSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restAmountMockMvc;

    private Amount amount;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final AmountResource amountResource = new AmountResource(amountService);
        this.restAmountMockMvc = MockMvcBuilders.standaloneSetup(amountResource)
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
    public static Amount createEntity(EntityManager em) {
        Amount amount = new Amount()
            .total(DEFAULT_TOTAL)
            .currency(DEFAULT_CURRENCY);
        return amount;
    }

    @Before
    public void initTest() {
        amount = createEntity(em);
    }

    @Test
    @Transactional
    public void createAmount() throws Exception {
        int databaseSizeBeforeCreate = amountRepository.findAll().size();

        // Create the Amount
        AmountDTO amountDTO = amountMapper.toDto(amount);
        restAmountMockMvc.perform(post("/api/amounts")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(amountDTO)))
            .andExpect(status().isCreated());

        // Validate the Amount in the database
        List<Amount> amountList = amountRepository.findAll();
        assertThat(amountList).hasSize(databaseSizeBeforeCreate + 1);
        Amount testAmount = amountList.get(amountList.size() - 1);
        assertThat(testAmount.getTotal()).isEqualTo(DEFAULT_TOTAL);
        assertThat(testAmount.getCurrency()).isEqualTo(DEFAULT_CURRENCY);

        // Validate the Amount in Elasticsearch
        verify(mockAmountSearchRepository, times(1)).save(testAmount);
    }

    @Test
    @Transactional
    public void createAmountWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = amountRepository.findAll().size();

        // Create the Amount with an existing ID
        amount.setId(1L);
        AmountDTO amountDTO = amountMapper.toDto(amount);

        // An entity with an existing ID cannot be created, so this API call must fail
        restAmountMockMvc.perform(post("/api/amounts")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(amountDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Amount in the database
        List<Amount> amountList = amountRepository.findAll();
        assertThat(amountList).hasSize(databaseSizeBeforeCreate);

        // Validate the Amount in Elasticsearch
        verify(mockAmountSearchRepository, times(0)).save(amount);
    }

    @Test
    @Transactional
    public void getAllAmounts() throws Exception {
        // Initialize the database
        amountRepository.saveAndFlush(amount);

        // Get all the amountList
        restAmountMockMvc.perform(get("/api/amounts?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(amount.getId().intValue())))
            .andExpect(jsonPath("$.[*].total").value(hasItem(DEFAULT_TOTAL.doubleValue())))
            .andExpect(jsonPath("$.[*].currency").value(hasItem(DEFAULT_CURRENCY.toString())));
    }
    
    @Test
    @Transactional
    public void getAmount() throws Exception {
        // Initialize the database
        amountRepository.saveAndFlush(amount);

        // Get the amount
        restAmountMockMvc.perform(get("/api/amounts/{id}", amount.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(amount.getId().intValue()))
            .andExpect(jsonPath("$.total").value(DEFAULT_TOTAL.doubleValue()))
            .andExpect(jsonPath("$.currency").value(DEFAULT_CURRENCY.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingAmount() throws Exception {
        // Get the amount
        restAmountMockMvc.perform(get("/api/amounts/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateAmount() throws Exception {
        // Initialize the database
        amountRepository.saveAndFlush(amount);

        int databaseSizeBeforeUpdate = amountRepository.findAll().size();

        // Update the amount
        Amount updatedAmount = amountRepository.findById(amount.getId()).get();
        // Disconnect from session so that the updates on updatedAmount are not directly saved in db
        em.detach(updatedAmount);
        updatedAmount
            .total(UPDATED_TOTAL)
            .currency(UPDATED_CURRENCY);
        AmountDTO amountDTO = amountMapper.toDto(updatedAmount);

        restAmountMockMvc.perform(put("/api/amounts")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(amountDTO)))
            .andExpect(status().isOk());

        // Validate the Amount in the database
        List<Amount> amountList = amountRepository.findAll();
        assertThat(amountList).hasSize(databaseSizeBeforeUpdate);
        Amount testAmount = amountList.get(amountList.size() - 1);
        assertThat(testAmount.getTotal()).isEqualTo(UPDATED_TOTAL);
        assertThat(testAmount.getCurrency()).isEqualTo(UPDATED_CURRENCY);

        // Validate the Amount in Elasticsearch
        verify(mockAmountSearchRepository, times(1)).save(testAmount);
    }

    @Test
    @Transactional
    public void updateNonExistingAmount() throws Exception {
        int databaseSizeBeforeUpdate = amountRepository.findAll().size();

        // Create the Amount
        AmountDTO amountDTO = amountMapper.toDto(amount);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAmountMockMvc.perform(put("/api/amounts")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(amountDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Amount in the database
        List<Amount> amountList = amountRepository.findAll();
        assertThat(amountList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Amount in Elasticsearch
        verify(mockAmountSearchRepository, times(0)).save(amount);
    }

    @Test
    @Transactional
    public void deleteAmount() throws Exception {
        // Initialize the database
        amountRepository.saveAndFlush(amount);

        int databaseSizeBeforeDelete = amountRepository.findAll().size();

        // Get the amount
        restAmountMockMvc.perform(delete("/api/amounts/{id}", amount.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Amount> amountList = amountRepository.findAll();
        assertThat(amountList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the Amount in Elasticsearch
        verify(mockAmountSearchRepository, times(1)).deleteById(amount.getId());
    }

    @Test
    @Transactional
    public void searchAmount() throws Exception {
        // Initialize the database
        amountRepository.saveAndFlush(amount);
        when(mockAmountSearchRepository.search(queryStringQuery("id:" + amount.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(amount), PageRequest.of(0, 1), 1));
        // Search the amount
        restAmountMockMvc.perform(get("/api/_search/amounts?query=id:" + amount.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(amount.getId().intValue())))
            .andExpect(jsonPath("$.[*].total").value(hasItem(DEFAULT_TOTAL.doubleValue())))
            .andExpect(jsonPath("$.[*].currency").value(hasItem(DEFAULT_CURRENCY.toString())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Amount.class);
        Amount amount1 = new Amount();
        amount1.setId(1L);
        Amount amount2 = new Amount();
        amount2.setId(amount1.getId());
        assertThat(amount1).isEqualTo(amount2);
        amount2.setId(2L);
        assertThat(amount1).isNotEqualTo(amount2);
        amount1.setId(null);
        assertThat(amount1).isNotEqualTo(amount2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(AmountDTO.class);
        AmountDTO amountDTO1 = new AmountDTO();
        amountDTO1.setId(1L);
        AmountDTO amountDTO2 = new AmountDTO();
        assertThat(amountDTO1).isNotEqualTo(amountDTO2);
        amountDTO2.setId(amountDTO1.getId());
        assertThat(amountDTO1).isEqualTo(amountDTO2);
        amountDTO2.setId(2L);
        assertThat(amountDTO1).isNotEqualTo(amountDTO2);
        amountDTO1.setId(null);
        assertThat(amountDTO1).isNotEqualTo(amountDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(amountMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(amountMapper.fromId(null)).isNull();
    }
}
