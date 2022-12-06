FROM openjdk

RUN mkdir /app

ADD ./target/backend-dictionary-0.0.1-SNAPSHOT.jar /app/backend-dictionary-0.0.1-SNAPSHOT.jar
ADD ./target/serviceAccount.json /app/serviceAccount.json
WORKDIR /app

CMD ["java","-jar","backend-dictionary-0.0.1-SNAPSHOT.jar"]
