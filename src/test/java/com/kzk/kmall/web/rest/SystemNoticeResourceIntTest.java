package com.kzk.kmall.web.rest;

import com.kzk.kmall.KmallApp;

import com.kzk.kmall.domain.SystemNotice;
import com.kzk.kmall.domain.User;
import com.kzk.kmall.repository.SystemNoticeRepository;
import com.kzk.kmall.service.SystemNoticeService;
import com.kzk.kmall.repository.search.SystemNoticeSearchRepository;
import com.kzk.kmall.web.rest.errors.ExceptionTranslator;
import com.kzk.kmall.service.dto.SystemNoticeCriteria;
import com.kzk.kmall.service.SystemNoticeQueryService;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.List;

import static com.kzk.kmall.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the SystemNoticeResource REST controller.
 *
 * @see SystemNoticeResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = KmallApp.class)
public class SystemNoticeResourceIntTest {

    private static final String DEFAULT_CONTENT = "AAAAAAAAAA";
    private static final String UPDATED_CONTENT = "BBBBBBBBBB";

    private static final String DEFAULT_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_TITLE = "BBBBBBBBBB";

    @Autowired
    private SystemNoticeRepository systemNoticeRepository;

    @Autowired
    private SystemNoticeService systemNoticeService;

    @Autowired
    private SystemNoticeSearchRepository systemNoticeSearchRepository;

    @Autowired
    private SystemNoticeQueryService systemNoticeQueryService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restSystemNoticeMockMvc;

    private SystemNotice systemNotice;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final SystemNoticeResource systemNoticeResource = new SystemNoticeResource(systemNoticeService, systemNoticeQueryService);
        this.restSystemNoticeMockMvc = MockMvcBuilders.standaloneSetup(systemNoticeResource)
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
    public static SystemNotice createEntity(EntityManager em) {
        SystemNotice systemNotice = new SystemNotice()
            .content(DEFAULT_CONTENT)
            .title(DEFAULT_TITLE);
        return systemNotice;
    }

    @Before
    public void initTest() {
        systemNoticeSearchRepository.deleteAll();
        systemNotice = createEntity(em);
    }

    @Test
    @Transactional
    public void createSystemNotice() throws Exception {
        int databaseSizeBeforeCreate = systemNoticeRepository.findAll().size();

        // Create the SystemNotice
        restSystemNoticeMockMvc.perform(post("/api/system-notices")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(systemNotice)))
            .andExpect(status().isCreated());

        // Validate the SystemNotice in the database
        List<SystemNotice> systemNoticeList = systemNoticeRepository.findAll();
        assertThat(systemNoticeList).hasSize(databaseSizeBeforeCreate + 1);
        SystemNotice testSystemNotice = systemNoticeList.get(systemNoticeList.size() - 1);
        assertThat(testSystemNotice.getContent()).isEqualTo(DEFAULT_CONTENT);
        assertThat(testSystemNotice.getTitle()).isEqualTo(DEFAULT_TITLE);

