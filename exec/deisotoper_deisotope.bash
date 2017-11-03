#!/bin/bash


# INPUT  mgf
# OUTPUT mgf
# TODO

# java my.jar /dev/stdin /dev/stdout

javac -cp '.:../inst/java/deisotoper-1.0-SNAPSHOT.jar' ExecDeisotoper.java
jar -cf ExecDeisotoper.jar ExecDeisotoper



java -cp 'ExecDeisotoper.jar:../inst/java/deisotoper-1.0-SNAPSHOT.jar:../inst/java/jgrapht-core-1.0.1.jar' ExecDeisotoper test.mgf test333.mgf
