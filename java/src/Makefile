# Christian Panse <cp@fgcz.ethz.ch>
# 2017-08-15

.SUFFIXES: .java .class

CLASSES = $(shell ls *.java | grep -v UnitTest)
JAR=../../inst/java/deisotoper.jar

test: jar
	jar tf $(JAR)

jar: clean $(CLASSES:.java=.class) md
	jar cf $(JAR) ./* 

.java.class:
	javac $< -cp .:../../inst/java/:../../inst/java/gson-2.8.1.jar

clean:
	$(RM) $(JAR) *.class

md:
	mkdir -p ../inst/java/
