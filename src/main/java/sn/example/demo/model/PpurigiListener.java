package sn.example.demo.model;

import java.util.Date;

import javax.persistence.PrePersist;

public class PpurigiListener {

	private static final long EXPIRY_TIME = 1000 * 60 * 10;
	
	@PrePersist
	void prePersist(Ppurigi ppurigi) {
		Date now = new Date();
		ppurigi.setRegDts(now);
		ppurigi.setExpDts(new Date(now.getTime() + EXPIRY_TIME));
	}
}
