library(shiny)
library(protViz)

shinyUI(fluidPage(
  titlePanel("PSM-Difference",windowTitle = "PSM-Difference"),
  fluidRow(
    column(12, align="center",
           hr(),
           textInput("text1", "URLs", "http://fgcz-ms.uzh.ch/~cpanse/R257544/F257664.RData", width = "100%"),
           textInput("text2", NULL, value = "http://fgcz-ms.uzh.ch/~cpanse/R257544/F257544.RData", width = "100%"),
           actionButton("load1", "load", width = "10%"),
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
