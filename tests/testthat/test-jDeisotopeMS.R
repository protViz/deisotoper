#R

context("Test jDeisotopeMS")

test_that("test deisotoper::jDeisotopeMS", {
  load(system.file("extdata", name='TP_HeLa_200ng_filtered_pd21.RData', package = "deisotoper"))
  MS <- jCreateMS(TP_HeLa_200ng_filtered_pd21[1])
  MSd <- jDeisotopeMS(MS)
  summary <- jSummaryMS(MSd)
  
  resulta <- c("nr_of_peaks", "min_intensity", "max_intensity", "sum_intensity", "min_mass", "max_mass", "min_peak_distance", "max_peak_distance", 
               "precursor_charge", "precursor_mass", "rt_in_seconds", "average_charge", "average_isotope", "sum_isotope", "max_charge", "max_isotope",
               "nr_z1", "nr_z2", "nr_z3")
  resultv <- c(81, 2603.17, 410818.6, 5277024, 110.0717, 1081.548, 0.00018, 68.98193, 2, 592.8093, 1581, 1.071429, 1, 14, 2, 1, 13, 1, 0)
  resulti <- c(10000, 10000, 10000, 10000, 10000, 10000, 10000, 10000, 10000, 10000, 10000, 10000, 10000, 10000, 10000, 10000, 10000, 10000, 10000)
  
  lapply(summary$Attribute == resulta, expect_true)
  lapply(round(summary$Value) == round(resultv), expect_true)
  lapply(summary$SpectrumID == resulti, expect_true)
  lapply(length(summary$Value) == 19, expect_true)
})