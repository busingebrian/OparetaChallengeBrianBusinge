# OparetaChallengeBrianBusinge
# Android QA coding assessment
# forked this Android project from https://github.com/opareta/OparetaChallenge
# Using continuous integration 
# added jacoco for code coverage
run: 
./gradlew createDebugCoverageReport 

# How to run reports on your local machine to generate the jacoco sonarqube reports
#Requirements
 - sonar local server 
  . install docker (must have prior knowledge about docker)
 - add sonarqube plugins to your gradle
 - run:  ./gradlew sonarqube -Dsonar.host.url=http://localhost:9000/ -Dsonar.login=$REPLACE_WITH_GENERATED_TOKEN
 - add jacoco library to your android application
 - run: ./gradlew clean jacocoTestReport sonarqube -Dsonar.host.url=http://localhost:9000/ -Dsonar.login=$REPLACE_WITH_GENERATED_TOKEN
 - add sonarqube to Android CI pipeline
 

