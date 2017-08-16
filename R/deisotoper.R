#R

# author:
# date:

#' Title blas blas 
#'
#' @param mZ 
#' @param intensity 
#'
#' @return
#' @export summary_psm
#'
#' @examples
summary_psm <- function(mZ, intensity){
  
  
}

find_mZpattern <- function(mZ = c(5.5,6.5,7.7,8.8,9.9,10.29), pattern = c(6,8.3), eps = 1){
  library(rJava)
  .jinit()
  .jaddClassPath("inst/java/deisotoper.jar")
  .jclassPath()
  FindPatternLinear <- .jnew("FindPatternLinear")
  
   # Running Java-Programm with data
  .jcall("FindPatternLinear", "V", "setMz", mZ)
  .jcall("FindPatternLinear", "V", "setPattern", pattern)
  .jcall("FindPatternLinear", "V", "setEps", eps)
  index <- .jcall("FindPatternLinear", "[D", "getIndex")

  print(index)
}
