
```shell
podman pull docker.1ms.run/prom/prometheus:v3.5.0
```

```shell
podman run -d --name prometheus -p 9090:9090 docker.1ms.run/prom/prometheus:v3.5.0
```

进入容器，通过下面的命令查看宿主机ip，配置prometheus.yml文件会用的

```shell
ip route show default | awk '/default/ {print $3}'
```

进入容器修改配置文件/etc/prometheus/prometheus.yml，添加如下内容，重启服务
```yaml
scrape_configs:
  - job_name: "spring-boot-prometheus"
    metrics_path: '/actuator/prometheus'
    # scheme defaults to 'http'.
    static_configs:
      - targets: ["192.168.64.1:8080"]
       # The label name is added as a label `label_name=<label_value>` to any timeseries scraped from this config.
        labels:
          app: "spring-boot-prometheus"
```

```shell
podman pull docker.1ms.run/grafana/grafana:12.0.2
```

```shell
podman run -d --name grafana -p 3000:3000 docker.1ms.run/grafana/grafana:12.0.2
```

访问127.0.0.1:3000，默认账号密码：admin/admin，创建数据源，选择prometheus，填写prometheus地址，保存
由于使用了同一个宿主机创建两个容器，可以通过prometheus地址可以配置为：http://host.containers.internal:9090

然后新增Dashboards，填写具体监控配置，可以直接使用21319监控模板，选择刚刚创建的数据源