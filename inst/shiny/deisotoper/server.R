shinyServer(function(input, output, session) {
  observeEvent(input$button1, {
    dir.create("temp")
    properties <- as.character(paste("temp/",generateRandomString(), ".properties", sep = ""))
    
    if(file.exists(properties)) {
      file.remove(properties)
    }
    
    deisotoper:::CreateProperties(properties)
    
    config <- unlist(strsplit(input$config, "\n"))
    
    if(input$config != ""){
      for(x in 1:length(config)) {
        string <- unlist(strsplit(config[x], "="))
        deisotoper:::AddToProperties(filename = properties, key = string[1], value = string[2])
      }
    } else {
      write(x = "# This file is empty", file = properties, append = TRUE)
    }
    
    deisotoper:::jBenchmark(input = input$ipath, output = input$opath, modus = input$modus, configfile = properties)
    
    showNotification("Finished deisotoping!", type = "message", duration = 1)
    
    if(file.exists(properties)) {
      file.remove(properties)
    }
  })
  
  msf <- eventReactive(input$button2, {
    name <- load(file = input$ipath)
    msm <- deisotoper:::jCreateMSM(get(name))
    
    msm
  })
  
  output$outputdiagram <- renderGrViz({
    grViz({
      dir.create("temp")
      properties <- as.character(paste("temp/",generateRandomString(), ".properties", sep = ""))
      
      if(file.exists(properties)) {
        file.remove(properties)
      }
      
      deisotoper:::CreateProperties(properties)
      
      config <- unlist(strsplit(input$config, "\n"))
      
      if(input$config != ""){
        for(x in 1:length(config)) {
          string <- unlist(strsplit(config[x], "="))
          deisotoper:::AddToProperties(filename = properties, key = string[1], value = string[2])
        }
      } else {
        write(x = "# This file is empty", file = properties, append = TRUE)
      }
      
      mslist <- msf()$getMSlist()
      ms <- mslist$get(as.integer(input$massspectrum))
      
      ims <- deisotoper:::jCreateIMS(massspectrum = ms, configfile = properties)
      dot <- deisotoper:::jGetDot(massspectrum = ms, isotopicmassspectrum = ims, index = input$isotopicset, configfile = properties)
      
      showNotification("Finished drawing!", type = "message", duration = 1)
      
      if(file.exists(properties)) {
        file.remove(properties)
      }
      
      dot
    })
  })
  
  output$outputplot <- renderPlot({
    dir.create("temp")
    properties <- as.character(paste("temp/",generateRandomString(), ".properties", sep = ""))
    
    if(file.exists(properties)) {
      file.remove(properties)
    }
    
    deisotoper:::CreateProperties(properties)
    
    config <- unlist(strsplit(input$config, "\n"))
    
    if(input$config != ""){
      for(x in 1:length(config)) {
        string <- unlist(strsplit(config[x], "="))
        deisotoper:::AddToProperties(filename = properties, key = string[1], value = string[2])
      }
    } else {
      write(x = "# This file is empty", file = properties, append = TRUE)
    }
    
    mslist <- msf()$getMSlist()
    ms <- mslist$get(as.integer(input$massspectrum))
    
    ims <- deisotoper:::jCreateIMS(massspectrum = ms, configfile = properties)
    is <- deisotoper:::jGetIS(ims, input$isotopicset)
    
    islist <- as.list(is$getIsotopicSet())
    
    peaks <- c()
    for(x in 1:length(islist)) {
      peaks <- c(peaks, islist[[x]])
    }
    
    min <- peaks[[1]]$getIsotopicCluster()$get(as.integer(0))$getMz() - 2.5
    max <- peaks[[length(peaks)]]$getIsotopicCluster()$get(as.integer(peaks[[length(peaks)]]$getIsotopicCluster()$size() - 1))$getMz() + 2.5
    
    delta <- delta(ms$getMzArray())
    
    plot(x = ms$getMzArray(), y = ms$getIntensityArray(), type = "h", axes = FALSE, xlab = "mZ", ylab = "Intensity", xlim = c(min, max))
    axis(side=1, at = ms$getMzArray())
    axis(side=2, at = seq(0, max(ms$getIntensityArray()) + 10000, by = 2000), labels = FALSE)
    axis(side=2, at = seq(0, max(ms$getIntensityArray()) + 10000, by = 8000), las = 1, tick = FALSE)
    axis(side=2, at = round(max(ms$getIntensityArray())), col = "red", col.axis="red", las = 1)
    #axis(side=2, at = round(min(ms$getIntensityArray())), col = "blue", col.axis="blue", las = 1)
    
    for(x in 1:length(delta)) {
      if(x %% 2 == 0) {
        text(x = ms$getMzArray()[[x]] + delta[[x]] * 0.5 , y = 0.3 * max(ms$getIntensityArray(), na.rm=TRUE), labels = delta[[x]], cex = 0.8)
      } else {
        text(x = ms$getMzArray()[[x]] + delta[[x]] * 0.5 , y = 0.35 * max(ms$getIntensityArray(), na.rm=TRUE), labels = delta[[x]], cex = 0.8)
      }
    }
    
    showNotification("Finished plotting!", type = "message", duration = 1)
    
    if(file.exists(properties)) {
      file.remove(properties)
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
    dir.create("temp")
    properties <- as.character(paste("temp/",generateRandomString(), ".properties", sep = ""))
    
    if(file.exists(properties)) {
      file.remove(properties)
    }
    
    deisotoper:::CreateProperties(properties)
    
    config <- unlist(strsplit(input$config, "\n"))
    
    if(input$config != ""){
      for(x in 1:length(config)) {
        string <- unlist(strsplit(config[x], "="))
        deisotoper:::AddToProperties(filename = properties, key = string[1], value = string[2])
      }
    } else {
      write(x = "# This file is empty", file = properties, append = TRUE)
    }
    
    msm <- mss2()
    stats <- deisotoper:::jGetStatisticMSM(msm = msm, configfile = properties)
    
    showNotification("Finished making statistic!", type = "message", duration = 1)
    
    if(file.exists(properties)) {
      file.remove(properties)
    }
    
    stats 
  })
})
