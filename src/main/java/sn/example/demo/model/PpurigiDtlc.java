package sn.example.demo.model;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import java.util.Date;

@Entity
@EntityListeners(PpurigiDtlcListener.class)
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

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }
}