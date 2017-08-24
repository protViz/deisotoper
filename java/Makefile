# Christian Panse <cp@fgcz.ethz.ch>
# 2017-08-15

.SUFFIXES: .java .class

CLASSES = $(shell ls *.java | grep -v UnitTest)
JAR=../inst/java/deisotoper.jar

test: jar
	jar tf $(JAR)

jar: clean $(CLASSES:.java=.class)
	jar cf ../inst/java/deisotoper.jar ./* 

.java.class:
	javac $< -cp .:../inst/java/                                      

clean:
	$(RM) $(JAR) *.class
