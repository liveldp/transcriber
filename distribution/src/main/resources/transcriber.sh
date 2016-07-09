#!/usr/bin/env bash

#download Java
#wget --no-cookies --header "Cookie: oraclelicense=accept-securebackup-cookie" http://download.oracle.com/otn-pub/java/jdk/8u91-b14/jdk-8u91-linux-x64.tar.gz

#unzip jdk
#tar -xvzf jdk-8u91-linux-x64.tar.gz

#remove the compressed file
#rm jdk-8u91-linux-x64.tar.gz

#export JDK_HOME=/opt/jdk1.8.0_91
#export JAVA_HOME=/opt/jdk1.8.0_91
#export PATH=$PATH:$JAVA_HOME/bin

java -jar -Dtranscriber.sparql.endpoint=http://138.4.249.224:8890/sparql -Dtranscriber.rabbitmq.host=rabbit transcriber-distribution-0.0.1.jar