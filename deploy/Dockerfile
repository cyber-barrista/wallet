FROM openjdk:17-jdk-buster

ENV PROJECT_HOME /opt/wallet

RUN mkdir -p $PROJECT_HOME

COPY rest*.jar $PROJECT_HOME/
COPY run-wallet.sh $PROJECT_HOME/

COPY * $PROJECT_HOME/

WORKDIR $PROJECT_HOME

CMD ./run-wallet.sh
