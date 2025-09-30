package com.peng.sms.entity;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 客户信息表
 * client_business
 */
@Data
public class ClientBusiness implements Serializable {
    private Long id;

    /**
     * 公司名
     */
    private String corpname;

    /**
     * HTTP接入的密码
     */
    private String apikey;

    /**
     * HTTP客户端的IP白名单（多个用,隔开）
     */
    private String ipAddress;

    /**
     * 状态报告是否返回：0 不返回 1 返回
     */
    private Byte isCallback;

    /**
     * 客户接收状态报告的URL地址
     */
    private String callbackUrl;

    /**
     * 联系人
     */
    private String clientLinkname;

    /**
     * 密保手机
     */
    private String clientPhone;

    /**
     * 策略校验顺序动态实现规则
     */
    private String clientFilters;

    /**
     * 创建时间，默认系统时间
     */
    private Date created;

    /**
     * 创建人id
     */
    private Long createId;

    /**
     * 修改时间，默认系统时间
     */
    private Date updated;

    /**
     * 修改人id
     */
    private Long updateId;

    /**
     * 是否删除 0-未删除 ， 1-已删除
     */
    private Byte isDelete;

    /**
     * 备用字段1
     */
    private String extend1;

    /**
     * 备用字段2
     */
    private String extend2;

    /**
     * 备用字段3
     */
    private String extend3;

    /**
     * 备用字段4
     */
    private String extend4;

    private static final long serialVersionUID = 1L;
}