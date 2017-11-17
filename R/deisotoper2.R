deisotoper <- function(AA_MASS = c(71.03711, 156.10111, 114.04293, 115.02694, 103.00919, 129.04259, 128.05858, 57.02146, 137.05891, 113.08406, 113.08406, 128.09496, 131.04049, 147.06841, 97.05276,
                                   87.03203, 101.04768, 186.07931, 163.06333, 99.06841), F1 = 0.8, F2 = 0.5, F3 = 0.1, F4 = 0.1, F5 = 0.1, DELTA = 0.003, ERRORTOLERANCE = 0.3, DISTANCE = 1.003, NOISE = 0.0, DECHARGE = FALSE) {
  dtoper <- .jnew("ch.fgcz.proteomics.R.FeaturesBasedDeisotoping")
  
  if(0.5 > DISTANCE || DISTANCE > 1.5) {
    stop("DISTANCE can not be lower than 0.5 and grater than 1.5!")
  }
  
  DCHECK <- DISTANCE/3 - DISTANCE/6
  if(DELTA > DCHECK) {
    stop("DELTA can not be greater than DISTANCE/3 - DISTANCE/6!")
  }
  
  if(length(AA_MASS) <= 1) {
    stop("AA_MASS must contain 2 or more amino acid masses!")
  }
  
  if(0 > ERRORTOLERANCE || ERRORTOLERANCE > 1) {
    stop("ERRORTOLERANCE can not be lower than 0 and grater than 1!")
  }
  
  if(0 > NOISE || NOISE > 100) {
    stop("NOISE can not be lower than 0 and grater than 100! (0 = deactivated)")
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
  
  .jcall(dtoper, "V", "setConfiguration", AA_MASS, F1, F2, F3, F4, F5, DELTA, ERRORTOLERANCE, DISTANCE, NOISE, DECHARGE)
  return(list(javaRef = dtoper, comment= "xx"))
}

deisotope <- function(deisotoper, massspectrum, modus = "first") {
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
  
  if(!(modus == "first" || modus == "highest" || modus == "none")) {
    stop("Modus is incorrect. Available modi: 'first', 'highest', 'none'")
  }
  
  .jcall(deisotoper$javaRef, "V", "setMz", massspectrum$mZ)
  .jcall(deisotoper$javaRef, "V", "setIntensity", massspectrum$intensity)
  .jcall(deisotoper$javaRef, "V", "setPepMass", massspectrum$pepmass)
  .jcall(deisotoper$javaRef, "V", "setCharge", as.integer(massspectrum$charge))
  .jcall(deisotoper$javaRef, "V", "deisotope", modus)
  
  mzout <- .jcall(deisotoper$javaRef, "[D", "getMz")
  intensityout <- .jcall(deisotoper$javaRef, "[D", "getIntensity")
  
  MS <- c(list(title = massspectrum$title,
               rtinseconds = massspectrum$rtinseconds,
               charge = massspectrum$charge,
               scan = massspectrum$scan,
               pepmass = massspectrum$pepmass,
               mZ = mzout, 
               intensity = intensityout,
               id = massspectrum$id))
  
  return(MS)
}

deisotope.list <- function(deisotoper, psmset, modus = "first") {
  res <- list()
  for(i in 1:length(psmset)) {
    res <- c(res, list(deisotope(deisotoper = deisotoper, massspectrum = psmset[[i]], modus = modus)))
  }
  return(res)
}

getDOTGraphs <- function(deisotoper) {
  DOT <- .jcall(deisotoper$javaRef, "[S", "getDOT")
  
  DOT
}

getAnnotatedSpectrum <- function(deisotoper) {
  AS <- .jcall(deisotoper$javaRef, "S", "getAnnotatedSpectrum")
  
  con <- textConnection(AS)
  read.csv(con, sep=',', header = TRUE)
}

summary <- function(deisotoper) {
  Summary <- .jcall(deisotoper$javaRef, "S", "getSummary")
  
  con <- textConnection(Summary)
  read.csv(con, sep=',', header = TRUE)
}

getConfig <- function(deisotoper) {
  config <- .jcall(deisotoper$javaRef, "S", "getConfiguration")
  
  config
}

.plotDOT <- function(DOT) {
  DiagrammeR::grViz(DOT)
}

.plot <- function(massspectrum1, massspectrum2) {
  maxintensity1 <- max(massspectrum1$intensity)
  maxintensity2 <- max(massspectrum2$intensity)
  
  if(maxintensity1 <= maxintensity2) {
    plot(x = massspectrum2$mZ, y = massspectrum2$intensity, type = "h", xlab = "mZ", ylab = "Intensity", col = "#0000FF99", axes = FALSE, xlim = c(0, 2000))
    lines(x = massspectrum1$mZ, y = massspectrum1$intensity, type = "h", xlab = "mZ", ylab = "Intensity", col = "#FF000099")
    mtext(text ="First Mass Spectrum", line = 2, adj = 0, col="blue")
    mtext(text ="Second Mass Spectrum", line = 1, adj = 0, col="red")
  } else if (maxintensity1 > maxintensity2) {
    plot(x = massspectrum1$mZ, y = massspectrum1$intensity, type = "h", xlab = "mZ", ylab = "Intensity", col = "#FF000099", axes = FALSE, xlim = c(0, 2000))
    lines(x = massspectrum2$mZ, y = massspectrum2$intensity, type = "h", xlab = "mZ", ylab = "Intensity", col = "#0000FF99")
    mtext(text ="First Mass Spectrum", line = 2, adj = 0, col="red")
    mtext(text ="Second Mass Spectrum", line = 1, adj = 0, col="blue")
  }
  
  axis(side=1, at = c(massspectrum1$mZ, massspectrum2$mZ))
  axis(side=2, at = seq(0, max(maxintensity1, maxintensity2) + 10000, by = 10000))
}