package com.skye.test.web;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
@Controller
@RequestMapping(value="/hello")
public class HelloController{
	//@RequestMapping("/hello.do")
	@ResponseBody
	public ModelAndView hello(@RequestParam(value = "error", required = false) boolean error,  
            ModelMap model){
		Map<String, Object>  model1 = new HashMap<String, Object>();
		ModelAndView modelAndView = new ModelAndView("test/helloSpringMVC", model1);
		 if (error == true) {  
	            // Assign an error message  
	            model.put("error",  
	                    "You have entered an invalid username or password!");  
	        } else {  
	            model.put("error", "");  
	        } 
		return modelAndView;
	}
	//@RequestMapping("/success.do")
	public String login(@RequestParam(value="username")String username,String age) throws UnsupportedEncodingException{
		//System.out.println("username:"+new String(username.getBytes("iso8859-1"), "utf-8").toString());
		System.out.println("username:"+username);
		System.out.println("age:"+age);
		System.out.println("弟弟");
		return "test/success";
	}
	//@RequestMapping("/denied.do")
	public String denied(){
		
		return "test/deniedpage";
	}
	//@RequestMapping("/admin.do")
	public String admin(){
		
		return "test/admin";
	}
	public String common(){
		
		return "test/common";
	}
	

}
