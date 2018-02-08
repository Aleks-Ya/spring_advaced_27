# Course "Spring Advanced 31.2"
Participant: Aleksei Iablokov

## Requirements
1) Java 8
2) Maven 3
3) Free 8080 port

## Local run
1) `mvn jetty:run`
2) Open `http://localhost:8080`

Predefined users:
  - `john@gmail.com`/`jpass` (REGISTERED_USER and BOOKING_MANAGER roles)
  - `mary@gmail.com`/`mpass` (REGISTERED_USER role)

## Sources
https://github.com/Aleks-Ya/spring_advanced_31-2

## Code analysis
[Sonar Report](https://sonarcloud.io/dashboard?id=com.epam%3Aspring-advanced-course)

Analyze command:
```
mvn clean org.jacoco:jacoco-maven-plugin:prepare-agent package sonar:sonar \
   -Dsonar.organization=aleks-ya-github \
   -Dsonar.host.url=https://sonarcloud.io \
   -Dsonar.login=the_api_token
```

## Release
Used maven-release-plugin.

There is no remote Maven repository, so `release:perform` is not executed.

Release command: `mvn release:prepare release:clean`. The command increments version, creates tag and pushes changes.

## Release notes
**3.2**
- Remove operation "Get event by name"
- Change "Get event by ID" endpoint path from "/id/{eventId}" to "/{eventId}"
- Change "Get user by ID" endpoint path from "/id/{userId}" to "/{userId}"

**v3.1**
- Create "Restricted area" on the home page
- Choose user on the refilling page 

**v3.0**
- User account feature
- Spring transactions
- Replace Hibernate XML configuration with annotations 
- Bug fixes
