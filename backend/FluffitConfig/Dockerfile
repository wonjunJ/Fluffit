FROM openjdk:17-jdk-slim

ARG JAR_FILE=build/libs/FluffitConfig-0.0.1-SNAPSHOT.jar

# 환경변수 추가
ENV JASYPT_KEY=$JASYPT_KEY
ENV JKS_PWD=$JKS_PWD
ENV JKS_ALIAS=$JKS_PWD

# jar 파일 복제
COPY ${JAR_FILE} app.jar

EXPOSE 8080
# 실행 명령어
ENTRYPOINT ["java", "-DJASYPT_KEY=${JASYPT_KEY}", "-DJKS_PWD=${JKS_PWD}", "-DJKS_ALIAS=${JKS_ALIAS}", "-jar", "app.jar"]
