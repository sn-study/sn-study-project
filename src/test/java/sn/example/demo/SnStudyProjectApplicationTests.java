package sn.example.demo;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.util.Assert;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import sn.example.demo.SnStudyProjectApplication;
import sn.example.demo.controller.PpurigiController;
import sn.example.demo.dto.ResultDto;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.core.Is.is;

import java.util.HashMap;
import java.util.Map;

@SpringBootTest(classes = SnStudyProjectApplication.class)
@AutoConfigureMockMvc
public class SnStudyProjectApplicationTests {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	ObjectMapper objectMapper;

	@Test
	void test() throws Exception{
		mockMvc.perform(MockMvcRequestBuilders.get("/test"))
				.andExpect(status().isOk());

	}
	
	@Test
	void send() throws Exception {
		//given
		String url = "/ppurigi";
		Map<String, Object> params = new HashMap<>();
		params.put("amount", 1000);
		params.put("reqCnt", 2);
		String content = objectMapper.writeValueAsString(params);

		//when
		mockMvc.perform(MockMvcRequestBuilders.post(url)
				.header("X-USER-ID", 12345)
				.header("X-ROOM-ID", "room1")
				.content(content)
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.resultCode", is("SUCCESS")));

		mockMvc.perform(MockMvcRequestBuilders.post(url)
				.header("X-USER-ID", 12345)
				.header("X-ROOM-ID", "room1")
				.content(content)
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.resultCode", is("FAIL")));

		//then
	}

	/*
	token에 해당하는 뿌리기 건 중 아직 누구에게도 할당되지 않은 분배건 하나를 API를 호출한 사용자에게 할당하고, 그 금액을 응답값으로 내려줍니다.
	 */
	@Test
	void receive() throws Exception{
		//given
		long sendUserId = 1;
		String roomId = "room1";
		int amount = 1000;
		int reqCnt = 3;

		//when
		//뿌리기
		Map<String, Object> params = new HashMap<>();
		params.put("amount", amount);
		params.put("reqCnt", reqCnt);
		String content = objectMapper.writeValueAsString(params);

		MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/ppurigi")
				.header("X-USER-ID", sendUserId)
				.header("X-ROOM-ID", roomId)
				.content(content)
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.resultCode", is("SUCCESS")))
				.andReturn();

		ObjectMapper mapper = new ObjectMapper();
		ResultDto resultDto = mapper.readValue(result.getResponse().getContentAsString(), ResultDto.class);
		String token = resultDto.getResult().get("token");

		//받기
		long userId = sendUserId + 1;
		params.clear();
		params.put("token", amount);
		content = objectMapper.writeValueAsString(params);
		result = mockMvc.perform(MockMvcRequestBuilders.post("/receive")
				.header("X-USER-ID", userId)
				.header("X-ROOM-ID", roomId)
				.content(content)
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.resultCode", is("SUCCESS")))
				.andReturn();
		resultDto = mapper.readValue(result.getResponse().getContentAsString(), ResultDto.class);
		int received = Integer.valueOf(resultDto.getResult().get("amount"));
		Assert.isTrue(received > 0 && received <= amount, "받은 금액은 0원 이상이어야 합니다.");

		//뿌리기 당 한 사용자는 한번만 받을 수 있습니다.
		mockMvc.perform(MockMvcRequestBuilders.post("/receive")
				.header("X-USER-ID", userId)
				.header("X-ROOM-ID", roomId)
				.content(content)
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.resultCode", is("FAIL")));

		//자신이 뿌리기한 건은 자신이 받을 수 없습니다.

		//뿌린기가 호출된 대화방과 동일한 대화방에 속한 사용자만이 받을 수 있습니다.

		//뿌린 건은 10분간만 유효합니다. 뿌린지 10분이 지난 요청에 대해서는 받기
		// 실패 응답이 내려가야 합니다.
	}

}
