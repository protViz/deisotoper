#R

#' Title
#'
#' @param lib 
#' @param pkg 
#'
#' @importFrom utils packageVersion
#' @return
#' @export
#' 
#'
#' @examples
.onAttach <- function(lib, pkg){
	if(interactive()){
		version <- packageVersion('deisotoper')
		packageStartupMessage("Package 'deisotoper' version ", version)
	  invisible()
	}
}
