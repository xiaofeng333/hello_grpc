# hello_grpc
在运行该项目前, 先使用`mvn compile`通过`protobuf-maven-plugin`来生成对应的grpc类, 其位置在`target/generated-sources`。
* proto位置放在`src/main/proto`或`src/test/proto`。
* 例子中定义了四种客服端和服务端会采用的交互方式
* 启动GRpcServer作为server端。
* 启动GRpcClient模拟client端发送请求。
# 待补充
* Authentication
* Error Handling
* gRpc提供的接口服务原生无法被http访问到, [Blog gRPC with REST and Open APIs](https://www.grpc.io/blog/coreos/)
# 参考
[grpc-java](https://github.com/grpc/grpc-java)  
[grpc官方文档中文版](http://doc.oschina.net/grpc?t=56831)