deisotoper <- function(AA_MASS = c(71.03711, 156.10111, 114.04293, 115.02694, 103.00919, 129.04259, 128.05858, 57.02146, 137.05891, 113.08406, 113.08406, 128.09496, 131.04049, 147.06841, 97.05276,
                              87.03203, 101.04768, 186.07931, 163.06333, 99.06841), F1 = 0.8, F2 = 0.5, F3 = 0.1, F4 = 0.1, F5 = 0.1, DELTA = 0.003, ERRORTOLERANCE = 0.3, DISTANCE = 1.003, NOISE = 0.0, DECHARGE = FALSE) {
  dtoper <- .jnew("ch.fgcz.proteomics.R.FeaturesBasedDeisotopingR")

  .jcall(dtoper, "V", "setConfiguration", AA_MASS, F1, F2, F3, F4, F5, DELTA, ERRORTOLERANCE, DISTANCE, NOISE, DECHARGE))

  dtoper
}

deisotope <- function(deisotoper, rin, modus = "first") {
  lapply(rin, function(x) {
    if (is.null(x)) {
      return (x)
    }
    
    .jcall(deisotoper, "V", "setMz", x$mZ)
    .jcall(deisotoper, "V", "setIntensity", x$intensity)
    .jcall(deisotoper, "V", "setPepMass", x$pepmass)
    .jcall(deisotoper, "V", "setCharge", as.integer(x$charge))
    
    .jcall(deisotoper, "V", "deisotope", modus)
    
    mzout <- .jcall(deisotoper, "[D", "getMz")
    intensityout <- .jcall(deisotoper, "[D", "getIntensity")
    
    MS <- c(list(title = x$title,
                 rtinseconds = x$rtinseconds,
                 charge = x$charge,
                 scan = x$scan,
                 pepmass = x$pepmass,
                 mZ = mzout, 
                 intensity = intensityout,
                 id = x$id))
    
    as.MS(MS)
  })
}

is.MS <- function(x){
  if (sum(c("title", "rtinseconds", "charge","scan","pepmass","mZ","intensity") %in% names(x)) != 7){
    warning('attributed check failed.')
    #return (FALSE)
  }
  
  if (!is.vector(x$mZ) || !is.numeric(x$mZ) || is.unsorted(x$mZ)){
    warning('mZ value check failed.')
    warning(x$mZ)
    return(FALSE)
  }
  
  if (!is.vector(x$intensity) || !is.numeric(x$intensity)){
    warning('intensity check failed.')
    return(FALSE)
  }
  
  if (!is.numeric(x$pepmass) || !is.numeric(x$rtinseconds) || !is.numeric(x$charge) || !is.numeric(x$id)){
    warning('pepmass, rtinseconds, ... check failed.')
    return (FALSE)
  }
  
  TRUE
}

as.MS<- function(x){
  if ('mZ' %in% names(x) && length(x$mZ) < 2){
    return (NULL)
  }
  if (!('pepmass' %in% names(x)) || is.na(x$pepmass)){
    x$pepmass <- 100.0
  }
  
  if (!('title' %in% names(x)) || is.na(x$title) ){
    x$title <- "no title"
  }
  
  if (!('rtinseconds' %in% names(x)) || is.na(x$rtinseconds) ){
    x$rtinseconds <- 1
  }
  
  if ( !('charge' %in% names(x)) || is.na(x$charge) ){
    x$charge <- 1
  }
  
  if ( !('id' %in% names(x)) || is.na(x$id)){
    x$id <- 1
  }
  
  
  if(is.unsorted(x$mZ)){
    idx<-order(x$mZ)
    x$mZ <- x$mZ[idx]
    x$intensity <- x$intensity[idx]
  }
  
  stopifnot(is.MS(x))
  
  x
}
