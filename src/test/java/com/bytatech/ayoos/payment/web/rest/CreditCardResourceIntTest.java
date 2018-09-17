package com.bytatech.ayoos.payment.web.rest;

import com.bytatech.ayoos.payment.PaymentApp;

import com.bytatech.ayoos.payment.domain.CreditCard;
import com.bytatech.ayoos.payment.repository.CreditCardRepository;
import com.bytatech.ayoos.payment.repository.search.CreditCardSearchRepository;
import com.bytatech.ayoos.payment.service.CreditCardService;
import com.bytatech.ayoos.payment.service.dto.CreditCardDTO;
import com.bytatech.ayoos.payment.service.mapper.CreditCardMapper;
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
 * Test class for the CreditCardResource REST controller.
 *
 * @see CreditCardResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = PaymentApp.class)
public class CreditCardResourceIntTest {

    private static final String DEFAULT_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_NUMBER = "BBBBBBBBBB";

    private static final String DEFAULT_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_TYPE = "BBBBBBBBBB";

    private static final Integer DEFAULT_EXPIRE_MONTH = 1;
    private static final Integer UPDATED_EXPIRE_MONTH = 2;

    private static final Integer DEFAULT_EXPIRE_YEAR = 1;
    private static final Integer UPDATED_EXPIRE_YEAR = 2;

    private static final Integer DEFAULT_CVV_2 = 1;
    private static final Integer UPDATED_CVV_2 = 2;

    @Autowired
    private CreditCardRepository creditCardRepository;

    @Autowired
    private CreditCardMapper creditCardMapper;
    
    @Autowired
    private CreditCardService creditCardService;

    /**
     * This repository is mocked in the com.bytatech.ayoos.payment.repository.search test package.
     *
     * @see com.bytatech.ayoos.payment.repository.search.CreditCardSearchRepositoryMockConfiguration
     */
    @Autowired
    private CreditCardSearchRepository mockCreditCardSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restCreditCardMockMvc;

    private CreditCard creditCard;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final CreditCardResource creditCardResource = new CreditCardResource(creditCardService);
        this.restCreditCardMockMvc = MockMvcBuilders.standaloneSetup(creditCardResource)
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
    public static CreditCard createEntity(EntityManager em) {
        CreditCard creditCard = new CreditCard()
            .number(DEFAULT_NUMBER)
            .type(DEFAULT_TYPE)
            .expireMonth(DEFAULT_EXPIRE_MONTH)
            .expireYear(DEFAULT_EXPIRE_YEAR)
            .cvv2(DEFAULT_CVV_2);
        return creditCard;
    }

    @Before
    public void initTest() {
        creditCard = createEntity(em);
    }

    @Test
    @Transactional
    public void createCreditCard() throws Exception {
        int databaseSizeBeforeCreate = creditCardRepository.findAll().size();

        // Create the CreditCard
        CreditCardDTO creditCardDTO = creditCardMapper.toDto(creditCard);
        restCreditCardMockMvc.perform(post("/api/credit-cards")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(creditCardDTO)))
            .andExpect(status().isCreated());

        // Validate the CreditCard in the database
        List<CreditCard> creditCardList = creditCardRepository.findAll();
        assertThat(creditCardList).hasSize(databaseSizeBeforeCreate + 1);
        CreditCard testCreditCard = creditCardList.get(creditCardList.size() - 1);
        assertThat(testCreditCard.getNumber()).isEqualTo(DEFAULT_NUMBER);
        assertThat(testCreditCard.getType()).isEqualTo(DEFAULT_TYPE);
        assertThat(testCreditCard.getExpireMonth()).isEqualTo(DEFAULT_EXPIRE_MONTH);
        assertThat(testCreditCard.getExpireYear()).isEqualTo(DEFAULT_EXPIRE_YEAR);
        assertThat(testCreditCard.getCvv2()).isEqualTo(DEFAULT_CVV_2);

