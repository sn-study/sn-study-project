package sn.example.demo.dto;

public class SendReqestDto {
    private String sendUserId;
    private Integer amount;
    private Integer reqCnt;

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
}
