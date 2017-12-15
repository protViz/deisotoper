#' Construct a Deisotoper Object
#' 
#' @param amino_acid_masses List of amino acid masses used for scoring. 
#' @param F1 F1 multiplier used for scoring.
#' @param F2 F2 multiplier used for scoring.
#' @param F3 F3 multiplier used for scoring.
#' @param F4 F4 multiplier used for scoring.
#' @param F5 F5 multiplier used for scoring.
#' @param delta Delta value used for clustering.
#' @param errortolerance Errortolerance used for scoring.
#' @param distance Distance between two peaks used by clustering.
#' @param noise Noise value for noise filtering (in percent).
#' @param decharge De- and activates decharging.
#' @param modus Modus of aggregation ('first' or 'highest').
#' @param comment default is empty word.
#' @references Features-Based Deisotoping Method for Tandem Mass Spectra \url{http://dx.doi.org/10.1155/2011/210805}.
#' @return deisotoper as list of JavaRef 
#' @import rJava
#' @export deisotoper
#' @aliases deisotope.list plot.deisotoper print.deisotoper dot.deisotoper summary.deisotoper config.deisotoper 
#' @author Lucas Schmidt, Christian Panse
#' @description 
#' \code{deisotoper} returns a deisotoper object.
#' 
#' \code{deisotope} detects and aggregates peaks which belong to the 
#' same isotopic cluster of a given mass spectrum.
#' @details
#' Input: a peak peaked mass spectrum.
#' 
#' The algorithm: The deisotoper algorithm detects and aggregates peaks
#' which belong to the same isotopic cluster of a given mass spectrum.
#' 
#' Output: 
#' 
#' @seealso \code{\link{deisotope}}
#' @examples
#' # EXAMPLE 1
#' # standart configurated deisotoper
#' dtoper <- deisotoper()
#' 
#' # return the configuration of dtoper
#' config <- config.deisotoper(dtoper)
#' 
#' # example data
#' x <- list(mZ = c(110.07172, 111.07504, 129.10249, 130.08649, 147.11302,
#'    149.04506, 167.05571, 175.11923, 181.06099, 199.07158, 216.09814, 
#'    223.15556, 239.09503, 251.15036, 261.15579, 262.13, 280.14053,
#'    281.14398, 285.00974, 299.06165, 303.02039, 328.11386, 332.20789,
#'    344.97641, 345.14056, 350.21866, 355.06995, 360.22412, 368.17529,
#'    373.08078, 415.03656, 418.99521, 430.2774, 431.28107, 464.26218,
#'    473.3085, 476.18176, 479.20718, 481.25989, 497.21811, 521.27063,
#'    521.77087, 540.7804, 559.31946, 560.32391, 580.32739, 582.30688,
#'    592.28766, 592.35815, 593.34113, 608.25214, 610.30243, 610.36755,
#'    611.30554, 611.37042, 630.35724, 631.36115, 642.572, 643.054,
#'    643.569, 644.062, 644.557, 681.37762, 684.31494, 691.36011,
#'    709.37109, 709.4353, 710.44037, 712.3092, 721.33459, 754.33899,
#'    774.36261, 790.38892, 791.39124, 792.39221, 813.4679, 820.40479,
#'    823.41522, 824.40546, 825.39423, 826.39734, 840.47681, 841.43036,
#'    841.47949, 896.4137, 903.47253, 904.47565, 905.47632, 906.47607,
#'    924.46271, 951.51038, 969.52002, 970.52283, 1038.50195, 1041.53308,
#'    1042.53845, 1080.55493, 1081.54773),
#'           intensity = c(378322, 32496.6, 85689.6, 46440.3, 49645.2, 
#'           25102.5, 32516.2, 83497.2, 74653.1, 37228, 196053, 83826.4,
#'           112718, 114812, 88089.5, 61521.3, 220054, 58888.5, 280334,
#'           122311, 14953.2, 26959.6, 24854, 27122.9, 86216.1, 63360.3,
#'           358968, 47393.5, 37893.2, 16532.9, 17259, 37250.4, 33679.8,
#'           21243.6, 17854.9, 51232.4, 12738.8, 19515.4, 31560.1, 48772.3,
#'           66481.2, 23353.6, 11994, 15211, 9883.29, 14753.7, 17304.7,
#'           51575.9, 10917.6, 40518.3, 15107.3, 62106.4, 72496.1, 9430.4,
#'           10289.3, 34831.3, 41981.1, 17000, 25000, 12000, 9000, 4000,
#'           9579.9, 10392.3, 13507.4, 38200.9, 29990.5, 9304.39, 19849, 
#'           10678.6, 8452.85, 14519.3, 111717, 185030, 56020.8, 3387.69,
#'           9478.08, 7878.29, 3167.8, 20670.7, 2774.25, 31114.4, 3385.92,
#'           4656.8, 3687.15, 65332.5, 207097, 68080.9, 11934.3, 3630.86,
#'           9201.02, 47579.2, 19125.8, 3439.48, 15082.1, 8280.57, 4170.47,
#'           2603.17),
#'           title = "TP filtered inserted example 2 of protViz::deisotoper.",
#'           rtinseconds = 1581,
#'           charge = 2,
#'           scan = 1,
#'           id = 1,
#'           pepmass = 592.8093)
#'
#' # deisotope the data
#' xd <- deisotope(dtoper, x)
#' summary.deisotoper(dtoper)
#' 
#' # plot the example data and the deisotoped data
#' op <- par(mfrow=c(2,2))
#' plot.deisotoper(x, xd)
#' plot.deisotoper(x, xd, xlim=c(275,285))
#' plot.deisotoper(x, xd, xlim=c(790,795))
#' plot.deisotoper(x, xd, xlim=c(901,910))
#' par(op)
#' 
#' # return the annotated spectrum of the above deisotoped data
#' print.deisotoper(dtoper)
#'
#' 
#' 
#' # EXAMPLE 2
#' # standart configurated deisotoper with changed delta and decharging
#' dtoper2 <- deisotoper(delta = 0.005, decharge = TRUE)
#' 
#' # return the configuration of dtoper2
#' config2 <- config.deisotoper(dtoper2)
#' 
#' \dontrun{
#' # return the GraphViz dot graphs of the above deisotoped data
#' xdot <- dot.deisotoper(dtoper)
#' 
#' # draws the isotopic cluster graphs in the browser (html)
#'   if(require(DiagrammeR)){
#'     lapply(xdot, DiagrammeR::grViz)
#'   }
#' }
#' 
deisotoper <- function(amino_acid_masses = list('A' = 71.03711, 'R' = 156.10111, 'N' = 114.04293, 'D' = 115.02694, 'C' = 103.00919, 
                                                'E' = 129.04259, 'Q' = 128.05858, 'G' = 57.02146, 'H' = 137.05891, 'I' = 113.08406, 
                                                'L' = 113.08406, 'K' = 128.09496, 'M' = 131.04049, 'F' = 147.06841, 'P' = 97.05276, 
                                                'S' = 87.03203, 'T' = 101.04768, 'W' = 186.07931, 'Y' = 163.06333, 'V' = 99.06841), 
                       F1 = 0.8, F2 = 0.5, F3 = 0.1, F4 = 0.1, F5 = 0.1, 
                       delta = 0.003, errortolerance = 0.3, distance = 1.00048, noise = 0.0, 
                       decharge = FALSE, modus = "first", comment = "") {
  dtoper <- .jnew("ch.fgcz.proteomics.r.FeaturesBasedDeisotoping")
  
  if(0.5 > distance || distance > 1.5) {
    stop("distance can not be lower than 0.5 and grater than 1.5!")
  }
  
  dcheck <- distance/3 - distance/6
  if(delta > dcheck) {
    stop("delta can not be greater than distance/3 - distance/6!")
  }
  
  if(0 > errortolerance || errortolerance > 1) {
    stop("errortolerance can not be lower than 0 and grater than 1!")
  }
  
  if(0 > noise || noise > 100) {
    stop("noise can not be lower than 0 and grater than 100! (0 = deactivated)")
  }
  
  if(0 > F1) {
    stop("F1 can not be lower than 0!")
  }
  
  if(0 > F2) {
    stop("F2 can not be lower than 0!")
  }
  
  if(0 > F3) {
    stop("F3 can not be lower than 0!")
  }
  
  if(0 > F4) {
    stop("F4 can not be lower than 0!")
  }
  
  if(0 > F5) {
    stop("F5 can not be lower than 0!")
  }
  
  if(length(amino_acid_masses) <= 1) {
    stop("amino_acid_masses must contain 2 or more amino acid masses!")
  }
  
  if(!(modus == "first" || modus == "highest")) {
    stop("Modus is incorrect. Available modi: 'first' or 'highest'")
  }
  
  tt <- .jcall(dtoper, "V", "setConfiguration",
               names(amino_acid_masses),
               as.vector(unlist(amino_acid_masses)), 
               F1, F2, F3, F4, F5, 
               delta, errortolerance, distance, noise, 
               decharge, modus)
  
  rv <- list(javaRef = dtoper, comment = comment)
  class(rv) <- c('deisotoper', class(rv))
  return(rv)
}

