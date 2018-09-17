package com.bytatech.ayoos.payment.web.rest;

import com.bytatech.ayoos.payment.PaymentApp;

import com.bytatech.ayoos.payment.domain.Payee;
import com.bytatech.ayoos.payment.repository.PayeeRepository;
import com.bytatech.ayoos.payment.repository.search.PayeeSearchRepository;
import com.bytatech.ayoos.payment.service.PayeeService;
import com.bytatech.ayoos.payment.service.dto.PayeeDTO;
import com.bytatech.ayoos.payment.service.mapper.PayeeMapper;
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
 * Test class for the PayeeResource REST controller.
 *
 * @see PayeeResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = PaymentApp.class)
public class PayeeResourceIntTest {

    private static final String DEFAULT_USER_ID = "AAAAAAAAAA";
    private static final String UPDATED_USER_ID = "BBBBBBBBBB";

    private static final String DEFAULT_MERCHAND_ID = "AAAAAAAAAA";
    private static final String UPDATED_MERCHAND_ID = "BBBBBBBBBB";

    private static final String DEFAULT_EMAIL = "AAAAAAAAAA";
    private static final String UPDATED_EMAIL = "BBBBBBBBBB";

    @Autowired
    private PayeeRepository payeeRepository;

    @Autowired
    private PayeeMapper payeeMapper;
    
    @Autowired
    private PayeeService payeeService;

    /**
     * This repository is mocked in the com.bytatech.ayoos.payment.repository.search test package.
     *
     * @see com.bytatech.ayoos.payment.repository.search.PayeeSearchRepositoryMockConfiguration
     */
    @Autowired
    private PayeeSearchRepository mockPayeeSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restPayeeMockMvc;

    private Payee payee;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final PayeeResource payeeResource = new PayeeResource(payeeService);
        this.restPayeeMockMvc = MockMvcBuilders.standaloneSetup(payeeResource)
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
    public static Payee createEntity(EntityManager em) {
        Payee payee = new Payee()
            .userId(DEFAULT_USER_ID)
            .merchandId(DEFAULT_MERCHAND_ID)
            .email(DEFAULT_EMAIL);
        return payee;
    }

    @Before
    public void initTest() {
        payee = createEntity(em);
    }

    @Test
    @Transactional
    public void createPayee() throws Exception {
        int databaseSizeBeforeCreate = payeeRepository.findAll().size();

        // Create the Payee
        PayeeDTO payeeDTO = payeeMapper.toDto(payee);
        restPayeeMockMvc.perform(post("/api/payees")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(payeeDTO)))
            .andExpect(status().isCreated());

        // Validate the Payee in the database
        List<Payee> payeeList = payeeRepository.findAll();
        assertThat(payeeList).hasSize(databaseSizeBeforeCreate + 1);
        Payee testPayee = payeeList.get(payeeList.size() - 1);
        assertThat(testPayee.getUserId()).isEqualTo(DEFAULT_USER_ID);
        assertThat(testPayee.getMerchandId()).isEqualTo(DEFAULT_MERCHAND_ID);
        assertThat(testPayee.getEmail()).isEqualTo(DEFAULT_EMAIL);

