package com.feng.custom.util;

import java.io.IOException;
import java.util.Properties;

public class PropertiesUtil {
    private static Properties properties;

    static {
        properties = new Properties();
        try {
            properties.load(PropertiesUtil.class.getResourceAsStream("/gRpc.properties"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static int getGRpcServerPort() {
        return Integer.parseInt(properties.getProperty("gRpc.port", "8080"));
    }

    public static String getGRpcServerHost() {
        return properties.getProperty("gRpc.host", "127.0.0.1");
    }
}
