shinyServer(function(input, output, session) {
  observeEvent(input$button1, {
    if(file.exists("tmp.properties")) {
      file.remove("tmp.properties")
    }
    
    deisotoper:::CreateProperties("tmp.properties")
    
    config <- unlist(strsplit(input$config, "\n"))
  
    if(input$config != ""){
      for(x in 1:length(config)) {
        string <- unlist(strsplit(config[x], "="))
        deisotoper:::AddToProperties(filename = "tmp.properties", key = string[1], value = string[2])
      }
    } else {
      write(x = "# This file is empty", file = "tmp.properties", append = TRUE)
    }
    
    deisotoper:::jBenchmark(input = input$ipath, output = input$opath, modus = input$modus, configfile = "tmp.properties")
    
    showNotification("Finished deisotoping!", type = "message", duration = 3)
    
    if(file.exists("tmp.properties")) {
      file.remove("tmp.properties")
    }
  })
  
  graph <- eventReactive(input$button2, {
    name <- load(file = input$ipath)
    msm <- deisotoper:::jCreateMSM(get(name))
    
    mslist <- msm$getMSlist()
    ms <- mslist$get(as.integer(input$massspectrum))
    ims <- deisotoper:::jCreateIMS(massspectrum = ms, configfile = "nofile")
    dot <- deisotoper:::jGetDot(isotopicmassspectrum = ims, index = input$isotopicset)
    
    showNotification("Finished drawing!", type = "message", duration = 3)
    
    dot
  })
  
  output$outputdiagram <- renderGrViz({
    grViz({ graph() })
  })
  
  summary <- eventReactive(input$button3, {
    name <- load(file = input$ipath)
    msm <- deisotoper:::jCreateMSM(get(name))
    
    mslist <- msm$getMSlist()
    ms <- mslist$get(as.integer(input$summaryindex))
    
    sum <- deisotoper:::jSummaryMS(ms)
    
    showNotification("Finished summarizing!", type = "message", duration = 3)
    
    sum
  })
  
  output$outputsummary <- renderTable({ summary() })
})
