package com.kzk.kmall.service;

import com.kzk.kmall.domain.Goods;
import com.kzk.kmall.repository.GoodsRepository;
import com.kzk.kmall.repository.search.GoodsSearchRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing Goods.
 */
@Service
@Transactional
public class GoodsService {

    private final Logger log = LoggerFactory.getLogger(GoodsService.class);

    private final GoodsRepository goodsRepository;

    private final GoodsSearchRepository goodsSearchRepository;

    public GoodsService(GoodsRepository goodsRepository, GoodsSearchRepository goodsSearchRepository) {
        this.goodsRepository = goodsRepository;
        this.goodsSearchRepository = goodsSearchRepository;
    }

    /**
     * Save a goods.
     *
     * @param goods the entity to save
     * @return the persisted entity
     */
    public Goods save(Goods goods) {
        log.debug("Request to save Goods : {}", goods);
        Goods result = goodsRepository.saveAndFlush(goods);
        goodsSearchRepository.save(result);
        return result;
    }

    /**
     * Get all the goods.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<Goods> findAll(Pageable pageable) {
        log.debug("Request to get all Goods");
        return goodsRepository.findAll(pageable);
    }

    /**
     * Get one goods by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public Goods findOne(Long id) {
        log.debug("Request to get Goods : {}", id);
        return goodsRepository.findOne(id);
    }

    /**
     * Delete the goods by id.
     *
     * @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete Goods : {}", id);
        goodsRepository.delete(id);
        goodsSearchRepository.delete(id);
    }

    /**
     * Search for the goods corresponding to the query.
     *
     * @param query the query of the search
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<Goods> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Goods for query {}", query);
        Page<Goods> result = goodsSearchRepository.search(queryStringQuery(query), pageable);
        return result;
    }
}
