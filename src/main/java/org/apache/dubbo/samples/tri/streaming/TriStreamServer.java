package org.apache.dubbo.samples.tri.streaming;

import org.apache.dubbo.common.constants.CommonConstants;
import org.apache.dubbo.config.ApplicationConfig;
import org.apache.dubbo.config.ProtocolConfig;
import org.apache.dubbo.config.RegistryConfig;
import org.apache.dubbo.config.ServiceConfig;
import org.apache.dubbo.config.bootstrap.DubboBootstrap;
import org.apache.dubbo.samples.tri.streaming.util.TriSampleConstants;

import java.io.IOException;

public class TriStreamServer {

    public static void main(String[] args) throws IOException {
        ServiceConfig<Greeter> service = new ServiceConfig<>();
        service.setInterface(Greeter.class);
        service.setRef(new GreeterImpl("tri-stub"));
        System.out.println("Dubbo triple streaming server to starting, port=" + TriSampleConstants.SERVER_PORT);

        ApplicationConfig applicationConfig = new ApplicationConfig("tri-stub-server");
        applicationConfig.setQosEnable(false);

        RegistryConfig registry  = new RegistryConfig(TriSampleConstants.ZK_ADDRESS);
        ProtocolConfig protocol =  new ProtocolConfig(CommonConstants.TRIPLE, TriSampleConstants.SERVER_PORT);
        protocol.setHost("172.16.120.93");

        DubboBootstrap bootstrap = DubboBootstrap.getInstance();
        bootstrap.application(applicationConfig)
                .registry(registry)
                .protocol(protocol)
                .service(service)
                .start()
                .await();
        System.out.println("Dubbo triple streaming server started, port=" + TriSampleConstants.SERVER_PORT);
    }
}
