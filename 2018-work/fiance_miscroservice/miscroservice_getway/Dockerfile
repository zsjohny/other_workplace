FROM java
VOLUME /tmp
ADD ./miscroservice*.jar /miscroservice.jar
ENV TZ=Asia/Shanghai
RUN ln -snf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone
ENTRYPOINT ["java", "-Djava.security.egd=file:/dev/./urandom","-Xms216m","-Xmx512m","-jar","/miscroservice.jar"]