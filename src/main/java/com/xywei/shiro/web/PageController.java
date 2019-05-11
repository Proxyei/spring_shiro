package com.xywei.shiro.web;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class PageController {

	@RequestMapping(value = { "/", "home" })
	public String home(Model model) {
		System.out.println("跳转首页");
		Subject subject = SecurityUtils.getSubject();
		if (subject.isRemembered()) {
			System.err.println("使用cookie登录认证了");
			String username = (String) subject.getPrincipal();
			model.addAttribute("username", username);
		}
		return "userHomePage";
	}

	@RequestMapping("/index")
	public String index() {
		return "index";
	}

	@RequestMapping("/forbidden403")
	public String forbidden403() {
		return "forbidden403";
	}

}