        // Validate the CreditCard in Elasticsearch
        verify(mockCreditCardSearchRepository, times(1)).save(testCreditCard);
    }

    @Test
    @Transactional
    public void createCreditCardWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = creditCardRepository.findAll().size();

        // Create the CreditCard with an existing ID
        creditCard.setId(1L);
        CreditCardDTO creditCardDTO = creditCardMapper.toDto(creditCard);

        // An entity with an existing ID cannot be created, so this API call must fail
        restCreditCardMockMvc.perform(post("/api/credit-cards")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(creditCardDTO)))
            .andExpect(status().isBadRequest());

        // Validate the CreditCard in the database
        List<CreditCard> creditCardList = creditCardRepository.findAll();
        assertThat(creditCardList).hasSize(databaseSizeBeforeCreate);

        // Validate the CreditCard in Elasticsearch
        verify(mockCreditCardSearchRepository, times(0)).save(creditCard);
    }

    @Test
    @Transactional
    public void getAllCreditCards() throws Exception {
        // Initialize the database
        creditCardRepository.saveAndFlush(creditCard);

        // Get all the creditCardList
        restCreditCardMockMvc.perform(get("/api/credit-cards?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(creditCard.getId().intValue())))
            .andExpect(jsonPath("$.[*].number").value(hasItem(DEFAULT_NUMBER.toString())))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].expireMonth").value(hasItem(DEFAULT_EXPIRE_MONTH)))
            .andExpect(jsonPath("$.[*].expireYear").value(hasItem(DEFAULT_EXPIRE_YEAR)))
            .andExpect(jsonPath("$.[*].cvv2").value(hasItem(DEFAULT_CVV_2)));
    }
    
    @Test
    @Transactional
    public void getCreditCard() throws Exception {
        // Initialize the database
        creditCardRepository.saveAndFlush(creditCard);

        // Get the creditCard
        restCreditCardMockMvc.perform(get("/api/credit-cards/{id}", creditCard.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(creditCard.getId().intValue()))
            .andExpect(jsonPath("$.number").value(DEFAULT_NUMBER.toString()))
            .andExpect(jsonPath("$.type").value(DEFAULT_TYPE.toString()))
            .andExpect(jsonPath("$.expireMonth").value(DEFAULT_EXPIRE_MONTH))
            .andExpect(jsonPath("$.expireYear").value(DEFAULT_EXPIRE_YEAR))
            .andExpect(jsonPath("$.cvv2").value(DEFAULT_CVV_2));
    }

    @Test
    @Transactional
    public void getNonExistingCreditCard() throws Exception {
        // Get the creditCard
        restCreditCardMockMvc.perform(get("/api/credit-cards/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateCreditCard() throws Exception {
        // Initialize the database
        creditCardRepository.saveAndFlush(creditCard);

        int databaseSizeBeforeUpdate = creditCardRepository.findAll().size();

        // Update the creditCard
        CreditCard updatedCreditCard = creditCardRepository.findById(creditCard.getId()).get();
        // Disconnect from session so that the updates on updatedCreditCard are not directly saved in db
        em.detach(updatedCreditCard);
        updatedCreditCard
            .number(UPDATED_NUMBER)
            .type(UPDATED_TYPE)
            .expireMonth(UPDATED_EXPIRE_MONTH)
            .expireYear(UPDATED_EXPIRE_YEAR)
            .cvv2(UPDATED_CVV_2);
        CreditCardDTO creditCardDTO = creditCardMapper.toDto(updatedCreditCard);

        restCreditCardMockMvc.perform(put("/api/credit-cards")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(creditCardDTO)))
            .andExpect(status().isOk());

        // Validate the CreditCard in the database
        List<CreditCard> creditCardList = creditCardRepository.findAll();
        assertThat(creditCardList).hasSize(databaseSizeBeforeUpdate);
        CreditCard testCreditCard = creditCardList.get(creditCardList.size() - 1);
        assertThat(testCreditCard.getNumber()).isEqualTo(UPDATED_NUMBER);
        assertThat(testCreditCard.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testCreditCard.getExpireMonth()).isEqualTo(UPDATED_EXPIRE_MONTH);
        assertThat(testCreditCard.getExpireYear()).isEqualTo(UPDATED_EXPIRE_YEAR);
        assertThat(testCreditCard.getCvv2()).isEqualTo(UPDATED_CVV_2);

        // Validate the CreditCard in Elasticsearch
        verify(mockCreditCardSearchRepository, times(1)).save(testCreditCard);
    }

    @Test
    @Transactional
    public void updateNonExistingCreditCard() throws Exception {
        int databaseSizeBeforeUpdate = creditCardRepository.findAll().size();

        // Create the CreditCard
        CreditCardDTO creditCardDTO = creditCardMapper.toDto(creditCard);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCreditCardMockMvc.perform(put("/api/credit-cards")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(creditCardDTO)))
            .andExpect(status().isBadRequest());

        // Validate the CreditCard in the database
        List<CreditCard> creditCardList = creditCardRepository.findAll();
        assertThat(creditCardList).hasSize(databaseSizeBeforeUpdate);

        // Validate the CreditCard in Elasticsearch
        verify(mockCreditCardSearchRepository, times(0)).save(creditCard);
    }

    @Test
    @Transactional
    public void deleteCreditCard() throws Exception {
        // Initialize the database
        creditCardRepository.saveAndFlush(creditCard);

        int databaseSizeBeforeDelete = creditCardRepository.findAll().size();

        // Get the creditCard
        restCreditCardMockMvc.perform(delete("/api/credit-cards/{id}", creditCard.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<CreditCard> creditCardList = creditCardRepository.findAll();
        assertThat(creditCardList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the CreditCard in Elasticsearch
        verify(mockCreditCardSearchRepository, times(1)).deleteById(creditCard.getId());
    }

    @Test
    @Transactional
    public void searchCreditCard() throws Exception {
        // Initialize the database
        creditCardRepository.saveAndFlush(creditCard);
        when(mockCreditCardSearchRepository.search(queryStringQuery("id:" + creditCard.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(creditCard), PageRequest.of(0, 1), 1));
        // Search the creditCard
        restCreditCardMockMvc.perform(get("/api/_search/credit-cards?query=id:" + creditCard.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(creditCard.getId().intValue())))
            .andExpect(jsonPath("$.[*].number").value(hasItem(DEFAULT_NUMBER.toString())))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].expireMonth").value(hasItem(DEFAULT_EXPIRE_MONTH)))
            .andExpect(jsonPath("$.[*].expireYear").value(hasItem(DEFAULT_EXPIRE_YEAR)))
            .andExpect(jsonPath("$.[*].cvv2").value(hasItem(DEFAULT_CVV_2)));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(CreditCard.class);
        CreditCard creditCard1 = new CreditCard();
        creditCard1.setId(1L);
        CreditCard creditCard2 = new CreditCard();
        creditCard2.setId(creditCard1.getId());
        assertThat(creditCard1).isEqualTo(creditCard2);
        creditCard2.setId(2L);
        assertThat(creditCard1).isNotEqualTo(creditCard2);
        creditCard1.setId(null);
        assertThat(creditCard1).isNotEqualTo(creditCard2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(CreditCardDTO.class);
        CreditCardDTO creditCardDTO1 = new CreditCardDTO();
        creditCardDTO1.setId(1L);
        CreditCardDTO creditCardDTO2 = new CreditCardDTO();
        assertThat(creditCardDTO1).isNotEqualTo(creditCardDTO2);
        creditCardDTO2.setId(creditCardDTO1.getId());
        assertThat(creditCardDTO1).isEqualTo(creditCardDTO2);
        creditCardDTO2.setId(2L);
        assertThat(creditCardDTO1).isNotEqualTo(creditCardDTO2);
        creditCardDTO1.setId(null);
        assertThat(creditCardDTO1).isNotEqualTo(creditCardDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(creditCardMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(creditCardMapper.fromId(null)).isNull();
    }
}
