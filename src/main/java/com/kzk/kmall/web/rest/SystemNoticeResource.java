package com.kzk.kmall.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.kzk.kmall.domain.SystemNotice;
import com.kzk.kmall.service.SystemNoticeService;
import com.kzk.kmall.web.rest.errors.BadRequestAlertException;
import com.kzk.kmall.web.rest.util.HeaderUtil;
import com.kzk.kmall.web.rest.util.PaginationUtil;
import com.kzk.kmall.service.dto.SystemNoticeCriteria;
import com.kzk.kmall.service.SystemNoticeQueryService;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing SystemNotice.
 */
@RestController
@RequestMapping("/api")
public class SystemNoticeResource {

    private final Logger log = LoggerFactory.getLogger(SystemNoticeResource.class);

    private static final String ENTITY_NAME = "systemNotice";

    private final SystemNoticeService systemNoticeService;

    private final SystemNoticeQueryService systemNoticeQueryService;

    public SystemNoticeResource(SystemNoticeService systemNoticeService, SystemNoticeQueryService systemNoticeQueryService) {
        this.systemNoticeService = systemNoticeService;
        this.systemNoticeQueryService = systemNoticeQueryService;
    }

    /**
     * POST  /system-notices : Create a new systemNotice.
     *
     * @param systemNotice the systemNotice to create
     * @return the ResponseEntity with status 201 (Created) and with body the new systemNotice, or with status 400 (Bad Request) if the systemNotice has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/system-notices")
    @Timed
    public ResponseEntity<SystemNotice> createSystemNotice(@Valid @RequestBody SystemNotice systemNotice) throws URISyntaxException {
        log.debug("REST request to save SystemNotice : {}", systemNotice);
        if (systemNotice.getId() != null) {
            throw new BadRequestAlertException("A new systemNotice cannot already have an ID", ENTITY_NAME, "idexists");
        }
        SystemNotice result = systemNoticeService.save(systemNotice);
        return ResponseEntity.created(new URI("/api/system-notices/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /system-notices : Updates an existing systemNotice.
     *
     * @param systemNotice the systemNotice to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated systemNotice,
     * or with status 400 (Bad Request) if the systemNotice is not valid,
     * or with status 500 (Internal Server Error) if the systemNotice couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/system-notices")
    @Timed
    public ResponseEntity<SystemNotice> updateSystemNotice(@Valid @RequestBody SystemNotice systemNotice) throws URISyntaxException {
        log.debug("REST request to update SystemNotice : {}", systemNotice);
        if (systemNotice.getId() == null) {
            return createSystemNotice(systemNotice);
        }
        SystemNotice result = systemNoticeService.save(systemNotice);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, systemNotice.getId().toString()))
            .body(result);
    }

    /**
     * GET  /system-notices : get all the systemNotices.
     *
     * @param pageable the pagination information
     * @param criteria the criterias which the requested entities should match
     * @return the ResponseEntity with status 200 (OK) and the list of systemNotices in body
     */
    @GetMapping("/system-notices")
    @Timed
    public ResponseEntity<List<SystemNotice>> getAllSystemNotices(SystemNoticeCriteria criteria, Pageable pageable) {
        log.debug("REST request to get SystemNotices by criteria: {}", criteria);
        Page<SystemNotice> page = systemNoticeQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/system-notices");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /system-notices/:id : get the "id" systemNotice.
     *
     * @param id the id of the systemNotice to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the systemNotice, or with status 404 (Not Found)
     */
    @GetMapping("/system-notices/{id}")
    @Timed
    public ResponseEntity<SystemNotice> getSystemNotice(@PathVariable Long id) {
        log.debug("REST request to get SystemNotice : {}", id);
        SystemNotice systemNotice = systemNoticeService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(systemNotice));
    }

    /**
     * DELETE  /system-notices/:id : delete the "id" systemNotice.
     *
     * @param id the id of the systemNotice to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/system-notices/{id}")
    @Timed
    public ResponseEntity<Void> deleteSystemNotice(@PathVariable Long id) {
        log.debug("REST request to delete SystemNotice : {}", id);
        systemNoticeService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/system-notices?query=:query : search for the systemNotice corresponding
     * to the query.
     *
     * @param query the query of the systemNotice search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/system-notices")
    @Timed
    public ResponseEntity<List<SystemNotice>> searchSystemNotices(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of SystemNotices for query {}", query);
        Page<SystemNotice> page = systemNoticeService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/system-notices");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

}
