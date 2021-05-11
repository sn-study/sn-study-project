package sn.example.demo;


import com.fasterxml.jackson.core.JsonProcessingException;
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
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import sn.example.demo.dto.ResultDto;

import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.core.Is.is;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = SnStudyProjectApplication.class)
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ListUnitTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    static String token;

    @BeforeAll
    void getToken() throws Exception {
        //뿌리기를 한번 해서 return된 token값을 가져오고 싶어...
        String url = "/ppurigi";
        Map<String, Object> params = new HashMap<>();
        params.put("amount", 1000);
        params.put("reqCnt", 2);
        String content = objectMapper.writeValueAsString(params);
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post(url)
                .header("X-USER-ID", 12345)
                .header("X-ROOM-ID", "room1")
                .content(content)
                .contentType(MediaType.APPLICATION_JSON))
                .andReturn();
        String response = result.getResponse().getContentAsString();
        ObjectMapper mapper = new ObjectMapper();
        token = mapper.readValue(response, ResultDto.class).getResult().get("token");
        System.out.println("토큰받아졌니" + token);
    }

    @Test
    @DisplayName("조회 테스트 - 성공")
    void list_ok() throws Exception {
        // 뿌리기 시 발급된 token + 뿌린사람 요청값으로 받습니다.
        // 뿌린시각,뿌린금액,받기완료된금액,받기완료된정보([받은금액,받은 사용자 아이디] 리스트)
        // 다른사람의 뿌리기건이나 유효하지 않은 token에 대해서는 조회 실패 응답이 내려가야 합니다.
        // 뿌린건에 대한 조회는 7일동안 할 수 있습니다.

        //given
        String url = "/ppurigi";
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("token", token);
        //String content = objectMapper.writeValueAsString(params);

        //when
//        RequestBuilder requestSuccess = MockMvcRequestBuilders.get(url)
//                .header("X-USER-ID", 163232)
//                .content(content)
//                .contentType(MediaType.APPLICATION_JSON);

        //then
        mockMvc.perform(MockMvcRequestBuilders.get(url)
                .header("X-USER-ID", 12345)
                .params(params)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.resultCode", is("SUCCESS"))
                );


    }
    @Test
    @DisplayName("조회 테스트 - 실패")
    void list_fail() throws Exception {
        // 뿌리기 시 발급된 token + 뿌린사람 요청값으로 받습니다.
        // 뿌린시각,뿌린금액,받기완료된금액,받기완료된정보([받은금액,받은 사용자 아이디] 리스트)
        // 다른사람의 뿌리기건이나 유효하지 않은 token에 대해서는 조회 실패 응답이 내려가야 합니다.
        // 뿌린건에 대한 조회는 7일동안 할 수 있습니다.
        //given
        String url = "/ppurigi";
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("token", "123");
        String content = objectMapper.writeValueAsString(params);


        //when
        RequestBuilder requestFail = MockMvcRequestBuilders.get(url)
                .header("X-USER-ID", 12345)
                .params(params)
                .contentType(MediaType.APPLICATION_JSON);

        //then
        mockMvc.perform(requestFail)
                .andExpect(jsonPath("$.resultCode", is("FAIL"))
                );


    }

}

