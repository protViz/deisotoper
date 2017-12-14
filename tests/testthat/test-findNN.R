#R

context("Test findNN")

test_that("test deisotoper::findNN", {
  # TEST 1
  r <- abs(deisotoper::findNN(c(3.5, 4), as.vector(1:5)) - deisotoper::findNN(c(3.5, 4), as.vector(1:6)))
  expect_true(sum(r) == 0)
  
  # TEST 2
  DB <- sort(rnorm(100, mean=100, sd=10))
  t2 <- deisotoper::findNN(DB, DB) == 1:length(DB)
  for (test2 in t2) {
    expect_true(test2)    
  }
  
  # TEST 3 -- testing lower and upper index
  DB <- seq(-1, 1, length = 101)
  query <- c(-1000, 0, 0.001, 10, 10000)
  result <- c(1, 51, 51, 101, 101)
  t3 <- deisotoper::findNN(query, DB) == result
  for (test3 in t3) {
    expect_true(test3)    
  }
})