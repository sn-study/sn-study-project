package sn.example.demo.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sn.example.demo.dto.ListRequestDto;
import sn.example.demo.dto.ReceiveRequestDto;
import sn.example.demo.dto.ResultDto;
import sn.example.demo.dto.SendRequestDto;
import sn.example.demo.error.PpurigiReciveException;
import sn.example.demo.error.TokenAlreadyExistsException;
import sn.example.demo.error.TokenInvalidException;
import sn.example.demo.model.Ppurigi;
import sn.example.demo.model.PpurigiDtlc;
import sn.example.demo.repository.PpurigiDtlcRepository;
import sn.example.demo.repository.PpurigiRepository;
import sn.example.demo.utils.TokenGenerator;

import java.util.*;

@Service
public class PpurigiService {

	private static final Logger Log = LoggerFactory.getLogger(PpurigiService.class);

	private final PpurigiRepository ppurigiRepository;
	private final PpurigiDtlcRepository ppurigiDtlcRepository;
	
	PpurigiService(PpurigiRepository ppurigiRepository, PpurigiDtlcRepository ppurigiDtlcRepository) {
		this.ppurigiRepository = ppurigiRepository;
		this.ppurigiDtlcRepository = ppurigiDtlcRepository;
	}
	
	@Transactional(rollbackFor = Exception.class)
	public String createPpurigi(SendRequestDto requestDto) throws TokenAlreadyExistsException {
		// 토큰 생성
		String token = TokenGenerator.getToken();
		if (this.findByTokenAndExpDtsLessThanNow(token).isPresent()) {
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

	@Transactional(rollbackFor = Exception.class)
	public Integer updatePpurigi(ReceiveRequestDto requestDto) throws PpurigiReciveException {
		// 토큰으로 조회
		Optional<Ppurigi> ppurigiOptional = this.findByTokenAndExpDtsLessThanNow(requestDto.getToken());
		if (!ppurigiOptional.isPresent()) {
			throw new PpurigiReciveException("요청 토큰에 해당하는 뿌리기가 없거나 만료되었습니다.");
		}
		Ppurigi ppurigi = ppurigiOptional.get();
		// 대화방 체크
		if (!ppurigi.getRoomId().equals(requestDto.getRoomId())) {
			throw new PpurigiReciveException("뿌리기가 호출된 대화방이 아닙니다.");
		}
		// 자신이 뿌린 건은 받을 수 없음
		if (ppurigi.getSendUserId().equals(requestDto.getReceiveUserId())) {
			throw new PpurigiReciveException("자신이 뿌린 건은 받을 수 없습니다.");
		}
		// 이미 받은 유저인지 확인
		if (ppurigiDtlcRepository.findByIdAndReceiveUserId(ppurigi.getId(), requestDto.getReceiveUserId()).isPresent()) {
			throw new PpurigiReciveException("뿌리기 당 한 사용자는 한 번만 받을 수 있습니다.");
		}
		// 받을 뿌리기 상세 조회
		Optional<PpurigiDtlc> ppurigiDtlcOptional = ppurigiDtlcRepository.findFirstByIdAndReceiveUserIdIsNull(ppurigi.getId());
		if (!ppurigiDtlcOptional.isPresent()) {
			throw new PpurigiReciveException("요청 토큰에 해당하는 뿌리기는 마감되었습니다.");
		}

		// 뿌리기 받기
		PpurigiDtlc ppurigiDtlc = ppurigiDtlcOptional.get();
		ppurigiDtlc.setReceiveUserId(requestDto.getReceiveUserId());
		ppurigiDtlc = ppurigiDtlcRepository.save(ppurigiDtlc);

		return ppurigiDtlc.getAmount();
	}

	Optional<Ppurigi> findByTokenAndExpDtsLessThanNow(String token) {
		return ppurigiRepository.findByTokenAndExpDtsGreaterThan(token, new Date());
	}

	public ResultDto listPpurigi(ListRequestDto requestDto){
		// 뿌리기 시 발급된 token + 뿌린사람 요청값으로 받습니다.
		// 뿌린시각,뿌린금액,받기완료된금액,받기완료된정보([받은금액,받은 사용자 아이디] 리스트)
		// 다른사람의 뿌리기건이나 유효하지 않은 token에 대해서는 조회 실패 응답이 내려가야 합니다.
		// 뿌린건에 대한 조회는 7일동안 할 수 있습니다.
		Ppurigi ppurigi = ppurigiRepository.findBySendUserIdAndTokenAndRegDtsGreaterThan(requestDto.getSendUserId(), requestDto.getToken(), validMinRegDts());
		if(ppurigi == null){
			throw new TokenInvalidException();
		}
		HashMap<String, String> map = new HashMap<>();
		map.put("뿌린 시각", ppurigi.getRegDts().toString());
		map.put("뿌린 금액", ppurigi.getAmount().toString());

		List<PpurigiDtlc> ppurigiDtlcList = ppurigiDtlcRepository.findByIdAndReceiveUserIdIsNotNull(ppurigi.getId());
		Integer sendAmount = 0;
		ArrayList<String> sendList = new ArrayList<String>();
		for (PpurigiDtlc ppurigiDtlc : ppurigiDtlcList) {
			sendAmount += ppurigiDtlc.getAmount();
			String[] temp = {ppurigiDtlc.getAmount().toString(), ppurigiDtlc.getReceiveUserId().toString()};
			sendList.add(Arrays.toString(temp));
		}
		map.put("받기 완료된 금액", sendAmount.toString());
		map.put("받기 완료된 정보", sendList.toString());

		ResultDto resultDto = new ResultDto.Builder("SUCCESS", "뿌리기 조회 성공").build();
		resultDto.setResult(map);

		return resultDto;

	}

	private Date validMinRegDts(){
		Calendar c = Calendar.getInstance();
		c.add(Calendar.DAY_OF_MONTH, -7);
		Date d = new Date(c.getTimeInMillis());
		return d;
	}

}
