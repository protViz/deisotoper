#!/bin/bash


# INPUT  mgf
# OUTPUT mgf
# TODO

# java my.jar /dev/stdin /dev/stdout

javac -cp '.:../inst/java/deisotoper-1.0-SNAPSHOT.jar:../inst/java/*.jar' ExecDeisotoper.java
jar -cf ExecDeisotoper.jar ExecDeisotoper
java -cp '.:../inst/java/deisotoper-1.0-SNAPSHOT.jar:../inst/java/*.jar' ExecDeisotoper