#' Deisotope a Mass Spectrum
#' @param deisotoper a \code{\link{deisotoper}} object.
#' @param massspectrum a list of numeric vectors \code{mZ} and \code{intensity}
#' where \code{mZ} is ordered and \code{mZ} and \code{intensity} have the same length.
#' @param algorithm the supported deisotope algorithms, default is \code{method="features-based"}.
#' @seealso \code{\link{deisotoper}}
#' @export deisotope
#' @references 
#' \itemize{
#' \item{Features-Based Deisotoping Method for Tandem Mass Spectra,
#'  \url{http://dx.doi.org/10.1155/2011/210805}.
#' }}
#' @author Lucas Schmidt, Christian Panse, Witold E. Wolski
#' @examples
#' 
#' x <- list(mZ = c(1, 2, 2.5, 3), intensity = rep(1, 4), pepmass=600, charge=2)
#' 
#' xd <- deisotope(dtoper <- deisotoper(), x)
#' plot.deisotoper(x, xd)
#' summary(dtoper)
#' 
#' @aliases 
deisotope <- function(deisotoper, massspectrum, algorithm = "features-based") {
  
  stopifnot(algorithm   == "features-based")
  
  if(!("mZ" %in% names(massspectrum))) {
    stop("mZ values are missing!")
  }
  
  if(!("intensity" %in% names(massspectrum))) {
    stop("Intensity values are missing!")
  }
  
  if(length(massspectrum$mZ) <= 1) {
    stop("mZ must contain 2 or more values!")
  }
  
  if(length(massspectrum$intensity) <= 1) {
    stop("Intensity must contain 2 or more values!")
  }
  
  if(!("pepmass" %in% names(massspectrum))) {
    stop("Peptid mass is missing!")
  }
  
  if(!("charge" %in% names(massspectrum))) {
    stop("Charge is missing!")
  }
  
  if(is.unsorted(massspectrum$mZ)){
    idx <- order(massspectrum$mZ)
    massspectrum$mZ <- massspectrum$mZ[idx]
    massspectrum$intensity <- massspectrum$intensity[idx]
  }
  
  .jcall(deisotoper$javaRef, "V", "setMz", massspectrum$mZ)
  .jcall(deisotoper$javaRef, "V", "setIntensity", massspectrum$intensity)
  .jcall(deisotoper$javaRef, "V", "setPepMass", massspectrum$pepmass)
  .jcall(deisotoper$javaRef, "V", "setCharge", as.integer(massspectrum$charge))
  .jcall(deisotoper$javaRef, "V", "deisotope")
  
  mzout <- .jcall(deisotoper$javaRef, "[D", "getMz")
  intensityout <- .jcall(deisotoper$javaRef, "[D", "getIntensity")
  
  # TOOD(cp): consider to return a psm object?
  list(title = massspectrum$title,
               rtinseconds = massspectrum$rtinseconds,
               charge = massspectrum$charge,
               scan = massspectrum$scan,
               pepmass = massspectrum$pepmass,
               mZ = mzout, 
               intensity = intensityout,
               id = massspectrum$id)
}

