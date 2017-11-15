shinyUI(fluidPage(
  titlePanel("DEISOTOPER", windowTitle = "DEISOTOPER"),
  fluidRow(
    hr(),
    column(
      12,
      align="middle",
      textInput("ipath", "Input-Path", "", width = "100%", placeholder = "Insert absolute path to RData-file here..."),
      tabsetPanel(
        tabPanel(
          title = "Deisotoping (uses Config.)", 
          textInput("opath", "Output-Path", "", width = "100%", placeholder = "Insert absolute path for output of mgf-file here..."),
          radioButtons("modus", label = "Deisotoping-Modus",
                       choices = list("aggregate first" = "first", "aggregate highest" = "highest"), selected = "first", inline = TRUE),
          actionButton("button1", label = "start deisotoper"), 
          hr()), 
        tabPanel("Graph & Plot (uses Config.)",
                 div(style="display: inline-block;vertical-align:top", numericInput("massspectrum", "Mass Spectrum Index", value = 0, min = 0)),
                 div(style="display: inline-block;vertical-align:top", numericInput("isotopicset", "Isotopic Set Index", value = 0, min = 0)),
                 br(),actionButton("button2", label = "draw graph"),
                 hr(),
                 grVizOutput("outputdiagram"),
                 plotOutput("outputplot", height = 460)
        ),
        tabPanel("Summary",
                 numericInput("summaryindex", "Mass Spectrum Index", value = 0, min = 0),
                 actionButton("button3", label = "make summary"),
                 hr(),
                 tableOutput("outputsummary")),
        tabPanel("Statistic (uses Config.)",
                 br(),
                 actionButton("button4", label = "make statistic"),
                 hr(),
                 tableOutput("outputstatistic")),
        tabPanel("Configuration", 
                 textInput("F1", "F1", "", width = "100%", placeholder = "Multiplier F1 for scoring"), 
                 textInput("F2", "F2", "", width = "100%", placeholder = "Multiplier F2 for scoring"), 
                 textInput("F3", "F3", "", width = "100%", placeholder = "Multiplier F3 for scoring"), 
                 textInput("F4", "F4", "", width = "100%", placeholder = "Multiplier F4 for scoring"), 
                 textInput("F5", "F5", "", width = "100%", placeholder = "Multiplier F5 for scoring"), 
                 textInput("DELTA", "Delta", "", width = "100%", placeholder = "The delta is used at clustering"), 
                 textInput("DISTANCE", "Distance between two peaks", "", width = "100%", placeholder = "The distance is used at clustering"), 
                 textInput("ERRORTOLERANCE", "Errortolerance", "", width = "100%", placeholder = "The errortolerance is used at scoring"), 
                 textInput("NOISE", "Noise filtering", "", width = "100%", placeholder = "Value between 0 and 100 in percent (0 is disabled)"), 
                 textInput("DECHARGE", "Decharge", "", width = "100%", placeholder = "TRUE or FALSE"), 
                 textInput("AAMASS", "Amino Acid Masses", "", width = "100%", placeholder = "Comma seperated values (minimum two or more), for example: 123,124,145"), 
                 hr()
        )
      )
    ))
))
