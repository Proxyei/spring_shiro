package com.xywei.shiro.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class PageController {

	@RequestMapping("/index")
	public String index() {
		return "index";
	}

	@RequestMapping("/forbidden403")
	public String forbidden403() {
		return "forbidden403";
	}

}
