package sn.example.demo.controller;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import sn.example.demo.dto.ReceiveRequestDto;
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
    ResultDto send(@RequestHeader(value = "X-USER-ID") Long userId, @RequestHeader(value = "X-ROOM-ID") String roomId, @RequestBody SendReqestDto requestDto) {

        ResultDto resultDto = new ResultDto.Builder("SUCCESS", "뿌리기 요청 성공").build();
        requestDto.setSendUserId(userId);
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

    @PutMapping("/ppurigi")
    ResultDto receive(@RequestHeader(value = "X-USER-ID") Long userId, @RequestHeader(value = "X-ROOM-ID") String roomId, @RequestBody ReceiveRequestDto requestDto) {
        ResultDto resultDto = new ResultDto.Builder("SUCCESS", "뿌리기 받기 성공").build();
        requestDto.setReceiveUserId(userId);
        requestDto.setRoomId(roomId);

        try {
            int amount = ppurigiService.updatePpurigi(requestDto);

            Map<String, String> result = new HashMap<>();
            result.put("amount", String.valueOf(amount));
            resultDto.setResult(result);
        } catch (Exception e) {
            return new ResultDto.Builder("FAIL", e.getMessage()).build();
        }

        return resultDto;
    }
}
