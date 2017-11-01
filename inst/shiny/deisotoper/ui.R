shinyUI(fluidPage(
  titlePanel("DEISOTOPER", windowTitle = "DEISOTOPER"),
  fluidRow( hr(),
    column(
      12,
      align="middle",
      textInput("ipath", "Input-Path", "", width = "100%"),
      tabsetPanel(
        tabPanel(
          title = "Deisotoping", 
          textInput("opath", "Output-Path", "", width = "100%"),
          textAreaInput("config", "Configuration", "", width = "100%", height = "400", resize = "none"),
          radioButtons("modus", label = "Deisotoping-Modus",
                       choices = list("aggregate first" = "first", "aggregate highest" = "highest"), selected = "first"),
          actionButton("button1", label = "start deisotoper"), 
          hr()), 
        tabPanel("Graph",
                 numericInput("massspectrum", "Mass Spectrum Index", value = 1, min = 1),
                 numericInput("isotopicset", "Isotopic Set Index", value = 1, min = 1),
                 actionButton("button2", label = "draw graph"),
                 hr(),
                 grVizOutput("outputdiagram") 
                 ),
        tabPanel("Summary",
                 numericInput("summaryindex", "Mass Spectrum Index", value = 1, min = 1),
                 actionButton("button3", label = "make summary"),
                 hr(),
                 tableOutput("outputsummary"))
        )
      ))
  ))
