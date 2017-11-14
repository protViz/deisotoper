.onLoad <- function(libname, pkgname) {
   options(java.parameters = "- Xmx10000000m") 
 	.jpackage(pkgname, lib.loc = libname)
}