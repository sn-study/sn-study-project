package sn.example.demo.service;

import java.util.Optional;
import java.util.Random;

import javax.swing.text.html.Option;
import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;

import sn.example.demo.dto.ReceiveRequestDto;
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
		if (ppurigiRepository.findByTokenLessThanExpDts(token).isPresent()) {
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
			ppurigiDtlc.setId(id);
			ppurigiDtlc.setSeq(seq);
			ppurigiDtlc.setAmount(value);

			ppurigiDtlcRepository.save(ppurigiDtlc);

			total += value;
		}
		
		return ppurigi.getToken();
	}

	@Transactional
	public Integer updatePpurigi(ReceiveRequestDto requestDto) throws Exception{
		// 토큰으로 조회
		Optional<Ppurigi> ppurigi = ppurigiRepository.findByTokenLessThanExpDts(requestDto.getToken());
		if (!ppurigi.isPresent()) {
			throw new Exception("요청 토큰에 해당하는 뿌리기가 없거나 만료되었습니다.");
		}
		// 대화방 체크
		if (!ppurigi.get().getRoomId().equals(requestDto.getRoomId())) {
			throw new Exception("뿌리기가 호출된 대화방이 아닙니다.");
		}
		// 자신이 뿌린 건은 받을 수 없음
		if (ppurigi.get().getSendUserId().equals(requestDto.getReceiveUserId())) {
			throw new Exception("자신이 뿌린 건은 받을 수 없습니다.");
		}
		// 이미 받은 유저인지 확인
		if (ppurigiDtlcRepository.findByIdAndReceiveUserId(ppurigi.get().getId(), requestDto.getReceiveUserId()).isPresent()) {
			throw new Exception("뿌리기 당 한 사용자는 한 번만 받을 수 있습니다.");
		}
		// 받을 뿌리기 상세 조회
		Optional<PpurigiDtlc> ppurigiDtlc = ppurigiDtlcRepository.findFirstByIdAndReceiveUserIdIsNull(ppurigi.get().getId());
		if (!ppurigiDtlc.isPresent()) {
			throw new Exception("요청 토큰에 해당하는 뿌리기는 마감되었습니다.");
		}
		// 뿌리기 받기
		PpurigiDtlc e = ppurigiDtlc.get();
		e.setReceiveUserId(requestDto.getReceiveUserId());
		e = ppurigiDtlcRepository.save(e);
		return e.getAmount();
	}
}
