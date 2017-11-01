#!/usr/bin/Rscript

args <- commandArgs(TRUE)

library(deisotoper)

deisotoper:::jBenchmark(input = args[[1]], output = paste(args[[2]], "_fdbm_first.mgf", sep = ""), modus="first")
if(file.exists("/srv/lucas1/deisotoper/properties/AminoAcidMassesWithCarbamidomethylDeamidationOxidation.properties")) {
  deisotoper:::jBenchmark(input = args[[1]], output = paste(args[[2]], "_fdbm_with_mod_first.mgf", sep = ""), modus="first", aamassfile = "/srv/lucas1/deisotoper/properties/AminoAcidMassesWithCarbamidomethylDeamidationOxidation.properties")
}

rm(list=ls())
