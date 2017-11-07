#R

context("Test jGetDot")

test_that("test deisotoper::jGetDot", {
  load(system.file("extdata", name='TP_HeLa_200ng_filtered_pd21.RData', package = "deisotoper"))
  MS <- jCreateMS(TP_HeLa_200ng_filtered_pd21[1])

  DOT <- jGetDot(MS, jCreateIMS(MS), index = 0)
  
  result <- "digraph {\nrankdir=LR;\nnode [shape=box];\nstart -> \"(0) [  (0) 110.07  (1) 111.08 ] z:1\"[color=\"black\",label=\"64.5\",weight=\"64.50000000000018\"];\n\"(0) [  (0) 110.07  (1) 111.08 ] z:1\" -> end[color=\"black\",label=\"0.0\",weight=\"0.0\"];\n}"

  lapply(DOT == result, expect_true)
})