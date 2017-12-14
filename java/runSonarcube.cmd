mvn clean org.jacoco:jacoco-maven-plugin:prepare-agent package sonar:sonar \
    -Dsonar.host.url=https://sonarcloud.io \
    -Dsonar.organization=wolski-github \
    -Dsonar.login=5a89957c8a1813b2f8fc561475c64a46050399f2 \

	
	
	
git checkout -- ../../inst/java/gson-2.8.1.jar
git checkout --  ../inst/java/LICENSE-AL.txt
git checkout --  ../inst/java/LICENSE-EPL.txt
git checkout --  ../inst/java/LICENSE-LGPL.txt
git checkout --  ../inst/java/LICENSE-README.txt
git checkout --  ../inst/java/gson-2.8.1.jar
git checkout --  ../inst/java/jgrapht-core-1.0.1.jar
