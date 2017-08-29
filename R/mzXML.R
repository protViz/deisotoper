#R




#' convert mzXML into psmSet object
#'
#' @param x a mzXML object
#'
#' @return a psmSet object
#' 
#' @import mzR
#' @import msdata
#' @export as.psmSet.mzXML
#'
#'
#' @examples
#' 
#' \dontrun{
#'  library(msdata); library(mzR)
#'   mzXML <- openMSfile("~/data/20161010_04_TP_HeLa_200ng.mzXML")
#' }

as.psmSet.mzXML <- function(obj){
  idx<-which(sapply(1:length(obj), function(x){header(obj, x)$msLevel}) == 2)
  
  rv <- lapply(idx, function(id){
    
    h <- header(obj, id)
    p <- peaks(obj, id)
    
    rv <- list(mZ = p[,1],
         intensity = p[,2],
         pepmass = as.double(h$precursorMZ),
         charge=as.integer(h$msLevel),   
         title = "",
         rtinseconds = as.double(h$retentionTime),
         scans = id,
         proteinInformation = "",
         peptideSequence = "",
         title = "")
    
    class(rv) <- c("psm", class(rv))
    rv
  })
  
  class(rv) <- c("psmSet", class(rv))
  rv
}
