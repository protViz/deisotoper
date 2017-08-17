# Christian Panse <cp@fgcz.ethz.ch>
# 2017-08-15

jar: javac
	jar cf ../inst/java/deisotoper.jar ./*

javac: clean
	javac FindPatternBinarySearch.java  FindPatternLinear.java

clean:
	$(RM) ../inst/java/deisotoper.jar *.class
