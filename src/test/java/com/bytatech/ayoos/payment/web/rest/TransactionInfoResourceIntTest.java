package com.bytatech.ayoos.payment.web.rest;

import com.bytatech.ayoos.payment.PaymentApp;

import com.bytatech.ayoos.payment.domain.TransactionInfo;
import com.bytatech.ayoos.payment.repository.TransactionInfoRepository;
import com.bytatech.ayoos.payment.repository.search.TransactionInfoSearchRepository;
import com.bytatech.ayoos.payment.service.TransactionInfoService;
import com.bytatech.ayoos.payment.service.dto.TransactionInfoDTO;
import com.bytatech.ayoos.payment.service.mapper.TransactionInfoMapper;
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
 * Test class for the TransactionInfoResource REST controller.
 *
 * @see TransactionInfoResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = PaymentApp.class)
public class TransactionInfoResourceIntTest {

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String DEFAULT_NOTE_TO_PAYEE = "AAAAAAAAAA";
    private static final String UPDATED_NOTE_TO_PAYEE = "BBBBBBBBBB";

    private static final String DEFAULT_INVOICE_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_INVOICE_NUMBER = "BBBBBBBBBB";

    @Autowired
    private TransactionInfoRepository transactionInfoRepository;

    @Autowired
    private TransactionInfoMapper transactionInfoMapper;
    
    @Autowired
    private TransactionInfoService transactionInfoService;

    /**
     * This repository is mocked in the com.bytatech.ayoos.payment.repository.search test package.
     *
     * @see com.bytatech.ayoos.payment.repository.search.TransactionInfoSearchRepositoryMockConfiguration
     */
    @Autowired
    private TransactionInfoSearchRepository mockTransactionInfoSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restTransactionInfoMockMvc;

    private TransactionInfo transactionInfo;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final TransactionInfoResource transactionInfoResource = new TransactionInfoResource(transactionInfoService);
        this.restTransactionInfoMockMvc = MockMvcBuilders.standaloneSetup(transactionInfoResource)
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
    public static TransactionInfo createEntity(EntityManager em) {
        TransactionInfo transactionInfo = new TransactionInfo()
            .description(DEFAULT_DESCRIPTION)
            .noteToPayee(DEFAULT_NOTE_TO_PAYEE)
            .invoiceNumber(DEFAULT_INVOICE_NUMBER);
        return transactionInfo;
    }

    @Before
    public void initTest() {
        transactionInfo = createEntity(em);
    }

