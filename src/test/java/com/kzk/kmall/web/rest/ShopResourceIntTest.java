package com.kzk.kmall.web.rest;

import com.kzk.kmall.KmallApp;

import com.kzk.kmall.domain.Shop;
import com.kzk.kmall.domain.User;
import com.kzk.kmall.repository.ShopRepository;
import com.kzk.kmall.service.ShopService;
import com.kzk.kmall.repository.search.ShopSearchRepository;
import com.kzk.kmall.web.rest.errors.ExceptionTranslator;
import com.kzk.kmall.service.dto.ShopCriteria;
import com.kzk.kmall.service.ShopQueryService;

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
 * Test class for the ShopResource REST controller.
 *
 * @see ShopResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = KmallApp.class)
public class ShopResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    @Autowired
    private ShopRepository shopRepository;

    @Autowired
    private ShopService shopService;

    @Autowired
    private ShopSearchRepository shopSearchRepository;

    @Autowired
    private ShopQueryService shopQueryService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restShopMockMvc;

    private Shop shop;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final ShopResource shopResource = new ShopResource(shopService, shopQueryService);
        this.restShopMockMvc = MockMvcBuilders.standaloneSetup(shopResource)
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
    public static Shop createEntity(EntityManager em) {
        Shop shop = new Shop()
            .name(DEFAULT_NAME)
            .description(DEFAULT_DESCRIPTION);
        // Add required entity
        User master = UserResourceIntTest.createEntity(em);
        em.persist(master);
        em.flush();
        shop.setMaster(master);
        return shop;
    }

    @Before
    public void initTest() {
        shopSearchRepository.deleteAll();
        shop = createEntity(em);
    }

    @Test
    @Transactional
    public void createShop() throws Exception {
        int databaseSizeBeforeCreate = shopRepository.findAll().size();

        // Create the Shop
        restShopMockMvc.perform(post("/api/shops")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(shop)))
            .andExpect(status().isCreated());

        // Validate the Shop in the database
        List<Shop> shopList = shopRepository.findAll();
        assertThat(shopList).hasSize(databaseSizeBeforeCreate + 1);
        Shop testShop = shopList.get(shopList.size() - 1);
        assertThat(testShop.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testShop.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);

        // Validate the Shop in Elasticsearch
        Shop shopEs = shopSearchRepository.findOne(testShop.getId());
        assertThat(shopEs).isEqualToIgnoringGivenFields(testShop);
    }

    @Test
    @Transactional
    public void createShopWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = shopRepository.findAll().size();

        // Create the Shop with an existing ID
        shop.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restShopMockMvc.perform(post("/api/shops")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(shop)))
            .andExpect(status().isBadRequest());

        // Validate the Shop in the database
        List<Shop> shopList = shopRepository.findAll();
        assertThat(shopList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = shopRepository.findAll().size();
        // set the field null
        shop.setName(null);

        // Create the Shop, which fails.

        restShopMockMvc.perform(post("/api/shops")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(shop)))
            .andExpect(status().isBadRequest());

        List<Shop> shopList = shopRepository.findAll();
        assertThat(shopList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllShops() throws Exception {
        // Initialize the database
        shopRepository.saveAndFlush(shop);

        // Get all the shopList
        restShopMockMvc.perform(get("/api/shops?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(shop.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())));
    }

    @Test
    @Transactional
    public void getShop() throws Exception {
        // Initialize the database
        shopRepository.saveAndFlush(shop);

        // Get the shop
        restShopMockMvc.perform(get("/api/shops/{id}", shop.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(shop.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()));
    }

    @Test
    @Transactional
    public void getAllShopsByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        shopRepository.saveAndFlush(shop);

        // Get all the shopList where name equals to DEFAULT_NAME
        defaultShopShouldBeFound("name.equals=" + DEFAULT_NAME);

        // Get all the shopList where name equals to UPDATED_NAME
        defaultShopShouldNotBeFound("name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllShopsByNameIsInShouldWork() throws Exception {
        // Initialize the database
        shopRepository.saveAndFlush(shop);

        // Get all the shopList where name in DEFAULT_NAME or UPDATED_NAME
        defaultShopShouldBeFound("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME);

        // Get all the shopList where name equals to UPDATED_NAME
        defaultShopShouldNotBeFound("name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllShopsByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        shopRepository.saveAndFlush(shop);

        // Get all the shopList where name is not null
        defaultShopShouldBeFound("name.specified=true");

        // Get all the shopList where name is null
        defaultShopShouldNotBeFound("name.specified=false");
    }

    @Test
    @Transactional
    public void getAllShopsByDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        shopRepository.saveAndFlush(shop);

        // Get all the shopList where description equals to DEFAULT_DESCRIPTION
        defaultShopShouldBeFound("description.equals=" + DEFAULT_DESCRIPTION);

        // Get all the shopList where description equals to UPDATED_DESCRIPTION
        defaultShopShouldNotBeFound("description.equals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllShopsByDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        shopRepository.saveAndFlush(shop);

        // Get all the shopList where description in DEFAULT_DESCRIPTION or UPDATED_DESCRIPTION
        defaultShopShouldBeFound("description.in=" + DEFAULT_DESCRIPTION + "," + UPDATED_DESCRIPTION);

        // Get all the shopList where description equals to UPDATED_DESCRIPTION
        defaultShopShouldNotBeFound("description.in=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllShopsByDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        shopRepository.saveAndFlush(shop);

        // Get all the shopList where description is not null
        defaultShopShouldBeFound("description.specified=true");

        // Get all the shopList where description is null
        defaultShopShouldNotBeFound("description.specified=false");
    }

    @Test
    @Transactional
    public void getAllShopsByMasterIsEqualToSomething() throws Exception {
        // Initialize the database
        User master = UserResourceIntTest.createEntity(em);
        em.persist(master);
        em.flush();
        shop.setMaster(master);
        shopRepository.saveAndFlush(shop);
        Long masterId = master.getId();

        // Get all the shopList where master equals to masterId
        defaultShopShouldBeFound("masterId.equals=" + masterId);

        // Get all the shopList where master equals to masterId + 1
        defaultShopShouldNotBeFound("masterId.equals=" + (masterId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned
     */
    private void defaultShopShouldBeFound(String filter) throws Exception {
        restShopMockMvc.perform(get("/api/shops?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(shop.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())));
    }

    /**
     * Executes the search, and checks that the default entity is not returned
     */
    private void defaultShopShouldNotBeFound(String filter) throws Exception {
        restShopMockMvc.perform(get("/api/shops?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());
    }


    @Test
    @Transactional
    public void getNonExistingShop() throws Exception {
        // Get the shop
        restShopMockMvc.perform(get("/api/shops/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateShop() throws Exception {
        // Initialize the database
        shopService.save(shop);

        int databaseSizeBeforeUpdate = shopRepository.findAll().size();

        // Update the shop
        Shop updatedShop = shopRepository.findOne(shop.getId());
        // Disconnect from session so that the updates on updatedShop are not directly saved in db
        em.detach(updatedShop);
        updatedShop
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION);

        restShopMockMvc.perform(put("/api/shops")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedShop)))
            .andExpect(status().isOk());

        // Validate the Shop in the database
        List<Shop> shopList = shopRepository.findAll();
        assertThat(shopList).hasSize(databaseSizeBeforeUpdate);
        Shop testShop = shopList.get(shopList.size() - 1);
        assertThat(testShop.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testShop.getDescription()).isEqualTo(UPDATED_DESCRIPTION);

        // Validate the Shop in Elasticsearch
        Shop shopEs = shopSearchRepository.findOne(testShop.getId());
        assertThat(shopEs).isEqualToIgnoringGivenFields(testShop);
    }

    @Test
    @Transactional
    public void updateNonExistingShop() throws Exception {
        int databaseSizeBeforeUpdate = shopRepository.findAll().size();

        // Create the Shop

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restShopMockMvc.perform(put("/api/shops")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(shop)))
            .andExpect(status().isCreated());

        // Validate the Shop in the database
        List<Shop> shopList = shopRepository.findAll();
        assertThat(shopList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteShop() throws Exception {
        // Initialize the database
        shopService.save(shop);

        int databaseSizeBeforeDelete = shopRepository.findAll().size();

        // Get the shop
        restShopMockMvc.perform(delete("/api/shops/{id}", shop.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean shopExistsInEs = shopSearchRepository.exists(shop.getId());
        assertThat(shopExistsInEs).isFalse();

        // Validate the database is empty
        List<Shop> shopList = shopRepository.findAll();
        assertThat(shopList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchShop() throws Exception {
        // Initialize the database
        shopService.save(shop);

        // Search the shop
        restShopMockMvc.perform(get("/api/_search/shops?query=id:" + shop.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(shop.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Shop.class);
        Shop shop1 = new Shop();
        shop1.setId(1L);
        Shop shop2 = new Shop();
        shop2.setId(shop1.getId());
        assertThat(shop1).isEqualTo(shop2);
        shop2.setId(2L);
        assertThat(shop1).isNotEqualTo(shop2);
        shop1.setId(null);
        assertThat(shop1).isNotEqualTo(shop2);
    }
}
