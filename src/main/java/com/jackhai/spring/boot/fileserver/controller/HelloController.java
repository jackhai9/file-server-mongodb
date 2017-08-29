package com.jackhai.spring.boot.fileserver.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author jackhai
 * @date 2017年8月26日
 */
@RestController
public class HelloController {

	@RequestMapping("/love")
	public String hello() {
		return "哈哈 love";
	}
}
