package com.peng.sms.constant;

public interface RabbitMQConstants {
    /**
     * Queue name for the interface module to send messages to the strategy module
     */
    String SMS_PRE_SEND = "sms_pre_send_topic";

    /**
     * Queue name for the strategy module to send mobile number location & carrier info to the backend management module
     */
    String MOBILE_AREA_OPERATOR = "mobile_area_operator_topic";

    /**
     * Queue for writing logs to Elasticsearch
     */
    String SMS_WRITE_LOG = "sms_write_log_topic";

    /**
     * Queue for pushing status reports
     */
    String SMS_PUSH_REPORT = "sms_push_report_topic";

    /**
     * Prefix of the queue name for the strategy module to push messages to the SMS gateway module; the channel ID needs to be appended
     */
    String SMS_GATEWAY = "sms_gateway_topic_";

    /**
     * Information related to the private queues used by the SMS gateway module
     */
    String SMS_GATEWAY_NORMAL_EXCHANGE = "sms_gateway_normal_exchange";
    String SMS_GATEWAY_NORMAL_QUEUE = "sms_gateway_normal_queue";
    String SMS_GATEWAY_DEAD_EXCHANGE = "sms_gateway_dead_exchange";
    String SMS_GATEWAY_DEAD_QUEUE = "sms_gateway_dead_queue";
}
