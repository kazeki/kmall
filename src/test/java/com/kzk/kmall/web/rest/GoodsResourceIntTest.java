package com.kzk.kmall.web.rest;

import com.kzk.kmall.KmallApp;

import com.kzk.kmall.domain.Goods;
import com.kzk.kmall.domain.Shop;
import com.kzk.kmall.domain.User;
import com.kzk.kmall.domain.Category;
import com.kzk.kmall.repository.GoodsRepository;
import com.kzk.kmall.service.GoodsService;
import com.kzk.kmall.repository.search.GoodsSearchRepository;
import com.kzk.kmall.web.rest.errors.ExceptionTranslator;
import com.kzk.kmall.service.dto.GoodsCriteria;
import com.kzk.kmall.service.GoodsQueryService;

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
import java.math.BigDecimal;
import java.util.List;

import static com.kzk.kmall.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the GoodsResource REST controller.
 *
 * @see GoodsResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = KmallApp.class)
public class GoodsResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final BigDecimal DEFAULT_PRICE = new BigDecimal(0);
    private static final BigDecimal UPDATED_PRICE = new BigDecimal(1);

    @Autowired
    private GoodsRepository goodsRepository;

    @Autowired
    private GoodsService goodsService;

    @Autowired
    private GoodsSearchRepository goodsSearchRepository;

    @Autowired
    private GoodsQueryService goodsQueryService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restGoodsMockMvc;

    private Goods goods;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final GoodsResource goodsResource = new GoodsResource(goodsService, goodsQueryService);
        this.restGoodsMockMvc = MockMvcBuilders.standaloneSetup(goodsResource)
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
    public static Goods createEntity(EntityManager em) {
        Goods goods = new Goods()
            .name(DEFAULT_NAME)
            .description(DEFAULT_DESCRIPTION)
            .price(DEFAULT_PRICE);
        // Add required entity
        Shop shop = ShopResourceIntTest.createEntity(em);
        em.persist(shop);
        em.flush();
        goods.setShop(shop);
        // Add required entity
        Category category = CategoryResourceIntTest.createEntity(em);
        em.persist(category);
        em.flush();
        goods.setCategory(category);
        return goods;
    }

    @Before
    public void initTest() {
        goodsSearchRepository.deleteAll();
        goods = createEntity(em);
    }

    @Test
    @Transactional
    public void createGoods() throws Exception {
        int databaseSizeBeforeCreate = goodsRepository.findAll().size();

        // Create the Goods
        restGoodsMockMvc.perform(post("/api/goods")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(goods)))
            .andExpect(status().isCreated());

        // Validate the Goods in the database
        List<Goods> goodsList = goodsRepository.findAll();
        assertThat(goodsList).hasSize(databaseSizeBeforeCreate + 1);
        Goods testGoods = goodsList.get(goodsList.size() - 1);
        assertThat(testGoods.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testGoods.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testGoods.getPrice()).isEqualTo(DEFAULT_PRICE);

        // Validate the Goods in Elasticsearch
        Goods goodsEs = goodsSearchRepository.findOne(testGoods.getId());
        assertThat(goodsEs).isEqualToIgnoringGivenFields(testGoods);
    }

    @Test
    @Transactional
    public void createGoodsWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = goodsRepository.findAll().size();

        // Create the Goods with an existing ID
        goods.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restGoodsMockMvc.perform(post("/api/goods")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(goods)))
            .andExpect(status().isBadRequest());

        // Validate the Goods in the database
        List<Goods> goodsList = goodsRepository.findAll();
        assertThat(goodsList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = goodsRepository.findAll().size();
        // set the field null
        goods.setName(null);

        // Create the Goods, which fails.

        restGoodsMockMvc.perform(post("/api/goods")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(goods)))
            .andExpect(status().isBadRequest());

        List<Goods> goodsList = goodsRepository.findAll();
        assertThat(goodsList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkPriceIsRequired() throws Exception {
        int databaseSizeBeforeTest = goodsRepository.findAll().size();
        // set the field null
        goods.setPrice(null);

        // Create the Goods, which fails.

        restGoodsMockMvc.perform(post("/api/goods")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(goods)))
            .andExpect(status().isBadRequest());

        List<Goods> goodsList = goodsRepository.findAll();
        assertThat(goodsList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllGoods() throws Exception {
        // Initialize the database
        goodsRepository.saveAndFlush(goods);

        // Get all the goodsList
        restGoodsMockMvc.perform(get("/api/goods?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(goods.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].price").value(hasItem(DEFAULT_PRICE.intValue())));
    }

    @Test
    @Transactional
    public void getGoods() throws Exception {
        // Initialize the database
        goodsRepository.saveAndFlush(goods);

        // Get the goods
        restGoodsMockMvc.perform(get("/api/goods/{id}", goods.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(goods.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()))
            .andExpect(jsonPath("$.price").value(DEFAULT_PRICE.intValue()));
    }

    @Test
    @Transactional
    public void getAllGoodsByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        goodsRepository.saveAndFlush(goods);

        // Get all the goodsList where name equals to DEFAULT_NAME
        defaultGoodsShouldBeFound("name.equals=" + DEFAULT_NAME);

        // Get all the goodsList where name equals to UPDATED_NAME
        defaultGoodsShouldNotBeFound("name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllGoodsByNameIsInShouldWork() throws Exception {
        // Initialize the database
        goodsRepository.saveAndFlush(goods);

        // Get all the goodsList where name in DEFAULT_NAME or UPDATED_NAME
        defaultGoodsShouldBeFound("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME);

        // Get all the goodsList where name equals to UPDATED_NAME
        defaultGoodsShouldNotBeFound("name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllGoodsByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        goodsRepository.saveAndFlush(goods);

        // Get all the goodsList where name is not null
        defaultGoodsShouldBeFound("name.specified=true");

        // Get all the goodsList where name is null
        defaultGoodsShouldNotBeFound("name.specified=false");
    }

    @Test
    @Transactional
    public void getAllGoodsByDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        goodsRepository.saveAndFlush(goods);

        // Get all the goodsList where description equals to DEFAULT_DESCRIPTION
        defaultGoodsShouldBeFound("description.equals=" + DEFAULT_DESCRIPTION);

        // Get all the goodsList where description equals to UPDATED_DESCRIPTION
        defaultGoodsShouldNotBeFound("description.equals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllGoodsByDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        goodsRepository.saveAndFlush(goods);

        // Get all the goodsList where description in DEFAULT_DESCRIPTION or UPDATED_DESCRIPTION
        defaultGoodsShouldBeFound("description.in=" + DEFAULT_DESCRIPTION + "," + UPDATED_DESCRIPTION);

        // Get all the goodsList where description equals to UPDATED_DESCRIPTION
        defaultGoodsShouldNotBeFound("description.in=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllGoodsByDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        goodsRepository.saveAndFlush(goods);

        // Get all the goodsList where description is not null
        defaultGoodsShouldBeFound("description.specified=true");

        // Get all the goodsList where description is null
        defaultGoodsShouldNotBeFound("description.specified=false");
    }

    @Test
    @Transactional
    public void getAllGoodsByPriceIsEqualToSomething() throws Exception {
        // Initialize the database
        goodsRepository.saveAndFlush(goods);

        // Get all the goodsList where price equals to DEFAULT_PRICE
        defaultGoodsShouldBeFound("price.equals=" + DEFAULT_PRICE);

        // Get all the goodsList where price equals to UPDATED_PRICE
        defaultGoodsShouldNotBeFound("price.equals=" + UPDATED_PRICE);
    }

    @Test
    @Transactional
    public void getAllGoodsByPriceIsInShouldWork() throws Exception {
        // Initialize the database
        goodsRepository.saveAndFlush(goods);

        // Get all the goodsList where price in DEFAULT_PRICE or UPDATED_PRICE
        defaultGoodsShouldBeFound("price.in=" + DEFAULT_PRICE + "," + UPDATED_PRICE);

        // Get all the goodsList where price equals to UPDATED_PRICE
        defaultGoodsShouldNotBeFound("price.in=" + UPDATED_PRICE);
    }

    @Test
    @Transactional
    public void getAllGoodsByPriceIsNullOrNotNull() throws Exception {
        // Initialize the database
        goodsRepository.saveAndFlush(goods);

        // Get all the goodsList where price is not null
        defaultGoodsShouldBeFound("price.specified=true");

        // Get all the goodsList where price is null
        defaultGoodsShouldNotBeFound("price.specified=false");
    }

    @Test
    @Transactional
    public void getAllGoodsByShopIsEqualToSomething() throws Exception {
        // Initialize the database
        Shop shop = ShopResourceIntTest.createEntity(em);
        em.persist(shop);
        em.flush();
        goods.setShop(shop);
        goodsRepository.saveAndFlush(goods);
        Long shopId = shop.getId();

        // Get all the goodsList where shop equals to shopId
        defaultGoodsShouldBeFound("shopId.equals=" + shopId);

        // Get all the goodsList where shop equals to shopId + 1
        defaultGoodsShouldNotBeFound("shopId.equals=" + (shopId + 1));
    }


    @Test
    @Transactional
    public void getAllGoodsByCreateByIsEqualToSomething() throws Exception {
        // Initialize the database
        User createBy = UserResourceIntTest.createEntity(em);
        em.persist(createBy);
        em.flush();
        goods.setCreateBy(createBy);
        goodsRepository.saveAndFlush(goods);
        Long createById = createBy.getId();

        // Get all the goodsList where createBy equals to createById
        defaultGoodsShouldBeFound("createById.equals=" + createById);

        // Get all the goodsList where createBy equals to createById + 1
        defaultGoodsShouldNotBeFound("createById.equals=" + (createById + 1));
    }


    @Test
    @Transactional
    public void getAllGoodsByCategoryIsEqualToSomething() throws Exception {
        // Initialize the database
        Category category = CategoryResourceIntTest.createEntity(em);
        em.persist(category);
        em.flush();
        goods.setCategory(category);
        goodsRepository.saveAndFlush(goods);
        Long categoryId = category.getId();

        // Get all the goodsList where category equals to categoryId
        defaultGoodsShouldBeFound("categoryId.equals=" + categoryId);

        // Get all the goodsList where category equals to categoryId + 1
        defaultGoodsShouldNotBeFound("categoryId.equals=" + (categoryId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned
     */
    private void defaultGoodsShouldBeFound(String filter) throws Exception {
        restGoodsMockMvc.perform(get("/api/goods?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(goods.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].price").value(hasItem(DEFAULT_PRICE.intValue())));
    }

    /**
     * Executes the search, and checks that the default entity is not returned
     */
    private void defaultGoodsShouldNotBeFound(String filter) throws Exception {
        restGoodsMockMvc.perform(get("/api/goods?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());
    }


    @Test
    @Transactional
    public void getNonExistingGoods() throws Exception {
        // Get the goods
        restGoodsMockMvc.perform(get("/api/goods/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateGoods() throws Exception {
        // Initialize the database
        goodsService.save(goods);

        int databaseSizeBeforeUpdate = goodsRepository.findAll().size();

        // Update the goods
        Goods updatedGoods = goodsRepository.findOne(goods.getId());
        // Disconnect from session so that the updates on updatedGoods are not directly saved in db
        em.detach(updatedGoods);
        updatedGoods
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .price(UPDATED_PRICE);

        restGoodsMockMvc.perform(put("/api/goods")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedGoods)))
            .andExpect(status().isOk());

        // Validate the Goods in the database
        List<Goods> goodsList = goodsRepository.findAll();
        assertThat(goodsList).hasSize(databaseSizeBeforeUpdate);
        Goods testGoods = goodsList.get(goodsList.size() - 1);
        assertThat(testGoods.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testGoods.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testGoods.getPrice()).isEqualTo(UPDATED_PRICE);

        // Validate the Goods in Elasticsearch
        Goods goodsEs = goodsSearchRepository.findOne(testGoods.getId());
        assertThat(goodsEs).isEqualToIgnoringGivenFields(testGoods);
    }

    @Test
    @Transactional
    public void updateNonExistingGoods() throws Exception {
        int databaseSizeBeforeUpdate = goodsRepository.findAll().size();

        // Create the Goods

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restGoodsMockMvc.perform(put("/api/goods")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(goods)))
            .andExpect(status().isCreated());

        // Validate the Goods in the database
        List<Goods> goodsList = goodsRepository.findAll();
        assertThat(goodsList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteGoods() throws Exception {
        // Initialize the database
        goodsService.save(goods);

        int databaseSizeBeforeDelete = goodsRepository.findAll().size();

        // Get the goods
        restGoodsMockMvc.perform(delete("/api/goods/{id}", goods.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean goodsExistsInEs = goodsSearchRepository.exists(goods.getId());
        assertThat(goodsExistsInEs).isFalse();

        // Validate the database is empty
        List<Goods> goodsList = goodsRepository.findAll();
        assertThat(goodsList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchGoods() throws Exception {
        // Initialize the database
        goodsService.save(goods);

        // Search the goods
        restGoodsMockMvc.perform(get("/api/_search/goods?query=id:" + goods.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(goods.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].price").value(hasItem(DEFAULT_PRICE.intValue())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Goods.class);
        Goods goods1 = new Goods();
        goods1.setId(1L);
        Goods goods2 = new Goods();
        goods2.setId(goods1.getId());
        assertThat(goods1).isEqualTo(goods2);
        goods2.setId(2L);
        assertThat(goods1).isNotEqualTo(goods2);
        goods1.setId(null);
        assertThat(goods1).isNotEqualTo(goods2);
    }
}
