#!/usr/bin/Rscript

args <- commandArgs(TRUE);
library(deisotoper);

print(args[[1]]);

a1 <- args[[1]]
a2 <- args[[2]]

deisotoper:::jBenchmark(input = a1, output = a2);
