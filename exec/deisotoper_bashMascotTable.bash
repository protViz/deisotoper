#!/bin/bash

#Usage (normal): bashMascotTable X Y Z >> A.csv (X, Y, Z are .dat-files and A.csv is the output .csv-file)
#Usage (parallel): find FOLDER -name "*.dat" | parallel ./bashMascotTable >> A.csv (FOLDER contains .dat-files and A.csv is the output .csv-file)

function csvline() {
        declare -n THIS=$1

	local line=""
	for a in "${!THIS[@]}"
        do
		line+="${line:+,}${THIS[${a}]}"
	done
	echo "$line"
}

function getparameter() {
# this runs only once over the dat-file
	in="$1"
	[ -e "$in" ] || exit 1

	unset -v A
	declare -a q=()
# index 1: filename
	q[1]="$( basename "$in" )"
# index 0: SearchName
	q[0]=$( awk -F"=" '/^COM=/{print $2;}' "$in" )

	if [[ ${q[1]} =~ ^F([[:digit:]]+) ]]; then
# index 2: JobId
		q[2]=${BASH_REMATCH[1]}
	fi

# index 3: NrSpectra
# index 4: NrSpectraOver25
        q+=( $( awk -F"," '/^q[[:digit:]]+_p1=/{c++; if($8 > 25) cx++;} END {print c; print cx;}' "$in" ) )
# index 5: Over25inPercent
	q+=( $( printf "%.2f" $( bc -l <<< "${q[4]}*100/${q[3]}" ) ) )

        csvline q
}

for i in "$@"
do
	getparameter "$i"
done
