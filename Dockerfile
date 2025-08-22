# 자바 이미지
FROM amazoncorretto:17

# 타임존 설정
ENV TZ=Asia/Seoul

# 작업 디렉토리 설정
# 컨테이너 내부의 작업 디렉토리를 지정 -> 이후 모든 명령은 /app 디렉토리 안에서 실행됨
WORKDIR /app

# JAR 파일을 컨테이너로 복사 -> /app/app.jar
COPY build/libs/board-0.0.1-SNAPSHOT.jar app.jar

# 애플리케이션 포트 (예: 8080)
EXPOSE 8080

# 애플리케이션 실행 -> 컨테이너가 시작될 때 java -jar app.jar 명령을 실행
ENTRYPOINT ["java", "-jar", "app.jar"]