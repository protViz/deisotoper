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