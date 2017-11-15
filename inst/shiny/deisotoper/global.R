library(shiny)
library(deisotoper)
library(DiagrammeR)

delta <- function(mz) {
  delta <- c()
  
  for(i in 2:length(mz)) {
    m <- mz[[i]] - mz[[i - 1]]
    delta <- c(delta, round(m, digits = 5))
  }
  
  return(delta)
}

formatAA <- function(aa) {
  a <- strsplit(aa, ",")
           
  aamasses <- a[[1]]
  
  aamasses2 <- c()
  for(i in 1:length(aamasses)) {
    aamasses2 <- c(aamasses2, as.double(aamasses[i]))
  }
  
  aamasses2
}

formatDECHARGE <- function(decharge) {
  boolean <- FALSE
  if(decharge == "TRUE"){
    boolean <- TRUE
  }
  
  boolean
}