package com.bytatech.ayoos.payment.web.rest;

import com.bytatech.ayoos.payment.PaymentApp;

import com.bytatech.ayoos.payment.domain.RelatedTransactions;
import com.bytatech.ayoos.payment.repository.RelatedTransactionsRepository;
import com.bytatech.ayoos.payment.repository.search.RelatedTransactionsSearchRepository;
import com.bytatech.ayoos.payment.service.RelatedTransactionsService;
import com.bytatech.ayoos.payment.service.dto.RelatedTransactionsDTO;
import com.bytatech.ayoos.payment.service.mapper.RelatedTransactionsMapper;
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
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Collections;
import java.util.List;


import static com.bytatech.ayoos.payment.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.bytatech.ayoos.payment.domain.enumeration.TransactionType;
/**
 * Test class for the RelatedTransactionsResource REST controller.
 *
 * @see RelatedTransactionsResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = PaymentApp.class)
public class RelatedTransactionsResourceIntTest {

    private static final TransactionType DEFAULT_TRANSACTION_TYPE = TransactionType.SALE;
    private static final TransactionType UPDATED_TRANSACTION_TYPE = TransactionType.AUTHORIZATION;

    private static final String DEFAULT_INTENT_ID = "AAAAAAAAAA";
    private static final String UPDATED_INTENT_ID = "BBBBBBBBBB";

    private static final String DEFAULT_PAYMENT_MODE = "AAAAAAAAAA";
    private static final String UPDATED_PAYMENT_MODE = "BBBBBBBBBB";

    private static final String DEFAULT_STATE = "AAAAAAAAAA";
    private static final String UPDATED_STATE = "BBBBBBBBBB";

    private static final String DEFAULT_SALE_REASON_CODE = "AAAAAAAAAA";
    private static final String UPDATED_SALE_REASON_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_PAYMENT_ID = "AAAAAAAAAA";
    private static final String UPDATED_PAYMENT_ID = "BBBBBBBBBB";

    private static final String DEFAULT_RECEIPT_ID = "AAAAAAAAAA";
    private static final String UPDATED_RECEIPT_ID = "BBBBBBBBBB";

    private static final Instant DEFAULT_CREATE_TIME = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATE_TIME = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_UPDATE_TIME = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_UPDATE_TIME = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_AUTH_VALID_UNTIL = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_AUTH_VALID_UNTIL = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_REFUND_REASON = "AAAAAAAAAA";
    private static final String UPDATED_REFUND_REASON = "BBBBBBBBBB";

    private static final String DEFAULT_ORDER_PENDING_REASON = "AAAAAAAAAA";
    private static final String UPDATED_ORDER_PENDING_REASON = "BBBBBBBBBB";

    private static final String DEFAULT_REFUND_SALE_ID = "AAAAAAAAAA";
    private static final String UPDATED_REFUND_SALE_ID = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    @Autowired
    private RelatedTransactionsRepository relatedTransactionsRepository;

    @Autowired
    private RelatedTransactionsMapper relatedTransactionsMapper;
    
    @Autowired
    private RelatedTransactionsService relatedTransactionsService;

    /**
     * This repository is mocked in the com.bytatech.ayoos.payment.repository.search test package.
     *
     * @see com.bytatech.ayoos.payment.repository.search.RelatedTransactionsSearchRepositoryMockConfiguration
     */
    @Autowired
    private RelatedTransactionsSearchRepository mockRelatedTransactionsSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restRelatedTransactionsMockMvc;

    private RelatedTransactions relatedTransactions;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final RelatedTransactionsResource relatedTransactionsResource = new RelatedTransactionsResource(relatedTransactionsService);
        this.restRelatedTransactionsMockMvc = MockMvcBuilders.standaloneSetup(relatedTransactionsResource)
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
    public static RelatedTransactions createEntity(EntityManager em) {
        RelatedTransactions relatedTransactions = new RelatedTransactions()
            .transactionType(DEFAULT_TRANSACTION_TYPE)
            .intentId(DEFAULT_INTENT_ID)
            .paymentMode(DEFAULT_PAYMENT_MODE)
            .state(DEFAULT_STATE)
            .saleReasonCode(DEFAULT_SALE_REASON_CODE)
            .paymentId(DEFAULT_PAYMENT_ID)
            .receiptId(DEFAULT_RECEIPT_ID)
            .createTime(DEFAULT_CREATE_TIME)
            .updateTime(DEFAULT_UPDATE_TIME)
            .authValidUntil(DEFAULT_AUTH_VALID_UNTIL)
            .refundReason(DEFAULT_REFUND_REASON)
            .orderPendingReason(DEFAULT_ORDER_PENDING_REASON)
            .refundSaleId(DEFAULT_REFUND_SALE_ID)
            .description(DEFAULT_DESCRIPTION);
        return relatedTransactions;
    }

    @Before
    public void initTest() {
        relatedTransactions = createEntity(em);
    }

    @Test
    @Transactional
    public void createRelatedTransactions() throws Exception {
        int databaseSizeBeforeCreate = relatedTransactionsRepository.findAll().size();

        // Create the RelatedTransactions
        RelatedTransactionsDTO relatedTransactionsDTO = relatedTransactionsMapper.toDto(relatedTransactions);
        restRelatedTransactionsMockMvc.perform(post("/api/related-transactions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(relatedTransactionsDTO)))
            .andExpect(status().isCreated());

        // Validate the RelatedTransactions in the database
        List<RelatedTransactions> relatedTransactionsList = relatedTransactionsRepository.findAll();
        assertThat(relatedTransactionsList).hasSize(databaseSizeBeforeCreate + 1);
        RelatedTransactions testRelatedTransactions = relatedTransactionsList.get(relatedTransactionsList.size() - 1);
        assertThat(testRelatedTransactions.getTransactionType()).isEqualTo(DEFAULT_TRANSACTION_TYPE);
        assertThat(testRelatedTransactions.getIntentId()).isEqualTo(DEFAULT_INTENT_ID);
        assertThat(testRelatedTransactions.getPaymentMode()).isEqualTo(DEFAULT_PAYMENT_MODE);
        assertThat(testRelatedTransactions.getState()).isEqualTo(DEFAULT_STATE);
        assertThat(testRelatedTransactions.getSaleReasonCode()).isEqualTo(DEFAULT_SALE_REASON_CODE);
        assertThat(testRelatedTransactions.getPaymentId()).isEqualTo(DEFAULT_PAYMENT_ID);
        assertThat(testRelatedTransactions.getReceiptId()).isEqualTo(DEFAULT_RECEIPT_ID);
        assertThat(testRelatedTransactions.getCreateTime()).isEqualTo(DEFAULT_CREATE_TIME);
        assertThat(testRelatedTransactions.getUpdateTime()).isEqualTo(DEFAULT_UPDATE_TIME);
        assertThat(testRelatedTransactions.getAuthValidUntil()).isEqualTo(DEFAULT_AUTH_VALID_UNTIL);
        assertThat(testRelatedTransactions.getRefundReason()).isEqualTo(DEFAULT_REFUND_REASON);
        assertThat(testRelatedTransactions.getOrderPendingReason()).isEqualTo(DEFAULT_ORDER_PENDING_REASON);
        assertThat(testRelatedTransactions.getRefundSaleId()).isEqualTo(DEFAULT_REFUND_SALE_ID);
        assertThat(testRelatedTransactions.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);

        // Validate the RelatedTransactions in Elasticsearch
        verify(mockRelatedTransactionsSearchRepository, times(1)).save(testRelatedTransactions);
    }

    @Test
    @Transactional
    public void createRelatedTransactionsWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = relatedTransactionsRepository.findAll().size();

        // Create the RelatedTransactions with an existing ID
        relatedTransactions.setId(1L);
        RelatedTransactionsDTO relatedTransactionsDTO = relatedTransactionsMapper.toDto(relatedTransactions);

        // An entity with an existing ID cannot be created, so this API call must fail
        restRelatedTransactionsMockMvc.perform(post("/api/related-transactions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(relatedTransactionsDTO)))
            .andExpect(status().isBadRequest());

        // Validate the RelatedTransactions in the database
        List<RelatedTransactions> relatedTransactionsList = relatedTransactionsRepository.findAll();
        assertThat(relatedTransactionsList).hasSize(databaseSizeBeforeCreate);

        // Validate the RelatedTransactions in Elasticsearch
        verify(mockRelatedTransactionsSearchRepository, times(0)).save(relatedTransactions);
    }

    @Test
    @Transactional
    public void getAllRelatedTransactions() throws Exception {
        // Initialize the database
        relatedTransactionsRepository.saveAndFlush(relatedTransactions);

        // Get all the relatedTransactionsList
        restRelatedTransactionsMockMvc.perform(get("/api/related-transactions?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(relatedTransactions.getId().intValue())))
            .andExpect(jsonPath("$.[*].transactionType").value(hasItem(DEFAULT_TRANSACTION_TYPE.toString())))
            .andExpect(jsonPath("$.[*].intentId").value(hasItem(DEFAULT_INTENT_ID.toString())))
            .andExpect(jsonPath("$.[*].paymentMode").value(hasItem(DEFAULT_PAYMENT_MODE.toString())))
            .andExpect(jsonPath("$.[*].state").value(hasItem(DEFAULT_STATE.toString())))
            .andExpect(jsonPath("$.[*].saleReasonCode").value(hasItem(DEFAULT_SALE_REASON_CODE.toString())))
            .andExpect(jsonPath("$.[*].paymentId").value(hasItem(DEFAULT_PAYMENT_ID.toString())))
            .andExpect(jsonPath("$.[*].receiptId").value(hasItem(DEFAULT_RECEIPT_ID.toString())))
            .andExpect(jsonPath("$.[*].createTime").value(hasItem(DEFAULT_CREATE_TIME.toString())))
            .andExpect(jsonPath("$.[*].updateTime").value(hasItem(DEFAULT_UPDATE_TIME.toString())))
            .andExpect(jsonPath("$.[*].authValidUntil").value(hasItem(DEFAULT_AUTH_VALID_UNTIL.toString())))
            .andExpect(jsonPath("$.[*].refundReason").value(hasItem(DEFAULT_REFUND_REASON.toString())))
            .andExpect(jsonPath("$.[*].orderPendingReason").value(hasItem(DEFAULT_ORDER_PENDING_REASON.toString())))
            .andExpect(jsonPath("$.[*].refundSaleId").value(hasItem(DEFAULT_REFUND_SALE_ID.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())));
    }
    
    @Test
    @Transactional
    public void getRelatedTransactions() throws Exception {
        // Initialize the database
        relatedTransactionsRepository.saveAndFlush(relatedTransactions);

        // Get the relatedTransactions
        restRelatedTransactionsMockMvc.perform(get("/api/related-transactions/{id}", relatedTransactions.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(relatedTransactions.getId().intValue()))
            .andExpect(jsonPath("$.transactionType").value(DEFAULT_TRANSACTION_TYPE.toString()))
            .andExpect(jsonPath("$.intentId").value(DEFAULT_INTENT_ID.toString()))
            .andExpect(jsonPath("$.paymentMode").value(DEFAULT_PAYMENT_MODE.toString()))
            .andExpect(jsonPath("$.state").value(DEFAULT_STATE.toString()))
            .andExpect(jsonPath("$.saleReasonCode").value(DEFAULT_SALE_REASON_CODE.toString()))
            .andExpect(jsonPath("$.paymentId").value(DEFAULT_PAYMENT_ID.toString()))
            .andExpect(jsonPath("$.receiptId").value(DEFAULT_RECEIPT_ID.toString()))
            .andExpect(jsonPath("$.createTime").value(DEFAULT_CREATE_TIME.toString()))
            .andExpect(jsonPath("$.updateTime").value(DEFAULT_UPDATE_TIME.toString()))
            .andExpect(jsonPath("$.authValidUntil").value(DEFAULT_AUTH_VALID_UNTIL.toString()))
            .andExpect(jsonPath("$.refundReason").value(DEFAULT_REFUND_REASON.toString()))
            .andExpect(jsonPath("$.orderPendingReason").value(DEFAULT_ORDER_PENDING_REASON.toString()))
            .andExpect(jsonPath("$.refundSaleId").value(DEFAULT_REFUND_SALE_ID.toString()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingRelatedTransactions() throws Exception {
        // Get the relatedTransactions
        restRelatedTransactionsMockMvc.perform(get("/api/related-transactions/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateRelatedTransactions() throws Exception {
        // Initialize the database
        relatedTransactionsRepository.saveAndFlush(relatedTransactions);

        int databaseSizeBeforeUpdate = relatedTransactionsRepository.findAll().size();

        // Update the relatedTransactions
        RelatedTransactions updatedRelatedTransactions = relatedTransactionsRepository.findById(relatedTransactions.getId()).get();
        // Disconnect from session so that the updates on updatedRelatedTransactions are not directly saved in db
        em.detach(updatedRelatedTransactions);
        updatedRelatedTransactions
            .transactionType(UPDATED_TRANSACTION_TYPE)
            .intentId(UPDATED_INTENT_ID)
            .paymentMode(UPDATED_PAYMENT_MODE)
            .state(UPDATED_STATE)
            .saleReasonCode(UPDATED_SALE_REASON_CODE)
            .paymentId(UPDATED_PAYMENT_ID)
            .receiptId(UPDATED_RECEIPT_ID)
            .createTime(UPDATED_CREATE_TIME)
            .updateTime(UPDATED_UPDATE_TIME)
            .authValidUntil(UPDATED_AUTH_VALID_UNTIL)
            .refundReason(UPDATED_REFUND_REASON)
            .orderPendingReason(UPDATED_ORDER_PENDING_REASON)
            .refundSaleId(UPDATED_REFUND_SALE_ID)
            .description(UPDATED_DESCRIPTION);
        RelatedTransactionsDTO relatedTransactionsDTO = relatedTransactionsMapper.toDto(updatedRelatedTransactions);

        restRelatedTransactionsMockMvc.perform(put("/api/related-transactions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(relatedTransactionsDTO)))
            .andExpect(status().isOk());

        // Validate the RelatedTransactions in the database
        List<RelatedTransactions> relatedTransactionsList = relatedTransactionsRepository.findAll();
        assertThat(relatedTransactionsList).hasSize(databaseSizeBeforeUpdate);
        RelatedTransactions testRelatedTransactions = relatedTransactionsList.get(relatedTransactionsList.size() - 1);
        assertThat(testRelatedTransactions.getTransactionType()).isEqualTo(UPDATED_TRANSACTION_TYPE);
        assertThat(testRelatedTransactions.getIntentId()).isEqualTo(UPDATED_INTENT_ID);
        assertThat(testRelatedTransactions.getPaymentMode()).isEqualTo(UPDATED_PAYMENT_MODE);
        assertThat(testRelatedTransactions.getState()).isEqualTo(UPDATED_STATE);
        assertThat(testRelatedTransactions.getSaleReasonCode()).isEqualTo(UPDATED_SALE_REASON_CODE);
        assertThat(testRelatedTransactions.getPaymentId()).isEqualTo(UPDATED_PAYMENT_ID);
        assertThat(testRelatedTransactions.getReceiptId()).isEqualTo(UPDATED_RECEIPT_ID);
        assertThat(testRelatedTransactions.getCreateTime()).isEqualTo(UPDATED_CREATE_TIME);
        assertThat(testRelatedTransactions.getUpdateTime()).isEqualTo(UPDATED_UPDATE_TIME);
        assertThat(testRelatedTransactions.getAuthValidUntil()).isEqualTo(UPDATED_AUTH_VALID_UNTIL);
        assertThat(testRelatedTransactions.getRefundReason()).isEqualTo(UPDATED_REFUND_REASON);
        assertThat(testRelatedTransactions.getOrderPendingReason()).isEqualTo(UPDATED_ORDER_PENDING_REASON);
        assertThat(testRelatedTransactions.getRefundSaleId()).isEqualTo(UPDATED_REFUND_SALE_ID);
        assertThat(testRelatedTransactions.getDescription()).isEqualTo(UPDATED_DESCRIPTION);

        // Validate the RelatedTransactions in Elasticsearch
        verify(mockRelatedTransactionsSearchRepository, times(1)).save(testRelatedTransactions);
    }

    @Test
    @Transactional
    public void updateNonExistingRelatedTransactions() throws Exception {
        int databaseSizeBeforeUpdate = relatedTransactionsRepository.findAll().size();

        // Create the RelatedTransactions
        RelatedTransactionsDTO relatedTransactionsDTO = relatedTransactionsMapper.toDto(relatedTransactions);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restRelatedTransactionsMockMvc.perform(put("/api/related-transactions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(relatedTransactionsDTO)))
            .andExpect(status().isBadRequest());

        // Validate the RelatedTransactions in the database
        List<RelatedTransactions> relatedTransactionsList = relatedTransactionsRepository.findAll();
        assertThat(relatedTransactionsList).hasSize(databaseSizeBeforeUpdate);

        // Validate the RelatedTransactions in Elasticsearch
        verify(mockRelatedTransactionsSearchRepository, times(0)).save(relatedTransactions);
    }

    @Test
    @Transactional
    public void deleteRelatedTransactions() throws Exception {
        // Initialize the database
        relatedTransactionsRepository.saveAndFlush(relatedTransactions);

        int databaseSizeBeforeDelete = relatedTransactionsRepository.findAll().size();

        // Get the relatedTransactions
        restRelatedTransactionsMockMvc.perform(delete("/api/related-transactions/{id}", relatedTransactions.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<RelatedTransactions> relatedTransactionsList = relatedTransactionsRepository.findAll();
        assertThat(relatedTransactionsList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the RelatedTransactions in Elasticsearch
        verify(mockRelatedTransactionsSearchRepository, times(1)).deleteById(relatedTransactions.getId());
    }

    @Test
    @Transactional
    public void searchRelatedTransactions() throws Exception {
        // Initialize the database
        relatedTransactionsRepository.saveAndFlush(relatedTransactions);
        when(mockRelatedTransactionsSearchRepository.search(queryStringQuery("id:" + relatedTransactions.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(relatedTransactions), PageRequest.of(0, 1), 1));
        // Search the relatedTransactions
        restRelatedTransactionsMockMvc.perform(get("/api/_search/related-transactions?query=id:" + relatedTransactions.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(relatedTransactions.getId().intValue())))
            .andExpect(jsonPath("$.[*].transactionType").value(hasItem(DEFAULT_TRANSACTION_TYPE.toString())))
            .andExpect(jsonPath("$.[*].intentId").value(hasItem(DEFAULT_INTENT_ID.toString())))
            .andExpect(jsonPath("$.[*].paymentMode").value(hasItem(DEFAULT_PAYMENT_MODE.toString())))
            .andExpect(jsonPath("$.[*].state").value(hasItem(DEFAULT_STATE.toString())))
            .andExpect(jsonPath("$.[*].saleReasonCode").value(hasItem(DEFAULT_SALE_REASON_CODE.toString())))
            .andExpect(jsonPath("$.[*].paymentId").value(hasItem(DEFAULT_PAYMENT_ID.toString())))
            .andExpect(jsonPath("$.[*].receiptId").value(hasItem(DEFAULT_RECEIPT_ID.toString())))
            .andExpect(jsonPath("$.[*].createTime").value(hasItem(DEFAULT_CREATE_TIME.toString())))
            .andExpect(jsonPath("$.[*].updateTime").value(hasItem(DEFAULT_UPDATE_TIME.toString())))
            .andExpect(jsonPath("$.[*].authValidUntil").value(hasItem(DEFAULT_AUTH_VALID_UNTIL.toString())))
            .andExpect(jsonPath("$.[*].refundReason").value(hasItem(DEFAULT_REFUND_REASON.toString())))
            .andExpect(jsonPath("$.[*].orderPendingReason").value(hasItem(DEFAULT_ORDER_PENDING_REASON.toString())))
            .andExpect(jsonPath("$.[*].refundSaleId").value(hasItem(DEFAULT_REFUND_SALE_ID.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(RelatedTransactions.class);
        RelatedTransactions relatedTransactions1 = new RelatedTransactions();
        relatedTransactions1.setId(1L);
        RelatedTransactions relatedTransactions2 = new RelatedTransactions();
        relatedTransactions2.setId(relatedTransactions1.getId());
        assertThat(relatedTransactions1).isEqualTo(relatedTransactions2);
        relatedTransactions2.setId(2L);
        assertThat(relatedTransactions1).isNotEqualTo(relatedTransactions2);
        relatedTransactions1.setId(null);
        assertThat(relatedTransactions1).isNotEqualTo(relatedTransactions2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(RelatedTransactionsDTO.class);
        RelatedTransactionsDTO relatedTransactionsDTO1 = new RelatedTransactionsDTO();
        relatedTransactionsDTO1.setId(1L);
        RelatedTransactionsDTO relatedTransactionsDTO2 = new RelatedTransactionsDTO();
        assertThat(relatedTransactionsDTO1).isNotEqualTo(relatedTransactionsDTO2);
        relatedTransactionsDTO2.setId(relatedTransactionsDTO1.getId());
        assertThat(relatedTransactionsDTO1).isEqualTo(relatedTransactionsDTO2);
        relatedTransactionsDTO2.setId(2L);
        assertThat(relatedTransactionsDTO1).isNotEqualTo(relatedTransactionsDTO2);
        relatedTransactionsDTO1.setId(null);
        assertThat(relatedTransactionsDTO1).isNotEqualTo(relatedTransactionsDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(relatedTransactionsMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(relatedTransactionsMapper.fromId(null)).isNull();
    }
}
