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

import com.kzk.kmall.domain.SystemNotice;
import com.kzk.kmall.domain.*; // for static metamodels
import com.kzk.kmall.repository.SystemNoticeRepository;
import com.kzk.kmall.repository.search.SystemNoticeSearchRepository;
import com.kzk.kmall.service.dto.SystemNoticeCriteria;


/**
 * Service for executing complex queries for SystemNotice entities in the database.
 * The main input is a {@link SystemNoticeCriteria} which get's converted to {@link Specifications},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link SystemNotice} or a {@link Page} of {@link SystemNotice} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class SystemNoticeQueryService extends QueryService<SystemNotice> {

    private final Logger log = LoggerFactory.getLogger(SystemNoticeQueryService.class);


    private final SystemNoticeRepository systemNoticeRepository;

    private final SystemNoticeSearchRepository systemNoticeSearchRepository;

    public SystemNoticeQueryService(SystemNoticeRepository systemNoticeRepository, SystemNoticeSearchRepository systemNoticeSearchRepository) {
        this.systemNoticeRepository = systemNoticeRepository;
        this.systemNoticeSearchRepository = systemNoticeSearchRepository;
    }

    /**
     * Return a {@link List} of {@link SystemNotice} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<SystemNotice> findByCriteria(SystemNoticeCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specifications<SystemNotice> specification = createSpecification(criteria);
        return systemNoticeRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link SystemNotice} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<SystemNotice> findByCriteria(SystemNoticeCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specifications<SystemNotice> specification = createSpecification(criteria);
        return systemNoticeRepository.findAll(specification, page);
    }

    /**
     * Function to convert SystemNoticeCriteria to a {@link Specifications}
     */
    private Specifications<SystemNotice> createSpecification(SystemNoticeCriteria criteria) {
        Specifications<SystemNotice> specification = Specifications.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), SystemNotice_.id));
            }
            if (criteria.getContent() != null) {
                specification = specification.and(buildStringSpecification(criteria.getContent(), SystemNotice_.content));
            }
            if (criteria.getTitle() != null) {
                specification = specification.and(buildStringSpecification(criteria.getTitle(), SystemNotice_.title));
            }
            if (criteria.getSendToId() != null) {
                specification = specification.and(buildReferringEntitySpecification(criteria.getSendToId(), SystemNotice_.sendTo, User_.id));
            }
        }
        return specification;
    }

}
