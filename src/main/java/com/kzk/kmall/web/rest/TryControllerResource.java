package com.kzk.kmall.web.rest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.codahale.metrics.annotation.Timed;

/**
 * TryController controller
 */
@RestController
@RequestMapping("/api/try-controller")
public class TryControllerResource {

    private final Logger log = LoggerFactory.getLogger(TryControllerResource.class);

    /**
    * GET trySayHello
    */
    @Timed
    @GetMapping("/try-say-hello")
    public String trySayHello() {
        return "trySayHello";
    }

    /**
    * POST tryPostMsg
    */
    @PostMapping("/try-post-msg")
    public String tryPostMsg() {
        return "tryPostMsg";
    }

}
