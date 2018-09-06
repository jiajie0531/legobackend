package com.delllogistics.client.notice;

import com.delllogistics.exception.ExceptionCode;
import com.delllogistics.exception.ServiceException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.apache.http.client.fluent.Request;
import org.apache.http.entity.ContentType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * Created by jiajie on 09/06/2017.
 */
@Component
public class NoticeClient {

    private static ObjectMapper objectMapper = new ObjectMapper();

    static {
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    /**
     * 服务URL
     */
    @Value("${notice.server.url}")
    private String url;
    /**
     * 短信服务Id
     */
    @Value("${notice.message.appKey}")
    private String messageAppKey;
    /**
     * 短信服务密码
     */
    @Value("${notice.message.secret}")
    private String messageSecret;

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    public void sendMessage(String templateId, String to, Map variables) {
        String vars = "";
        try {
            vars = objectMapper.writeValueAsString(variables);
        } catch (JsonProcessingException e) {
            logger.error("sendMessage序列化参数出错", e);
        }

        ObjectNode request = objectMapper.createObjectNode();
        request.put("appid", this.messageAppKey);
        request.put("to", to);
        request.put("project", templateId);
        request.put("vars", vars);
        request.put("timestamp", System.currentTimeMillis() / 1000);
        request.put("signature", this.messageSecret);

        execute(request, "/message/xsend");
    }

    public void multiSendMessage(String templateId, List<MultiMessageVariables> multiMessageVariables) {
        String multi = "";
        try {
            multi = objectMapper.writeValueAsString(multiMessageVariables);
        } catch (JsonProcessingException e) {
            logger.error("sendMessage序列化参数出错", e);
        }

        ObjectNode request = objectMapper.createObjectNode();
        request.put("appid", this.messageAppKey);
        request.put("project", templateId);
        request.put("multi", multi);
        request.put("timestamp", System.currentTimeMillis() / 1000);
        request.put("signature", this.messageSecret);

        execute(request, "/message/multixsend", true);
    }

    private void execute(ObjectNode request, String method) {
        execute(request, method, false);
    }

    private void execute(ObjectNode request, String method, boolean arrayResult) {
        ObjectNode response;
        try {
            long beginTime = System.nanoTime();

            logger.info(String.format("request [method:%s] %s ", method, request.toString()));
            String originResponse = Request.Post(url + method).bodyString(request.toString(), ContentType.APPLICATION_JSON).execute().returnContent().asString();

            if (arrayResult) {
                response = Arrays.asList(objectMapper.readValue(originResponse, ObjectNode[].class)).get(0);
            } else {
                response = objectMapper.readValue(originResponse, ObjectNode.class);
            }

            logger.info(String.format("response [method:%s] %s ", method, originResponse));

        } catch (IOException e) {
            logger.error("请求接口[" + method + "]出错", e);
            throw new ServiceException(ExceptionCode.SYSTEM, "消息服务工作异常");
        }

        checkApiError(response, method);
    }

    private void checkApiError(ObjectNode response, String method) {
        if (!"success".equals(response.get("status").asText())) {
            logger.error("请求接口[" + method + "]出错，错误原因： " + response);
            throw new ServiceException(ExceptionCode.SERVICE, "消息服务异常，请稍后重试");
        }
    }
}
