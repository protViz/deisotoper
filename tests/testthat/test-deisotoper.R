#R

context("Test deisotoper")

test_that("test deisotoper::deisotoper", {
  x0 <- list(
    mZ = c(1, 2, 2.5, 3),
    intensity = rep(1, 4),
    pepmass=600, charge=2)

  x1 <- list(
    mZ = c(1.01, 2, 2.5, 3),
    intensity = rep(1, 4),
    pepmass=600, charge=2)

  dtoper <- deisotoper()

  xd0 <- deisotope(dtoper, x0)
  xd1 <- deisotope(dtoper, x1)

  # number of ions can not disappear
  expect_true(sum(xd0$intensity)  == sum(x1$intensity))
  expect_true(sum(xd1$intensity)  == sum(x1$intensity))
})
