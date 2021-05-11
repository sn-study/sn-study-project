package sn.example.demo.controller;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import sn.example.demo.dto.ListRequestDto;
import sn.example.demo.dto.ReceiveRequestDto;
import sn.example.demo.dto.ResultDto;
import sn.example.demo.dto.SendRequestDto;
import sn.example.demo.error.PpurigiReciveException;
import sn.example.demo.error.TokenAlreadyExistsException;
import sn.example.demo.service.PpurigiService;

@RestController
public class PpurigiController {

    private static final Logger Log = LoggerFactory.getLogger(PpurigiController.class);

	@Autowired
	PpurigiService ppurigiService;
	
    @PostMapping("/ppurigi")
    ResultDto send(@RequestHeader(value = "X-USER-ID") Long userId, @RequestHeader(value = "X-ROOM-ID") String roomId, @RequestBody SendRequestDto requestDto) {

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
        } catch (PpurigiReciveException e) {
            Log.debug(e.getMessage());
            return new ResultDto.Builder("FAIL", e.getMessage()).build();
        } catch (Exception e) {
            Log.debug(e.getMessage());
            return new ResultDto.Builder("FAIL", "잠시 후에 다시 시도하세요.").build();
        }

        return resultDto;
    }

    @GetMapping("/ppurigi")
    ResultDto list(@RequestHeader(value = "X-USER-ID") Long sendUserId, @RequestParam String token) {


        //requestDto.setSendUserId(sendUserId);
        ResultDto resultDto = new ResultDto.Builder("SUCCESS", "뿌리기 조회 성공").build();

        try {
            ListRequestDto requestDto = new ListRequestDto();
            requestDto.setSendUserId(sendUserId);
            requestDto.setToken(token);
            resultDto = ppurigiService.listPpurigi(requestDto);
        } catch (Exception e){
        //} catch (TokenInvalidException e){
            return new ResultDto.Builder("FAIL", "유효하지 않은 요청").build();
        }

        return resultDto;
    }    
}
