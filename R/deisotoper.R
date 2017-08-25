#R

# author:
# date:

#' Title blas blas 
#'
#' @param jobj
#'
#' @return a \code{\link{data.frame}}
#' @export jSummaryMSM
#'
#' @examples
jSummaryMSM <- function(jobj){
  .jinit()
  .jaddClassPath("inst/java/deisotoper.jar")
  .jclassPath()
  
  sum <- .jnew("Summary")
  
  summary <- .jcall(sum, "S", "makeSummary", jobj)
  
  summary
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
#' \dontrun{
#' (NNidx <- findNN(q<-c(1, 1.0001, 1.24, 1.26), DB<-seq(1,5,by=0.25)))
#' (NNidx == c(1,1,2,2))
#' 
#' # should be 0
#' unique(DB[findNN(DB,DB)] - DB)
#' }
#' 
findNN <- function (q, vec, check = FALSE) {
  .jinit()
  .jaddClassPath("inst/java/deisotoper.jar")
  .jclassPath()

  jFindNN <- .jnew("FindNN")
  
  .jcall(jFindNN, "V", "setVec", as.double(vec))
  .jcall(jFindNN, "V", "setQ", as.double(q))
  
  idx <- (.jcall(jFindNN, "[D", "getIndex") + 1)
}

#' Serialize a set of data
#'
#' @param urlrdata
#'
#' @return returns a jcall object reference
#' 
#' @import rJava
#' @export jCreateMSM
#' @examples 
#' \dontrun{
#' urlrdata = "http://fgcz-ms.uzh.ch/~cpanse/data/386248.RData"
#' con <- url(urlrdata)
#' load(con)
#' S <- as.psmSet(F225712)
#' jo <- jcreateMSM(S)
#' 
jCreateMSM <- function (obj) {
  .jinit()
  .jaddClassPath("inst/java/deisotoper.jar")
  .jclassPath()

  MSM <- .jnew("MassSpectrometryMeasurement")
  
  lapply(obj, function(x){
    .jcall(MSM, "Ljava/util/List;", "addMSM", "MS2",
          x$searchEngine,
          x$mZ, x$intensity,
          x$pepmass,
          x$rtinseconds,
          as.integer(x$charge),
          as.integer(x$id),
          x$scans, x$title))
    })
  rv <- .jcall(MSM, "Ljava/util/List;", "getMSMlist")
  # class(rv) <- "jMSM"
}

jGetMSM <- function(jobj) {
  
}

#'
#'
#'
#' 
#'
jWriteMSM2JSON <- function(jobj, filename='test.json'){
  .jinit()
  .jaddClassPath("inst/java/deisotoper.jar")
  .jaddClassPath("inst/java/gson-2.8.1.jar")
  .jclassPath()
  GsonMSM <- .jnew("GsonMSM")
  
  json <- .jcall(GsonMSM, "S", "serializeMSMToJson", filename, jobj)
  
  json
}

#'
#'
#'
#' 
#'
jReadJSON2MSM <- function(filename='test.json'){
  .jinit()
  .jaddClassPath("inst/java/deisotoper.jar")
  .jaddClassPath("inst/java/gson-2.8.1.jar")
  .jclassPath()
  GsonMSM <- .jnew("GsonMSM")
  
  MSM <- .jcall(GsonMSM, "Ljava/util/List;", "deserializeJsonToMSM", filename)
  
  MSM
}