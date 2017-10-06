#!/usr/bin/Rscript

args <- commandArgs(TRUE)

library(deisotoper)

rm(list=ls())

deisotoper:::jBenchmark(input = args[[1]], output = paste(args[[2]], "_fdbm.mgf", sep = ""), method = "fdbm")
deisotoper:::jBenchmark(input = args[[1]], output = paste(args[[2]], "_fdbm.mgf", sep = ""), method = "fdbm", aamassfile = "/srv/lucas1/deisotoper/bash/AminoAcidMasses.ini")
deisotoper:::jBenchmark(input = args[[1]], output = paste(args[[2]], "_mspy.mgf", sep = ""), method = "mspy")

rm(list=ls())
