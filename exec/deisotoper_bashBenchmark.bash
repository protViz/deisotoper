#!/bin/bash

#Usage (normal): ./bashBenchmark X Y Z (X, Y, Z are .mgf-files)
#Usage (parallel): find FOLDER -name "*.mgf" | parallel ./bashBenchmark (FOLDER contains .mgf-files)

RBENCHMARK="./deisotoper_bashBenchmarkAdapter.R"
PROTVIZ=$(Rscript -e 'find.package("protViz")' | cut -d '"' -f2)
MGF2RDATA="${PROTVIZ}/protViz_mgf2RData.pl"

function benchmark() {
	in="$1"
	[ -e "$in" ] || exit 1

	out="${in%.*}_benchmark"
	tfile="R$RANDOM.RData"

	rm -f "$tfile"

	case "${in}" in
		*.mgf)
			"$MGF2RDATA" -n="${tfile%.*}" > /dev/null < "$in"
			[ -s "$tfile" ] || exit 1
			"$RBENCHMARK" "$tfile" "$out" > /dev/null
			rm -f "$tfile"
		;;
		*.RData)
			"$RBENCHMARK" "$in" "$out" > /dev/null
		;;
		*)
			echo "Wrong data format. Must be .mgf or .RData!"
			exit 1
		;;
	esac
}

for i in "$@" 
do
	benchmark "$i"
done
