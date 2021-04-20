package sn.example.demo.service;

import org.springframework.stereotype.Service;

import sn.example.demo.model.Ppurigi;
import sn.example.demo.repository.PpurigiRepository;

@Service
public class PpurigiService {

	private final PpurigiRepository ppurigiRepository;
	
	PpurigiService(PpurigiRepository ppurigiRepository) {
		this.ppurigiRepository = ppurigiRepository;
	}
	
	public String createPpurigi(String sendUserId, int amount, int reqCnt) {
		Ppurigi e = new Ppurigi();
		e.setSendUserId(sendUserId);
		e.setAmount(amount);
		e.setReqCnt(reqCnt);
		
		return ppurigiRepository.save(e).getId().toString();
	}
}
