#!/usr/bin/Rscript

args <- commandArgs(TRUE)

library(deisotoper)

deisotoper:::jBenchmark(input = args[[1]], output = paste(args[[2]], "_fdbm_first.mgf", sep = ""), method = "fdbm", modus="first")
deisotoper:::jBenchmark(input = args[[1]], output = paste(args[[2]], "_fdbm_mean.mgf", sep = ""), method = "fdbm", modus="mean")
deisotoper:::jBenchmark(input = args[[1]], output = paste(args[[2]], "_fdbm_last.mgf", sep = ""), method = "fdbm", modus="last")
deisotoper:::jBenchmark(input = args[[1]], output = paste(args[[2]], "_fdbm_highest.mgf", sep = ""), method = "fdbm", modus="highest")
if(file.exists("/srv/lucas1/deisotoper/bash/AminoAcidMasses.ini")) {
  deisotoper:::jBenchmark(input = args[[1]], output = paste(args[[2]], "_fdbm_with_mod_first.mgf", sep = ""), method = "fdbm", modus="first", aamassfile = "/srv/lucas1/deisotoper/bash/AminoAcidMasses.ini")
  deisotoper:::jBenchmark(input = args[[1]], output = paste(args[[2]], "_fdbm_with_mod_mean.mgf", sep = ""), method = "fdbm", modus="mean", aamassfile = "/srv/lucas1/deisotoper/bash/AminoAcidMasses.ini")
  deisotoper:::jBenchmark(input = args[[1]], output = paste(args[[2]], "_fdbm_with_mod_last.mgf", sep = ""), method = "fdbm", modus="last", aamassfile = "/srv/lucas1/deisotoper/bash/AminoAcidMasses.ini")
  deisotoper:::jBenchmark(input = args[[1]], output = paste(args[[2]], "_fdbm_highest.mgf", sep = ""), method = "fdbm", modus="highest", aamassfile = "/srv/lucas1/deisotoper/bash/AminoAcidMasses.ini")
}
#deisotoper:::jBenchmark(input = args[[1]], output = paste(args[[2]], "_mspy.mgf", sep = ""), method = "mspy")

rm(list=ls())
