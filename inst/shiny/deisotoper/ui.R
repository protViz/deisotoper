shinyUI(fluidPage(
  titlePanel("DEISOTOPER", windowTitle = "DEISOTOPER"),
  fluidRow( hr(),
    column(
      12,
      align="middle",
      textInput("ipath", "Input-Path", "/srv/lucas1/Downloads/MascotDaemon/input/Rdata/HeLa/TP_HeLa_200ng_bestCluster.RData", width = "100%"),
      tabsetPanel(
        tabPanel(
          title = "Deisotoping", 
          textInput("opath", "Output-Path", "", width = "100%"),
          radioButtons("modus", label = "Deisotoping-Modus",
                       choices = list("aggregate first" = "first", "aggregate highest" = "highest"), selected = "first"),
          actionButton("button1", label = "start deisotoper"), 
          hr()), 
        tabPanel("Graph & Plot",
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
        tabPanel("Configuration", 
                 htmlOutput("explanation", container = ),
                 textAreaInput("config", "Configuration", "", width = "100%", height = "500", resize = "none")
        )
        )
      ))
  ))
