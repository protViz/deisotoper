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
  if(decharge == "TRUE") {
    boolean <- TRUE
  } else if (decharge == "") {
    boolean <- FALSE
  } else {
    stop("Format error at decharge!")
  }
  
  boolean
}

formatF1 <- function(d) {
  d2 <- 0
  if(d != "") {
    d2 <- as.double(d)
  } else {
    d2 <- 0.8
  }
  
  if(is.na(d2)) {
    stop("Format error at F1!")
  }
  
  d2
}

formatF2 <- function(d) {
  d2 <- 0
  if(d != "") {
    d2 <- as.double(d)
  } else {
    d2 <- 0.5
  }
  
  if(is.na(d2)) {
    stop("Format error at F2!")
  }
  
  d2
}

formatF3 <- function(d) {
  d2 <- 0
  if(d != "") {
    d2 <- as.double(d)
  } else {
    d2 <- 0.1
  }
  
  if(is.na(d2)) {
    stop("Format error at F3!")
  }
  
  d2
}

formatF4 <- function(d) {
  d2 <- 0
  if(d != "") {
    d2 <- as.double(d)
  } else {
    d2 <- 0.1
  }
  
  if(is.na(d2)) {
    stop("Format error at F4!")
  }
  
  d2
}

formatF5 <- function(d) {
  d2 <- 0
  if(d != "") {
    d2 <- as.double(d)
  } else {
    d2 <- 0.1
  }
  
  if(is.na(d2)) {
    stop("Format error at F5!")
  }
  
  d2
}

formatDELTA <- function(d) {
  d2 <- 0
  if(d != "") {
    d2 <- as.double(d)
  } else {
    d2 <- 0.003
  }
  
  if(is.na(d2)) {
    stop("Format error at delta!")
  }
  
  d2
}

formatERROR <- function(d) {
  d2 <- 0
  if(d != "") {
    d2 <- as.double(d)
  } else {
    d2 <- 0.3
  }
  
  if(is.na(d2)) {
    stop("Format error at errortolerance!")
  }
  
  d2
}

formatDISTANCE <- function(d) {
  d2 <- 0
  if(d != "") {
    d2 <- as.double(d)
  } else {
    d2 <- 1.003
  }
  
  if(is.na(d2)) {
    stop("Format error at distance!")
  }
  
  d2
}

formatNOISE <- function(d) {
  d2 <- 0
  if(d != "") {
    d2 <- as.double(d)
  } else {
    d2 <- 0.0
  }
  
  if(is.na(d2)) {
    stop("Format error at noise!")
  }
  
  d2
}