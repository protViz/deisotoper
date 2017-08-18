#R

#' Title
#'
#' @param lib 
#' @param pkg 
#'
#' @return
#' @export
#' @importMethodsFrom packageVersion utils
#'
#' @examples
.onAttach <- function(lib, pkg){
	if(interactive()){
		version <- packageVersion('deisotoper')
		packageStartupMessage("Package 'deisotoper' version ", version)
	  invisible()
	}
}
