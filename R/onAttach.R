#R

#' When loading the library
#'
#' @param lib 
#' @param pkg 
#'
#' @importFrom utils packageVersion
.onAttach <- function(lib, pkg){
	if(interactive()){
		version <- packageVersion('deisotoper')
		packageStartupMessage("R Package: deisotoper, Version: ", version, "\n", .version())
	  invisible()
	}
}