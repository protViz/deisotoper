#R

context("Test jGetStatisticMSM")

test_that("test deisotoper::jGetStatisticMSM", {
  load(system.file("extdata", name='TP_HeLa_200ng_filtered_pd21.RData', package = "deisotoper"))
  MSM <- jCreateMSM(TP_HeLa_200ng_filtered_pd21)
  
  stats <- jGetStatisticMSM(MSM)
  
  lapply(stats$NumberOfMassSpectra == 47, expect_true)
  lapply(stats$NumberOfIsotopicSets == 537, expect_true)
  lapply(stats$NumberOfIsotopicClusters == 624, expect_true)
  lapply(stats$NumberOfPeaksInIsotopicClusters == 1114, expect_true)
  lapply(stats$NumberOfPeaks == 4070, expect_true)
})