package com.kzk.kmall.service;

import com.kzk.kmall.domain.SystemNotice;
import com.kzk.kmall.repository.SystemNoticeRepository;
import com.kzk.kmall.repository.search.SystemNoticeSearchRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing SystemNotice.
 */
@Service
@Transactional
public class SystemNoticeService {

    private final Logger log = LoggerFactory.getLogger(SystemNoticeService.class);

    private final SystemNoticeRepository systemNoticeRepository;

    private final SystemNoticeSearchRepository systemNoticeSearchRepository;

    public SystemNoticeService(SystemNoticeRepository systemNoticeRepository, SystemNoticeSearchRepository systemNoticeSearchRepository) {
        this.systemNoticeRepository = systemNoticeRepository;
        this.systemNoticeSearchRepository = systemNoticeSearchRepository;
    }

    /**
     * Save a systemNotice.
     *
     * @param systemNotice the entity to save
     * @return the persisted entity
     */
    public SystemNotice save(SystemNotice systemNotice) {
        log.debug("Request to save SystemNotice : {}", systemNotice);
        log.debug(">>>>>>Request to save SystemNotice : {} {}", systemNotice.getCreatedDate(),systemNotice.getLastModifiedDate());
        SystemNotice result = systemNoticeRepository.save(systemNotice);
        log.debug(">>>>>>Request to save SystemNotice : {} {}", systemNotice.getCreatedDate(),systemNotice.getLastModifiedDate());
        log.debug(">>>>>>Request to save result : {} {}", result.getCreatedDate(),result.getLastModifiedDate());
        systemNoticeSearchRepository.save(result);
        log.debug(">>>>>>Request to save result : {} {}", result.getCreatedDate(),result.getLastModifiedDate());
        return result;
    }

    /**
     * Get all the systemNotices.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<SystemNotice> findAll(Pageable pageable) {
        log.debug("Request to get all SystemNotices");
        return systemNoticeRepository.findAll(pageable);
    }

    /**
     * Get one systemNotice by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public SystemNotice findOne(Long id) {
        log.debug("Request to get SystemNotice : {}", id);
        return systemNoticeRepository.findOne(id);
    }

    /**
     * Delete the systemNotice by id.
     *
     * @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete SystemNotice : {}", id);
        systemNoticeRepository.delete(id);
        systemNoticeSearchRepository.delete(id);
    }

    /**
     * Search for the systemNotice corresponding to the query.
     *
     * @param query the query of the search
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<SystemNotice> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of SystemNotices for query {}", query);
        Page<SystemNotice> result = systemNoticeSearchRepository.search(queryStringQuery(query), pageable);
        return result;
    }
}
