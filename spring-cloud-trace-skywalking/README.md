
下载镜像

```shell
podman pull docker.1ms.run/apache/skywalking-oap-server:9.4.0
```

```shell
podman pull docker.1ms.run/apache/skywalking-ui:9.4.0
```

启动容器

```shell
podman run --name skywalking-oap --restart always -d -p 11800:11800 -p 12800:12800 -p 9411:9411 -p 9412:9412 apache/skywalking-oap-server:9.7.0
```

```shell
podman run --name skywalking-ui --restart always -d -p 8080:8080 -e SW_OAP_ADDRESS=http://172.28.88.84:12800 -e SW_ZIPKIN_ADDRESS=http://172.28.88.84:9412 apache/skywalking-ui:9.7.0
```

启动类加VM参数

```shell
-javaagent:C:\Users\yq\Downloads\apache-skywalking-java-agent-9.4.0\skywalking-agent\skywalking-agent.jar
-DSW_AGENT_NAME=trace-skywalking
-DSW_AGENT_COLLECTOR_BACKEND_SERVICES=172.28.88.84:11800
```