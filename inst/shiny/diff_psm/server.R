library(shiny)
library(protViz)

shinyServer(function(input, output, session) {
  button <- eventReactive(input$load1, {
    url1 <- get(load(con <- url(input$text1)))
    url2 <- get(load(con <- url(input$text2)))
    
    index1 <- order(as.numeric(protViz:::.get_q_peptide(url1, attribute = 'pep_score')), decreasing = TRUE)
    index2 <- order(as.numeric(protViz:::.get_q_peptide(url2, attribute = 'pep_score')), decreasing = TRUE)
    
    sort <- input$sort
    
    if(sort == 1) {
      j <- 1
      for(i in index1) {
        url1$queries[j] <- url1$queries[i]
        url2$queries[j] <- url2$queries[i]
        j <- j + 1
      }
    } else if (sort == 2){ 
      j <- 1
      for(i in index2) {
        url1$queries[j] <- url1$queries[i]
        url2$queries[j] <- url2$queries[i]
        j <- j + 1
      }
    }

    list(one = url1, two = url2)
  })
  
  output$slider <- renderUI({
    sliderInput("id", label = "Spectrum ID", min = 1, max = max(length(button()$one$queries), length(button()$two$queries)), value = 1, width = "600", step = 1)
  })
  
  output$plot1 <- renderPlot({
    val <- button()$one
    if(!is.null(input$id)) {
    if(!is.null(val$queries[[input$id]]$q_peptide$pep_score)) {
      spec <- protViz:::.get_ms2(val$queries[[input$id]])
      rv <- peakplot(peptideSequence = val$queries[[input$id]]$q_peptide$pep_seq, spec = spec, sub='', xlim = c(input$zoom[1],input$zoom[2]), itol = input$itol, ylim = c(0, 1.1 * max(max(spec$intensity, na.rm=TRUE), max(protViz:::.get_ms2(button()$two$queries[[input$id]])$intensity, na.rm=TRUE))))
      axis(side=1, at = spec$mZ, line = 0, labels = FALSE)
      axis(side=1, at = spec$mZ, line = 1, tick = FALSE)
      
      difflist <- diff(val$queries[[input$id]], button()$two$queries[[input$id]])
      
      delta <- delta(spec$mZ)

      if(!is.null(input$check)) {
        if(1 %in% input$check) {
          abline(v=difflist$mZ, col="#0000FF33", lwd = 4)
        } 
        if (2 %in% input$check){
          abline(v=difflist$intensity, col="#FF000033", lwd = 4)
        }
        if (3 %in% input$check){
          for(x in 1:length(delta)) {
            if(x %% 2 == 0) {
              text(x = spec$mZ[[x]] + delta[[x]] * 0.5 , y = 0.3 * max(max(spec$intensity, na.rm=TRUE), max(protViz:::.get_ms2(button()$two$queries[[input$id]])$intensity, na.rm=TRUE)), labels = delta[[x]], cex = 0.8)
            } else {
              text(x = spec$mZ[[x]] + delta[[x]] * 0.5 , y = 0.35 * max(max(spec$intensity, na.rm=TRUE), max(protViz:::.get_ms2(button()$two$queries[[input$id]])$intensity, na.rm=TRUE)), labels = delta[[x]], cex = 0.8)
            }
          }
        } 
      }
      
      mtext(text = paste("Query Number: ", val$queries[input$id]$query$.attrs, ", Mascot Score: ", val$queries[[input$id]]$q_peptide$pep_score, ", Peptide Sequence: ", val$queries[[input$id]]$q_peptide$pep_seq, sep = ""), line = 2, adj = 0, col="black")
    }
    }
  })
  
  output$plot2 <- renderPlot({   
    val <- button()$two
    if(!is.null(input$id)) {
    if(!is.null(val$queries[[input$id]]$q_peptide$pep_score)) {
      spec <- protViz:::.get_ms2(val$queries[[input$id]])
      rv <- peakplot(peptideSequence = val$queries[[input$id]]$q_peptide$pep_seq, spec = spec, sub='', xlim = c(input$zoom[1],input$zoom[2]), itol = input$itol, ylim = c(0, 1.1 * max(max(spec$intensity, na.rm=TRUE), max(protViz:::.get_ms2(button()$one$queries[[input$id]])$intensity, na.rm=TRUE))))
      axis(side=1, at = spec$mZ, line = 0, labels = FALSE)
      axis(side=1, at = spec$mZ, line = 1, tick = FALSE)
      
      difflist <- diff(button()$one$queries[[input$id]], val$queries[[input$id]])
      
      delta <- delta(spec$mZ)
      
      delta <- delta(spec$mZ)
      
      if(!is.null(input$check)) {
        if(1 %in% input$check) {
          abline(v=difflist$mZ, col="#0000FF33", lwd = 4)
        } 
        if (2 %in% input$check){
          abline(v=difflist$intensity, col="#FF000033", lwd = 4)
        }
        if (3 %in% input$check){
          for(x in 1:length(delta)) {
            if(x %% 2 == 0) {
              text(x = spec$mZ[[x]] + delta[[x]] * 0.5 , y = 0.3 * max(max(spec$intensity, na.rm=TRUE), max(protViz:::.get_ms2(button()$two$queries[[input$id]])$intensity, na.rm=TRUE)), labels = delta[[x]], cex = 0.8)
            } else {
              text(x = spec$mZ[[x]] + delta[[x]] * 0.5 , y = 0.35 * max(max(spec$intensity, na.rm=TRUE), max(protViz:::.get_ms2(button()$two$queries[[input$id]])$intensity, na.rm=TRUE)), labels = delta[[x]], cex = 0.8)
            }
          }
        } 
      }

      mtext(text = paste("Query Number: ", val$queries[input$id]$query$.attrs, ", Mascot Score: ", val$queries[[input$id]]$q_peptide$pep_score, ", Peptide Sequence: ", val$queries[[input$id]]$q_peptide$pep_seq, sep = ""), line = 2, adj = 0, col="black")
    }
    }
  })
  
  ranges <- reactiveValues(x = NULL, y = NULL)
  
  observeEvent(input$dbclick, {
    brush <- input$brush
    if (!is.null(brush)) {
      ranges$x <- c(brush$xmin, brush$xmax)
      ranges$y <- c(brush$ymin, brush$ymax)
    } else {
      ranges$x <- NULL
      ranges$y <- NULL
    }
  })
  
  output$plot3 <- renderPlot({  
    pep_score1 <- protViz:::.get_q_peptide(button()$one, attribute = 'pep_score')
    pep_score2 <- protViz:::.get_q_peptide(button()$two, attribute = 'pep_score')
    index1 <- order(as.numeric(protViz:::.get_q_peptide(button()$one, attribute = 'pep_score')), decreasing = TRUE)
    index2 <- order(as.numeric(protViz:::.get_q_peptide(button()$two, attribute = 'pep_score')), decreasing = TRUE)
    plot(pep_score1[index1],col="#0000FF", type="p", cex = 0.8, ylab = "Mascot-Score", xlab = "Index", xlim = ranges$x, ylim = ranges$y, pch = 16)
    points(pep_score2[index2],col="#FF0000", type="p", cex = 0.8, pch = 16)
    mtext(text ="First Mascot-Scores", line = 2, adj = 0, col="blue")
    mtext(text ="Second Mascot-Scores", line = 1, adj = 0, col="red")
    abline(v=input$id, col="#FF00FF77", lwd = 1)
    mtext(text ="Position of the plotted Mass Spectra", line = 0, adj = 0, col="#FF00FF")
  })
})

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

delta <- function(mz) {
  delta <- c()
  
  for(i in 2:length(mz)) {
    m <- mz[[i]] - mz[[i - 1]]
    delta <- c(delta, round(m, digits = 2))
  }
  
  return(delta)
}

diffIon <- function(rv1, rv2) {
  diffb <- c(setdiff(rv1$fragmentIon$b, rv2$fragmentIon$b), setdiff(rv2$fragmentIon$b, rv1$fragmentIon$b))
  diffy <- c(setdiff(rv1$fragmentIon$y, rv2$fragmentIon$y), setdiff(rv2$fragmentIon$y, rv1$fragmentIon$y))
  diffc <- c(setdiff(rv1$fragmentIon$c, rv2$fragmentIon$c), setdiff(rv2$fragmentIon$c, rv1$fragmentIon$c))
  diffz <- c(setdiff(rv1$fragmentIon$z, rv2$fragmentIon$z), setdiff(rv2$fragmentIon$z, rv1$fragmentIon$z))
  diffall <- c(diffb, diffy, diffc, diffz)
  
  return(diffall)
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