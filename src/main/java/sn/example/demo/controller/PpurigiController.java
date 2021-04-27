package sn.example.demo.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import sn.example.demo.dto.ResultDto;
import sn.example.demo.dto.SendReqestDto;
import sn.example.demo.service.PpurigiService;

@RestController
public class PpurigiController {

    private static final Logger Log = LoggerFactory.getLogger(PpurigiController.class);

	@Autowired
	PpurigiService ppurigiService;
	
    @PostMapping("/ppurigi")
    ResultDto send(@RequestBody SendReqestDto requestDto){
        ResultDto resultDto = new ResultDto();
        
        String id = ppurigiService.createPpurigi(requestDto.getSendUserId(), requestDto.getAmount(), requestDto.getReqCnt());
        resultDto.getResult().put("id", id);
        
        return resultDto;
    }


}