#' @export
#' @author Lucas Schmidt
deisotope.list <- function(deisotoper, psmset) {
  res <- list()
  for(i in 1:length(psmset)) {
    res <- c(res, list(deisotope(deisotoper = deisotoper, massspectrum = psmset[[i]])))
  }
  return(res)
}



#' @export print.deisotoper
#' @S3method print deisotoper
#' @importFrom utils read.csv
#' @author Lucas Schmidt
print.deisotoper <- function(x, ...) {
  AS <- .jcall(x$javaRef, "S", "getAnnotatedSpectrum")
  
  con <- textConnection(AS)
  read.csv(con, sep=',', header = TRUE)
}

#' @export summary.deisotoper
#' @S3method summary deisotoper
#' @author Lucas Schmidt
summary.deisotoper <- function(object, ...) {
  Summary <- .jcall(object$javaRef, "S", "getSummary")
  
  con <- textConnection(Summary)
  read.csv(con, sep=',', header = TRUE)
}

#' @export plot.deisotoper
#' @S3method plot deisotoper
#' @import graphics
#' @author Lucas Schmidt
plot.deisotoper <- function(x, y, ...) {
  plot(c(x$mZ, y$mZ), 
       c(x$intensity, -y$intensity),
       type = "n", 
       xlab = "m/Z",
       ylab = "Intensity",
       axes = FALSE, 
       ...)
  abline(h=0)
  lines(x$mZ, 
        x$intensity, 
        type = "h", 
        col = "red")
  
  lines(y$mZ, 
        -y$intensity, 
        type = "h", 
        col = "blue")

  axis(side = 3, x$mZ[-1] - (0.5 * diff(x$mZ)), round(diff(x$mZ), 2) )
  
  mtext(text = deparse(substitute(y)), line = 3, adj = 0, col = "blue")
  mtext(text = deparse(substitute(x)), line = 2, adj = 0, col = "red")
  
  axis(side = 2)
  axis(side = 1, at = c(x$mZ, y$mZ))
}

