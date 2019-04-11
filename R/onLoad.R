.onLoad <- function(libname, pkgname) {
   options(java.parameters = "-Xmx1000m") 
  rJava::.jpackage(pkgname, lib.loc = libname)
}