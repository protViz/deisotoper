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

#' Title
#'
#' @param mZ 
#' @param pattern 
#' @param eps 
#'
#' @return
#' 
#' @import rJava
#' @export find_mZpattern
#'
#' @examples
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


#' find index of nearest neighbor 
#'
#' @param q 
#' @param vec 
#' @param check 
#'
#' @description 
#'
#' @seealso 
#' @return
#' @export findNN
#' 
#' @examples
#' (NNidx <- findNN(q<-c(1, 1.0001, 1.24, 1.26), DB<-seq(1,5,by=0.25)))
#' (NNidx == c(1,1,2,2))
#' 
#' # should be 0
#' unique(DB[findNN(DB,DB)] - DB)
#' 
findNN <- function (q, vec, check = FALSE) {
  message("TO BE IMPLEMENTED.")
  return (-1)
}