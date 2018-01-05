package com.kzk.kmall.web.api;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.codahale.metrics.annotation.Timed;
import com.kzk.kmall.domain.User;
import com.kzk.kmall.web.api.HelloApi;
import com.kzk.kmall.web.api.model.Msg;

@RestController
@RequestMapping("/api/v22")
public class HelloResource implements HelloApi {
	@Override
	@Timed
	public ResponseEntity<Msg> sayHello(String name, String role) {
		Msg msg = new Msg();
		msg.setId(111);
		msg.setRole(Msg.RoleEnum.ADMIN);
		msg.setName("kazeki");
		return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(msg);
	}
	
	@Timed
	@GetMapping("/sayHello2")
	public ResponseEntity<User> sayHello2(String name, String role) {
		User msg = new User();
		return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(msg);
	}
}