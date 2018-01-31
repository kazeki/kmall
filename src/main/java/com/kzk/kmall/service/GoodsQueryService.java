package com.kzk.kmall.service;


import java.math.BigDecimal;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specifications;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.github.jhipster.service.QueryService;

import com.kzk.kmall.domain.Goods;
import com.kzk.kmall.domain.*; // for static metamodels
import com.kzk.kmall.repository.GoodsRepository;
import com.kzk.kmall.repository.search.GoodsSearchRepository;
import com.kzk.kmall.service.dto.GoodsCriteria;


/**
 * Service for executing complex queries for Goods entities in the database.
 * The main input is a {@link GoodsCriteria} which get's converted to {@link Specifications},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link Goods} or a {@link Page} of {@link Goods} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class GoodsQueryService extends QueryService<Goods> {

    private final Logger log = LoggerFactory.getLogger(GoodsQueryService.class);


    private final GoodsRepository goodsRepository;

    private final GoodsSearchRepository goodsSearchRepository;

    public GoodsQueryService(GoodsRepository goodsRepository, GoodsSearchRepository goodsSearchRepository) {
        this.goodsRepository = goodsRepository;
        this.goodsSearchRepository = goodsSearchRepository;
    }

    /**
     * Return a {@link List} of {@link Goods} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<Goods> findByCriteria(GoodsCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specifications<Goods> specification = createSpecification(criteria);
        return goodsRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link Goods} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<Goods> findByCriteria(GoodsCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specifications<Goods> specification = createSpecification(criteria);
        return goodsRepository.findAll(specification, page);
    }

    /**
     * Function to convert GoodsCriteria to a {@link Specifications}
     */
    private Specifications<Goods> createSpecification(GoodsCriteria criteria) {
        Specifications<Goods> specification = Specifications.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), Goods_.id));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), Goods_.name));
            }
            if (criteria.getDescription() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDescription(), Goods_.description));
            }
            if (criteria.getPrice() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getPrice(), Goods_.price));
            }
            if (criteria.getShopId() != null) {
                specification = specification.and(buildReferringEntitySpecification(criteria.getShopId(), Goods_.shop, Shop_.id));
            }
            if (criteria.getCreateById() != null) {
                specification = specification.and(buildReferringEntitySpecification(criteria.getCreateById(), Goods_.createBy, User_.id));
            }
            if (criteria.getCategoryId() != null) {
                specification = specification.and(buildReferringEntitySpecification(criteria.getCategoryId(), Goods_.category, Category_.id));
            }
        }
        return specification;
    }

}
