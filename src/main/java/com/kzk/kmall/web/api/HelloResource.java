package com.kzk.kmall.web.api;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kzk.kmall.web.api.HelloApi;
import com.kzk.kmall.web.api.model.Msg;

@RestController
@RequestMapping("/api")
public class HelloResource implements HelloApi {
	@Override
	public ResponseEntity<Msg> sayHello(String name, String role) {
		Msg msg = new Msg();
		msg.setId(111);
		msg.setRole(Msg.RoleEnum.ADMIN);
		msg.setName("kazeki");
		return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(msg);
	}
}