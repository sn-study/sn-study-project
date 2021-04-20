package sn.example.demo.model;

import java.util.Date;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;

@Entity
public class PpurigiDtlc {

	@EmbeddedId
	private PpurigiDtlcId id;
	private Integer amount;
    private String receiveUserId;
    private Date regDts;
    private Date modDts;

    public PpurigiDtlcId getId() {
		return id;
	}

	public void setId(PpurigiDtlcId id) {
		this.id = id;
	}    

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    public String getReceiveUserId() {
        return receiveUserId;
    }

    public void setReceiveUserId(String receiveUserId) {
        this.receiveUserId = receiveUserId;
    }

    public Date getRegDts() {
        return regDts;
    }

    public void setRegDts(Date regDts) {
        this.regDts = regDts;
    }

    public Date getModDts() {
        return modDts;
    }

    public void setModDts(Date modDts) {
        this.modDts = modDts;
    }
}