        // Validate the Payee in Elasticsearch
        verify(mockPayeeSearchRepository, times(1)).save(testPayee);
    }

    @Test
    @Transactional
    public void createPayeeWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = payeeRepository.findAll().size();

        // Create the Payee with an existing ID
        payee.setId(1L);
        PayeeDTO payeeDTO = payeeMapper.toDto(payee);

        // An entity with an existing ID cannot be created, so this API call must fail
        restPayeeMockMvc.perform(post("/api/payees")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(payeeDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Payee in the database
        List<Payee> payeeList = payeeRepository.findAll();
        assertThat(payeeList).hasSize(databaseSizeBeforeCreate);

        // Validate the Payee in Elasticsearch
        verify(mockPayeeSearchRepository, times(0)).save(payee);
    }

    @Test
    @Transactional
    public void getAllPayees() throws Exception {
        // Initialize the database
        payeeRepository.saveAndFlush(payee);

        // Get all the payeeList
        restPayeeMockMvc.perform(get("/api/payees?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(payee.getId().intValue())))
            .andExpect(jsonPath("$.[*].userId").value(hasItem(DEFAULT_USER_ID.toString())))
            .andExpect(jsonPath("$.[*].merchandId").value(hasItem(DEFAULT_MERCHAND_ID.toString())))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL.toString())));
    }
    
    @Test
    @Transactional
    public void getPayee() throws Exception {
        // Initialize the database
        payeeRepository.saveAndFlush(payee);

        // Get the payee
        restPayeeMockMvc.perform(get("/api/payees/{id}", payee.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(payee.getId().intValue()))
            .andExpect(jsonPath("$.userId").value(DEFAULT_USER_ID.toString()))
            .andExpect(jsonPath("$.merchandId").value(DEFAULT_MERCHAND_ID.toString()))
            .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingPayee() throws Exception {
        // Get the payee
        restPayeeMockMvc.perform(get("/api/payees/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updatePayee() throws Exception {
        // Initialize the database
        payeeRepository.saveAndFlush(payee);

        int databaseSizeBeforeUpdate = payeeRepository.findAll().size();

        // Update the payee
        Payee updatedPayee = payeeRepository.findById(payee.getId()).get();
        // Disconnect from session so that the updates on updatedPayee are not directly saved in db
        em.detach(updatedPayee);
        updatedPayee
            .userId(UPDATED_USER_ID)
            .merchandId(UPDATED_MERCHAND_ID)
            .email(UPDATED_EMAIL);
        PayeeDTO payeeDTO = payeeMapper.toDto(updatedPayee);

        restPayeeMockMvc.perform(put("/api/payees")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(payeeDTO)))
            .andExpect(status().isOk());

        // Validate the Payee in the database
        List<Payee> payeeList = payeeRepository.findAll();
        assertThat(payeeList).hasSize(databaseSizeBeforeUpdate);
        Payee testPayee = payeeList.get(payeeList.size() - 1);
        assertThat(testPayee.getUserId()).isEqualTo(UPDATED_USER_ID);
        assertThat(testPayee.getMerchandId()).isEqualTo(UPDATED_MERCHAND_ID);
        assertThat(testPayee.getEmail()).isEqualTo(UPDATED_EMAIL);

        // Validate the Payee in Elasticsearch
        verify(mockPayeeSearchRepository, times(1)).save(testPayee);
    }

    @Test
    @Transactional
    public void updateNonExistingPayee() throws Exception {
        int databaseSizeBeforeUpdate = payeeRepository.findAll().size();

        // Create the Payee
        PayeeDTO payeeDTO = payeeMapper.toDto(payee);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPayeeMockMvc.perform(put("/api/payees")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(payeeDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Payee in the database
        List<Payee> payeeList = payeeRepository.findAll();
        assertThat(payeeList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Payee in Elasticsearch
        verify(mockPayeeSearchRepository, times(0)).save(payee);
    }

    @Test
    @Transactional
    public void deletePayee() throws Exception {
        // Initialize the database
        payeeRepository.saveAndFlush(payee);

        int databaseSizeBeforeDelete = payeeRepository.findAll().size();

        // Get the payee
        restPayeeMockMvc.perform(delete("/api/payees/{id}", payee.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Payee> payeeList = payeeRepository.findAll();
        assertThat(payeeList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the Payee in Elasticsearch
        verify(mockPayeeSearchRepository, times(1)).deleteById(payee.getId());
    }

    @Test
    @Transactional
    public void searchPayee() throws Exception {
        // Initialize the database
        payeeRepository.saveAndFlush(payee);
        when(mockPayeeSearchRepository.search(queryStringQuery("id:" + payee.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(payee), PageRequest.of(0, 1), 1));
        // Search the payee
        restPayeeMockMvc.perform(get("/api/_search/payees?query=id:" + payee.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(payee.getId().intValue())))
            .andExpect(jsonPath("$.[*].userId").value(hasItem(DEFAULT_USER_ID.toString())))
            .andExpect(jsonPath("$.[*].merchandId").value(hasItem(DEFAULT_MERCHAND_ID.toString())))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL.toString())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Payee.class);
        Payee payee1 = new Payee();
        payee1.setId(1L);
        Payee payee2 = new Payee();
        payee2.setId(payee1.getId());
        assertThat(payee1).isEqualTo(payee2);
        payee2.setId(2L);
        assertThat(payee1).isNotEqualTo(payee2);
        payee1.setId(null);
        assertThat(payee1).isNotEqualTo(payee2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(PayeeDTO.class);
        PayeeDTO payeeDTO1 = new PayeeDTO();
        payeeDTO1.setId(1L);
        PayeeDTO payeeDTO2 = new PayeeDTO();
        assertThat(payeeDTO1).isNotEqualTo(payeeDTO2);
        payeeDTO2.setId(payeeDTO1.getId());
        assertThat(payeeDTO1).isEqualTo(payeeDTO2);
        payeeDTO2.setId(2L);
        assertThat(payeeDTO1).isNotEqualTo(payeeDTO2);
        payeeDTO1.setId(null);
        assertThat(payeeDTO1).isNotEqualTo(payeeDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(payeeMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(payeeMapper.fromId(null)).isNull();
    }
}
