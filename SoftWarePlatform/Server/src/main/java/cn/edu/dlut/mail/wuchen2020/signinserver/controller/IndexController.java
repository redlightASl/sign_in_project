package cn.edu.dlut.mail.wuchen2020.signinserver.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class IndexController {
	
	@RequestMapping("/")
	public String index() {
		return "Welcome to Sign In project!";
	}
	
}
