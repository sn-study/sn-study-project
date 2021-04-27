package sn.example.demo.model;

import javax.persistence.PrePersist;
import java.util.Date;

public class PpurigiDtlcListener {

    @PrePersist
    void prePersist(PpurigiDtlc ppurigiDtlc) {
        Date now = new Date();
        ppurigiDtlc.setRegDts(now);
        ppurigiDtlc.setModDts(now);
    }

}
