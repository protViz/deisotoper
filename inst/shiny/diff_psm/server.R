library(shiny)
library(protViz)

shinyServer(function(input, output, session) {
  values <- reactiveValues(i = 1)
  
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
  
  observe({
    input$action2
    isolate({ values$i <- values$i + 1 })
  })
  
  observe({
    input$action1
    isolate( if(values$i != 1) { values$i <- values$i - 1 })
  })
  
  output$num <- renderText({
    values$i
  })
  
  output$plot1 <- renderPlot({
    val <- button()$one
    if(!is.null(val$queries[[values$i]]$q_peptide$pep_score)) {
      spec <- protViz:::.get_ms2(val$queries[[values$i]])
      peakplot(peptideSequence = val$queries[[values$i]]$q_peptide$pep_seq, spec = spec, sub='', xlim = c(input$zoom[1],input$zoom[2]), ylim = c(0, max(spec$intensity)))
    
      difflist <- diff(val$queries[[values$i]], button()$two$queries[[values$i]])
      
      if(!is.null(input$check)) {
        if(input$check == 1 && length(input$check) == 1) {
          abline(v=difflist$mZ, col="#0000FF33", lwd = 4)
        } else if (input$check == 2 && length(input$check) == 1){
          abline(v=difflist$intensity, col="#FF000033", lwd = 4)
        } else if (length(input$check) == 2){
          abline(v=difflist$mZ, col="#0000FF33", lwd = 4)
          abline(v=difflist$intensity, col="#FF000033", lwd = 4)
        }
      }
      
      if(as.numeric(val$queries[[values$i]]$q_peptide$pep_score) > 25) {
        mtext(text = paste("Mascot Score:", val$queries[[values$i]]$q_peptide$pep_score), line = 2, adj = 0, col="red")
      } else {
        mtext(text = paste("Mascot Score:", val$queries[[values$i]]$q_peptide$pep_score), line = 2, adj = 0, col="black")
      }
    }
  })
  
  output$plot2 <- renderPlot({   
    val <- button()$two
    if(!is.null(val$queries[[values$i]]$q_peptide$pep_score)) {
      spec <- protViz:::.get_ms2(val$queries[[values$i]])
      peakplot(peptideSequence = val$queries[[values$i]]$q_peptide$pep_seq, spec = spec, sub='', xlim = c(input$zoom[1],input$zoom[2]), ylim = c(0, max(spec$intensity)))

      difflist <- diff(button()$one$queries[[values$i]], val$queries[[values$i]])
      
      if(!is.null(input$check)) {
        if(input$check == 1 && length(input$check) == 1) {
          abline(v=difflist$mZ, col="#0000FF33", lwd = 4)
        } else if (input$check == 2 && length(input$check) == 1){
          abline(v=difflist$intensity, col="#FF000033", lwd = 4)
        } else if (length(input$check) == 2){
          abline(v=difflist$mZ, col="#0000FF33", lwd = 4)
          abline(v=difflist$intensity, col="#FF000033", lwd = 4)
        }
      }

      if(as.numeric(val$queries[[values$i]]$q_peptide$pep_score) > 25) {
        mtext(text = paste("Mascot Score:", val$queries[[values$i]]$q_peptide$pep_score), line = 2, adj = 0, col="red")
      } else {
        mtext(text = paste("Mascot Score:", val$queries[[values$i]]$q_peptide$pep_score), line = 2, adj = 0, col="black")
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
    abline(v=values$i, col="#FF00FF77", lwd = 1)
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