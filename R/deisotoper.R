#' Creates a deisotoper.
#'
#' @param amino_acid_masses List of amino acid masses used for scoring
#' @param F1 F1 multiplier used for scoring
#' @param F2 F2 multiplier used for scoring
#' @param F3 F3 multiplier used for scoring
#' @param F4 F4 multiplier used for scoring
#' @param F5 F5 multiplier used for scoring
#' @param delta Delta value used for clustering
#' @param errortolerance Errortolerance used for scoring
#' @param distance Distance between two peaks used by clustering
#' @param noise Noise value for noise filtering (in percent)
#' @param decharge De- and activates decharging
#' @param modus Modus of aggregation ('first' or 'highest')
#'
#' @return deisotoper
#' @export deisotoper
#' @aliases deisotope deisotope.list getAnnotatedSpectrum getDOTGraphs getSummary .plot .plotDOT getConfig
#' @author Lucas Schmidt
#'
#' @examples
#' # EXAMPLE 1
#' # standart configurated deisotoper
#' dtoper <- deisotoper()
#' 
#' # return the configuration of dtoper
#' config <- getConfig(dtoper)
#' 
#' # example data
#' x <- list(mZ = c(110.07172, 111.07504, 129.10249, 130.08649, 147.11302, 149.04506, 167.05571, 175.11923, 181.06099, 199.07158, 216.09814, 223.15556, 239.09503, 251.15036, 261.15579, 262.13, 280.14053, 281.14398, 285.00974, 299.06165, 303.02039, 328.11386, 332.20789, 344.97641, 345.14056, 350.21866, 355.06995, 360.22412, 368.17529, 373.08078, 415.03656, 418.99521, 430.2774, 431.28107, 464.26218, 473.3085, 476.18176, 479.20718, 481.25989, 497.21811, 521.27063, 521.77087, 540.7804, 559.31946, 560.32391, 580.32739, 582.30688, 592.28766, 592.35815, 593.34113, 608.25214, 610.30243, 610.36755, 611.30554, 611.37042, 630.35724, 631.36115, 642.572, 643.054, 643.569, 644.062, 644.557, 681.37762, 684.31494, 691.36011, 709.37109, 709.4353, 710.44037, 712.3092, 721.33459, 754.33899, 774.36261, 790.38892, 791.39124, 792.39221, 813.4679, 820.40479, 823.41522, 824.40546, 825.39423, 826.39734, 840.47681, 841.43036, 841.47949, 896.4137, 903.47253, 904.47565, 905.47632, 906.47607, 924.46271, 951.51038, 969.52002, 970.52283, 1038.50195, 1041.53308, 1042.53845, 1080.55493, 1081.54773),
#'           intensity = c(378322, 32496.6, 85689.6, 46440.3, 49645.2, 25102.5, 32516.2, 83497.2, 74653.1, 37228, 196053, 83826.4, 112718, 114812, 88089.5, 61521.3, 220054, 58888.5, 280334, 122311, 14953.2, 26959.6, 24854, 27122.9, 86216.1, 63360.3, 358968, 47393.5, 37893.2, 16532.9, 17259, 37250.4, 33679.8, 21243.6, 17854.9, 51232.4, 12738.8, 19515.4, 31560.1, 48772.3, 66481.2, 23353.6, 11994, 15211, 9883.29, 14753.7, 17304.7, 51575.9, 10917.6, 40518.3, 15107.3, 62106.4, 72496.1, 9430.4, 10289.3, 34831.3, 41981.1, 17000, 25000, 12000, 9000, 4000, 9579.9, 10392.3, 13507.4, 38200.9, 29990.5, 9304.39, 19849, 10678.6, 8452.85, 14519.3, 111717, 185030, 56020.8, 3387.69, 9478.08, 7878.29, 3167.8, 20670.7, 2774.25, 31114.4, 3385.92, 4656.8, 3687.15, 65332.5, 207097, 68080.9, 11934.3, 3630.86, 9201.02, 47579.2, 19125.8, 3439.48, 15082.1, 8280.57, 4170.47, 2603.17),
#'           title = "TP filtered inserted example 2 of protViz::deisotoper.",
#'           rtinseconds = 1581,
#'           charge = 2,
#'           scan = 1,
#'           id = 1,
#'           pepmass = 592.8093)
#'
#' # deisotope the data
#' xd <- deisotope(dtoper, x)
#' 
#' # return the annotated spectrum of the above deisotoped data
#' xas <- getAnnotatedSpectrum(dtoper)
#' 
#' # return the dot graphs of the above deisotoped data
#' xdot <- getDOTGraphs(dtoper)
#' 
#' # plot the example data and the deisotoped data
#' .plot(x, xd)
#' 
#' # draw one of the dot graphs
#' .plotDOT(xdot[[1]])
#' 
#' # EXAMPLE 2
#' # standart configurated deisotoper with changed delta and decharging
#' dtoper2 <- deisotoper(delta = 0.005, decharge = TRUE)
#' 
#' # return the configuration of dtoper2
#' config2 <- getConfig(dtoper2)
deisotoper <- function(amino_acid_masses = list('A' = 71.03711, 'R' = 156.10111, 'N' = 114.04293, 'D' = 115.02694, 'C' = 103.00919, 
                                                'E' = 129.04259, 'Q' = 128.05858, 'G' = 57.02146, 'H' = 137.05891, 'I' = 113.08406, 
                                                'L' = 113.08406, 'K' = 128.09496, 'M' = 131.04049, 'F' = 147.06841, 'P' = 97.05276, 
                                                'S' = 87.03203, 'T' = 101.04768, 'W' = 186.07931, 'Y' = 163.06333, 'V' = 99.06841), 
                       F1 = 0.8, F2 = 0.5, F3 = 0.1, F4 = 0.1, F5 = 0.1, 
                       delta = 0.003, errortolerance = 0.3, distance = 1.003, noise = 0.0, 
                       decharge = FALSE, modus = "first", comment = "no comment") {
  dtoper <- .jnew("ch.fgcz.proteomics.R.FeaturesBasedDeisotoping")
  
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
  
  .jcall(dtoper, "V", "setConfiguration", 
         as.vector(unlist(amino_acid_masses)), F1, F2, F3, F4, F5, 
         delta, errortolerance, distance, noise, 
         decharge, modus)
  return(list(javaRef = dtoper, comment = comment))
}

