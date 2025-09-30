package kr.co.lunasoft.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import kr.co.lunasoft.service.TokenService;
import kr.co.lunasoft.vo.UserInfo;

@RestController
@RequestMapping("/token")
public class TokenController {

	private static final String HEADER = "Authorization";
	
	@Autowired
	TokenService tokenService;

	@GetMapping(value = "/sign")
	public Map<String, Object> sign(HttpServletResponse response, @RequestParam String id, @RequestParam String pwd) {
		Map<String, Object> obj = new HashMap<String, Object>();

		UserInfo userInfo = tokenService.compare(id, pwd);
		if (userInfo == null) {
			obj.put("code", "100104");
			obj.put("msg", "fail user");
			obj.put("data", "");
		} else {
			String token = tokenService.sign(userInfo);
			obj.put("code", "100200");
			obj.put("msg", "success");
			obj.put("data", token);
			response.setHeader(HEADER, token);
		}

		return obj;
	}

	@GetMapping(value = "/verify")
	public Map<String, Object> verify(HttpServletRequest request) {
		String token = request.getHeader(HEADER);
		boolean isLogin = false;
		if(token != null) {
			isLogin = tokenService.verify(token);
		}
		Map<String, Object> obj = new HashMap<String, Object>();
		obj.put("code", "100200");
		obj.put("msg", "success");
		obj.put("data", isLogin);
		return obj;
	}

}
