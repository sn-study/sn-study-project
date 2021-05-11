package sn.example.demo.model;

import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import java.util.Date;

public class PpurigiDtlcListener {

    @PrePersist
    void prePersist(PpurigiDtlc ppurigiDtlc) {
        Date now = new Date();
        ppurigiDtlc.setRegDts(now);
        ppurigiDtlc.setModDts(now);
    }

    @PreUpdate
    void preUpdate(PpurigiDtlc ppurigiDtlc) {
        Date now = new Date();
        ppurigiDtlc.setModDts(now);
    }
}
