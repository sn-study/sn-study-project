package sn.example.demo;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.core.Is.is;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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
}
