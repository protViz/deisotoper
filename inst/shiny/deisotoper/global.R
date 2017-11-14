library(shiny)
library(deisotoper)
library(DiagrammeR)
pathofscript <- getSrcDirectory(function(x) {x})

unlink(paste(pathofscript, "/temp", sep = ""), recursive = TRUE, force = TRUE)

generateRandomString <- function() {
  a <- do.call(paste0, replicate(5, sample(LETTERS, 1, TRUE), FALSE))
  paste0(a, sprintf("%04d", sample(9999, 1, TRUE)), sample(LETTERS, 1, TRUE))
}

delta <- function(mz) {
  delta <- c()
  
  for(i in 2:length(mz)) {
    m <- mz[[i]] - mz[[i - 1]]
    delta <- c(delta, round(m, digits = 5))
  }
  
  return(delta)
}