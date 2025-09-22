package com.peng.sms.api.client;

import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(value = "beacon-cache")
public interface BeaconCacheClient {

}
