package com.peng.sms.entity;

public class MobileTransfer {

    private String transferNumber;

    // the supply
    private Integer nowIsp;

    public String getTransferNumber() {
        return transferNumber;
    }

    public void setTransferNumber(String transferNumber) {
        this.transferNumber = transferNumber;
    }

    public Integer getNowIsp() {
        return nowIsp;
    }

    public void setNowIsp(Integer nowIsp) {
        this.nowIsp = nowIsp;
    }

    @Override
    public String toString() {
        return "MobileTransfer{" +
                "transferNumber='" + transferNumber + '\'' +
                ", nowIsp=" + nowIsp +
                '}';
    }
}
