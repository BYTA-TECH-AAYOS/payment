package com.bytatech.ayoos.payment.web.rest;

import com.bytatech.ayoos.payment.PaymentApp;

import com.bytatech.ayoos.payment.domain.AmountDetails;
import com.bytatech.ayoos.payment.repository.AmountDetailsRepository;
import com.bytatech.ayoos.payment.repository.search.AmountDetailsSearchRepository;
import com.bytatech.ayoos.payment.service.AmountDetailsService;
import com.bytatech.ayoos.payment.service.dto.AmountDetailsDTO;
import com.bytatech.ayoos.payment.service.mapper.AmountDetailsMapper;
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
 * Test class for the AmountDetailsResource REST controller.
 *
 * @see AmountDetailsResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = PaymentApp.class)
public class AmountDetailsResourceIntTest {

    private static final Double DEFAULT_SUBTOTAL = 1D;
    private static final Double UPDATED_SUBTOTAL = 2D;

    private static final Double DEFAULT_TAX = 1D;
    private static final Double UPDATED_TAX = 2D;

    private static final Double DEFAULT_SHIPPING = 1D;
    private static final Double UPDATED_SHIPPING = 2D;

    private static final Double DEFAULT_HANDLING_FEE = 1D;
    private static final Double UPDATED_HANDLING_FEE = 2D;

    private static final Double DEFAULT_SHIPPING_DISCOUNT = 1D;
    private static final Double UPDATED_SHIPPING_DISCOUNT = 2D;

    private static final Double DEFAULT_INSURANCE = 1D;
    private static final Double UPDATED_INSURANCE = 2D;

    private static final Double DEFAULT_OTHER_FEE = 1D;
    private static final Double UPDATED_OTHER_FEE = 2D;

    @Autowired
    private AmountDetailsRepository amountDetailsRepository;

    @Autowired
    private AmountDetailsMapper amountDetailsMapper;
    
    @Autowired
    private AmountDetailsService amountDetailsService;

    /**
     * This repository is mocked in the com.bytatech.ayoos.payment.repository.search test package.
     *
     * @see com.bytatech.ayoos.payment.repository.search.AmountDetailsSearchRepositoryMockConfiguration
     */
    @Autowired
    private AmountDetailsSearchRepository mockAmountDetailsSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restAmountDetailsMockMvc;

    private AmountDetails amountDetails;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final AmountDetailsResource amountDetailsResource = new AmountDetailsResource(amountDetailsService);
        this.restAmountDetailsMockMvc = MockMvcBuilders.standaloneSetup(amountDetailsResource)
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
    public static AmountDetails createEntity(EntityManager em) {
        AmountDetails amountDetails = new AmountDetails()
            .subtotal(DEFAULT_SUBTOTAL)
            .tax(DEFAULT_TAX)
            .shipping(DEFAULT_SHIPPING)
            .handlingFee(DEFAULT_HANDLING_FEE)
            .shippingDiscount(DEFAULT_SHIPPING_DISCOUNT)
            .insurance(DEFAULT_INSURANCE)
            .otherFee(DEFAULT_OTHER_FEE);
        return amountDetails;
    }

    @Before
    public void initTest() {
        amountDetails = createEntity(em);
    }

    @Test
    @Transactional
    public void createAmountDetails() throws Exception {
        int databaseSizeBeforeCreate = amountDetailsRepository.findAll().size();

        // Create the AmountDetails
        AmountDetailsDTO amountDetailsDTO = amountDetailsMapper.toDto(amountDetails);
        restAmountDetailsMockMvc.perform(post("/api/amount-details")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(amountDetailsDTO)))
            .andExpect(status().isCreated());

        // Validate the AmountDetails in the database
        List<AmountDetails> amountDetailsList = amountDetailsRepository.findAll();
        assertThat(amountDetailsList).hasSize(databaseSizeBeforeCreate + 1);
        AmountDetails testAmountDetails = amountDetailsList.get(amountDetailsList.size() - 1);
        assertThat(testAmountDetails.getSubtotal()).isEqualTo(DEFAULT_SUBTOTAL);
        assertThat(testAmountDetails.getTax()).isEqualTo(DEFAULT_TAX);
        assertThat(testAmountDetails.getShipping()).isEqualTo(DEFAULT_SHIPPING);
        assertThat(testAmountDetails.getHandlingFee()).isEqualTo(DEFAULT_HANDLING_FEE);
        assertThat(testAmountDetails.getShippingDiscount()).isEqualTo(DEFAULT_SHIPPING_DISCOUNT);
        assertThat(testAmountDetails.getInsurance()).isEqualTo(DEFAULT_INSURANCE);
        assertThat(testAmountDetails.getOtherFee()).isEqualTo(DEFAULT_OTHER_FEE);

