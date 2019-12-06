package com.feng.custom.client;

import com.feng.custom.rpc.Feature;
import com.feng.custom.rpc.Point;
import com.feng.custom.rpc.Product;
import com.feng.custom.rpc.RouteGuideGrpc;
import com.feng.custom.util.PropertiesUtil;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

import java.util.Iterator;

public class GRpcClient {
    private final ManagedChannel channel;

    // 同步
    private final RouteGuideGrpc.RouteGuideBlockingStub blockingStub;

    // 异步
    private final RouteGuideGrpc.RouteGuideStub asyncStub;

    public GRpcClient(String host, int port) {
        channel = ManagedChannelBuilder.forAddress(host, port).usePlaintext().build();
        blockingStub = RouteGuideGrpc.newBlockingStub(channel);
        asyncStub = RouteGuideGrpc.newStub(channel);
    }

    public static void main(String[] args) {

        // 简单请求
        GRpcClient gRpcClient = new GRpcClient(PropertiesUtil.getGRpcServerHost(), PropertiesUtil.getGRpcServerPort());
        Point request = Point.newBuilder().setLatitude(13).setLongitude(13).build();
        Feature feature = gRpcClient.blockingStub.getFeature(request);
        System.out.println(feature);

        // 服务器返回流式数据
        Product product = Product.newBuilder().setName("product").build();
        Iterator<Feature> featureIterator = gRpcClient.blockingStub.listFeatures(product);
        while (featureIterator.hasNext()) {
            System.out.println(featureIterator.next());
            System.out.println(System.currentTimeMillis());
        }

        // 客服端流式请求

    }
}
