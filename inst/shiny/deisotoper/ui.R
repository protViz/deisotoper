shinyUI(fluidPage(
  titlePanel("DEISOTOPER", windowTitle = "DEISOTOPER"),
  fluidRow(
    hr(),
    column(
      12,
      align="middle",
      textInput("ipath", "Input-Path", "/srv/lucas1/Downloads/MascotDaemon/input/Rdata/HeLa/TP_HeLa_200ng.RData", width = "100%", placeholder = "Insert absolute path to RData-file here..."),
      tabsetPanel(
        tabPanel(
          title = "Deisotoping (uses Config.)", 
          textInput("opath", "Output-Path", "", width = "100%", placeholder = "Insert absolute path for output of mgf-file here..."),
          radioButtons("modus", label = "Deisotoping-Modus",
                       choices = list("aggregate first" = "first", "aggregate highest" = "highest"), selected = "first"),
          actionButton("button1", label = "start deisotoper"), 
          hr()), 
        tabPanel("Graph & Plot (uses Config.)",
                 numericInput("massspectrum", "Mass Spectrum Index", value = 0, min = 0),
                 numericInput("isotopicset", "Isotopic Set Index", value = 0, min = 0),
                 actionButton("button2", label = "draw graph"),
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
                 htmlOutput("explanation"),
                 hr(),
                 textAreaInput("config", "Configuration", "", width = "100%", height = "500", resize = "none", placeholder = "For example:
                               ALA=71.03711
                                              F1=0.8
                                              F2=0.5
                                              F3=0.1
                               DISTANCE=1.003"), hr()
        )
        )
      ))
  ))
