.onLoad <- function(libname, pkgname) {
  options(java.parameters = "-Xmx1g")
  rJava::.jpackage(pkgname, lib.loc = libname)
}
