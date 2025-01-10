package org.apache.dubbo.samples.tri.streaming.util;

public class TriSampleConstants {
    public static final int SERVER_PORT = Integer.parseInt(System.getProperty("provider.port",  "50052"));
    public static final String ZK_HOST = System.getProperty("zookeeper.host", "127.0.0.1");
    public static final int ZK_PORT = Integer.parseInt(System.getProperty("zookeeper.port", "2181"));
    public static final String ZK_ADDRESS = "zookeeper://" + ZK_HOST + ":" + ZK_PORT;
}
