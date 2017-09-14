#R

# $HeadURL: http://fgcz-svn.unizh.ch/repos/fgcz/testing/proteomics/R/protViz/R/PTM_MarkerFinder.R $
# $Id: PTM_MarkerFinder.R 6318 2014-04-02 13:02:51Z cpanse $
# $Date: 2014-04-02 15:02:51 +0200 (Wed, 02 Apr 2014) $


.write_mgf_header <- function(filename){
    FILE <- file(filename, "a")
    
    writeLines(as.character(paste("# deisotoper packageVersion ", packageVersion('deisotoper'))), FILE)
    
    writeLines(paste("# ",date(),sep=''), FILE)
    
    close(FILE)
}

.append_mgf <- function(obj, filename){
    FILE <- file(filename, "a")

    writeLines("BEGIN IONS", FILE)
    writeLines(paste("TITLE=",as.character(obj$title),sep=''), FILE)
    writeLines(paste("PEPMASS=",as.character(obj$pepmass),sep=''), FILE)
    writeLines(paste("CHARGE=",as.character(obj$charge),'+',sep=''), FILE)
    writeLines(paste("SCANS=",as.character(obj$scans),sep=''), FILE)
    writeLines(paste("RTINSECONDS=",as.character(obj$rtinseconds),sep=''), FILE)
    writeLines(as.character(paste(obj$mZ, obj$intensity, sep=' ')), FILE)
    writeLines("END IONS", FILE)                              
    writeLines("", FILE)                              
    close(FILE)
}

#' write Mascot Generic File (MGF)
#'
#' @param obj 
#' @param filename 
#'
#' @return \code{file.info(filename)}
#' @export mgf
#'
#' @examples
mgf <- function(obj, filename = paste(tempfile(), 'mgf', sep='.')){
  stopifnot(is.MSM(obj))
  message(paste("writing mgf to file", filename, "..."))
  .write_mgf_header(filename = filename)
  rv <- lapply(obj, function(x){.append_mgf(x, filename = filename)})
  file.info(filename)
}