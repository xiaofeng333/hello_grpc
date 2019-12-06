package com.feng.custom.server;

import com.feng.custom.rpc.Feature;
import com.feng.custom.rpc.Point;
import com.feng.custom.rpc.Product;
import com.feng.custom.rpc.RouteGuideGrpc;
import com.feng.custom.util.PropertiesUtil;
import io.grpc.Server;
import io.grpc.ServerBuilder;
import io.grpc.stub.StreamObserver;

import java.io.IOException;

public class GRpcServer {
    private final int port;
    private final Server server;

    public GRpcServer(int port) {
        this.port = port;
        server = ServerBuilder.forPort(port)

                // 添加gRpc service的实现
                .addService(new RouteGuideService())

                // 创建gRpc server
                .build();
    }

    public static void main(String[] args) throws IOException, InterruptedException {
        GRpcServer gRpcServer = new GRpcServer(PropertiesUtil.getGRpcServerPort());
        gRpcServer.start();

        // 保证主线程不会结束
        gRpcServer.blockUntilShutdown();
    }

    public void start() throws IOException {

        // 启动gRpc server
        server.start();
        System.out.println("gRpc server start...");
    }

    /**
     * Await termination on the main thread since the grpc library uses daemon threads.
     */
    private void blockUntilShutdown() throws InterruptedException {
        if (server != null) {
            server.awaitTermination();
        }
    }

    public static class RouteGuideService extends RouteGuideGrpc.RouteGuideImplBase {

        /**
         * 简单的rpc
         *
         * @param request          请求参数
         * @param responseObserver a response observer, 特殊的接口用于response。
         */
        @Override
        public void getFeature(Point request, StreamObserver<Feature> responseObserver) {
            Feature feature = Feature.newBuilder()
                    .setName("ty")
                    .setLocation(request)
                    .build();

            // 返回Feature
            responseObserver.onNext(feature);

            // 标明server已完成处理
            responseObserver.onCompleted();
        }

        /**
         * 返回流式数据
         */
        @Override
        public void listFeatures(Product request, StreamObserver<Feature> responseObserver) {
            String name = request.getName();

            // 根据对应地名, 返回对应的经纬度, 此处不做处理
            for (int i = 0; i < 10; i++) {
                responseObserver.onNext(Feature.newBuilder().setName(name + i).build());
                System.out.println(System.currentTimeMillis());
            }
            responseObserver.onCompleted();
        }

        /**
         * 客户端流式请求
         */
        @Override
        public StreamObserver<Product> recordRoute(StreamObserver<Feature> responseObserver) {
            return new StreamObserver<Product>() {
                int pointCount;

                /**
                 * 获取客服端流中的数据
                 */
                @Override
                public void onNext(Product product) {
                    pointCount++;
                }

                @Override
                public void onError(Throwable throwable) {
                    System.out.println(throwable);
                }

                /**
                 * 客户端已经传完数据
                 */
                @Override
                public void onCompleted() {
                    responseObserver.onNext(Feature.newBuilder().setName(String.valueOf(pointCount)).build());
                    responseObserver.onCompleted();
                }
            };
        }

        @Override
        public StreamObserver<Point> routeChat(StreamObserver<Feature> responseObserver) {
            return new StreamObserver<Point>() {

                @Override
                public void onNext(Point point) {

                    // 仍会继续接受客户端数据时, 返回数据
                    responseObserver.onNext(Feature.newBuilder().setName("server-" + point.getLatitude()).build());
                }

                @Override
                public void onError(Throwable throwable) {
                    System.out.println(throwable);
                }

                @Override
                public void onCompleted() {
                    responseObserver.onCompleted();
                }
            };
        }
    }
}
