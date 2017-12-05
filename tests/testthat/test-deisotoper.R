#R

context("Test deisotoper")

test_that("test deisotoper::deisotoper", {
  x0 <- list(
    mZ = c(101, 102, 102.5, 103),
    intensity = rep(1, 4),
    pepmass=600, charge=2)

  x1 <- list(
    mZ = c(101.01, 102, 102.5, 103),
    intensity = rep(1, 4),
    pepmass=600, charge=2)

  dtoper <- deisotoper()

  xd0 <- deisotope(dtoper, x0)
  xd1 <- deisotope(dtoper, x1)

  # number of ions can not disappear
  expect_true(sum(xd0$intensity)  == sum(x0$intensity))
  expect_true(sum(xd1$intensity)  == sum(x1$intensity))

  expect_true(xd0$mZ == c(101.0, 102.51) && rep(TRUE, 2))
  expect_true(xd1$mZ == c(101.01, 102.0) && rep(TRUE, 2))
	   
  expect_true(xd0$intensity == c(3, 1) && rep(TRUE, 2))
  expect_true(xd1$intensity == c(1, 3) && rep(TRUE, 2))
})
