MVNOPT=-X

cmp:
	cd deisotoper && mvn install -Dmaven.test.skip=true $(MVNOPT)
	mv ../temp/java/deisotoper-1.0-SNAPSHOT.jar ../inst/java/

clean:
	cd deisotoper && mvn clean:clean $(MVNOPT)

test:
	cd deisotoper && mvn install $(MVNOPT)
	mv ../temp/java/deisotoper-1.0-SNAPSHOT.jar ../inst/java/
