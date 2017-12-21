#R

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
        if(!is.null(url1$queries[[i]]$q_peptide$pep_score) && !is.null(url2$queries[[i]]$q_peptide$pep_seq)) {
          url1$queries[j] <- url1$queries[i]
          url2$queries[j] <- url2$queries[i]
          j <- j + 1
        }
      }
    } else if (sort == 2){ 
      j <- 1
      for(i in index2) {
        if(!is.null(url1$queries[[i]]$q_peptide$pep_score) && !is.null(url2$queries[[i]]$q_peptide$pep_seq)) {
          url1$queries[j] <- url1$queries[i]
          url2$queries[j] <- url2$queries[i]
          j <- j + 1
        }
      }
    }

    list(one = url1, two = url2)
  })
  
  output$slider <- renderUI({
    sliderInput("id", label = "Spectrum ID", min = 1, max = max(length(button()$one$queries), length(button()$two$queries)), value = 1, width = "1400", step = 1)
  })
  
  output$plot1 <- renderPlot({
    val <- button()$one
    if(!is.null(input$id)) {
    if(!is.null(val$queries[[input$id]]$q_peptide$pep_score) && !is.null(button()$two$queries[[input$id]]$q_peptide$pep_seq)) {
      spec <- protViz:::.get_ms2(val$queries[[input$id]])
      rv <- peakplot(peptideSequence = val$queries[[input$id]]$q_peptide$pep_seq, spec = spec, sub='', xlim = c(input$zoom[1],input$zoom[2]), itol = input$itol, ylim = c(0, 1.1 * max(max(spec$intensity, na.rm=TRUE), max(protViz:::.get_ms2(button()$two$queries[[input$id]])$intensity, na.rm=TRUE))))
      axis(side=1, at = spec$mZ, line = 0, labels = FALSE)
      axis(side=1, at = spec$mZ, line = 1, tick = FALSE)
      
      difflist <- diff(val$queries[[input$id]], button()$two$queries[[input$id]])
      
      delta <- delta(spec$mZ)
      
      difflist2 <- diff2(rv1 = rv, rv2 = psm(button()$two$queries[[input$id]]$q_peptide$pep_seq, protViz:::.get_ms2(button()$two$queries[[input$id]]), plot=FALSE), spec1 = spec, spec2 = protViz:::.get_ms2(button()$two$queries[[input$id]]), itol = input$itol)
      
      if(!is.null(input$check)) {
        if(1 %in% input$check) {
          abline(v=difflist$mZ, col="#0000FF33", lwd = 4)
        } 
        if (2 %in% input$check){
          abline(v=difflist$intensity, col="#FF000033", lwd = 4)
        }
        if(3 %in% input$check) {
          abline(v=difflist2, col="#00FF0033", lwd = 4)
        }
        if (4 %in% input$check){
          for(x in 1:length(delta)) {
            if(x %% 2 == 0) {
              text(x = spec$mZ[[x]] + delta[[x]] * 0.5 , y = 0.3 * max(max(spec$intensity, na.rm=TRUE), max(protViz:::.get_ms2(button()$two$queries[[input$id]])$intensity, na.rm=TRUE)), labels = delta[[x]], cex = 0.8)
            } else {
              text(x = spec$mZ[[x]] + delta[[x]] * 0.5 , y = 0.35 * max(max(spec$intensity, na.rm=TRUE), max(protViz:::.get_ms2(button()$two$queries[[input$id]])$intensity, na.rm=TRUE)), labels = delta[[x]], cex = 0.8)
            }
          }
        } 
      }
      
      #mtext(text = paste("Query Number: ", val$queries[input$id]$query$.attrs, ", Mascot Score: ", val$queries[[input$id]]$q_peptide$pep_score, ", Peptide Sequence: ", val$queries[[input$id]]$q_peptide$pep_seq, sep = ""), line = 2, adj = 0, col="black")
    }
    }
  })
  
  output$textout1 <- renderText({
    val <- button()$one
    if (!is.null(input$id)) {
      if (!is.null(val$queries[[input$id]]$q_peptide$pep_score) && !is.null(button()$two$queries[[input$id]]$q_peptide$pep_seq)) {
        spec <- protViz:::.get_ms2(val$queries[[input$id]])
        paste("Query Number: ", val$queries[input$id]$query$.attrs, ", Mascot Score: ", val$queries[[input$id]]$q_peptide$pep_score, ", Peptide Sequence: ", val$queries[[input$id]]$q_peptide$pep_seq, ", Number of Peaks: ", length(spec$mZ), sep = "")
      }
    }
  })
  
  output$plot2 <- renderPlot({   
    val <- button()$two
    if(!is.null(input$id)) {
    if(!is.null(val$queries[[input$id]]$q_peptide$pep_score) && !is.null(button()$two$queries[[input$id]]$q_peptide$pep_seq)) {
      spec <- protViz:::.get_ms2(val$queries[[input$id]])
      rv <- peakplot(peptideSequence = val$queries[[input$id]]$q_peptide$pep_seq, spec = spec, sub='', xlim = c(input$zoom[1],input$zoom[2]), itol = input$itol, ylim = c(0, 1.1 * max(max(spec$intensity, na.rm=TRUE), max(protViz:::.get_ms2(button()$one$queries[[input$id]])$intensity, na.rm=TRUE))))
      axis(side=1, at = spec$mZ, line = 0, labels = FALSE)
      axis(side=1, at = spec$mZ, line = 1, tick = FALSE)
      
      difflist <- diff(button()$one$queries[[input$id]], val$queries[[input$id]])
      
      delta <- delta(spec$mZ)
      
      difflist2 <- diff2(rv2 = rv, rv1 = psm(button()$one$queries[[input$id]]$q_peptide$pep_seq, protViz:::.get_ms2(button()$one$queries[[input$id]]), plot=FALSE), spec2 = spec, spec1 = protViz:::.get_ms2(button()$one$queries[[input$id]]), itol = input$itol)
      
      if(!is.null(input$check)) {
        if(1 %in% input$check) {
          abline(v=difflist$mZ, col="#0000FF33", lwd = 4)
        } 
        if (2 %in% input$check){
          abline(v=difflist$intensity, col="#FF000033", lwd = 4)
        }
        if(3 %in% input$check) {
          abline(v=difflist2, col="#00FF0033", lwd = 4)
        }
        if (4 %in% input$check){
          for(x in 1:length(delta)) {
            if(x %% 2 == 0) {
              text(x = spec$mZ[[x]] + delta[[x]] * 0.5 , y = 0.3 * max(max(spec$intensity, na.rm=TRUE), max(protViz:::.get_ms2(button()$two$queries[[input$id]])$intensity, na.rm=TRUE)), labels = delta[[x]], cex = 0.8)
            } else {
              text(x = spec$mZ[[x]] + delta[[x]] * 0.5 , y = 0.35 * max(max(spec$intensity, na.rm=TRUE), max(protViz:::.get_ms2(button()$two$queries[[input$id]])$intensity, na.rm=TRUE)), labels = delta[[x]], cex = 0.8)
            }
          }
        } 
      }

      #mtext(text = paste("Query Number: ", val$queries[input$id]$query$.attrs, ", Mascot Score: ", val$queries[[input$id]]$q_peptide$pep_score, ", Peptide Sequence: ", val$queries[[input$id]]$q_peptide$pep_seq, sep = ""), line = 2, adj = 0, col="black")
    }
    }
  })
  
  output$textout2 <- renderText({ 
    val <- button()$two
    if(!is.null(input$id)) {
      if(!is.null(val$queries[[input$id]]$q_peptide$pep_score) && !is.null(button()$two$queries[[input$id]]$q_peptide$pep_seq)) {
        spec <- protViz:::.get_ms2(val$queries[[input$id]])
        paste("Query Number: ", val$queries[input$id]$query$.attrs, ", Mascot Score: ", val$queries[[input$id]]$q_peptide$pep_score, ", Peptide Sequence: ", val$queries[[input$id]]$q_peptide$pep_seq, ", Number of Peaks: ", length(spec$mZ), sep = "")
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
    mtext(text ="Position of the plotted Mass Spectra", line = 0, adj = 0, col="#FF00FF")
  })
})
