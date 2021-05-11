package sn.example.demo.dto;

import java.util.Map;

public class ReceiveResultDto {
    private String resultCode;
    private String resultMessage;
    private Map<String, Object> result;

    ReceiveResultDto() {}

    private ReceiveResultDto(Builder builder) {
    	this.resultCode = builder.resultCode;
    	this.resultMessage = builder.resultMessage;
    	this.result = builder.result;
    }
    
    public Map<String, Object> getResult() {
        return result;
    }

    public void setResult(Map<String, Object> result) {
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


    public static class Builder {
    	private String resultCode;
        private String resultMessage;
        private Map<String, Object> result;
        
        public Builder(String resultCode, String resultMessage) {
        	this.resultCode = resultCode;
        	this.resultMessage = resultMessage;
        }
        
        public Builder result(Map<String, Object> result) {
        	this.result = result;
        	return this;
        }
        
        public ReceiveResultDto build() {
        	return new ReceiveResultDto(this);
        }
    }
    
}
