package com.example.demo;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

@SpringBootTest
class SnStudyProjectApplicationTests {

	@Autowired
	MockMvc mockMvc;
	
	@Test
	void send() throws Exception {
		//given
		MultiValueMap<String, String> params = new LinkedMultiValueMap<String, String>();
		params.add("sendUserId", "hannah");
		params.add("amount", "1000");
		params.add("reqCnt", "2");
		
		//when
		mockMvc.perform(post("/send").params(params))
				.andDo(print());
		
		//then
	}

}
