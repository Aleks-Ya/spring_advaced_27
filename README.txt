Aleksei Iablokov, Spring Advanced 31.2

Requirements:
1) Java 8
2) Maven 3
3) Free 8080 port

Local run:
1) mvn jetty:run
2) Open http://localhost:8080

Base URL: http://localhost:8080

Predefined users:
  - john@gmail.com/jpass (REGISTERED_USER and BOOKING_MANAGER roles)
  - mary@gmail.com/mpass (REGISTERED_USER role)

GitHub: https://github.com/Aleks-Ya/spring_advanced_31-2

Sonar Report: https://sonarcloud.io/dashboard?id=com.epam%3Aspring-advanced-course
Analyze command:
    mvn clean org.jacoco:jacoco-maven-plugin:prepare-agent package sonar:sonar \
       -Dsonar.organization=aleks-ya-github \
       -Dsonar.host.url=https://sonarcloud.io \
       -Dsonar.login=the_token