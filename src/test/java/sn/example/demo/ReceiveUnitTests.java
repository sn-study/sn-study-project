package sn.example.demo;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.util.Assert;
import sn.example.demo.dto.ResultDto;

import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.core.Is.is;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest(classes = SnStudyProjectApplication.class)
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ReceiveUnitTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    private static String token;

    @BeforeAll
    @DisplayName("0.뿌리기 시 발급된 token을 요청값으로 받습니다.")
    public void beforeAll() throws Exception {
        //given
        String url = "/ppurigi";
        long userId = 1;
        String roomId = "room1";
        int amount = 1000;
        int reqCnt = 3;

        //when, then
        Map<String, Object> params = new HashMap<>();
        params.put("amount", amount);
        params.put("reqCnt", reqCnt);
        String content = objectMapper.writeValueAsString(params);

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post(url)
                .header("X-USER-ID", userId)
                .header("X-ROOM-ID", roomId)
                .content(content)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.resultCode", is("SUCCESS")))
                .andReturn();

        ObjectMapper mapper = new ObjectMapper();
        ResultDto resultDto = mapper.readValue(result.getResponse().getContentAsString(), ResultDto.class);
        token = resultDto.getResult().get("token");
    }

    @Test
    @DisplayName("1.token에 해당하는 뿌리기 건 중 아직 누구에게도 할당되지 않은 분배건 하나를 API를 호출한 사용자에게 할당하고, 그 금액을 응답값으로 내려줍니다.")
    public void test1() throws Exception {
        //given
        String url = "/ppurigi";
        long userId = 2;
        String roomId = "room1";

        //when, then
        Map<String, Object> params = new HashMap<>();
        params.put("token", token);
        String content = objectMapper.writeValueAsString(params);

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.put(url)
                .header("X-USER-ID", userId)
                .header("X-ROOM-ID", roomId)
                .content(content)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.resultCode", is("SUCCESS")))
                .andReturn();

        ObjectMapper mapper = new ObjectMapper();
        ResultDto resultDto = mapper.readValue(result.getResponse().getContentAsString(), ResultDto.class);
        int amount = Integer.valueOf(resultDto.getResult().get("amount"));

        Assert.isTrue(amount > 0, "받은 금액은 1원 이상이어야 합니다.");
    }

    @Test
    @DisplayName("2.뿌리기 당 한 사용자는 한번만 받을 수 있습니다.")
    public void test2() throws Exception {
        //given
        String url = "/ppurigi";
        long userId = 3;
        String roomId = "room1";

        //when, then
        Map<String, Object> params = new HashMap<>();
        params.put("token", token);
        String content = objectMapper.writeValueAsString(params);

        mockMvc.perform(MockMvcRequestBuilders.put(url)
                .header("X-USER-ID", userId)
                .header("X-ROOM-ID", roomId)
                .content(content)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.resultCode", is("SUCCESS")));

        mockMvc.perform(MockMvcRequestBuilders.put(url)
                .header("X-USER-ID", userId)
                .header("X-ROOM-ID", roomId)
                .content(content)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.resultCode", is("FAIL")));
    }

    @Test
    @DisplayName("3.자신이 뿌리기한 건은 자신이 받을 수 없습니다.")
    public void test3() throws Exception {
        //given
        String url = "/ppurigi";
        long userId = 1;
        String roomId = "room1";

        //when, then
        Map<String, Object> params = new HashMap<>();
        params.put("token", token);
        String content = objectMapper.writeValueAsString(params);

        mockMvc.perform(MockMvcRequestBuilders.put(url)
                .header("X-USER-ID", userId)
                .header("X-ROOM-ID", roomId)
                .content(content)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.resultCode", is("FAIL")));
    }

    @Test
    @DisplayName("4.뿌리기가 호출된 대화방과 동일한 대화방에 속한 사용자만이 받을 수 있습니다.")
    public void test4() throws Exception {
        //given
        String url = "/ppurigi";
        long userId = 4;
        String roomId = "room2";

        //when, then
        Map<String, Object> params = new HashMap<>();
        params.put("token", token);
        String content = objectMapper.writeValueAsString(params);

        mockMvc.perform(MockMvcRequestBuilders.put(url)
                .header("X-USER-ID", userId)
                .header("X-ROOM-ID", roomId)
                .content(content)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.resultCode", is("FAIL")));
    }

    @Test
    @DisplayName("5.뿌린 건은 10분간만 유효합니다. 뿌린지 10분이 지난 요청에 대해서는 받기 실패 응답이 내려가야 합니다.")
    public void test5() throws Exception {
        //given
        String url = "/ppurigi";
        long userId = 4;
        String roomId = "room_2";

        //when, then
        Map<String, Object> params = new HashMap<>();
        params.put("token", "DEF");
        String content = objectMapper.writeValueAsString(params);

        mockMvc.perform(MockMvcRequestBuilders.put(url)
                .header("X-USER-ID", userId)
                .header("X-ROOM-ID", roomId)
                .content(content)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.resultCode", is("FAIL")));
    }

    @Test
    @DisplayName("6.분배가 끝난 뿌리기 요청은 실패를 응답해야 합니다.")
    public void test6() throws Exception {
        //given
        String url = "/ppurigi";
        long userId = 4;
        String roomId = "room_1";

        //when, then
        Map<String, Object> params = new HashMap<>();
        params.put("token", "ABC");
        String content = objectMapper.writeValueAsString(params);

        mockMvc.perform(MockMvcRequestBuilders.put(url)
                .header("X-USER-ID", userId)
                .header("X-ROOM-ID", roomId)
                .content(content)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.resultCode", is("FAIL")));
    }
}
