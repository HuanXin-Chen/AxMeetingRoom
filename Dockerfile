FROM openjdk:8-jre-slim AS base

# 作者
MAINTAINER 3500
# 配置
ENV PARAMS=""
# 时区
ENV TZ=PRC
RUN ln -snf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone
# 添加应用
ADD target/meeting.jar /meeting.jar
## 在镜像运行为容器后执行的命令
ENTRYPOINT ["sh","-c","java -jar $JAVA_OPTS /meeting.jar $PARAMS"]

