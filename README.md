# 前言
> 这是菜狗的大二上课程设计，缝缝补补只为了随便应付作业，写得不咋地哈。
> 挂GitHub留一个纪念，期间开发老师的要求也是有点逆天。

## 部署指南
### 前端
前提：需要到本人Github仓库克隆相应的项目。
```c
git clone https://github.com/HuanXin-Chen/AxMeetingAdmin.git
git clone https://github.com/HuanXin-Chen/AxMeetingUser.git
```
最后：统一npm安装

- 注意：确保nodejs的版本不高于18
```c
npm install
npm run serve
```
### SDK
SDK：因为未上传的官方仓库，故需要在本地进行安装。
```c
git clone https://github.com/HuanXin-Chen/ChatGLM-SDK.git
```
最后进行本地安装
```c
mvn install
```
### 后端
前提：克隆仓库到本地

- 注意：本项目使用的是JDK8
```c
git clone https://github.com/HuanXin-Chen/AxMeetingRoom.git
```
后端：确保本地有docker服务，脚本一键部署。
```yaml
# 命令执行 docker-compose up -d
# 执行脚本；docker-compose -f docker-compose.yml up -d
# 拷贝配置；docker container cp grafana:/etc/grafana/ ./docs/dev-ops/
version: '3.9'
services:
  mysql:
    image: mysql:5.7
    container_name: mysql5.7
    command: --default-authentication-plugin=mysql_native_password
    restart: always
    environment:
      TZ: Asia/Shanghai
#      MYSQL_ALLOW_EMPTY_PASSWORD: 'yes' # 可配置无密码，注意配置 SPRING_DATASOURCE_PASSWORD=
      MYSQL_ROOT_PASSWORD: 123456
      MYSQL_USER: chx
      MYSQL_PASSWORD: 123456
    depends_on:
      - mysql-job-dbdata
    ports:
      - "13306:3306"
    volumes:
      - ./sql:/docker-entrypoint-initdb.d
    volumes_from:
      - mysql-job-dbdata

  # 自动加载数据
  mysql-job-dbdata:
    image: alpine:3.18.2
    container_name: mysql-job-dbdata
    volumes:
      - /var/lib/mysql
  # 数据采集
  prometheus:
    image: bitnami/prometheus:2.47.2
    container_name: prometheus
    restart: always
    ports:
      - 9090:9090
    volumes:
      - ./etc/prometheus/prometheus.yml:/etc/prometheus/prometheus.yml
  # 监控界面
  grafana:
    image: grafana/grafana:10.2.0
    container_name: grafana
    restart: always
    ports:
      - 4003:4003
    depends_on:
      - prometheus
    volumes:
      - ./etc/grafana:/etc/grafana
  redis:
    image: redis:7.2.0
    container_name: redis
    ports:
      - 6379:6379
    volumes:
      - ./redis/redis.conf:/usr/local/etc/redis/redis.conf
    command: redis-server /usr/local/etc/redis/redis.conf
  rocketmq:
    image: livinphp/rocketmq:5.1.0
    container_name: rocketmq
    ports:
      - 9009:9009
      - 9876:9876
      - 10909:10909
      - 10911:10911
      - 10912:10912
    volumes:
      - ./rocketmq/data:/home/app/data
    environment:
      TZ: "Asia/Shanghai"
      NAMESRV_ADDR: "rocketmq:9876"
  portainer:
    image: portainer/portainer-ce:2.17.0
    container_name: portainer
    ports:
      - 9000:9000
    volumes:
      - /home/app/portainer/data:/data
      - /var/run/docker.sock:/var/run/docker.sock
  portainer-agent:
    image: portainer/agent:2.17.0
    container_name: portainer-agent
    ports:
      -  "9001:9001"
    volumes:
      - /var/run/docker.sock:/var/run/docker.sock
      - /var/lib/docker/volumes:/var/lib/docker/volumes
```
项目配置：修改你的配置，主要是邮件和AI服务。
```yaml
server:
  port: 8080
  tomcat:
    mbeanregistry:
      enabled: true
    max-connections: 20 # 最大连接数
    threads:
      max: 20         # 设定处理客户请求的线程的最大数目，决定了服务器可以同时响应客户请求的数,默认200
      min-spare: 10   # 初始化线程数,最小空闲线程数,默认是10
    accept-count: 10  # 等待队列长度

# 监控上报
management:
  endpoints:
    web:
      exposure:
        include: "*" # 暴露所有端点，包括自定义端点
  endpoint:
    health:
      show-details: always # 显示详细的健康检查信息
  metrics:
    export:
      prometheus:
        enabled: true # 启用Prometheus
  prometheus:
    enabled: true # 启用Prometheus端点

spring:
  datasource:
    username: root
    password: 123456
    url: jdbc:mysql://localhost:13306/meeting?useUnicode=true&characterEncoding=utf8&autoReconnect=true&zeroDateTimeBehavior=convertToNull&serverTimezone=UTC&useSSL=false
    driver-class-name: com.mysql.jdbc.Driver
    type: com.alibaba.druid.pool.DruidDataSource
    #   数据源其他配置
    initialSize: 5
    minIdle: 5
    maxActive: 20
    maxWait: 60000
    timeBetweenEvictionRunsMillis: 60000
    minEvictableIdleTimeMillis: 300000
    validationQuery: SELECT 1 FROM DUAL
    testWhileIdle: true
    testOnBorrow: false
    testOnReturn: false
    poolPreparedStatements: true
  mail:
    host: smtp.qq.com
    username: 1056216208@qq.com
    password: nwxn**********

mybatis:
  config-location: classpath:mybatis-config.xml
  mapper-locations: classpath:mapper/*.xml

# ChatGLM SDK Config
chatglm:
  sdk:
    config:
      # 状态；true = 开启、false 关闭
      enabled: true
      # 官网地址
      api-host: https://open.bigmodel.cn/
      # 官网申请 https://open.bigmodel.cn/usercenter/apikeys
      api-secret-key: *******2c5ca0d056de4a1e*******

# 线程池配置
thread:
  pool:
    executor:
      config:
        core-pool-size: 20
        max-pool-size: 50
        keep-alive-time: 5000
        block-queue-size: 5000
        policy: CallerRunsPolicy

redis:
  sdk:
    config:
      host: localhost
      port: 6379
      password: 123456
      pool-size: 10
      min-idle-size: 5
      idle-timeout: 30000
      connect-timeout: 5000
      retry-attempts: 3
      retry-interval: 1000
      ping-interval: 60000
      keep-alive: true

rocketmq:
  name-server: 127.0.0.1:9876
  consumer:
    group: chx-groupA
    # 一次拉取消息最大值，注意是拉取消息的最大值而非消费最大值
    pull-batch-size: 10
  producer:
    # 发送同一类消息的设置为同一个group，保证唯一
    group: chx-groupA
    # 发送消息超时时间，默认3000
    sendMessageTimeout: 10000
    # 发送消息失败重试次数，默认2
    retryTimesWhenSendFailed: 2
    # 异步消息重试此处，默认2
    retryTimesWhenSendAsyncFailed: 2
    # 消息最大长度，默认1024 * 1024 * 4(默认4M)
    maxMessageSize: 4096
    # 压缩消息阈值，默认4k(1024 * 4)
    compressMessageBodyThreshold: 4096
    # 是否在内部发送失败时重试另一个broker，默认false
    retryNextServer: false

```
最后便可以一键启动！
### 打包
这里主要侧重前端，后端可以根据自己喜欢合理安排组件。<br />使用我微调过的模板，将dist目录切换为目标目录即可。
```python
https://github.com/HuanXin-Chen/electron-quick-start.git
```
最后可以使用inno进行制定安装包，这样就能完成作业了~~
```python
; Script generated by the Inno Setup Script Wizard.
; SEE THE DOCUMENTATION FOR DETAILS ON CREATING INNO SETUP SCRIPT FILES!

#define MyAppName "AxMeet-User"
#define MyAppVersion "1.5"
#define MyAppPublisher "AxMeet"
#define MyAppExeName "App.exe"
#define MyAppAssocName MyAppName + " File"
#define MyAppAssocExt ".myp"
#define MyAppAssocKey StringChange(MyAppAssocName, " ", "") + MyAppAssocExt

[Setup]
; NOTE: The value of AppId uniquely identifies this application. Do not use the same AppId value in installers for other applications.
; (To generate a new GUID, click Tools | Generate GUID inside the IDE.)
AppId={{07102336-D6DC-4C31-B748-01C974F7BD2E}
AppName={#MyAppName}
AppVersion={#MyAppVersion}
;AppVerName={#MyAppName} {#MyAppVersion}
AppPublisher={#MyAppPublisher}
DefaultDirName={autopf}\{#MyAppName}
ChangesAssociations=yes
DisableProgramGroupPage=yes
; Uncomment the following line to run in non administrative install mode (install for current user only.)
;PrivilegesRequired=lowest
OutputDir=D:\setup
OutputBaseFilename=AxMeet-User
SetupIconFile=F:\electron-quick-start\AxMeetingUser\resources\app\dist\favicon.ico
Compression=lzma
SolidCompression=yes
WizardStyle=modern

[Languages]
Name: "english"; MessagesFile: "compiler:Default.isl"

[Tasks]
Name: "desktopicon"; Description: "{cm:CreateDesktopIcon}"; GroupDescription: "{cm:AdditionalIcons}"; Flags: unchecked

[Files]
Source: "F:\electron-quick-start\AxMeetingUser\{#MyAppExeName}"; DestDir: "{app}"; Flags: ignoreversion
Source: "F:\electron-quick-start\AxMeetingUser\*"; DestDir: "{app}"; Flags: ignoreversion recursesubdirs createallsubdirs
; NOTE: Don't use "Flags: ignoreversion" on any shared system files

[Registry]
Root: HKA; Subkey: "Software\Classes\{#MyAppAssocExt}\OpenWithProgids"; ValueType: string; ValueName: "{#MyAppAssocKey}"; ValueData: ""; Flags: uninsdeletevalue
Root: HKA; Subkey: "Software\Classes\{#MyAppAssocKey}"; ValueType: string; ValueName: ""; ValueData: "{#MyAppAssocName}"; Flags: uninsdeletekey
Root: HKA; Subkey: "Software\Classes\{#MyAppAssocKey}\DefaultIcon"; ValueType: string; ValueName: ""; ValueData: "{app}\{#MyAppExeName},0"
Root: HKA; Subkey: "Software\Classes\{#MyAppAssocKey}\shell\open\command"; ValueType: string; ValueName: ""; ValueData: """{app}\{#MyAppExeName}"" ""%1"""
Root: HKA; Subkey: "Software\Classes\Applications\{#MyAppExeName}\SupportedTypes"; ValueType: string; ValueName: ".myp"; ValueData: ""

[Icons]
Name: "{autoprograms}\{#MyAppName}"; Filename: "{app}\{#MyAppExeName}"
Name: "{autodesktop}\{#MyAppName}"; Filename: "{app}\{#MyAppExeName}"; Tasks: desktopicon

[Run]
Filename: "{app}\{#MyAppExeName}"; Description: "{cm:LaunchProgram,{#StringChange(MyAppName, '&', '&&')}}"; Flags: nowait postinstall skipifsilent


```
### 快速安装
可以通过提供的安装包，进行一键安装，快速体验。<br />![image.png](https://cdn.nlark.com/yuque/0/2024/png/29466846/1705376538391-c6e90f46-626d-4cff-ae8b-8e973a88975f.png#averageHue=%231e1d1c&clientId=u6b08f4bd-6db7-4&from=paste&height=63&id=Zy9IV&originHeight=95&originWidth=1042&originalType=binary&ratio=1.5&rotation=0&showTitle=false&size=10797&status=done&style=none&taskId=u4eb70752-295d-4de4-a973-d9cbb410872&title=&width=694.6666666666666)
## 1、实验目的
通过本课程，进一步复习、巩固所学的数据结构与算法、计算机系统基础、密码学基础、 社会科学中的计算思维方法等课程的基本概念、技术原理、软件设计的方法与技术等，初步掌握软件设计的流程与基本方法，能够运用C/C++、Java、Python等至少一种编程语言进行软件开发。  

- 工具层次： 掌握主流软件开发环境搭建、 熟练使用主流软件开发GUI工 具进行软件的编写、调试、 发布等。  
- 方法论层次： 了解软件需求与问题域抽象 方法、常见软件设计开发模式等。  
- 综合应用层次：独立或分组合作完成一个软件开发项目，运用相关知识和工具进行软件系统分析、设计、代码编写、调试、测试和发布。

## 2、实验内容
设计与开发一个会议室预订与管理系统，功能包括但不限于：支持管理员登录功能、支持对会议室资源进行管理，会议室资源包括会议室名称、容纳人数等；支持对会议室进行预约管理，从满足需求的可用会议室资源中给出预约建议，能够对会议室进行预约/取消预约。

## 3、实验工具和环境

- 环境：win11+ubuntu22
- 工具：idea+vscode+postman+navicat+阿里云ECS+docker+git

## 4、软件设计
### 4.1 需求分析
#### 4.1.1 登录需求
1、需要区分普通用户和管理员，管理员与用户之间的权限不同<br />2、在使用时需要使用账号密码登录，不同用户的账号密码应当不同<br />3、需要有输入验证，用户账号密码错误应当提示<br />4、软件界面需要整洁、美观，而且要有菜单栏，菜单栏可收回<br />5、软件登录后要有退出登录的功能<br />6、需要提供更改密码的功能<br />7、需要提供用户注册功能
#### 4.1.2 管理员需求

- **对会议室进行管理**

  (1)显示现有会议室的信息，包括会议室的容量、会议室内设施情况，并能对会议室的信息进行修改<br />（2）能够增加、删减会议室<br />（3）能够查看会议室的使用记录，包括已进行和预约但未进行的会议，要能显示会议的基本信息<br />（4）当会议室历史记录过多时，要能定位到某一天，某个会议室的使用记录

- **对用户进行管理**

（1）能够显示已有用户的基本信息，包括部门名称、账号、密码、联系方式，并对用户信息进行修改<br />（2）能够增加、删减用户<br />（3）能够查看用户的使用会议室记录，包括已进行和预约但未进行的会议，要能显示会议的基本信息<br />（4）当历史记录过多时，要能定位到某一天，某个部门的使用记录

- **对会议室的申请进行审核**

（1）接收用户会议室申请，查看用户的申请记录，包括已审核和未审核的记录，显示用户申请的基本信息<br />（2）可对用户的申请进行审核，根据用户给出的申请理由进行审核，若未通过需给出拒绝理由。<br />（3）审核完成后，需将审核结果发送至用户
#### 4.1.3 用户需求

- **会议室预约**

（1）能查看现有会议室信息，能查看会议室的使用情况、预约情况，能显示并能提前预约七天内的会议室。<br />（2）能查看某个时间段内的会议室是否已经被预约<br />（3）当会议室被预约过多不好选择时，可以给出适当的建议<br />（4）能够取消已经预约的申请

- **会议室预约申请的审核记录**

（1）能够查看用户自身预约会议室的进程，进程包括已通过、未通过以及审核中，若申请未通过则显示拒绝理由<br />（2）能够查看用户自身使用会议室的记录，包括已进行和预约但未进行的会议，要能显示会议的基本信息<br />（3）当历史记录过多时，要能定位到某一天的使用记录
#### 4.1.4 用例图
![image.png](https://cdn.nlark.com/yuque/0/2024/png/29466846/1705423341255-d6f8b2f8-b7d0-4669-bc17-3922c881648e.png#averageHue=%23fdfcfb&clientId=u3f6ef46a-c93e-4&from=paste&height=725&id=uac4f5629&originHeight=1088&originWidth=864&originalType=binary&ratio=1.5&rotation=0&showTitle=false&size=418145&status=done&style=none&taskId=u72f6ff9e-500b-4b1a-b1dc-69d27d7c9e9&title=&width=576)
### 4.2 概要设计
该系统为面向学校部门级别的会议室管理系统，以管理员中心，下发不同部门账号。<br />前端提供普通用户与管理员两个操作界面，后台则为相应的服务，其中包括涉及的分布式中间件，针对运维层面，提供普罗米修斯等监控工具对集群进行实时管理。<br />提供的业务功能包括：预约、信息管理、邮箱通知、AI提示、历史查询等。<br />分层架构图如下：

- 可视层：AxMeetingAdmin，AxMeetingUser
- 业务层：AxMeetingRoom
- 基础层：Redis，MySQL，RocketMQ
- 运维层：Docker，Prometheus，Grafana
- 接入层：ChatGLM-SDK，Snowfake，easy-rateLimiter，springboot-email

![](https://cdn.nlark.com/yuque/0/2024/jpeg/29466846/1704345680763-b2ba1044-9877-440d-8e3e-a52fc0d74eee.jpeg)<br />如下是库表设计：<br />![](https://cdn.nlark.com/yuque/0/2024/jpeg/29466846/1705378827108-fe765e58-71b0-4acf-95a5-31443b248e64.jpeg)
### 4.3 流程设计
#### 4.3.1 区分管理员与用户的登录设计
面向不同的用户群体，我们设计了两个客户端，其一是管理员端，其二是用户端。<br />登录设计模块，其中包括必要的字段，如用户名与密码。

- 对于普通用户：其账号由管理员下发，与部门直接关联
- 对于管理员：其账户由运维人员下发，需要联系运维人员添加

进行登录设置后，下发token令牌，后续用户便无须再验证，保证了安全性的同时，也带来比较好的用户体验。<br />![](https://cdn.nlark.com/yuque/0/2024/jpeg/29466846/1705371480241-8d58cf55-0314-4caa-bac1-54aabc9ffbf3.jpeg)
#### 4.3.2 会议室信息显示与预约功能设计
会议室的信息显示，用户从后台获取数据，为了方便用户操作，我们设计了日期表，映射到数据库中就是一张矩阵表格，用0/1来标记会议室状态。<br />用户填写信息之后，提交表单，后台进行校验，添加记录到审核中，同时下发预约成功通知。<br />![](https://cdn.nlark.com/yuque/0/2024/jpeg/29466846/1705375063067-fd597393-8a89-46a1-8981-69f92a66823a.jpeg)
#### 4.3.3 预约各项记录信息查看
预约记录应该包括已通过、未通过、审核中。<br />因为预约的记录可能会过多，我们这里要实现一个分页查询的效果。<br />用户到后台查询数据后，返回给用户进行查看。<br />![](https://cdn.nlark.com/yuque/0/2024/jpeg/29466846/1705375146074-737cfa80-0e04-47da-98c3-2a9692c67581.jpeg)
#### 4.3.4 智能AI预约建议提示
对于AI智能提示，客户端只需向远程后台发送请求服务。<br />因为AI模型是外部用户提供，故需要开发SDK去进行异步请求。<br />最后，将请求到的信息进行封装，返回给用户。<br />![](https://cdn.nlark.com/yuque/0/2024/jpeg/29466846/1705375146107-318e95e7-a2f6-4801-8e77-90b87ad2767b.jpeg)
#### 4.3.5 预约成功与审核邮箱消息通知
当用户或者管理员进行审核后，将邮箱信息进行推送到消息队列中。<br />最后由消息队列进行异步返回。<br />其目的是实现削峰填谷，避免瞬间的大量请求导致系统奔溃。<br />![](https://cdn.nlark.com/yuque/0/2024/jpeg/29466846/1705374935003-bf946f5d-b934-4578-b8a8-828096a1dc28.jpeg)
#### 4.3.6 管理员审批记录
预约之后，其记录会添加到记录表中。<br />管理员查询记录表中的信息，最后对记录信息进行审核。<br />提交表单，后台校验后，会触发成功通知。<br />![](https://cdn.nlark.com/yuque/0/2024/jpeg/29466846/1705375146144-d58efe72-c527-4990-8294-901a126bf574.jpeg)
#### 4.3.7 管理员会议室与部门管理
会议室信息和部门信息由两个单独的表存储。<br />用户查询后台信息，返回给界面，最后根据界面进行操作信息。<br />![](https://cdn.nlark.com/yuque/0/2024/jpeg/29466846/1705375366107-af6097c5-f2f2-4b89-b240-563ecceed604.jpeg)
#### 4.3.8 性能监控与集群管理
这里通过普罗米修斯采集服务信息，docker-agent采集分布式中间件的信息。<br />通过可视化面板，进行监控，便于运维人员实时管理集群的信息。<br />![](https://cdn.nlark.com/yuque/0/2024/jpeg/29466846/1705376447076-e30018c8-ea04-475c-b61f-8e657b4c022f.jpeg)
## 5.软件运行效果
### 5.1 软件包
用户可以选择安装对应的用户端或者管理员端。
### ![image.png](https://cdn.nlark.com/yuque/0/2024/png/29466846/1705377869482-4834d9c3-a3b0-417e-b799-f5a3491cd269.png#averageHue=%231d1c1b&clientId=ud967c1b8-8596-4&from=paste&height=69&id=u9b390ab9&originHeight=104&originWidth=1059&originalType=binary&ratio=1.5&rotation=0&showTitle=false&size=10935&status=done&style=none&taskId=udc94824f-8b60-40b3-a126-7840704db50&title=&width=706)
安装引导<br />![image.png](https://cdn.nlark.com/yuque/0/2024/png/29466846/1705377937660-880d9879-7915-4faf-8ecc-1f4448d4871f.png#averageHue=%23d3d3d3&clientId=ud967c1b8-8596-4&from=paste&height=483&id=udafb8064&originHeight=725&originWidth=974&originalType=binary&ratio=1.5&rotation=0&showTitle=false&size=31141&status=done&style=none&taskId=uf865bbe5-5e38-45d8-af24-b91a141c590&title=&width=649.3333333333334)

### 5.2 用户端
#### 5.2.1 登录演示：通过管理员下发账户
用户输入正确的账户和密码，即可登录成功。<br />![image.png](https://cdn.nlark.com/yuque/0/2024/png/29466846/1704247248451-a3808a34-0fcd-4062-a2c3-a5d17456794b.png#averageHue=%2395a0a0&clientId=uee5df6db-df6c-4&from=paste&height=856&id=u14a386b8&originHeight=1284&originWidth=2277&originalType=binary&ratio=1.5&rotation=0&showTitle=false&size=1363258&status=done&style=none&taskId=u28f45a85-ee54-4c0c-8785-cdc5a10723f&title=&width=1518)<br />![image.png](https://cdn.nlark.com/yuque/0/2024/png/29466846/1704247344926-dc83f416-195f-45c6-ba82-a87a67db8b47.png#averageHue=%23fcfcfb&clientId=uee5df6db-df6c-4&from=paste&height=934&id=u46c3e059&originHeight=1408&originWidth=2547&originalType=binary&ratio=1.5&rotation=0&showTitle=false&size=313097&status=done&style=none&taskId=uc24cb286-7bf3-4607-b841-66e594affbf&title=&width=1689)
#### 5.2.2 预约演示：上交申请，等待管理员审核
用户查询会议情况，根据会议情况，上传预约表单。<br />![image.png](https://cdn.nlark.com/yuque/0/2024/png/29466846/1704247162645-408aca16-2be1-43e8-96ee-e203731c188c.png#averageHue=%23fdfdfd&clientId=uee5df6db-df6c-4&from=paste&height=769&id=u09ac47de&originHeight=1153&originWidth=2159&originalType=binary&ratio=1.5&rotation=0&showTitle=false&size=167139&status=done&style=none&taskId=u39a074e8-d97b-4366-8cf3-2c272b8a1d1&title=&width=1439.3333333333333)
#### 5.2.3 记录查看：查看自己提交的预约结果、会议历史、取消预约等。
用户可以根据需要，特定条件查询自己的预约记录。<br />![image.png](https://cdn.nlark.com/yuque/0/2024/png/29466846/1704246973647-84d9a283-6ca4-485d-b962-af340837d7a3.png#averageHue=%23fefefe&clientId=uee5df6db-df6c-4&from=paste&height=259&id=u75372581&originHeight=389&originWidth=2207&originalType=binary&ratio=1.5&rotation=0&showTitle=false&size=27916&status=done&style=none&taskId=u1c09bebc-f6e2-4c96-bde4-84ca376815e&title=&width=1471.3333333333333)<br />![image.png](https://cdn.nlark.com/yuque/0/2024/png/29466846/1704247009726-0e4af162-f268-4926-b44d-319b6f677a6e.png#averageHue=%23fefefe&clientId=uee5df6db-df6c-4&from=paste&height=266&id=u4935bbba&originHeight=399&originWidth=2208&originalType=binary&ratio=1.5&rotation=0&showTitle=false&size=30652&status=done&style=none&taskId=ue29355e9-2017-4caf-9709-2d667d6f820&title=&width=1472)<br />![image.png](https://cdn.nlark.com/yuque/0/2024/png/29466846/1704247088346-40548d54-8414-413b-a280-951baf651d1c.png#averageHue=%23fefefe&clientId=uee5df6db-df6c-4&from=paste&height=260&id=uc6380117&originHeight=390&originWidth=2218&originalType=binary&ratio=1.5&rotation=0&showTitle=false&size=30648&status=done&style=none&taskId=ua790c4e1-9f5a-4af6-896a-4639d134712&title=&width=1478.6666666666667)<br />![image.png](https://cdn.nlark.com/yuque/0/2024/png/29466846/1704247195038-b423f549-3a64-4257-959a-aa736becc3fa.png#averageHue=%23fefefe&clientId=uee5df6db-df6c-4&from=paste&height=267&id=u8a21d048&originHeight=401&originWidth=2215&originalType=binary&ratio=1.5&rotation=0&showTitle=false&size=27812&status=done&style=none&taskId=u9140d961-7ba7-4c9f-a3cb-7932d17cce6&title=&width=1476.6666666666667)<br />![image.png](https://cdn.nlark.com/yuque/0/2024/png/29466846/1704247286586-90b5006f-eac0-4ee3-a664-598cbce511e4.png#averageHue=%23fefdfc&clientId=uee5df6db-df6c-4&from=paste&height=351&id=u89cec371&originHeight=526&originWidth=2220&originalType=binary&ratio=1.5&rotation=0&showTitle=false&size=40621&status=done&style=none&taskId=u63b953ca-96a1-4425-8a4c-f5f4d611764&title=&width=1480)
#### 5.2.4 预约建议：openAI提供预约建议
用户可以向AI助手进行提问，从而获得智能的建议。<br />![image.png](https://cdn.nlark.com/yuque/0/2024/png/29466846/1704246735561-1743ea26-660e-41fd-af49-deecf10c58a7.png#averageHue=%23fcfbfa&clientId=uee5df6db-df6c-4&from=paste&height=823&id=ufbb13824&originHeight=1234&originWidth=1957&originalType=binary&ratio=1.5&rotation=0&showTitle=false&size=279628&status=done&style=none&taskId=uf697b824-cd7f-4a4e-80e5-c517559010a&title=&width=1304.6666666666667)
#### 5.2.5 消息通知：预约成功与审核结果通知
预约提交与审核通过，用户都可以收到相应的短信通知。<br />![image.png](https://cdn.nlark.com/yuque/0/2024/png/29466846/1704246783805-63acdd0b-017e-47a2-b478-83a7e42a6432.png#averageHue=%23a7c290&clientId=uee5df6db-df6c-4&from=paste&height=137&id=u519a4a6a&originHeight=208&originWidth=948&originalType=binary&ratio=1.5&rotation=0&showTitle=false&size=25086&status=done&style=none&taskId=u13c3ed74-b4aa-4bae-b869-29733e06e35&title=&width=626)<br />![image.png](https://cdn.nlark.com/yuque/0/2024/png/29466846/1704246817043-40c2b3f1-f9f4-4cdc-bfad-851fb6e4c19f.png#averageHue=%23acd9d3&clientId=uee5df6db-df6c-4&from=paste&height=121&id=uef263219&originHeight=182&originWidth=941&originalType=binary&ratio=1.5&rotation=0&showTitle=false&size=25513&status=done&style=none&taskId=u74120170-dc71-44d8-bb91-49a92cd2a00&title=&width=627.3333333333334)
### 5.3 管理员端
#### 5.3.1 登录演示：由运维人员下发账户
管理员输入正确的账户和密码，即可登录成功。<br />![image.png](https://cdn.nlark.com/yuque/0/2024/png/29466846/1704247390537-58eca8b7-1783-403c-bc3b-6006cae7d599.png#averageHue=%23597480&clientId=uee5df6db-df6c-4&from=paste&height=826&id=u92ed6fad&originHeight=1239&originWidth=2229&originalType=binary&ratio=1.5&rotation=0&showTitle=false&size=2740462&status=done&style=none&taskId=ua72b8ca1-9c94-49fe-be12-a8d80bba617&title=&width=1486)<br />![image.png](https://cdn.nlark.com/yuque/0/2024/png/29466846/1704247422054-99d04ab6-b003-4db7-bdd9-5ce3b156addd.png#averageHue=%23fdfcfc&clientId=uee5df6db-df6c-4&from=paste&height=936&id=u4aaffc7d&originHeight=1404&originWidth=2530&originalType=binary&ratio=1.5&rotation=0&showTitle=false&size=112808&status=done&style=none&taskId=u1aa0f459-66af-4fe7-b044-75dbc1850c2&title=&width=1686.6666666666667)
#### 5.3.2 审批记录：同意与接受，并下发邮箱通知
管理员可以根据需要，特定条件查询自己的审核记录，并下发对应通知。<br />![image.png](https://cdn.nlark.com/yuque/0/2024/png/29466846/1704247560270-1010b7eb-7772-4eef-9056-eea73e0c3f7c.png#averageHue=%23fefefe&clientId=uee5df6db-df6c-4&from=paste&height=346&id=u51a88d5e&originHeight=519&originWidth=2232&originalType=binary&ratio=1.5&rotation=0&showTitle=false&size=35552&status=done&style=none&taskId=ua0662c67-eea2-4219-b165-798b4d13207&title=&width=1488)<br />![image.png](https://cdn.nlark.com/yuque/0/2024/png/29466846/1704247571746-9211d6b4-c6c1-4e02-8749-91cd0c212f36.png#averageHue=%23fefefe&clientId=uee5df6db-df6c-4&from=paste&height=316&id=u264bf053&originHeight=474&originWidth=2229&originalType=binary&ratio=1.5&rotation=0&showTitle=false&size=38927&status=done&style=none&taskId=u87ec5ee0-9fd7-42a3-8fd5-38682315289&title=&width=1486)<br />![image.png](https://cdn.nlark.com/yuque/0/2024/png/29466846/1704247581566-db6e06be-f3c6-4fae-8802-6407bd779b7c.png#averageHue=%23fefefe&clientId=uee5df6db-df6c-4&from=paste&height=275&id=uae70c3c0&originHeight=412&originWidth=2226&originalType=binary&ratio=1.5&rotation=0&showTitle=false&size=34061&status=done&style=none&taskId=u9a054239-e948-44a9-905d-13b41812d30&title=&width=1484)
#### 5.3.3 部门管理：授权部门账号权限，即添加管理部门信息
管理员可对部门信息进行管理。<br />![image.png](https://cdn.nlark.com/yuque/0/2024/png/29466846/1704247500151-f33e1ad3-d468-49df-a5d3-c384dab050b0.png#averageHue=%23fdfcfc&clientId=uee5df6db-df6c-4&from=paste&height=479&id=uc10a838c&originHeight=719&originWidth=2202&originalType=binary&ratio=1.5&rotation=0&showTitle=false&size=54880&status=done&style=none&taskId=u18366453-c5a6-47eb-b913-a0ebeefdbe7&title=&width=1468)
#### 5.3.4 会议室管理：管理会议室情况，包括修改状态、添加会议室、删除会议室、搜索会议室
管理员可对会议室信息进行管理。<br />![image.png](https://cdn.nlark.com/yuque/0/2024/png/29466846/1704247452806-161bfd70-7ce6-4bb6-865b-ab7fcb54e60f.png#averageHue=%23fdfdfd&clientId=uee5df6db-df6c-4&from=paste&height=649&id=MTd8I&originHeight=973&originWidth=2220&originalType=binary&ratio=1.5&rotation=0&showTitle=false&size=61615&status=done&style=none&taskId=uc8dba43d-096d-41f0-b723-aff344e09a8&title=&width=1480)
### 5.4 运维端
#### 5.4.1 性能监控：对服务进行实时监控
运维人员可以实时查看服务器的情况，监控服务性能，保证可用。

- ip：[http://localhost:4003/](http://localhost:4003/)

![image.png](https://cdn.nlark.com/yuque/0/2024/png/29466846/1704265763234-d9fb8af5-2974-469c-9843-ff6e97516ab2.png#averageHue=%231a1d22&clientId=u1e991df6-a3a9-4&from=paste&height=832&id=GJkoH&originHeight=1248&originWidth=2549&originalType=binary&ratio=1.5&rotation=0&showTitle=false&size=275010&status=done&style=none&taskId=ub6e8ea7f-bec2-4800-9296-f8ded93417f&title=&width=1699.3333333333333)
#### 5.4.2 集群管理：对docker集群健康监控
运维人员可以对集群的健康状态进行观察，防止中间件异常。

- ip：[http://localhost:9000/](http://localhost:9000/#!/home)

![image.png](https://cdn.nlark.com/yuque/0/2024/png/29466846/1704253931924-beb6edb2-8ffb-42b7-add9-41aa22d97559.png#averageHue=%23242424&clientId=uee5df6db-df6c-4&from=paste&height=600&id=WOpzZ&originHeight=900&originWidth=2081&originalType=binary&ratio=1.5&rotation=0&showTitle=false&size=219449&status=done&style=none&taskId=u8139d0bc-5760-4165-af33-fcbf6d32d5c&title=&width=1387.3333333333333)
## 6.实验体会
本次实验我们小组所选取的内容是：设计与开发一个会议室预订与管理系统，功能包括但不限于：支持管理员登录功能、支持对会议室资源进行管理，会议室资源包括会议室名称、容纳人数等；支持对会议室进行预约管理，从满足需求的可用会议室资源中给出预约建议，能够对会议室进行预约/取消预约。<br />拿到这个题目的时候，我们先进行了分工与需求分析。在需求分析这块，我们收获很多，我们采取的思路是进行头脑风暴，互相挖掘对方需求的潜在需求。如登录，则包括是否有对登录的信息进行校验等。在这种头脑风暴下，我们的功能设计更加完善。<br />其次的收获是在软件工具的使用，我们这次采取了很多工具，如用postman来进行接口测试，用git来做版本管理，用navicat来进行数据库管理，这些工具的使用，极大提升了我们的开发效率。其中最让我们喜欢的就是docker，docker的使用，方便了我们对于集群的管理，简化了部署流程，从而把精力集中在开发上。<br />总体上，此次我们的设计较为满意，在一个星期内，一起合伙开发软件从前端到后端，到最后的打包，软件发布等。其中也涉及学到了很多网络安全知识。作为一个网络安全专业的同学，未来我们会更加努力学习软件开发知识，在开发中去巩固潜在的安全知识，从而多方面提升自己的硬实力！
