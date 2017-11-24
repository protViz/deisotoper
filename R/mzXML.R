#R

#' convert mzXML into psmSet object
#'
#' @param obj a mzXML object
#'
#' @return a psmSet object
#'
#' @examples
#' \dontrun{
#'  mzXMLfilename <- system.file("extdata",
#'       name='20161010_04_TP_HeLa_200ng.mzXML', package = "deisotoper")
#'       
#'  library(mzR)
#'  jo <- jCreateMSM(as.psmSet.mzXML(openMSfile(mzXMLfilename)))
#'  
#'  library(lattice)
#'   xyplot(Value ~ SpectrumID | Attribute, 
#'      data = jSummaryMSM(jo), 
#'      scales = list(relation = "sliced"), 
#'      pch=22)
#' } 
as.psmSet.mzXML <- function(obj){
  idx <- which(sapply(1:length(obj), function(x){mzR::header(obj, x)$msLevel}) == 2)
  
  rv <- lapply(idx, function(id){
    
    h <- mzR::header(obj, id)
    p <- mzR::peaks(obj, id)
    
    rv <- list(mZ = p[,1],
         intensity = p[,2],
         pepmass = as.double(h$precursorMZ),
         charge=as.integer(h$msLevel),   
         title = "xx",
         rtinseconds = as.double(h$retentionTime),
         scans = id,
         id = id,
         proteinInformation = "xx",
         peptideSequence = "xx",
         title = "xx",
         searchEngine = "xx")
    
    class(rv) <- c("psm", class(rv))
    rv
    
  })
  
  class(rv) <- c("psmSet", class(rv))
  rv
}