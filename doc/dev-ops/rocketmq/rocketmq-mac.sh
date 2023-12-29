# https://hub.docker.com/r/xuchengen/rocketmq
docker run -itd \
 --name=rocketmq \
 --hostname rocketmq \
 --restart=always \
 -p 7397:7397 \
 -p 9876:9876 \
 -p 10909:10909 \
 -p 10911:10911 \
 -p 10912:10912 \
 -v rocketmq_data:/home/app/data \
 -v /etc/localtime:/etc/localtime \
 -v /var/run/docker.sock:/var/run/docker.sock \
 --net=host \
 xuchengen/rocketmq:latest