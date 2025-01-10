package org.apache.dubbo.samples.tri.streaming;

import org.apache.dubbo.common.constants.CommonConstants;
import org.apache.dubbo.common.stream.StreamObserver;
import org.apache.dubbo.config.ApplicationConfig;
import org.apache.dubbo.config.ReferenceConfig;
import org.apache.dubbo.config.RegistryConfig;
import org.apache.dubbo.config.bootstrap.DubboBootstrap;
import org.apache.dubbo.samples.tri.streaming.util.TriSampleConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

public class TriStreamClient {
    private static final Logger LOGGER = LoggerFactory.getLogger(TriStreamClient.class);

    public static void main(String[] args) throws IOException, InterruptedException {
        DubboBootstrap bootstrap = DubboBootstrap.getInstance();
        ReferenceConfig<Greeter> ref = new ReferenceConfig<>();
        ref.setInterface(Greeter.class);
        ref.setProtocol(CommonConstants.TRIPLE);
        ref.setProxy(CommonConstants.NATIVE_STUB);
        ref.setTimeout(3000);

        ApplicationConfig applicationConfig = new ApplicationConfig("tri-stub-consumer");
        applicationConfig.setQosEnable(false);
        bootstrap.application(applicationConfig).reference(ref).registry(new RegistryConfig(TriSampleConstants.ZK_ADDRESS)).start();
        Greeter greeter = ref.get();

        System.out.println(">>>Dubbo triple serverStream client call...");
        //server stream
        serverStream(greeter);

        CountDownLatch latch = new CountDownLatch(1);
        latch.await();
    }

    private static void biStream(Greeter greeter) {
        StreamObserver<GreeterRequest> requestStreamObserver = greeter.biStream(new SampleStreamObserver());
        for (int i = 0; i < 10; i++) {
            GreeterRequest request = GreeterRequest.newBuilder().setName("name-" + i).build();
            System.out.printf(">>>Dubbo triple biStream client request:%s\n", request.getName());
            requestStreamObserver.onNext(request);
        }
        requestStreamObserver.onCompleted();
    }

    private static void serverStream(Greeter greeter) {
        GreeterRequest request = GreeterRequest.newBuilder().setName("server stream request.").build();
        System.out.printf(">>>Dubbo triple serverStream client request:%s\n", request.getName());
        greeter.serverStream(request, new SampleStreamObserver());
    }

    private static class SampleStreamObserver implements StreamObserver<GreeterReply> {

        @Override
        public void onNext(GreeterReply data) {
            LOGGER.info("stream <- reply:{}", data);
        }

        @Override
        public void onError(Throwable throwable) {
            LOGGER.error("stream onError", throwable);
            throwable.printStackTrace();
        }

        @Override
        public void onCompleted() {
            LOGGER.info("stream completed");
        }
    }
}