
```shell
podman pull docker.1ms.run/apache/hertzbeat:1.7.2
```

```shell
podman run -d --name hertzbeat -p 1157:1157 -p 1158:1158 docker.1ms.run/apache/hertzbeat:1.7.2
```

登录http://127.0.0.1:1157/passport/login
账号密码：admin/hertzbeat

通过监控中心-新增监控-应用服务监控-SpringBoot3.0添加，目标Host填写host.docker.internal，保存即可

通过监控中心-新增监控-AUTO添加监控prometheus指标，但是保存时会保存“测试连接失败 Collect Timeout No Response”，查看日志发现以下信息，
暂不确定是否是prometheus指标格式问题，还是Bug问题
```text
2025-07-16 22:41:42.935 [1000000000-_prometheus_Gentle_Dragon_77Zy-auto-2894] ERROR org.apache.hertzbeat.collector.collect.prometheus.parser.OnlineParser Line:74  - prometheus parser failed because of wrong input format. null
2025-07-16 22:41:42.936 [1000000000-_prometheus_Gentle_Dragon_77Zy-auto-2894] ERROR org.apache.hertzbeat.collector.dispatch.MetricsCollect Line:477 - [Collect Failed] Response metrics data is null.
```