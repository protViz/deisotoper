#!/usr/bin/Rscript

args <- commandArgs(TRUE)
library(deisotoper)

deisotoper:::jBenchmark(input = args[[1]], output = paste(args[[2]], "_fdbm"), method = "fdbm")
deisotoper:::jBenchmark(input = args[[1]], output = paste(args[[2]], "_mspy"), method = "mspy")