        // Validate the SystemNotice in Elasticsearch
        SystemNotice systemNoticeEs = systemNoticeSearchRepository.findOne(testSystemNotice.getId());
        assertThat(systemNoticeEs).isEqualToIgnoringGivenFields(testSystemNotice);
    }

    @Test
    @Transactional
    public void createSystemNoticeWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = systemNoticeRepository.findAll().size();

        // Create the SystemNotice with an existing ID
        systemNotice.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restSystemNoticeMockMvc.perform(post("/api/system-notices")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(systemNotice)))
            .andExpect(status().isBadRequest());

        // Validate the SystemNotice in the database
        List<SystemNotice> systemNoticeList = systemNoticeRepository.findAll();
        assertThat(systemNoticeList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkContentIsRequired() throws Exception {
        int databaseSizeBeforeTest = systemNoticeRepository.findAll().size();
        // set the field null
        systemNotice.setContent(null);

        // Create the SystemNotice, which fails.

        restSystemNoticeMockMvc.perform(post("/api/system-notices")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(systemNotice)))
            .andExpect(status().isBadRequest());

        List<SystemNotice> systemNoticeList = systemNoticeRepository.findAll();
        assertThat(systemNoticeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkTitleIsRequired() throws Exception {
        int databaseSizeBeforeTest = systemNoticeRepository.findAll().size();
        // set the field null
        systemNotice.setTitle(null);

        // Create the SystemNotice, which fails.

        restSystemNoticeMockMvc.perform(post("/api/system-notices")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(systemNotice)))
            .andExpect(status().isBadRequest());

        List<SystemNotice> systemNoticeList = systemNoticeRepository.findAll();
        assertThat(systemNoticeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllSystemNotices() throws Exception {
        // Initialize the database
        systemNoticeRepository.saveAndFlush(systemNotice);

        // Get all the systemNoticeList
        restSystemNoticeMockMvc.perform(get("/api/system-notices?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(systemNotice.getId().intValue())))
            .andExpect(jsonPath("$.[*].content").value(hasItem(DEFAULT_CONTENT.toString())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE.toString())));
    }

    @Test
    @Transactional
    public void getSystemNotice() throws Exception {
        // Initialize the database
        systemNoticeRepository.saveAndFlush(systemNotice);

        // Get the systemNotice
        restSystemNoticeMockMvc.perform(get("/api/system-notices/{id}", systemNotice.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(systemNotice.getId().intValue()))
            .andExpect(jsonPath("$.content").value(DEFAULT_CONTENT.toString()))
            .andExpect(jsonPath("$.title").value(DEFAULT_TITLE.toString()));
    }

    @Test
    @Transactional
    public void getAllSystemNoticesByContentIsEqualToSomething() throws Exception {
        // Initialize the database
        systemNoticeRepository.saveAndFlush(systemNotice);

        // Get all the systemNoticeList where content equals to DEFAULT_CONTENT
        defaultSystemNoticeShouldBeFound("content.equals=" + DEFAULT_CONTENT);

        // Get all the systemNoticeList where content equals to UPDATED_CONTENT
        defaultSystemNoticeShouldNotBeFound("content.equals=" + UPDATED_CONTENT);
    }

    @Test
    @Transactional
    public void getAllSystemNoticesByContentIsInShouldWork() throws Exception {
        // Initialize the database
        systemNoticeRepository.saveAndFlush(systemNotice);

        // Get all the systemNoticeList where content in DEFAULT_CONTENT or UPDATED_CONTENT
        defaultSystemNoticeShouldBeFound("content.in=" + DEFAULT_CONTENT + "," + UPDATED_CONTENT);

        // Get all the systemNoticeList where content equals to UPDATED_CONTENT
        defaultSystemNoticeShouldNotBeFound("content.in=" + UPDATED_CONTENT);
    }

    @Test
    @Transactional
    public void getAllSystemNoticesByContentIsNullOrNotNull() throws Exception {
        // Initialize the database
        systemNoticeRepository.saveAndFlush(systemNotice);

        // Get all the systemNoticeList where content is not null
        defaultSystemNoticeShouldBeFound("content.specified=true");

        // Get all the systemNoticeList where content is null
        defaultSystemNoticeShouldNotBeFound("content.specified=false");
    }

    @Test
    @Transactional
    public void getAllSystemNoticesByTitleIsEqualToSomething() throws Exception {
        // Initialize the database
        systemNoticeRepository.saveAndFlush(systemNotice);

        // Get all the systemNoticeList where title equals to DEFAULT_TITLE
        defaultSystemNoticeShouldBeFound("title.equals=" + DEFAULT_TITLE);

        // Get all the systemNoticeList where title equals to UPDATED_TITLE
        defaultSystemNoticeShouldNotBeFound("title.equals=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    public void getAllSystemNoticesByTitleIsInShouldWork() throws Exception {
        // Initialize the database
        systemNoticeRepository.saveAndFlush(systemNotice);

        // Get all the systemNoticeList where title in DEFAULT_TITLE or UPDATED_TITLE
        defaultSystemNoticeShouldBeFound("title.in=" + DEFAULT_TITLE + "," + UPDATED_TITLE);

        // Get all the systemNoticeList where title equals to UPDATED_TITLE
        defaultSystemNoticeShouldNotBeFound("title.in=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    public void getAllSystemNoticesByTitleIsNullOrNotNull() throws Exception {
        // Initialize the database
        systemNoticeRepository.saveAndFlush(systemNotice);

        // Get all the systemNoticeList where title is not null
        defaultSystemNoticeShouldBeFound("title.specified=true");

        // Get all the systemNoticeList where title is null
        defaultSystemNoticeShouldNotBeFound("title.specified=false");
    }

    @Test
    @Transactional
    public void getAllSystemNoticesBySendToIsEqualToSomething() throws Exception {
        // Initialize the database
        User sendTo = UserResourceIntTest.createEntity(em);
        em.persist(sendTo);
        em.flush();
        systemNotice.setSendTo(sendTo);
        systemNoticeRepository.saveAndFlush(systemNotice);
        Long sendToId = sendTo.getId();

        // Get all the systemNoticeList where sendTo equals to sendToId
        defaultSystemNoticeShouldBeFound("sendToId.equals=" + sendToId);

        // Get all the systemNoticeList where sendTo equals to sendToId + 1
        defaultSystemNoticeShouldNotBeFound("sendToId.equals=" + (sendToId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned
     */
    private void defaultSystemNoticeShouldBeFound(String filter) throws Exception {
        restSystemNoticeMockMvc.perform(get("/api/system-notices?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(systemNotice.getId().intValue())))
            .andExpect(jsonPath("$.[*].content").value(hasItem(DEFAULT_CONTENT.toString())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE.toString())));
    }

    /**
     * Executes the search, and checks that the default entity is not returned
     */
    private void defaultSystemNoticeShouldNotBeFound(String filter) throws Exception {
        restSystemNoticeMockMvc.perform(get("/api/system-notices?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());
    }


    @Test
    @Transactional
    public void getNonExistingSystemNotice() throws Exception {
        // Get the systemNotice
        restSystemNoticeMockMvc.perform(get("/api/system-notices/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateSystemNotice() throws Exception {
        // Initialize the database
        systemNoticeService.save(systemNotice);

        int databaseSizeBeforeUpdate = systemNoticeRepository.findAll().size();

        // Update the systemNotice
        SystemNotice updatedSystemNotice = systemNoticeRepository.findOne(systemNotice.getId());
        // Disconnect from session so that the updates on updatedSystemNotice are not directly saved in db
        em.detach(updatedSystemNotice);
        updatedSystemNotice
            .content(UPDATED_CONTENT)
            .title(UPDATED_TITLE);

        restSystemNoticeMockMvc.perform(put("/api/system-notices")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedSystemNotice)))
            .andExpect(status().isOk());

        // Validate the SystemNotice in the database
        List<SystemNotice> systemNoticeList = systemNoticeRepository.findAll();
        assertThat(systemNoticeList).hasSize(databaseSizeBeforeUpdate);
        SystemNotice testSystemNotice = systemNoticeList.get(systemNoticeList.size() - 1);
        assertThat(testSystemNotice.getContent()).isEqualTo(UPDATED_CONTENT);
        assertThat(testSystemNotice.getTitle()).isEqualTo(UPDATED_TITLE);

        // Validate the SystemNotice in Elasticsearch
        SystemNotice systemNoticeEs = systemNoticeSearchRepository.findOne(testSystemNotice.getId());
        assertThat(systemNoticeEs).isEqualToIgnoringGivenFields(testSystemNotice);
    }

    @Test
    @Transactional
    public void updateNonExistingSystemNotice() throws Exception {
        int databaseSizeBeforeUpdate = systemNoticeRepository.findAll().size();

        // Create the SystemNotice

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restSystemNoticeMockMvc.perform(put("/api/system-notices")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(systemNotice)))
            .andExpect(status().isCreated());

        // Validate the SystemNotice in the database
        List<SystemNotice> systemNoticeList = systemNoticeRepository.findAll();
        assertThat(systemNoticeList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteSystemNotice() throws Exception {
        // Initialize the database
        systemNoticeService.save(systemNotice);

        int databaseSizeBeforeDelete = systemNoticeRepository.findAll().size();

        // Get the systemNotice
        restSystemNoticeMockMvc.perform(delete("/api/system-notices/{id}", systemNotice.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean systemNoticeExistsInEs = systemNoticeSearchRepository.exists(systemNotice.getId());
        assertThat(systemNoticeExistsInEs).isFalse();

        // Validate the database is empty
        List<SystemNotice> systemNoticeList = systemNoticeRepository.findAll();
        assertThat(systemNoticeList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchSystemNotice() throws Exception {
        // Initialize the database
        systemNoticeService.save(systemNotice);

        // Search the systemNotice
        restSystemNoticeMockMvc.perform(get("/api/_search/system-notices?query=id:" + systemNotice.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(systemNotice.getId().intValue())))
            .andExpect(jsonPath("$.[*].content").value(hasItem(DEFAULT_CONTENT.toString())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE.toString())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(SystemNotice.class);
        SystemNotice systemNotice1 = new SystemNotice();
        systemNotice1.setId(1L);
        SystemNotice systemNotice2 = new SystemNotice();
        systemNotice2.setId(systemNotice1.getId());
        assertThat(systemNotice1).isEqualTo(systemNotice2);
        systemNotice2.setId(2L);
        assertThat(systemNotice1).isNotEqualTo(systemNotice2);
        systemNotice1.setId(null);
        assertThat(systemNotice1).isNotEqualTo(systemNotice2);
    }
}