#' @export
#' @author Lucas Schmidt
.version <- function() {
  .jinit(parameters = "-XX:-UseGCOverheadLimit")
  .jaddClassPath("inst/java/deisotoper.jar")
  .jclassPath()
  
  version <- .jnew("ch.fgcz.proteomics.Version")
  
  version <- .jcall(version, "S", "version")
  
  version
}

#' find index of nearest neighbor.
#'
#' @param q a double vector which can be considered as query objects.
#' @param vec a sorted double vector which can be considered as a data base.
#'
#' @description Given a vector of sorted double values \code{vec} of size
#' \code{n} and a vector of \code{m} query objects \code{q}.
#'
#' \code{findNN} determines for each element \code{q[i]} in \code{q}
#' the nearest neighbor index \code{o} so that the following remains true:
#'  
#'  there is no element \code{k} with \code{1} \eqn{\le} \code{k}
#' \eqn{\le} \code{n} and \code{k} is not \code{o} so that
#'
#' \code{abs(vec[k] - q[i])} < \code{abs(vec[o] - q[i])}.
#' 
#'  The internal algorithm of \code{findNN} is implemented as binary search.
#' \code{findNN} has \eqn{O(m * log(n))} time complexity.
#'
#' @author Lucas Schmidt, Christian Panse
#' @seealso \code{protViz::findNN}'s cpluplus implementation.
#' @return an integer vector 
#' @export findNN
#' 
#' @examples
#' (NNidx <- findNN(q<-c(1, 1.0001, 1.24, 1.26), DB<-seq(1,5,by=0.25)))
#' (NNidx == c(1,1,2,2))
#' 
#' # should be 0
#' unique(DB[findNN(DB,DB)] - DB)
findNN <- function (q, vec) {
  .jinit()
  .jaddClassPath("inst/java/deisotoper.jar")
  .jclassPath()
  
  jFindNN <- .jnew("ch.fgcz.proteomics.r.Utilities")
  
  idx <- .jcall(jFindNN, "[D", "findNNR", as.double(q), as.double(vec))
  
  idx
}


#' @export
#' @author Lucas Schmidt
config.deisotoper <- function(deisotoper) {
  config <- .jcall(deisotoper$javaRef, "S", "getConfiguration")
  
  con <- textConnection(config)
  read.csv(con, sep=',', header = TRUE)
}


#' @export
#' @author Lucas Schmidt
dot.deisotoper <- function(deisotoper) {
  DOT <- .jcall(deisotoper$javaRef, "[S", "getDot")
  
  DOT
}


#' @author Lucas Schmidt
.plotDOT <- function(dot) {
  DiagrammeR::grViz(dot)
}
