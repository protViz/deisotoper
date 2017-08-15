#R

.onAttach <- function(lib, pkg){
	if(interactive()){
		version <- packageVersion('deisotoper')
		packageStartupMessage("Package 'deisotoper' version ", version)
	  invisible()
	}
}
