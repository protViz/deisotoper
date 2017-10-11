library(shiny)
library(protViz)

shinyUI(fluidPage(
  titlePanel(
    "PSM-Difference",
    windowTitle = "PSM-Difference"),
  fluidRow(
    column(
      12,
      align="center",
      hr(),
      textInput("text1", "URLs", "http://fgcz-ms.uzh.ch/~cpanse/R257544/F257664.RData", width = "100%"),
      textInput("text2", NULL, value = "http://fgcz-ms.uzh.ch/~cpanse/R257544/F257544.RData", width = "100%"),
      radioButtons("sort", NULL, choiceNames = c("Sort first", "Sort second", "Sort none"), choiceValues = c(1, 2, 3), inline = TRUE),
      actionButton("load1", "load & sort", width = "150px"),
      
      hr(),
      
      div(style="display: inline-block;vertical-align:top; width: 10%;", actionButton("action1", "-", width = "50px")),
      div(style="display: inline-block;vertical-align:top; width: 3%;font-size:2em;", textOutput("num"), width = "50px"),
      div(style="display: inline-block;vertical-align:top; width: 10%;", actionButton("action2", "+", width = "50px")),
      div(style="display: inline-block;vertical-align:top; width: 75%;", sliderInput("zoom", label = NULL, min = 0, max = 2000, value = c(0, 2000), width = "100%")),
     
      hr(),
      
      plotOutput("plot1", height = "510"), 
      plotOutput("plot2", height = "510")
      )
    )
  )
)
