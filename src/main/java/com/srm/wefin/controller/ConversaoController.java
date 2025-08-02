package com.srm.wefin.controller;

import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.srm.wefin.dto.ConversaoRequest;
import com.srm.wefin.dto.ConversaoResponse;
import com.srm.wefin.service.ConversaoService;

@RestController
@RequestMapping("/api/v1/conversoes")
@RequiredArgsConstructor
public class ConversaoController {

	private final ConversaoService conversaoService;

	@PostMapping
	public ResponseEntity<ConversaoResponse> realizarConversao(@RequestBody ConversaoRequest request) {
		ConversaoResponse response = conversaoService.realizarConversao(request);
		return ResponseEntity.status(HttpStatus.CREATED).body(response);
	}
}