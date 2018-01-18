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

import com.kzk.kmall.domain.Category;
import com.kzk.kmall.domain.*; // for static metamodels
import com.kzk.kmall.repository.CategoryRepository;
import com.kzk.kmall.repository.search.CategorySearchRepository;
import com.kzk.kmall.service.dto.CategoryCriteria;


/**
 * Service for executing complex queries for Category entities in the database.
 * The main input is a {@link CategoryCriteria} which get's converted to {@link Specifications},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link Category} or a {@link Page} of {@link Category} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class CategoryQueryService extends QueryService<Category> {

    private final Logger log = LoggerFactory.getLogger(CategoryQueryService.class);


    private final CategoryRepository categoryRepository;

    private final CategorySearchRepository categorySearchRepository;

    public CategoryQueryService(CategoryRepository categoryRepository, CategorySearchRepository categorySearchRepository) {
        this.categoryRepository = categoryRepository;
        this.categorySearchRepository = categorySearchRepository;
    }

    /**
     * Return a {@link List} of {@link Category} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<Category> findByCriteria(CategoryCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specifications<Category> specification = createSpecification(criteria);
        return categoryRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link Category} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<Category> findByCriteria(CategoryCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specifications<Category> specification = createSpecification(criteria);
        return categoryRepository.findAll(specification, page);
    }

    /**
     * Function to convert CategoryCriteria to a {@link Specifications}
     */
    private Specifications<Category> createSpecification(CategoryCriteria criteria) {
        Specifications<Category> specification = Specifications.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), Category_.id));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), Category_.name));
            }
            if (criteria.getDescription() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDescription(), Category_.description));
            }
            if (criteria.getEnabled() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getEnabled(), Category_.enabled));
            }
            if (criteria.getParentId() != null) {
                specification = specification.and(buildReferringEntitySpecification(criteria.getParentId(), Category_.parent, Category_.id));
            }
        }
        return specification;
    }

}
