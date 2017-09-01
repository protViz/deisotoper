# Christian Panse <cp@fgcz.ethz.ch>
# 2017-08-15

.SUFFIXES: .java .class

MKDIR = mkdir -p
CLASSES = $(shell ls *.java | grep -v UnitTest)
CLASSPATH = .:../../inst/java/:../../inst/java/gson-2.8.1.jar
JAR=../../inst/java/deisotoper.jar

test: jar
	jar tf $(JAR)

jar: clean $(CLASSES:.java=.class) md
	jar cf $(JAR) ./* 

.java.class:
	javac $< -cp $(CLASSPATH)

clean:
	$(RM) $(JAR) *.class

md:
	$(MKDIR) ../../inst/java/

