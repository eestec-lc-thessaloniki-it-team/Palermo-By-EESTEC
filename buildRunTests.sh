#!/bin/sh
username=
password=



mvn clean install -DskipTests
cp -r mysql/ target/wildfly-8.1.0.Final/modules/com/
cp -r mysql/ target/wildfly-8.1.0.Final/modules/com/
cp standalone.xml target/wildfly-8.1.0.Final/standalone/configuration/standalone.xml
export usernameDB=$username
export passwordDB=$password
printenv > env.properties
sh target/wildfly-8.1.0.Final/bin/jboss-cli.sh --properties=env.properties --commands=quit

mvn test
