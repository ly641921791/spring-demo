
```shell
podman pull docker.1ms.run/apache/hertzbeat:1.7.2
```

```shell
podman run -d --name hertzbeat -p 1157:1157 -p 1158:1158 docker.1ms.run/apache/hertzbeat:1.7.2
```

登录http://127.0.0.1:1157/passport/login
账号密码：admin/hertzbeat
通过监控中心-新增监控-AUTO添加监控prometheus指标

由于解决不了容器内访问宿主机网络的问题，所以这里没做完