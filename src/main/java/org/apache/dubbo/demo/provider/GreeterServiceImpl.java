package org.apache.dubbo.demo.provider;

import com.alibaba.fastjson2.JSON;
import org.apache.dubbo.common.stream.StreamObserver;
import org.apache.dubbo.demo.hello.GreeterService;
import org.apache.dubbo.demo.hello.Reply;
import org.apache.dubbo.demo.hello.Reply2;
import org.apache.dubbo.demo.hello.Request;
import org.apache.dubbo.rpc.RpcContext;

import java.math.BigDecimal;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GreeterServiceImpl implements GreeterService {

    private static final Logger LOGGER = LoggerFactory.getLogger(GreeterServiceImpl.class);

    @Override
    public Reply sayHello(Request request) {
        Map<String, Object> serverAttachments = RpcContext.getServerAttachment().getObjectAttachments();
        System.out.println("|||1.sayHello ContextService serverAttachments:" + JSON.toJSONString(serverAttachments));

        LOGGER.info("Received sayHello request: {}", request.getMessage());
        return toReply("Hello " + request.getMessage());
    }
    private static Reply toReply(String message) {
        return new Reply(message);
    }

    @Override
    public Reply2 sayHello2(Request request) {
        Map<String, Object> serverAttachments = RpcContext.getServerAttachment().getObjectAttachments();
        System.out.println("|||1.sayHello2 ContextService serverAttachments:" + JSON.toJSONString(serverAttachments));

        LOGGER.info("Received sayHello2 request: {}", request.getMessage());
        BigDecimal lnt = new BigDecimal("100.5494959494355");
        return toReply2("Hello " + request.getMessage(), lnt);
    }

    private static Reply2 toReply2(String message, BigDecimal lnt) {
        return new Reply2(message, lnt);
    }

    @Override
    public StreamObserver<Request> sayTripleBiStream(StreamObserver<Reply> responseObserver) {
        // 获取附加信息
        Map<String, Object> serverAttachments = RpcContext.getServerAttachment().getObjectAttachments();
        System.out.println(
                "|||1.sayTripleBiStream ContextService serverAttachments:" + JSON.toJSONString(serverAttachments));

        LOGGER.info("3.sayTripleBiStream request in server");
        return new StreamObserver<Request>() {
            private String message = "";

            @Override
            public void onNext(Request request) {
                message = request.getMessage();
               LOGGER.info("4.sayTripleBiStream onNext message: {}", message);
                responseObserver.onNext(toTripleResponse(message));
            }

            @Override
            public void onError(Throwable throwable) {
                LOGGER.error("6.sayTripleBiStream onError", throwable);
            }

            @Override
            public void onCompleted() {
                LOGGER.info("5.sayTripleBiStream onCompleted");
                responseObserver.onCompleted();
            }
        };
    }

    private static Reply toTripleResponse(String message) {
        return new Reply(message);
    }
}
