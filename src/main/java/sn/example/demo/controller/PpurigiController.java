package sn.example.demo.controller;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import sn.example.demo.dto.ResultDto;
import sn.example.demo.dto.SendReqestDto;
import sn.example.demo.error.TokenAlreadyExistsException;
import sn.example.demo.service.PpurigiService;

@RestController
public class PpurigiController {

    private static final Logger Log = LoggerFactory.getLogger(PpurigiController.class);

	@Autowired
	PpurigiService ppurigiService;
	
    @PostMapping("/ppurigi")
    ResultDto send(@RequestHeader(value = "X-USER-ID") Long sendUserId, @RequestHeader(value = "X-ROOM-ID") String roomId, @RequestBody SendReqestDto requestDto){

        ResultDto resultDto = new ResultDto.Builder("SUCCESS", "뿌리기 요청 성공").build();
        requestDto.setSendUserId(sendUserId);
        requestDto.setRoomId(roomId);
        try {
            String token = ppurigiService.createPpurigi(requestDto);

            Map<String, String> result = new HashMap<>();
            result.put("token", token);
            resultDto.setResult(result);
        } catch (TokenAlreadyExistsException e){
            return new ResultDto.Builder("FAIL", "토큰생성오류").build();
        }

        return resultDto;
    }    
}
