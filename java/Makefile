MVNOPT=-X

cmp:
	cd deisotoper && mvn install $(MVNOPT)

clean:
	cd deisotoper && mvn clean:clean $(MVNOPT)

test:
	cd deisotoper && mvn -Dmaven.test.skip=true $(MVNOPT)

