package com.kzk.kmall.web.rest;

import com.kzk.kefu.core.KefuMeiMeiService;
import com.kzk.kmall.web.api.KefuHelpApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.codahale.metrics.annotation.Timed;
import com.kzk.kmall.web.api.model.Msg;

@RestController
@RequestMapping("/api")
public class KefuHelpResource implements KefuHelpApi {
    @Autowired
    KefuMeiMeiService kefuMeiMeiService;

    @Override
    @Timed
    public ResponseEntity<Msg> ask(String question) {
        String msgContent = kefuMeiMeiService.talk(question);
        Msg msg = new Msg();
        msg.setContent(msgContent);
        return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(msg);
    }
}
