shinyServer(function(input, output, session) {
  observeEvent(input$button1, {
    config <- deisotoper:::jCreateConfiguration(
      aa = formatAA(input$AAMASS),
      F1 = formatF1(input$F1),
      F2 = formatF2(input$F2),
      F3 = formatF3(input$F3),
      F4 = formatF4(input$F4),
      F5 = formatF5(input$F5),
      DELTA = formatDELTA(input$DELTA),
      ERRORTOLERANCE = formatERROR(input$ERRORTOLERANCE),
      DISTANCE = formatDISTANCE(input$DISTANCE),
      NOISE = formatNOISE(input$NOISE),
      DECHARGE = formatDECHARGE(input$DECHARGE))
    
    deisotoper:::jBenchmark(input = input$ipath, output = input$opath, modus = input$modus, configuration = config)
    
    showNotification("Finished deisotoping!", type = "message", duration = 1)
  })
  
  msf <- eventReactive(input$button2, {
    name <- load(file = input$ipath)
    msm <- deisotoper:::jCreateMSM(get(name))
    
    msm
  })
  
  output$outputdiagram <- renderGrViz({
    grViz({
      config <- deisotoper:::jCreateConfiguration(
        aa = formatAA(input$AAMASS),
        F1 = formatF1(input$F1),
        F2 = formatF2(input$F2),
        F3 = formatF3(input$F3),
        F4 = formatF4(input$F4),
        F5 = formatF5(input$F5),
        DELTA = formatDELTA(input$DELTA),
        ERRORTOLERANCE = formatERROR(input$ERRORTOLERANCE),
        DISTANCE = formatDISTANCE(input$DISTANCE),
        NOISE = formatNOISE(input$NOISE),
        DECHARGE = formatDECHARGE(input$DECHARGE))
      
      mslist <- msf()$getMSlist()
      ms <- mslist$get(as.integer(input$massspectrum))
      
      ims <- deisotoper:::jCreateIMS(massspectrum = ms, configuration = config)
      dot <- deisotoper:::jGetDot(massspectrum = ms, isotopicmassspectrum = ims, index = input$isotopicset, configuration = config)
      
      showNotification("Finished drawing!", type = "message", duration = 1)
      
      dot
    })
  })
  
  output$outputplot <- renderPlot({
    config <- deisotoper:::jCreateConfiguration(
      aa = formatAA(input$AAMASS),
      F1 = formatF1(input$F1),
      F2 = formatF2(input$F2),
      F3 = formatF3(input$F3),
      F4 = formatF4(input$F4),
      F5 = formatF5(input$F5),
      DELTA = formatDELTA(input$DELTA),
      ERRORTOLERANCE = formatERROR(input$ERRORTOLERANCE),
      DISTANCE = formatDISTANCE(input$DISTANCE),
      NOISE = formatNOISE(input$NOISE),
      DECHARGE = formatDECHARGE(input$DECHARGE))
    
    mslist <- msf()$getMSlist()
    ms <- mslist$get(as.integer(input$massspectrum))
    
    ims <- deisotoper:::jCreateIMS(massspectrum = ms, configuration = config)
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
    config <- deisotoper:::jCreateConfiguration(
      aa = formatAA(input$AAMASS),
      F1 = formatF1(input$F1),
      F2 = formatF2(input$F2),
      F3 = formatF3(input$F3),
      F4 = formatF4(input$F4),
      F5 = formatF5(input$F5),
      DELTA = formatDELTA(input$DELTA),
      ERRORTOLERANCE = formatERROR(input$ERRORTOLERANCE),
      DISTANCE = formatDISTANCE(input$DISTANCE),
      NOISE = formatNOISE(input$NOISE),
      DECHARGE = formatDECHARGE(input$DECHARGE))
    
    msm <- mss2()
    stats <- deisotoper:::jGetStatisticMSM(msm = msm, configuration = config)
    
    showNotification("Finished making statistic!", type = "message", duration = 1)
    
    stats 
  })
})