#' @export
#' @author Lucas Schmidt
deisotope <- function(deisotoper, massspectrum) {
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
    idx<-order(x$mZ)
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
  
  massspectrumout <- c(list(title = massspectrum$title,
               rtinseconds = massspectrum$rtinseconds,
               charge = massspectrum$charge,
               scan = massspectrum$scan,
               pepmass = massspectrum$pepmass,
               mZ = mzout, 
               intensity = intensityout,
               id = massspectrum$id))
  
  return(massspectrumout)
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

#' @export
#' @author Lucas Schmidt
getDOTGraphs <- function(deisotoper) {
  DOT <- .jcall(deisotoper$javaRef, "[S", "getDOT")
  
  DOT
}

#' @export
#' @author Lucas Schmidt
getAnnotatedSpectrum <- function(deisotoper) {
  AS <- .jcall(deisotoper$javaRef, "S", "getAnnotatedSpectrum")
  
  con <- textConnection(AS)
  read.csv(con, sep=',', header = TRUE)
}

#' @export
#' @author Lucas Schmidt
getSummary <- function(deisotoper) {
  Summary <- .jcall(deisotoper$javaRef, "S", "getSummary")
  
  con <- textConnection(Summary)
  read.csv(con, sep=',', header = TRUE)
}

#' @export
#' @author Lucas Schmidt
getConfig <- function(deisotoper) {
  config <- .jcall(deisotoper$javaRef, "S", "getConfiguration")
  
  con <- textConnection(config)
  read.csv(con, sep=',', header = TRUE)
}

#' @export 
#' @author Lucas Schmidt
.plotDOT <- function(dot) {
  DiagrammeR::grViz(dot)
}

#' @export 
#' @author Lucas Schmidt
.plot <- function(massspectrum1, massspectrum2, zoom = c(0, 2000)) {
  maxintensity1 <- max(massspectrum1$intensity)
  maxintensity2 <- max(massspectrum2$intensity)
  
  if(maxintensity1 <= maxintensity2) {
    plot(x = massspectrum2$mZ, y = massspectrum2$intensity, type = "h", xlab = "mZ", ylab = "Intensity", col = "#0000FF99", axes = FALSE, xlim = zoom, ylim = c(0, maxintensity2 + 100))
    lines(x = massspectrum1$mZ, y = massspectrum1$intensity, type = "h", xlab = "mZ", ylab = "Intensity", col = "#FF000099")
    mtext(text =deparse(substitute(massspectrum2)), line = 2, adj = 0, col="blue")
    mtext(text =deparse(substitute(massspectrum1)), line = 1, adj = 0, col="red")
  } else if (maxintensity1 > maxintensity2) {
    plot(x = massspectrum1$mZ, y = massspectrum1$intensity, type = "h", xlab = "mZ", ylab = "Intensity", col = "#FF000099", axes = FALSE, xlim = zoom, ylim = c(0, maxintensity1 + 100))
    lines(x = massspectrum2$mZ, y = massspectrum2$intensity, type = "h", xlab = "mZ", ylab = "Intensity", col = "#0000FF99")
    mtext(text =deparse(substitute(massspectrum1)), line = 2, adj = 0, col="red")
    mtext(text =deparse(substitute(massspectrum2)), line = 1, adj = 0, col="blue")
  }
  
  axis(side=1, at = c(massspectrum1$mZ, massspectrum2$mZ))
  axis(side=2, at = seq(0, max(maxintensity1, maxintensity2) + 10000, by = 10000))
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