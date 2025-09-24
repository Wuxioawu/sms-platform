package com.peng.sms.entity;


public class Channel {

    private long id;
    private String channelName;
    private long channelType;
    private String channelArea;
    private String channelAreaCode;
    private long channelPrice;
    private long channelProtocal;
    private String channelIp;
    private long channelPort;
    private String channelUsername;
    private String channelPassword;
    private String channelNumber;
    private long isAvailable;


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }


    public String getChannelName() {
        return channelName;
    }

    public void setChannelName(String channelName) {
        this.channelName = channelName;
    }


    public long getChannelType() {
        return channelType;
    }

    public void setChannelType(long channelType) {
        this.channelType = channelType;
    }


    public String getChannelArea() {
        return channelArea;
    }

    public void setChannelArea(String channelArea) {
        this.channelArea = channelArea;
    }


    public String getChannelAreaCode() {
        return channelAreaCode;
    }

    public void setChannelAreaCode(String channelAreaCode) {
        this.channelAreaCode = channelAreaCode;
    }


    public long getChannelPrice() {
        return channelPrice;
    }

    public void setChannelPrice(long channelPrice) {
        this.channelPrice = channelPrice;
    }


    public long getChannelProtocal() {
        return channelProtocal;
    }

    public void setChannelProtocal(long channelProtocal) {
        this.channelProtocal = channelProtocal;
    }


    public String getChannelIp() {
        return channelIp;
    }

    public void setChannelIp(String channelIp) {
        this.channelIp = channelIp;
    }


    public long getChannelPort() {
        return channelPort;
    }

    public void setChannelPort(long channelPort) {
        this.channelPort = channelPort;
    }


    public String getChannelUsername() {
        return channelUsername;
    }

    public void setChannelUsername(String channelUsername) {
        this.channelUsername = channelUsername;
    }


    public String getChannelPassword() {
        return channelPassword;
    }

    public void setChannelPassword(String channelPassword) {
        this.channelPassword = channelPassword;
    }


    public String getChannelNumber() {
        return channelNumber;
    }

    public void setChannelNumber(String channelNumber) {
        this.channelNumber = channelNumber;
    }


    public long getIsAvailable() {
        return isAvailable;
    }

    public void setIsAvailable(long isAvailable) {
        this.isAvailable = isAvailable;
    }


    @Override
    public String toString() {
        return "Channel{" +
                "id=" + id +
                ", channelName='" + channelName + '\'' +
                ", channelType=" + channelType +
                ", channelArea='" + channelArea + '\'' +
                ", channelAreaCode='" + channelAreaCode + '\'' +
                ", channelPrice=" + channelPrice +
                ", channelProtocal=" + channelProtocal +
                ", channelIp='" + channelIp + '\'' +
                ", channelPort=" + channelPort +
                ", channelUsername='" + channelUsername + '\'' +
                ", channelPassword='" + channelPassword + '\'' +
                ", channelNumber='" + channelNumber + '\'' +
                ", isAvailable=" + isAvailable +
                '}';
    }
}
