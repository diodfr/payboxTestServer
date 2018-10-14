FROM alpine as sign
RUN apk update && \
  apk add --no-cache openssl && \
  rm -rf /var/cache/apk/*
RUN mkdir /work
RUN openssl genrsa -out /work/prvkey.pem 1024
RUN openssl pkcs8 -topk8 -inform PEM -outform DER -in /work/prvkey.pem -nocrypt -out /work/prvkey.der
RUN openssl rsa -in /work/prvkey.pem -pubout -out /work/pubkey.pem

FROM openjdk:11-jdk-slim as runner
RUN mkdir /work
COPY --from=sign /work/prvkey.pem /work
COPY --from=sign /work/prvkey.der /work
COPY --from=sign /work/pubkey.pem /work
COPY build/libs/*-all.jar PayboxTestServer.jar
CMD java ${JAVA_OPTS} -jar PayboxTestServer.jar