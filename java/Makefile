# Christian Panse <cp@fgcz.ethz.ch>
# 2017-08-15

.SUFFIXES: .java .class

CLASSES = $(shell ls *.java | grep -v UnitTest)

test: jar
	jar tf ../inst/java/deisotoper.jar

jar: clean $(CLASSES:.java=.class)
	jar cf ../inst/java/deisotoper.jar ./*

.java.class:
	javac $<

clean:
	$(RM) ../inst/java/deisotoper.jar *.class
