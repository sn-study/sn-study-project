package sn.example.demo.service;

import java.util.Optional;
import java.util.Random;

import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;

import sn.example.demo.dto.SendReqestDto;
import sn.example.demo.error.TokenAlreadyExistsException;
import sn.example.demo.model.Ppurigi;
import sn.example.demo.model.PpurigiDtlc;
import sn.example.demo.model.PpurigiDtlcId;
import sn.example.demo.repository.PpurigiDtlcRepository;
import sn.example.demo.repository.PpurigiRepository;
import sn.example.demo.utils.TokenGenerator;

@Service
public class PpurigiService {

	private static final Logger Log = LoggerFactory.getLogger(PpurigiService.class);

	private final PpurigiRepository ppurigiRepository;
	private final PpurigiDtlcRepository ppurigiDtlcRepository;
	
	PpurigiService(PpurigiRepository ppurigiRepository, PpurigiDtlcRepository ppurigiDtlcRepository) {
		this.ppurigiRepository = ppurigiRepository;
		this.ppurigiDtlcRepository = ppurigiDtlcRepository;
	}
	
	@Transactional
	public <Optional>String createPpurigi(SendReqestDto requestDto) {
		// 토큰 생성
		String token = TokenGenerator.getToken();
		if (ppurigiRepository.findByTokenLessThanExpDts(token).isPresent()){
			throw new TokenAlreadyExistsException(token);
		}
		
		// 뿌리기 저장
		Ppurigi ppurigi = new Ppurigi();
		ppurigi.setSendUserId(requestDto.getSendUserId());
		ppurigi.setRoomId(requestDto.getRoomId());
		ppurigi.setAmount(requestDto.getAmount());
		ppurigi.setReqCnt(requestDto.getReqCnt());
		ppurigi.setToken(token);
		Long id = ppurigiRepository.save(ppurigi).getId();

		// 요청 인원수만큼 뿌리기상세 저장
		int total = 0;
		Random random = new Random();
		for (int seq = 1; seq <= requestDto.getReqCnt(); seq++) {
			int max = requestDto.getAmount() - total - requestDto.getReqCnt() + seq;
			int value = seq == requestDto.getReqCnt()? max : random.nextInt(max);

			PpurigiDtlc ppurigiDtlc = new PpurigiDtlc();
			ppurigiDtlc.setId(new PpurigiDtlcId(id, seq));
			ppurigiDtlc.setAmount(value);

			ppurigiDtlcRepository.save(ppurigiDtlc);

			total += value;
		}
		
		return ppurigi.getToken();
	}
}
