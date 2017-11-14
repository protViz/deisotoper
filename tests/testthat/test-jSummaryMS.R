#R

context("Test jSummaryMS")

test_that("test deisotoper::jSummaryMS", {
  load(system.file("extdata", name='TP_HeLa_200ng_filtered_pd21.RData', package = "deisotoper"))
  MS <- jCreateMS(TP_HeLa_200ng_filtered_pd21[1])
  summary <- jSummaryMS(MS)
  
  resulta <- c("nr_of_peaks", "min_intensity", "max_intensity", "sum_intensity", "min_mass", "max_mass", 
               "min_peak_distance", "max_peak_distance", "precursor_charge", "precursor_mass", "rt_in_seconds")
  resultv <- c(93, 2603.17, 378322, 4760795, 110.0717, 1081.548, 0.04913, 67.97912, 2, 592.8093, 1581)
  resulti <- c(10000, 10000, 10000, 10000, 10000, 10000, 10000, 10000, 10000, 10000, 10000)
  
  lapply(summary$Attribute == resulta, expect_true)
  lapply(round(summary$Value) == round(resultv), expect_true)
  lapply(summary$SpectrumID == resulti, expect_true)
  lapply(length(summary)== 3, expect_true)
})