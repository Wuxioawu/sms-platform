package com.peng.sms.service;

import java.io.IOException;
import java.util.Map;

public interface SearchService {

    /**
     * Add a document to Elasticsearch
     */
    void index(String index, String id, String json) throws IOException;

    /**
     * Check whether a document exists in the specified index
     */
    boolean exists(String index, String id) throws IOException;

    /**
     * Update document information
     */
    void update(String index, String id, Map<String, Object> doc) throws IOException;

    /**
     * Query SMS records based on page parameters
     */
    Map<String, Object> findSmsByParameters(Map<String, Object> parameters) throws IOException;
}
