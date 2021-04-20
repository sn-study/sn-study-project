package sn.example.demo.dto;

import java.util.HashMap;

public class ResultDto {
    private String resultCode;
    private String resultMessage;
    private HashMap<String, String> result;

    public HashMap<String, String> getResult() {
        return result;
    }

    public void setResult(HashMap<String, String> result) {
        this.result = result;
    }

    public String getResultCode() {
        return resultCode;
    }

    public void setResultCode(String resultCode) {
        this.resultCode = resultCode;
    }

    public String getResultMessage() {
        return resultMessage;
    }

    public void setResultMessage(String resultMessage) {
        this.resultMessage = resultMessage;
    }



}
