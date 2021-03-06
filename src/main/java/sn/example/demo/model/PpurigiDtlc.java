package sn.example.demo.model;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.hibernate.annotations.OptimisticLockType;
import org.hibernate.annotations.OptimisticLocking;

import javax.persistence.*;
import java.util.Date;

@Entity
@EntityListeners(PpurigiDtlcListener.class)
@IdClass(PpurigiDtlcId.class)
@OptimisticLocking(type = OptimisticLockType.VERSION)
public class PpurigiDtlc {

	@Id
	private Long id;
	@Id
    private Integer seq;
    @Column(nullable = false)
	private Integer amount;
    private Long receiveUserId;
    @Column(nullable = false)
    private Date regDts;
    @Column(nullable = false)
    private Date modDts;

    @Version
    private int version;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getSeq() {
        return seq;
    }

    public void setSeq(Integer seq) {
        this.seq = seq;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    public Long getReceiveUserId() {
        return receiveUserId;
    }

    public void setReceiveUserId(Long receiveUserId) {
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

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }
}