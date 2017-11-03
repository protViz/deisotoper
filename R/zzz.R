#R

#' Title
#'
#' @param lib 
#' @param pkg 
#'
#' @importFrom utils packageVersion
.onAttach <- function(lib, pkg){
	if(interactive()){
		version <- packageVersion('deisotoper')
		packageStartupMessage("Package 'deisotoper' version ",
		                      version, " (Java ", jVersionMSM(), ")")
	  invisible()
	}
}
