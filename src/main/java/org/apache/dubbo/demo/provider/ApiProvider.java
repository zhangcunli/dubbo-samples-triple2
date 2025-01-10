
package org.apache.dubbo.demo.provider;

import org.apache.dubbo.common.constants.CommonConstants;
import org.apache.dubbo.config.ApplicationConfig;
import org.apache.dubbo.config.ProtocolConfig;
import org.apache.dubbo.config.RegistryConfig;
import org.apache.dubbo.config.ServiceConfig;
import org.apache.dubbo.config.bootstrap.DubboBootstrap;
import org.apache.dubbo.demo.hello.GreeterService;
import org.apache.dubbo.rpc.Constants;

import java.util.Collections;

public class ApiProvider {

    public static void main(String[] args) {
        ProtocolConfig protocol = new ProtocolConfig(CommonConstants.TRIPLE, -1);
        protocol.setHost("172.16.120.97"); //环境变量：DUBBO_IP_TO_REGISTRY=172.16.120.90
        protocol.setSerialization("hessian2");
        protocol.setKeepAlive(Boolean.TRUE);
        protocol.setPort(50057);

        ServiceConfig<GreeterService> serviceConfig = new ServiceConfig<>();
        serviceConfig.setInterface(GreeterService.class);
        serviceConfig.setRef(new GreeterServiceImpl());
        serviceConfig.setProtocol(protocol);
        if (args.length > 0 && Constants.HTTP3_KEY.equals(args[0])) {
            serviceConfig.setParameters(Collections.singletonMap(Constants.HTTP3_KEY, "true"));
        }

        ApplicationConfig applicationConfig = new ApplicationConfig("dubbo-demo-triple-api-provider");
        applicationConfig.setOrganization("test.com");
        applicationConfig.setOwner("ZX");
        applicationConfig.setVersion("1.0");

        RegistryConfig registryConfig = new RegistryConfig("zookeeper://127.0.0.1:2181");
        registryConfig.setId("dubbo_zk_1");
        registryConfig.setTimeout(3000);
        //registryConfig.setRegisterMode();

        DubboBootstrap bootstrap = DubboBootstrap.getInstance();
        bootstrap.application(applicationConfig)
                .registry(registryConfig)
                .service(serviceConfig)
                .start()
                .await();
    }
}
