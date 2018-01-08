#R

#' When loading the library
#'
#' @param lib 
#' @param pkg 
#'
#' @importFrom utils packageVersion
.onAttach <- function(lib, pkg){
  
  
  .jinit()
  jv <- .jcall("java/lang/System", "S", "getProperty", "java.runtime.version")
  if(substr(jv, 1L, 1L) == "1") {
    jvn <- as.numeric(paste0(strsplit(jv, "[.]")[[1L]][1:2], collapse = "."))
    if(jvn < 1.8) stop("Java 8 is needed for this package but not available")
  } 
  
	if(interactive()){
		version <- packageVersion('deisotoper')
		packageStartupMessage("R Package: deisotoper, Version: ", version, "\n", .version())
	  invisible()
	}
}