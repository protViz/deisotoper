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
    
    showNotification("Finished deisotoping!", type = "message", duration = 1)
    
    if(file.exists("tmp.properties")) {
      file.remove("tmp.properties")
    }
  })
  
  msf <- eventReactive(input$button2, {
    name <- load(file = input$ipath)
    msm <- deisotoper:::jCreateMSM(get(name))
    
    msm
  })
  
  output$outputdiagram <- renderGrViz({
    grViz({
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
      
      mslist <- msf()$getMSlist()
      ms <- mslist$get(as.integer(input$massspectrum))

      ims <- deisotoper:::jCreateIMS(massspectrum = ms, configfile = "tmp.properties")
      dot <- deisotoper:::jGetDot(massspectrum = ms, isotopicmassspectrum = ims, index = input$isotopicset, configfile = "tmp.properties")
    
      showNotification("Finished drawing!", type = "message", duration = 1)
    
      if(file.exists("tmp.properties")) {
        file.remove("tmp.properties")
      }
      
      dot
      })
  })

  output$outputplot <- renderPlot({
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
    
    mslist <- msf()$getMSlist()
    ms <- mslist$get(as.integer(input$massspectrum))
    
    ims <- deisotoper:::jCreateIMS(massspectrum = ms, configfile = "tmp.properties")
    is <- deisotoper:::jGetIS(ims, input$isotopicset)
    
    islist <- as.list(is$getIsotopicSet())
    
    peaks <- c()
    for(x in 1:length(islist)) {
      peaks <- c(peaks, islist[[x]])
    }
    
    min <- peaks[[1]]$getIsotopicCluster()$get(as.integer(0))$getMz() - 2.5
    max <- peaks[[length(peaks)]]$getIsotopicCluster()$get(as.integer(peaks[[length(peaks)]]$getIsotopicCluster()$size() - 1))$getMz() + 2.5

    plot(x = ms$getMzArray(), y = ms$getIntensityArray(), type = "h", axes = FALSE, xlab = "mZ", ylab = "Intensity", xlim = c(min, max))
    axis(side=1, at = ms$getMzArray())
    axis(side=2, at = ms$getIntensityArray())
    
    showNotification("Finished plotting!", type = "message", duration = 1)
    
    if(file.exists("tmp.properties")) {
      file.remove("tmp.properties")
    }
  })
  
  mss <- eventReactive(input$button3, {
    name <- load(file = input$ipath)
    msm <- deisotoper:::jCreateMSM(get(name))
    
    msm
  })
  
  output$outputsummary <- renderTable({    
    mslist <- mss()$getMSlist()
  ms <- mslist$get(as.integer(input$summaryindex))
  
  sum <- deisotoper:::jSummaryMS(ms)
  
  showNotification("Finished summarizing!", type = "message", duration = 1)
  
  sum })
  
  mss2 <- eventReactive(input$button4, {
    name <- load(file = input$ipath)
    msm <- deisotoper:::jCreateMSM(get(name))
    
    msm
  })
  
  output$outputstatistic <- renderTable({    
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
    
    msm <- mss2()
    stats <- deisotoper:::jGetStatisticMSM(msm = msm, configfile = "tmp.properties")
    
    showNotification("Finished making statistic!", type = "message", duration = 1)
    
    if(file.exists("tmp.properties")) {
      file.remove("tmp.properties")
    }
    
    stats 
  })
  
  output$explanation <- renderText(HTML("The configuration must be in the following format:<br/>
                            AMINOACID=123<br/>
                            F1=1.2<br/>
                            DELTA=0.01<br/>
                            <br/>
                            List of fixed parameters who are predefined: F1, F2, F3, F4, F5, ERRORTOLERANCE, DELTA, NOISE and DISTANCE<br/>
                            Other parameters will be collected as amino acid masses. You can name them whatever you like. It is recommended that you use the 1 or 3 letter-code.<br/>
                            <br/>
                            It is neccessary, that when you add one parameter, you must insert a full amino acid masses set plus the parameter you added! Otherwise only the given parameters will be taken and no amino acid masses!")
                                   )
})