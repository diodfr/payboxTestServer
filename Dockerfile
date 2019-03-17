FROM alpine as sign
RUN apk update && \
  apk add --no-cache openssl && \
  rm -rf /var/cache/apk/*
RUN mkdir /work
RUN openssl genrsa -out /work/prvkey.pem 1024
RUN openssl pkcs8 -topk8 -inform PEM -outform DER -in /work/prvkey.pem -nocrypt -out /work/prvkey.der
RUN openssl rsa -in /work/prvkey.pem -pubout -out /work/pubkey.pem

FROM alpine/git:latest as git
RUN mkdir /work
RUN cd / && git clone https://github.com/diodfr/payboxTestServer.git work

FROM openjdk:11-jdk-slim as builder
RUN mkdir /work
COPY --from=git /work /work/
RUN cd /work && ./gradlew build

FROM openjdk:11-jre-slim as runner
RUN mkdir /work
COPY --from=sign /work/prvkey.pem /work
COPY --from=sign /work/prvkey.der /work
COPY --from=sign /work/pubkey.pem /work
COPY --from=builder /work/build/libs/*-all.jar PayboxTestServer.jar
CMD java ${JAVA_OPTS} -jar PayboxTestServer.jar