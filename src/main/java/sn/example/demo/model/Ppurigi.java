package sn.example.demo.model;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.Date;

import javax.persistence.*;

@Entity
@EntityListeners(PpurigiListener.class)
public class Ppurigi {
    private @Id @GeneratedValue(strategy = GenerationType.IDENTITY) Long id;
    private String sendUserId;
    private Integer amount;
    private Integer reqCnt;
    private Date regDts;
    private Date expDts;
    private String token;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSendUserId() {
        return sendUserId;
    }

    public void setSendUserId(String sendUserId) {
        this.sendUserId = sendUserId;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    public Integer getReqCnt() {
        return reqCnt;
    }

    public void setReqCnt(Integer reqCnt) {
        this.reqCnt = reqCnt;
    }

    public Date getRegDts() {
        return regDts;
    }

    public void setRegDts(Date regDts) {
        this.regDts = regDts;
    }

    public Date getExpDts() {
        return expDts;
    }

    public void setExpDts(Date expDts) {
        this.expDts = expDts;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }
}