        // Validate the AmountDetails in Elasticsearch
        verify(mockAmountDetailsSearchRepository, times(1)).save(testAmountDetails);
    }

    @Test
    @Transactional
    public void createAmountDetailsWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = amountDetailsRepository.findAll().size();

        // Create the AmountDetails with an existing ID
        amountDetails.setId(1L);
        AmountDetailsDTO amountDetailsDTO = amountDetailsMapper.toDto(amountDetails);

        // An entity with an existing ID cannot be created, so this API call must fail
        restAmountDetailsMockMvc.perform(post("/api/amount-details")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(amountDetailsDTO)))
            .andExpect(status().isBadRequest());

        // Validate the AmountDetails in the database
        List<AmountDetails> amountDetailsList = amountDetailsRepository.findAll();
        assertThat(amountDetailsList).hasSize(databaseSizeBeforeCreate);

        // Validate the AmountDetails in Elasticsearch
        verify(mockAmountDetailsSearchRepository, times(0)).save(amountDetails);
    }

    @Test
    @Transactional
    public void getAllAmountDetails() throws Exception {
        // Initialize the database
        amountDetailsRepository.saveAndFlush(amountDetails);

        // Get all the amountDetailsList
        restAmountDetailsMockMvc.perform(get("/api/amount-details?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(amountDetails.getId().intValue())))
            .andExpect(jsonPath("$.[*].subtotal").value(hasItem(DEFAULT_SUBTOTAL.doubleValue())))
            .andExpect(jsonPath("$.[*].tax").value(hasItem(DEFAULT_TAX.doubleValue())))
            .andExpect(jsonPath("$.[*].shipping").value(hasItem(DEFAULT_SHIPPING.doubleValue())))
            .andExpect(jsonPath("$.[*].handlingFee").value(hasItem(DEFAULT_HANDLING_FEE.doubleValue())))
            .andExpect(jsonPath("$.[*].shippingDiscount").value(hasItem(DEFAULT_SHIPPING_DISCOUNT.doubleValue())))
            .andExpect(jsonPath("$.[*].insurance").value(hasItem(DEFAULT_INSURANCE.doubleValue())))
            .andExpect(jsonPath("$.[*].otherFee").value(hasItem(DEFAULT_OTHER_FEE.doubleValue())));
    }
    
    @Test
    @Transactional
    public void getAmountDetails() throws Exception {
        // Initialize the database
        amountDetailsRepository.saveAndFlush(amountDetails);

        // Get the amountDetails
        restAmountDetailsMockMvc.perform(get("/api/amount-details/{id}", amountDetails.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(amountDetails.getId().intValue()))
            .andExpect(jsonPath("$.subtotal").value(DEFAULT_SUBTOTAL.doubleValue()))
            .andExpect(jsonPath("$.tax").value(DEFAULT_TAX.doubleValue()))
            .andExpect(jsonPath("$.shipping").value(DEFAULT_SHIPPING.doubleValue()))
            .andExpect(jsonPath("$.handlingFee").value(DEFAULT_HANDLING_FEE.doubleValue()))
            .andExpect(jsonPath("$.shippingDiscount").value(DEFAULT_SHIPPING_DISCOUNT.doubleValue()))
            .andExpect(jsonPath("$.insurance").value(DEFAULT_INSURANCE.doubleValue()))
            .andExpect(jsonPath("$.otherFee").value(DEFAULT_OTHER_FEE.doubleValue()));
    }

    @Test
    @Transactional
    public void getNonExistingAmountDetails() throws Exception {
        // Get the amountDetails
        restAmountDetailsMockMvc.perform(get("/api/amount-details/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateAmountDetails() throws Exception {
        // Initialize the database
        amountDetailsRepository.saveAndFlush(amountDetails);

        int databaseSizeBeforeUpdate = amountDetailsRepository.findAll().size();

        // Update the amountDetails
        AmountDetails updatedAmountDetails = amountDetailsRepository.findById(amountDetails.getId()).get();
        // Disconnect from session so that the updates on updatedAmountDetails are not directly saved in db
        em.detach(updatedAmountDetails);
        updatedAmountDetails
            .subtotal(UPDATED_SUBTOTAL)
            .tax(UPDATED_TAX)
            .shipping(UPDATED_SHIPPING)
            .handlingFee(UPDATED_HANDLING_FEE)
            .shippingDiscount(UPDATED_SHIPPING_DISCOUNT)
            .insurance(UPDATED_INSURANCE)
            .otherFee(UPDATED_OTHER_FEE);
        AmountDetailsDTO amountDetailsDTO = amountDetailsMapper.toDto(updatedAmountDetails);

        restAmountDetailsMockMvc.perform(put("/api/amount-details")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(amountDetailsDTO)))
            .andExpect(status().isOk());

        // Validate the AmountDetails in the database
        List<AmountDetails> amountDetailsList = amountDetailsRepository.findAll();
        assertThat(amountDetailsList).hasSize(databaseSizeBeforeUpdate);
        AmountDetails testAmountDetails = amountDetailsList.get(amountDetailsList.size() - 1);
        assertThat(testAmountDetails.getSubtotal()).isEqualTo(UPDATED_SUBTOTAL);
        assertThat(testAmountDetails.getTax()).isEqualTo(UPDATED_TAX);
        assertThat(testAmountDetails.getShipping()).isEqualTo(UPDATED_SHIPPING);
        assertThat(testAmountDetails.getHandlingFee()).isEqualTo(UPDATED_HANDLING_FEE);
        assertThat(testAmountDetails.getShippingDiscount()).isEqualTo(UPDATED_SHIPPING_DISCOUNT);
        assertThat(testAmountDetails.getInsurance()).isEqualTo(UPDATED_INSURANCE);
        assertThat(testAmountDetails.getOtherFee()).isEqualTo(UPDATED_OTHER_FEE);

        // Validate the AmountDetails in Elasticsearch
        verify(mockAmountDetailsSearchRepository, times(1)).save(testAmountDetails);
    }

    @Test
    @Transactional
    public void updateNonExistingAmountDetails() throws Exception {
        int databaseSizeBeforeUpdate = amountDetailsRepository.findAll().size();

        // Create the AmountDetails
        AmountDetailsDTO amountDetailsDTO = amountDetailsMapper.toDto(amountDetails);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAmountDetailsMockMvc.perform(put("/api/amount-details")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(amountDetailsDTO)))
            .andExpect(status().isBadRequest());

        // Validate the AmountDetails in the database
        List<AmountDetails> amountDetailsList = amountDetailsRepository.findAll();
        assertThat(amountDetailsList).hasSize(databaseSizeBeforeUpdate);

        // Validate the AmountDetails in Elasticsearch
        verify(mockAmountDetailsSearchRepository, times(0)).save(amountDetails);
    }

    @Test
    @Transactional
    public void deleteAmountDetails() throws Exception {
        // Initialize the database
        amountDetailsRepository.saveAndFlush(amountDetails);

        int databaseSizeBeforeDelete = amountDetailsRepository.findAll().size();

        // Get the amountDetails
        restAmountDetailsMockMvc.perform(delete("/api/amount-details/{id}", amountDetails.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<AmountDetails> amountDetailsList = amountDetailsRepository.findAll();
        assertThat(amountDetailsList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the AmountDetails in Elasticsearch
        verify(mockAmountDetailsSearchRepository, times(1)).deleteById(amountDetails.getId());
    }

    @Test
    @Transactional
    public void searchAmountDetails() throws Exception {
        // Initialize the database
        amountDetailsRepository.saveAndFlush(amountDetails);
        when(mockAmountDetailsSearchRepository.search(queryStringQuery("id:" + amountDetails.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(amountDetails), PageRequest.of(0, 1), 1));
        // Search the amountDetails
        restAmountDetailsMockMvc.perform(get("/api/_search/amount-details?query=id:" + amountDetails.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(amountDetails.getId().intValue())))
            .andExpect(jsonPath("$.[*].subtotal").value(hasItem(DEFAULT_SUBTOTAL.doubleValue())))
            .andExpect(jsonPath("$.[*].tax").value(hasItem(DEFAULT_TAX.doubleValue())))
            .andExpect(jsonPath("$.[*].shipping").value(hasItem(DEFAULT_SHIPPING.doubleValue())))
            .andExpect(jsonPath("$.[*].handlingFee").value(hasItem(DEFAULT_HANDLING_FEE.doubleValue())))
            .andExpect(jsonPath("$.[*].shippingDiscount").value(hasItem(DEFAULT_SHIPPING_DISCOUNT.doubleValue())))
            .andExpect(jsonPath("$.[*].insurance").value(hasItem(DEFAULT_INSURANCE.doubleValue())))
            .andExpect(jsonPath("$.[*].otherFee").value(hasItem(DEFAULT_OTHER_FEE.doubleValue())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(AmountDetails.class);
        AmountDetails amountDetails1 = new AmountDetails();
        amountDetails1.setId(1L);
        AmountDetails amountDetails2 = new AmountDetails();
        amountDetails2.setId(amountDetails1.getId());
        assertThat(amountDetails1).isEqualTo(amountDetails2);
        amountDetails2.setId(2L);
        assertThat(amountDetails1).isNotEqualTo(amountDetails2);
        amountDetails1.setId(null);
        assertThat(amountDetails1).isNotEqualTo(amountDetails2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(AmountDetailsDTO.class);
        AmountDetailsDTO amountDetailsDTO1 = new AmountDetailsDTO();
        amountDetailsDTO1.setId(1L);
        AmountDetailsDTO amountDetailsDTO2 = new AmountDetailsDTO();
        assertThat(amountDetailsDTO1).isNotEqualTo(amountDetailsDTO2);
        amountDetailsDTO2.setId(amountDetailsDTO1.getId());
        assertThat(amountDetailsDTO1).isEqualTo(amountDetailsDTO2);
        amountDetailsDTO2.setId(2L);
        assertThat(amountDetailsDTO1).isNotEqualTo(amountDetailsDTO2);
        amountDetailsDTO1.setId(null);
        assertThat(amountDetailsDTO1).isNotEqualTo(amountDetailsDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(amountDetailsMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(amountDetailsMapper.fromId(null)).isNull();
    }
}
