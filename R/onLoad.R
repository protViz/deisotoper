.onLoad <- function(libname, pkgname) {
   options(java.parameters = "- Xmx10000000m") 
  rJava::.jpackage(pkgname, lib.loc = libname)
}