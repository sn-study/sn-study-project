package sn.example.demo.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import sn.example.demo.dto.ResultDto;
import sn.example.demo.dto.SendReqestDto;

@RestController
public class PpurigiController {

    @PostMapping("/ppurigi")
    ResultDto send(@RequestBody SendReqestDto requestDto){


        ResultDto resultDto = new ResultDto();
        return resultDto;
    }


}
