package org.apache.dubbo.demo.consumer;

import org.apache.dubbo.common.constants.CommonConstants;
import org.apache.dubbo.common.stream.StreamObserver;
import org.apache.dubbo.config.ProtocolConfig;
import org.apache.dubbo.config.ReferenceConfig;
import org.apache.dubbo.config.RegistryConfig;
import org.apache.dubbo.config.bootstrap.DubboBootstrap;
import org.apache.dubbo.demo.hello.Reply;
import org.apache.dubbo.demo.hello.Request;
import org.apache.dubbo.rpc.Invoker;
import org.apache.dubbo.rpc.RpcInvocation;
import org.apache.dubbo.rpc.model.ConsumerModel;
import org.apache.dubbo.rpc.model.ServiceModel;

import java.util.Map;

public class ApiConsumer {

    public static void main(String[] args) throws InterruptedException {

        ReferenceConfig<?> referenceConfig = new ReferenceConfig<>();
        referenceConfig.setInterface("org.apache.dubbo.demo.hello.GreeterService");
        referenceConfig.setCheck(false);
        referenceConfig.setProtocol(CommonConstants.TRIPLE);
        referenceConfig.setLazy(true);
        referenceConfig.setTimeout(100000);

        try {
            DubboBootstrap bootstrap = DubboBootstrap.getInstance();
            bootstrap.registry(new RegistryConfig("zookeeper://127.0.0.1:2181"));
            bootstrap.protocol(new ProtocolConfig(CommonConstants.TRIPLE, -1));
            bootstrap.reference(referenceConfig);
            bootstrap.start();

            StreamObserver<Reply> responseObserver = new StreamObserver<Reply>() {
                @Override
                public void onNext(Reply reply) {
                    System.out.println("sayHelloServerStream onNext: " + reply.getMessage());
                }

                @Override
                public void onError(Throwable t) {
                    System.out.println("sayHelloServerStream onError: " + t.getMessage());
                }

                @Override
                public void onCompleted() {
                    System.out.println("sayHelloServerStream onCompleted");
                }
            };
            System.out.println("Call sayHelloServerStream");
            Invoker invoker = referenceConfig.getInvoker();
            RpcInvocation rpcInvocation = getRpcInvocation(
                    "org.apache.dubbo.demo.hello.GreeterService",
                    "sayHelloServerStream",
                    new Class[] {Request.class, StreamObserver.class},
                    new Object[] {buildRequest("triple"), responseObserver},
                    false, invoker);
            invoker.invoke(rpcInvocation);
        } catch (Throwable t) {
            t.printStackTrace();
        }
        Thread.sleep(2000);
    }

    private static Request buildRequest(String message) {
        return new Request(message);
    }

    private static Reply toReply(Map<String, Object> messageMap) {
        return new Reply(messageMap.get("message").toString());
    }

    private static RpcInvocation getRpcInvocation(
            String interfaceName,
            String method,
            Class<?>[] methodParamTypes,
            Object[] methodParams,
            Boolean isAsync,
            Invoker invoker) {
        ServiceModel serviceModel = invoker.getUrl().getServiceModel();
        RpcInvocation rpcInvocation = new RpcInvocation(serviceModel, method, invoker.getInterface()
                .getName(), invoker.getUrl().getProtocolServiceKey(), methodParamTypes, methodParams);
        System.out.printf(">>>getRpcInvocation url:%s\n", invoker.getUrl());

        if (serviceModel instanceof ConsumerModel) {
            rpcInvocation.put(org.apache.dubbo.rpc.Constants.CONSUMER_MODEL, serviceModel);
            rpcInvocation.put(org.apache.dubbo.rpc.Constants.METHOD_MODEL, ((ConsumerModel) serviceModel).getMethodModel(method));
        }
        return rpcInvocation;
    }
}
