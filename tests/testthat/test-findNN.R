#R

context("Test findNN")

test_that("test deisotoper::findNN", {
  
   # TEST 1
   # the drawback of the findNN implementation. Use findNN_!
   expect_true(abs(deisotoper::findNN(3.5, 1:5) - deisotoper::findNN(3.5, 1:6)) == 0)


   # TEST 2
   DB <- sort(rnorm(100, mean=100, sd=10))
   expect_true(sum(deisotoper::findNN_(DB,DB) == 1:length(DB)) == length(DB))

   # TEST 3 -- testing lower and upper index
   DB <- seq(-1, 1, length = 101)

   query <- c(-1000, 0, 0.001, 10, 10000)

   result <- c(1, 51, 51, 101, 101)

   lapply(1:length(query), function(idx){
	   expect_true (deisotoper::findNN(q = query[idx], vec = DB) == result[idx])
   })

})
