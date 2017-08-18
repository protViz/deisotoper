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
  .jinit()
  .jaddClassPath("inst/java/deisotoper.jar")
  .jclassPath()
  FindPatternLinear <- .jnew("FindPatternLinear")
  
   # Running Java-Programm with data
  .jcall("FindPatternLinear", "V", "setMz", as.double(mZ))
  
  .jcall("FindPatternLinear", "V", "setPattern", as.double(pattern))
  
  .jcall("FindPatternLinear", "V", "setEps", eps)
  
  idx <- .jcall("FindPatternLinear", "[D", "getIndex")

  idx
}


#' find index of nearest neighbor 
#'
#' @param q a double vector which can be considered as query objects.
#' @param vec a sorted double vector which can be considered as a data base.
#' @param check boolean enables test if \code{vec} is sorted. default is \code{FALSE}
#'
#' @description Given a vector of sorted double values \code{vec} of size
#' \code{n} and a vector of \code{m} query objects \code{q}.
#'
#' \code{findNN} determines for each element \code{q[i]} in \code{q}
#' the nearest neighbor index \code{o} so that the following remains true:
#'  
#'  there is no element \code{k} with \code{1} \eqn{\le} \code{k}
#' \eqn{\le} \code{n} and \code{k} is not \code{o} so that
#'
#' \code{abs(vec[k] - q[i])} < \code{abs(vec[o] - q[i])}.
#' 
#'  The internal algorithm of \code{findNN} is implemented as binary search.
#' \code{findNN} has \eqn{O(m * log(n))} time complexity.
#'
#'
#' @seealso \code{link{protViz::findNN}}
#' @return a integer vector 
#' @export findNN
#' 
#' @examples
#' 
#' \dont{
#' (NNidx <- findNN(q<-c(1, 1.0001, 1.24, 1.26), DB<-seq(1,5,by=0.25)))
#' (NNidx == c(1,1,2,2))
#' 
#' # should be 0
#' unique(DB[findNN(DB,DB)] - DB)
#' }
#' 
findNN <- function (q, vec, check = FALSE) {
  message("TO BE IMPLEMENTED.")
  return (-1)
}