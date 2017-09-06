# Christian Panse <cp@fgcz.ethz.ch>
# 2017-08-15

.SUFFIXES: .java .class

MKDIR = mkdir -p
CLASSES = $(shell ls *.java | grep -v Test)

CLASSPATH = .:$(JARDIR):$(JARDIR)/gson-2.8.1.jar

JAR=$(JARDIR)/deisotoper.jar

test: jar
	jar tf $(JAR)

jar: clean $(CLASSES:.java=.class) md
	jar cf $(JAR) ./* 

.java.class:
	javac $< -cp $(CLASSPATH)

clean:
	$(RM) $(JAR) *.class

md:
	$(MKDIR) $(JARDIR)

