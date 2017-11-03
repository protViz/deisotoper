#!/bin/bash
# Usage: cat in.mgf | deisotoper_deisotope.bash > out.mgf
TEMPFILEIN=$( mktemp )
TEMPFILEOUT=$( mktemp )
cat > "$TEMPFILEIN" 
java -cp '../inst/java/ExecDeisotoper.jar:../inst/java/deisotoper-1.0-SNAPSHOT.jar:../inst/java/jgrapht-core-1.0.1.jar' ExecDeisotoper "$TEMPFILEIN" "$TEMPFILEOUT"
cat "$TEMPFILEOUT"
rm -f "$TEMPFILEIN" "$TEMPFILEOUT"

