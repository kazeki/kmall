package com.kzk.kmall.service;


import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specifications;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.github.jhipster.service.QueryService;

import com.kzk.kmall.domain.Shop;
import com.kzk.kmall.domain.*; // for static metamodels
import com.kzk.kmall.repository.ShopRepository;
import com.kzk.kmall.repository.search.ShopSearchRepository;
import com.kzk.kmall.service.dto.ShopCriteria;


/**
 * Service for executing complex queries for Shop entities in the database.
 * The main input is a {@link ShopCriteria} which get's converted to {@link Specifications},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link Shop} or a {@link Page} of {@link Shop} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class ShopQueryService extends QueryService<Shop> {

    private final Logger log = LoggerFactory.getLogger(ShopQueryService.class);


    private final ShopRepository shopRepository;

    private final ShopSearchRepository shopSearchRepository;

    public ShopQueryService(ShopRepository shopRepository, ShopSearchRepository shopSearchRepository) {
        this.shopRepository = shopRepository;
        this.shopSearchRepository = shopSearchRepository;
    }

    /**
     * Return a {@link List} of {@link Shop} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<Shop> findByCriteria(ShopCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specifications<Shop> specification = createSpecification(criteria);
        return shopRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link Shop} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<Shop> findByCriteria(ShopCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specifications<Shop> specification = createSpecification(criteria);
        return shopRepository.findAll(specification, page);
    }

    /**
     * Function to convert ShopCriteria to a {@link Specifications}
     */
    private Specifications<Shop> createSpecification(ShopCriteria criteria) {
        Specifications<Shop> specification = Specifications.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), Shop_.id));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), Shop_.name));
            }
            if (criteria.getDescription() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDescription(), Shop_.description));
            }
            if (criteria.getMasterId() != null) {
                specification = specification.and(buildReferringEntitySpecification(criteria.getMasterId(), Shop_.master, User_.id));
            }
        }
        return specification;
    }

}
