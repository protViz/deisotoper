#!/usr/bin/Rscript

args <- commandArgs(TRUE)

library(deisotoper)

deisotoper:::jBenchmark(input = args[[1]], output = paste(args[[2]], "_fdbm_first.mgf", sep = ""), method = "fdbm", modus="first")
if(file.exists("/srv/lucas1/deisotoper/ini/AminoAcidMasses.ini")) {
  deisotoper:::jBenchmark(input = args[[1]], output = paste(args[[2]], "_fdbm_with_mod_first.mgf", sep = ""), method = "fdbm", modus="first", aamassfile = "/srv/lucas1/deisotoper/ini/AminoAcidMasses.ini")
}
#deisotoper:::jBenchmark(input = args[[1]], output = paste(args[[2]], "_mspy.mgf", sep = ""), method = "mspy")

rm(list=ls())
