library(shiny)
library(protViz)

shinyServer(function(input, output, session) {
  values <- reactiveValues(i = 1)
  
  button <- eventReactive(input$load1, {
    list(one = get(load(con <- url(input$text1))), two = get(load(con <- url(input$text2))))
    
  })
  
  observe({
    input$action2
    
    isolate({
      values$i <- values$i + 1
    })
  })
  
  observe({
    input$action1
    isolate(if(values$i != 1) { values$i <- values$i - 1 })
  })
  
  output$num <- renderText({
    values$i
  })
  
  output$plot1 <- renderPlot({
    val <- button()$one
    rv <- plot.mascot_query(val$queries[[values$i]], val)
    
    difflist <- diff(val$queries[[values$i]], button()$two$queries[[values$i]])
    abline(v=difflist$mZ, col="#00FF0033", lwd = 4)
    abline(v=difflist$intensity, col="#FFFF0033", lwd = 4)
    if(!is.null(val$queries[[values$i]]$q_peptide$pep_score)) {
      if(as.numeric(val$queries[[values$i]]$q_peptide$pep_score) > 25) {
        mtext(text = paste("Mascot Score:", val$queries[[values$i]]$q_peptide$pep_score), line = 2, adj = 0, col="red")
      } else {
        mtext(text = paste("Mascot Score:", val$queries[[values$i]]$q_peptide$pep_score), line = 2, adj = 0, col="black")
      }
    }
  })
  
  output$plot2 <- renderPlot({   
    val <- button()$two
    rv <- plot.mascot_query(val$queries[[values$i]], val)
    
    difflist <- diff(button()$one$queries[[values$i]], val$queries[[values$i]])
    abline(v=difflist$mZ, col="#00FF0033", lwd = 4)
    abline(v=difflist$intensity, col="#FFFF0033", lwd = 4)
    if(!is.null(val$queries[[values$i]]$q_peptide$pep_score)) {
      if(as.numeric(val$queries[[values$i]]$q_peptide$pep_score) > 25) {
        mtext(text = paste("Mascot Score:", val$queries[[values$i]]$q_peptide$pep_score), line = 2, adj = 0, col="red")
      } else {
        mtext(text = paste("Mascot Score:", val$queries[[values$i]]$q_peptide$pep_score), line = 2, adj = 0, col="black")
      }
    }
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
  
  return(list(mZ=diffmz, intensity=diffintall))
}