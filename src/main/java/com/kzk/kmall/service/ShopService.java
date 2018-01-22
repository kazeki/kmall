package com.kzk.kmall.service;

import com.kzk.kmall.domain.Shop;
import com.kzk.kmall.repository.ShopRepository;
import com.kzk.kmall.repository.search.ShopSearchRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing Shop.
 */
@Service
@Transactional
public class ShopService {

    private final Logger log = LoggerFactory.getLogger(ShopService.class);

    private final ShopRepository shopRepository;

    private final ShopSearchRepository shopSearchRepository;

    public ShopService(ShopRepository shopRepository, ShopSearchRepository shopSearchRepository) {
        this.shopRepository = shopRepository;
        this.shopSearchRepository = shopSearchRepository;
    }

    /**
     * Save a shop.
     *
     * @param shop the entity to save
     * @return the persisted entity
     */
    public Shop save(Shop shop) {
        log.debug("Request to save Shop : {}", shop);
        Shop result = shopRepository.saveAndFlush(shop);
        shopSearchRepository.save(result);
        return result;
    }

    /**
     * Get all the shops.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<Shop> findAll(Pageable pageable) {
        log.debug("Request to get all Shops");
        return shopRepository.findAll(pageable);
    }

    /**
     * Get one shop by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public Shop findOne(Long id) {
        log.debug("Request to get Shop : {}", id);
        return shopRepository.findOne(id);
    }

    /**
     * Delete the shop by id.
     *
     * @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete Shop : {}", id);
        shopRepository.delete(id);
        shopSearchRepository.delete(id);
    }

    /**
     * Search for the shop corresponding to the query.
     *
     * @param query the query of the search
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<Shop> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Shops for query {}", query);
        Page<Shop> result = shopSearchRepository.search(queryStringQuery(query), pageable);
        return result;
    }
}
