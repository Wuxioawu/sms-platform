package com.peng.sms.entity;

public class MobileBlack {

    private String blackNumber;

    private Integer clientId;

    public String getBlackNumber() {
        return blackNumber;
    }

    public void setBlackNumber(String blackNumber) {
        this.blackNumber = blackNumber;
    }

    public Integer getClientId() {
        return clientId;
    }

    public void setClientId(Integer clientId) {
        this.clientId = clientId;
    }

    @Override
    public String toString() {
        return "MobileBlack{" +
                "blackNumber='" + blackNumber + '\'' +
                ", clientId=" + clientId +
                '}';
    }
}
