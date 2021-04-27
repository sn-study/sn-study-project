package sn.example.demo.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import sn.example.demo.controller.PpurigiController;
import sn.example.demo.model.Ppurigi;
import sn.example.demo.model.PpurigiDtlc;
import sn.example.demo.model.PpurigiDtlcId;
import sn.example.demo.repository.PpurigiDtlcRepository;
import sn.example.demo.repository.PpurigiRepository;

import java.util.Random;

@Service
public class PpurigiService {

	private static final Logger Log = LoggerFactory.getLogger(PpurigiService.class);

	private final PpurigiRepository ppurigiRepository;
	private final PpurigiDtlcRepository ppurigiDtlcRepository;
	
	PpurigiService(PpurigiRepository ppurigiRepository, PpurigiDtlcRepository ppurigiDtlcRepository) {
		this.ppurigiRepository = ppurigiRepository;
		this.ppurigiDtlcRepository = ppurigiDtlcRepository;
	}
	
	public String createPpurigi(String sendUserId, int amount, int reqCnt) {
		// 토큰 생성
		String token = "1";
		
		// 뿌리기 저장
		Ppurigi ppurigi = new Ppurigi();
		ppurigi.setSendUserId(sendUserId);
		ppurigi.setAmount(amount);
		ppurigi.setReqCnt(reqCnt);
		ppurigi.setToken(token);
		Long id = ppurigiRepository.save(ppurigi).getId();

		// 요청 인원수만큼 뿌리기상세 저장
		int total = 0;
		Random random = new Random();
		for (int seq = 1; seq <= reqCnt; seq++) {
			int max = amount - total - reqCnt + seq;
			int value = seq == reqCnt? max : random.nextInt(max);

			PpurigiDtlc ppurigiDtlc = new PpurigiDtlc();
			ppurigiDtlc.setId(new PpurigiDtlcId(id, seq));
			ppurigiDtlc.setAmount(value);

			ppurigiDtlcRepository.save(ppurigiDtlc);

			total += value;
		}
		
		return ppurigi.getToken();
	}
}
