mvn clean org.jacoco:jacoco-maven-plugin:prepare-agent package sonar:sonar \
    -Dsonar.host.url=https://sonarcloud.io \
    -Dsonar.organization=wolski-github \
    -Dsonar.login=5a89957c8a1813b2f8fc561475c64a46050399f2