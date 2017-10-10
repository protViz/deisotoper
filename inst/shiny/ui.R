library(shiny)

shinyUI(fluidPage(
  titlePanel("PSM-Difference",windowTitle = "PSM-Difference"),
  fluidRow(
    column(12, align="center",
           hr(),
           textInput("text1", "URL (RData-file)", "file:///srv/lucas1/Downloads/MascotDaemon/deisotoped/fdbmaggregatednew/TP_HeLa_200ng_benchmark_fdbm_first.RData", width = "100%"),
           actionButton("load1", "load", width = "10%", style="center-align"),
           hr(),
           textInput("text2", "URL (RData-file)", value = "file:///srv/lucas1/Downloads/MascotDaemon/deisotoped/fdbmaggregatednew/TP_HeLa_200ng_benchmark_fdbm_with_mod_first.RData", width = "100%"),
           actionButton("load2", "load", width = "10%"),
           hr(),
           div(style="display: inline-block;vertical-align:top; width: 150px;", actionButton("action1", "-", width = "30%")),
           div(style="display: inline-block;vertical-align:top; width: 150px;",textOutput("num")),
           div(style="display: inline-block;vertical-align:top; width: 150px;",actionButton("action2", "+", width = "30%")),
           hr(),
           plotOutput("plot1", height = "520"), 
           plotOutput("plot2", height = "520")
    )
  )
)
)
