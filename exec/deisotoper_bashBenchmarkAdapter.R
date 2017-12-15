#!/usr/bin/Rscript

args <- commandArgs(TRUE)

library(deisotoper)

.write_mgf_header <- function(filename){
  FILE <- file(filename, "a")
  
  writeLines(as.character(paste("# deisotoper packageVersion ", packageVersion('deisotoper'))), FILE)
  
  writeLines(paste("# ",date(),sep=''), FILE)
  
  close(FILE)
}

.append_mgf <- function(obj, filename){
  FILE <- file(filename, "a")
  
  writeLines("BEGIN IONS", FILE)
  writeLines(paste("TITLE=",as.character(obj$title),sep=''), FILE)
  writeLines(paste("PEPMASS=",as.character(obj$pepmass),sep=''), FILE)
  writeLines(paste("CHARGE=",as.character(obj$charge),'+',sep=''), FILE)
  writeLines(paste("SCANS=",as.character(obj$scans),sep=''), FILE)
  writeLines(paste("RTINSECONDS=",as.character(obj$rtinseconds),sep=''), FILE)
  writeLines(as.character(paste(obj$mZ, obj$intensity, sep=' ')), FILE)
  writeLines("END IONS", FILE)                              
  writeLines("", FILE)                              
  close(FILE)
}

mgf <- function(obj, filename = paste(tempfile(), 'mgf', sep='.')){
  message(paste("writing mgf to file", filename, "..."))
  .write_mgf_header(filename = filename)
  rv <- lapply(obj, function(x){.append_mgf(x, filename = filename)})
  file.info(filename)
}

dtoper <- deisotoper() 

psmset <- load(args[[1]])

psmsetdeisotoped <- deisotope.list(dtoper, get(psmset))

mgf(psmsetdeisotoped, paste(args[[2]], "_deisotoped.mgf", sep = ""))
  
rm(list=ls())
