#R
library(shiny)
library(protViz)
library(rJava)

.jinit()
toolkit <- J("java.awt.Toolkit")
default_toolkit <- .jrcall(toolkit, "getDefaultToolkit")
dim <- .jrcall(default_toolkit, "getScreenSize")
height <- .jcall(dim, "D", "getHeight")

delta <- function(mz) {
  delta <- c()
  
  for(i in 2:length(mz)) {
    m <- mz[[i]] - mz[[i - 1]]
    delta <- c(delta, round(m, digits = 2))
  }
  
  return(delta)
}

diff <- function(query1, query2) {
  qms1 <- protViz:::.get_ms2(query1)
  qms2 <- protViz:::.get_ms2(query2)
  qms <- list(mZ = c(qms1$mZ, qms2$mZ), intensity = c(qms1$intensity, qms2$intensity))
  diffmz <- c(setdiff(x = qms1$mZ, y = qms2$mZ), setdiff(x = qms2$mZ, y = qms1$mZ))
  
  diffint <- c(setdiff(x = qms1$intensity, y = qms2$intensity), setdiff(x = qms2$intensity, y = qms1$intensity))
  diffintall <- list()
  
  i <- 1
  for (x in qms$mZ) {
    if(qms$intensity[[i]] %in% diffint) {
      diffintall <- c(x, diffintall)
    }
    i <- i + 1
  }
  
  return(list(mZ=diffmz, intensity=diffintall, realintensity=diffint))
}

diff2 <- function(rv1, rv2, spec1, spec2, itol) {
  LABEL.abc1 <- (abs(rv1$mZ.Da.error) < itol) & (regexpr("[abc].*", rv1$label) > 0)
  LABEL.xyz1 <- (abs(rv1$mZ.Da.error) < itol) & (regexpr("[xyz].*", rv1$label) > 0)
  
  mz1 <-c(spec1$mZ[rv1$idx[LABEL.abc1]], spec1$mZ[rv1$idx[LABEL.xyz1]])
  
  LABEL.abc2 <- (abs(rv2$mZ.Da.error) < itol) & (regexpr("[abc].*", rv2$label) > 0)
  LABEL.xyz2 <- (abs(rv2$mZ.Da.error) < itol) & (regexpr("[xyz].*", rv2$label) > 0)
  
  mz2 <- c(spec2$mZ[rv2$idx[LABEL.abc2]], spec2$mZ[rv2$idx[LABEL.xyz2]])
  
  mz1 <- round(mz1, 2)
  mz2 <- round(mz2, 2)
  
  diffmz1 <- setdiff(mz1, mz2)
  diffmz2 <- setdiff(mz2, mz1)
  
  diffmz <- c(diffmz1, diffmz2)
  
  return(diffmz)
}

peakplot <- function(peptideSequence,
                     spec, 
                     FUN=defaultIon, 
                     fi=fragmentIon(peptideSequence, FUN=FUN)[[1]],
                     main=NULL,
                     sub=paste(peptideSequence, spec$title, sep=" / "),
                     xlim=range(spec$mZ, na.rm=TRUE),
                     ylim=range(spec$intensity, na.rm=TRUE),
                     itol=0.6,
                     pattern.abc="[abc].*",
                     pattern.xyz="[xyz].*",
                     ion.axes=TRUE){ 
  
  n<-nchar(peptideSequence)
  
  m<-psm(peptideSequence, spec, FUN, fi=fi, plot=FALSE)
  
  max.intensity<-max(spec$intensity, na.rm=TRUE)
  yMax <- 1.0 * max.intensity
  
  plot(spec$mZ, spec$intensity,
       xlab='m/z',
       ylab='Intensity',
       type='h',
       main=main,
       xlim=xlim,
       ylim=ylim,
       sub=sub,
       axes='F'
  ) 
  
  LABEL.abc<-(abs(m$mZ.Da.error) < itol) & (regexpr(pattern.abc, m$label) > 0)
  LABEL.xyz<-(abs(m$mZ.Da.error) < itol) & (regexpr(pattern.xyz, m$label) > 0)
  
  if (ion.axes){
    if (length(m$idx[LABEL.abc]) > 0){
      axis(1, spec$mZ[m$idx[LABEL.abc]], m$label[LABEL.abc],las=2)
    }
    axis(2)
    if (length(m$idx[LABEL.xyz]) > 0){
      axis(3, spec$mZ[m$idx[LABEL.xyz]], m$label[LABEL.xyz], col.axis='blue', las=2)
    }
  }else{
    axis(1)
    axis(2)
    a.at <- spec$mZ[m$idx[LABEL.abc | LABEL.xyz]]
    a.label <- m$label[LABEL.abc | LABEL.xyz]
    
    if (length(a.at)>0) {
      axis(3,a.at, a.label, col.axis='black', las=2)
    } else {
      print ("WARNING")
      print (a.at)
      print (a.label)
    }
    box()
  }
  axis(4,seq(0,yMax,length=6), seq(0,100,length=6))
  
  protViz:::.peakplot.label(spec=spec, match=m, yMax=yMax)
  
  return(m)
}