    @Test
    @Transactional
    public void createTransactionInfo() throws Exception {
        int databaseSizeBeforeCreate = transactionInfoRepository.findAll().size();

        // Create the TransactionInfo
        TransactionInfoDTO transactionInfoDTO = transactionInfoMapper.toDto(transactionInfo);
        restTransactionInfoMockMvc.perform(post("/api/transaction-infos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(transactionInfoDTO)))
            .andExpect(status().isCreated());

        // Validate the TransactionInfo in the database
        List<TransactionInfo> transactionInfoList = transactionInfoRepository.findAll();
        assertThat(transactionInfoList).hasSize(databaseSizeBeforeCreate + 1);
        TransactionInfo testTransactionInfo = transactionInfoList.get(transactionInfoList.size() - 1);
        assertThat(testTransactionInfo.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testTransactionInfo.getNoteToPayee()).isEqualTo(DEFAULT_NOTE_TO_PAYEE);
        assertThat(testTransactionInfo.getInvoiceNumber()).isEqualTo(DEFAULT_INVOICE_NUMBER);

        // Validate the TransactionInfo in Elasticsearch
        verify(mockTransactionInfoSearchRepository, times(1)).save(testTransactionInfo);
    }

    @Test
    @Transactional
    public void createTransactionInfoWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = transactionInfoRepository.findAll().size();

        // Create the TransactionInfo with an existing ID
        transactionInfo.setId(1L);
        TransactionInfoDTO transactionInfoDTO = transactionInfoMapper.toDto(transactionInfo);

        // An entity with an existing ID cannot be created, so this API call must fail
        restTransactionInfoMockMvc.perform(post("/api/transaction-infos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(transactionInfoDTO)))
            .andExpect(status().isBadRequest());

        // Validate the TransactionInfo in the database
        List<TransactionInfo> transactionInfoList = transactionInfoRepository.findAll();
        assertThat(transactionInfoList).hasSize(databaseSizeBeforeCreate);

        // Validate the TransactionInfo in Elasticsearch
        verify(mockTransactionInfoSearchRepository, times(0)).save(transactionInfo);
    }

    @Test
    @Transactional
    public void getAllTransactionInfos() throws Exception {
        // Initialize the database
        transactionInfoRepository.saveAndFlush(transactionInfo);

        // Get all the transactionInfoList
        restTransactionInfoMockMvc.perform(get("/api/transaction-infos?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(transactionInfo.getId().intValue())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].noteToPayee").value(hasItem(DEFAULT_NOTE_TO_PAYEE.toString())))
            .andExpect(jsonPath("$.[*].invoiceNumber").value(hasItem(DEFAULT_INVOICE_NUMBER.toString())));
    }
    
    @Test
    @Transactional
    public void getTransactionInfo() throws Exception {
        // Initialize the database
        transactionInfoRepository.saveAndFlush(transactionInfo);

        // Get the transactionInfo
        restTransactionInfoMockMvc.perform(get("/api/transaction-infos/{id}", transactionInfo.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(transactionInfo.getId().intValue()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()))
            .andExpect(jsonPath("$.noteToPayee").value(DEFAULT_NOTE_TO_PAYEE.toString()))
            .andExpect(jsonPath("$.invoiceNumber").value(DEFAULT_INVOICE_NUMBER.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingTransactionInfo() throws Exception {
        // Get the transactionInfo
        restTransactionInfoMockMvc.perform(get("/api/transaction-infos/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateTransactionInfo() throws Exception {
        // Initialize the database
        transactionInfoRepository.saveAndFlush(transactionInfo);

        int databaseSizeBeforeUpdate = transactionInfoRepository.findAll().size();

        // Update the transactionInfo
        TransactionInfo updatedTransactionInfo = transactionInfoRepository.findById(transactionInfo.getId()).get();
        // Disconnect from session so that the updates on updatedTransactionInfo are not directly saved in db
        em.detach(updatedTransactionInfo);
        updatedTransactionInfo
            .description(UPDATED_DESCRIPTION)
            .noteToPayee(UPDATED_NOTE_TO_PAYEE)
            .invoiceNumber(UPDATED_INVOICE_NUMBER);
        TransactionInfoDTO transactionInfoDTO = transactionInfoMapper.toDto(updatedTransactionInfo);

        restTransactionInfoMockMvc.perform(put("/api/transaction-infos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(transactionInfoDTO)))
            .andExpect(status().isOk());

        // Validate the TransactionInfo in the database
        List<TransactionInfo> transactionInfoList = transactionInfoRepository.findAll();
        assertThat(transactionInfoList).hasSize(databaseSizeBeforeUpdate);
        TransactionInfo testTransactionInfo = transactionInfoList.get(transactionInfoList.size() - 1);
        assertThat(testTransactionInfo.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testTransactionInfo.getNoteToPayee()).isEqualTo(UPDATED_NOTE_TO_PAYEE);
        assertThat(testTransactionInfo.getInvoiceNumber()).isEqualTo(UPDATED_INVOICE_NUMBER);

        // Validate the TransactionInfo in Elasticsearch
        verify(mockTransactionInfoSearchRepository, times(1)).save(testTransactionInfo);
    }

    @Test
    @Transactional
    public void updateNonExistingTransactionInfo() throws Exception {
        int databaseSizeBeforeUpdate = transactionInfoRepository.findAll().size();

        // Create the TransactionInfo
        TransactionInfoDTO transactionInfoDTO = transactionInfoMapper.toDto(transactionInfo);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTransactionInfoMockMvc.perform(put("/api/transaction-infos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(transactionInfoDTO)))
            .andExpect(status().isBadRequest());

        // Validate the TransactionInfo in the database
        List<TransactionInfo> transactionInfoList = transactionInfoRepository.findAll();
        assertThat(transactionInfoList).hasSize(databaseSizeBeforeUpdate);

        // Validate the TransactionInfo in Elasticsearch
        verify(mockTransactionInfoSearchRepository, times(0)).save(transactionInfo);
    }

    @Test
    @Transactional
    public void deleteTransactionInfo() throws Exception {
        // Initialize the database
        transactionInfoRepository.saveAndFlush(transactionInfo);

        int databaseSizeBeforeDelete = transactionInfoRepository.findAll().size();

        // Get the transactionInfo
        restTransactionInfoMockMvc.perform(delete("/api/transaction-infos/{id}", transactionInfo.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<TransactionInfo> transactionInfoList = transactionInfoRepository.findAll();
        assertThat(transactionInfoList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the TransactionInfo in Elasticsearch
        verify(mockTransactionInfoSearchRepository, times(1)).deleteById(transactionInfo.getId());
    }

    @Test
    @Transactional
    public void searchTransactionInfo() throws Exception {
        // Initialize the database
        transactionInfoRepository.saveAndFlush(transactionInfo);
        when(mockTransactionInfoSearchRepository.search(queryStringQuery("id:" + transactionInfo.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(transactionInfo), PageRequest.of(0, 1), 1));
        // Search the transactionInfo
        restTransactionInfoMockMvc.perform(get("/api/_search/transaction-infos?query=id:" + transactionInfo.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(transactionInfo.getId().intValue())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].noteToPayee").value(hasItem(DEFAULT_NOTE_TO_PAYEE.toString())))
            .andExpect(jsonPath("$.[*].invoiceNumber").value(hasItem(DEFAULT_INVOICE_NUMBER.toString())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(TransactionInfo.class);
        TransactionInfo transactionInfo1 = new TransactionInfo();
        transactionInfo1.setId(1L);
        TransactionInfo transactionInfo2 = new TransactionInfo();
        transactionInfo2.setId(transactionInfo1.getId());
        assertThat(transactionInfo1).isEqualTo(transactionInfo2);
        transactionInfo2.setId(2L);
        assertThat(transactionInfo1).isNotEqualTo(transactionInfo2);
        transactionInfo1.setId(null);
        assertThat(transactionInfo1).isNotEqualTo(transactionInfo2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(TransactionInfoDTO.class);
        TransactionInfoDTO transactionInfoDTO1 = new TransactionInfoDTO();
        transactionInfoDTO1.setId(1L);
        TransactionInfoDTO transactionInfoDTO2 = new TransactionInfoDTO();
        assertThat(transactionInfoDTO1).isNotEqualTo(transactionInfoDTO2);
        transactionInfoDTO2.setId(transactionInfoDTO1.getId());
        assertThat(transactionInfoDTO1).isEqualTo(transactionInfoDTO2);
        transactionInfoDTO2.setId(2L);
        assertThat(transactionInfoDTO1).isNotEqualTo(transactionInfoDTO2);
        transactionInfoDTO1.setId(null);
        assertThat(transactionInfoDTO1).isNotEqualTo(transactionInfoDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(transactionInfoMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(transactionInfoMapper.fromId(null)).isNull();
    }
}
