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

find_mZpattern <- function(mZ = 1:100, pattern = 1:10, eps = 0.01){
        .jinit()
  #      .jaddClassPath("/srv/lucas1/eclipse-workspace/deisotoper/bin")
  #      .jclassPath()
  #      findpattern <- .jnew("findpattern")
  
   # Running Java-Programm with data
  .jcall("findpattern", "V", "setmZ", mZ)
  .jcall("findpattern", "V", "setpattern", pattern)
  .jcall("findpattern", "V", "seteps", eps)
  index <- .jcall("findpattern", "[D", "getIndex")

  index
}
