package com.bytatech.ayoos.payment.web.rest;

import com.bytatech.ayoos.payment.PaymentApp;

import com.bytatech.ayoos.payment.domain.Payer;
import com.bytatech.ayoos.payment.repository.PayerRepository;
import com.bytatech.ayoos.payment.repository.search.PayerSearchRepository;
import com.bytatech.ayoos.payment.service.PayerService;
import com.bytatech.ayoos.payment.service.dto.PayerDTO;
import com.bytatech.ayoos.payment.service.mapper.PayerMapper;
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

import com.bytatech.ayoos.payment.domain.enumeration.PaymentMethod;
/**
 * Test class for the PayerResource REST controller.
 *
 * @see PayerResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = PaymentApp.class)
public class PayerResourceIntTest {

    private static final PaymentMethod DEFAULT_PAYMENT_METHOD = PaymentMethod.CASH;
    private static final PaymentMethod UPDATED_PAYMENT_METHOD = PaymentMethod.PAYPAL;

    private static final String DEFAULT_STATUS = "AAAAAAAAAA";
    private static final String UPDATED_STATUS = "BBBBBBBBBB";

    private static final String DEFAULT_USER_ID = "AAAAAAAAAA";
    private static final String UPDATED_USER_ID = "BBBBBBBBBB";

    private static final String DEFAULT_PAYER_ID = "AAAAAAAAAA";
    private static final String UPDATED_PAYER_ID = "BBBBBBBBBB";

    @Autowired
    private PayerRepository payerRepository;

    @Autowired
    private PayerMapper payerMapper;
    
    @Autowired
    private PayerService payerService;

    /**
     * This repository is mocked in the com.bytatech.ayoos.payment.repository.search test package.
     *
     * @see com.bytatech.ayoos.payment.repository.search.PayerSearchRepositoryMockConfiguration
     */
    @Autowired
    private PayerSearchRepository mockPayerSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restPayerMockMvc;

    private Payer payer;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final PayerResource payerResource = new PayerResource(payerService);
        this.restPayerMockMvc = MockMvcBuilders.standaloneSetup(payerResource)
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
    public static Payer createEntity(EntityManager em) {
        Payer payer = new Payer()
            .paymentMethod(DEFAULT_PAYMENT_METHOD)
            .status(DEFAULT_STATUS)
            .userId(DEFAULT_USER_ID)
            .payerId(DEFAULT_PAYER_ID);
        return payer;
    }

    @Before
    public void initTest() {
        payer = createEntity(em);
    }

    @Test
    @Transactional
    public void createPayer() throws Exception {
        int databaseSizeBeforeCreate = payerRepository.findAll().size();

        // Create the Payer
        PayerDTO payerDTO = payerMapper.toDto(payer);
        restPayerMockMvc.perform(post("/api/payers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(payerDTO)))
            .andExpect(status().isCreated());

        // Validate the Payer in the database
        List<Payer> payerList = payerRepository.findAll();
        assertThat(payerList).hasSize(databaseSizeBeforeCreate + 1);
        Payer testPayer = payerList.get(payerList.size() - 1);
        assertThat(testPayer.getPaymentMethod()).isEqualTo(DEFAULT_PAYMENT_METHOD);
        assertThat(testPayer.getStatus()).isEqualTo(DEFAULT_STATUS);
        assertThat(testPayer.getUserId()).isEqualTo(DEFAULT_USER_ID);
        assertThat(testPayer.getPayerId()).isEqualTo(DEFAULT_PAYER_ID);

        // Validate the Payer in Elasticsearch
        verify(mockPayerSearchRepository, times(1)).save(testPayer);
    }

    @Test
    @Transactional
    public void createPayerWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = payerRepository.findAll().size();

        // Create the Payer with an existing ID
        payer.setId(1L);
        PayerDTO payerDTO = payerMapper.toDto(payer);

        // An entity with an existing ID cannot be created, so this API call must fail
        restPayerMockMvc.perform(post("/api/payers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(payerDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Payer in the database
        List<Payer> payerList = payerRepository.findAll();
        assertThat(payerList).hasSize(databaseSizeBeforeCreate);

        // Validate the Payer in Elasticsearch
        verify(mockPayerSearchRepository, times(0)).save(payer);
    }

    @Test
    @Transactional
    public void getAllPayers() throws Exception {
        // Initialize the database
        payerRepository.saveAndFlush(payer);

        // Get all the payerList
        restPayerMockMvc.perform(get("/api/payers?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(payer.getId().intValue())))
            .andExpect(jsonPath("$.[*].paymentMethod").value(hasItem(DEFAULT_PAYMENT_METHOD.toString())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].userId").value(hasItem(DEFAULT_USER_ID.toString())))
            .andExpect(jsonPath("$.[*].payerId").value(hasItem(DEFAULT_PAYER_ID.toString())));
    }
    
    @Test
    @Transactional
    public void getPayer() throws Exception {
        // Initialize the database
        payerRepository.saveAndFlush(payer);

        // Get the payer
        restPayerMockMvc.perform(get("/api/payers/{id}", payer.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(payer.getId().intValue()))
            .andExpect(jsonPath("$.paymentMethod").value(DEFAULT_PAYMENT_METHOD.toString()))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()))
            .andExpect(jsonPath("$.userId").value(DEFAULT_USER_ID.toString()))
            .andExpect(jsonPath("$.payerId").value(DEFAULT_PAYER_ID.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingPayer() throws Exception {
        // Get the payer
        restPayerMockMvc.perform(get("/api/payers/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updatePayer() throws Exception {
        // Initialize the database
        payerRepository.saveAndFlush(payer);

        int databaseSizeBeforeUpdate = payerRepository.findAll().size();

        // Update the payer
        Payer updatedPayer = payerRepository.findById(payer.getId()).get();
        // Disconnect from session so that the updates on updatedPayer are not directly saved in db
        em.detach(updatedPayer);
        updatedPayer
            .paymentMethod(UPDATED_PAYMENT_METHOD)
            .status(UPDATED_STATUS)
            .userId(UPDATED_USER_ID)
            .payerId(UPDATED_PAYER_ID);
        PayerDTO payerDTO = payerMapper.toDto(updatedPayer);

        restPayerMockMvc.perform(put("/api/payers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(payerDTO)))
            .andExpect(status().isOk());

        // Validate the Payer in the database
        List<Payer> payerList = payerRepository.findAll();
        assertThat(payerList).hasSize(databaseSizeBeforeUpdate);
        Payer testPayer = payerList.get(payerList.size() - 1);
        assertThat(testPayer.getPaymentMethod()).isEqualTo(UPDATED_PAYMENT_METHOD);
        assertThat(testPayer.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testPayer.getUserId()).isEqualTo(UPDATED_USER_ID);
        assertThat(testPayer.getPayerId()).isEqualTo(UPDATED_PAYER_ID);

        // Validate the Payer in Elasticsearch
        verify(mockPayerSearchRepository, times(1)).save(testPayer);
    }

    @Test
    @Transactional
    public void updateNonExistingPayer() throws Exception {
        int databaseSizeBeforeUpdate = payerRepository.findAll().size();

        // Create the Payer
        PayerDTO payerDTO = payerMapper.toDto(payer);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPayerMockMvc.perform(put("/api/payers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(payerDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Payer in the database
        List<Payer> payerList = payerRepository.findAll();
        assertThat(payerList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Payer in Elasticsearch
        verify(mockPayerSearchRepository, times(0)).save(payer);
    }

    @Test
    @Transactional
    public void deletePayer() throws Exception {
        // Initialize the database
        payerRepository.saveAndFlush(payer);

        int databaseSizeBeforeDelete = payerRepository.findAll().size();

        // Get the payer
        restPayerMockMvc.perform(delete("/api/payers/{id}", payer.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Payer> payerList = payerRepository.findAll();
        assertThat(payerList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the Payer in Elasticsearch
        verify(mockPayerSearchRepository, times(1)).deleteById(payer.getId());
    }

    @Test
    @Transactional
    public void searchPayer() throws Exception {
        // Initialize the database
        payerRepository.saveAndFlush(payer);
        when(mockPayerSearchRepository.search(queryStringQuery("id:" + payer.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(payer), PageRequest.of(0, 1), 1));
        // Search the payer
        restPayerMockMvc.perform(get("/api/_search/payers?query=id:" + payer.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(payer.getId().intValue())))
            .andExpect(jsonPath("$.[*].paymentMethod").value(hasItem(DEFAULT_PAYMENT_METHOD.toString())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].userId").value(hasItem(DEFAULT_USER_ID.toString())))
            .andExpect(jsonPath("$.[*].payerId").value(hasItem(DEFAULT_PAYER_ID.toString())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Payer.class);
        Payer payer1 = new Payer();
        payer1.setId(1L);
        Payer payer2 = new Payer();
        payer2.setId(payer1.getId());
        assertThat(payer1).isEqualTo(payer2);
        payer2.setId(2L);
        assertThat(payer1).isNotEqualTo(payer2);
        payer1.setId(null);
        assertThat(payer1).isNotEqualTo(payer2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(PayerDTO.class);
        PayerDTO payerDTO1 = new PayerDTO();
        payerDTO1.setId(1L);
        PayerDTO payerDTO2 = new PayerDTO();
        assertThat(payerDTO1).isNotEqualTo(payerDTO2);
        payerDTO2.setId(payerDTO1.getId());
        assertThat(payerDTO1).isEqualTo(payerDTO2);
        payerDTO2.setId(2L);
        assertThat(payerDTO1).isNotEqualTo(payerDTO2);
        payerDTO1.setId(null);
        assertThat(payerDTO1).isNotEqualTo(payerDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(payerMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(payerMapper.fromId(null)).isNull();
    }
}
