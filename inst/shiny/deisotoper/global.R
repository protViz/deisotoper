library(shiny)
library(deisotoper)
library(DiagrammeR)

generateRandomString <- function() {
  a <- do.call(paste0, replicate(5, sample(LETTERS, 1, TRUE), FALSE))
  paste0(a, sprintf("%04d", sample(9999, 1, TRUE)), sample(LETTERS, 1, TRUE))
}