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
      textInput("text1", "First URL", "http://fgcz-ms.uzh.ch/~cpanse/R257544/F257664.RData", width = "100%"),
      textInput("text2", "Second URL", value = "http://fgcz-ms.uzh.ch/~cpanse/R257544/F257544.RData", width = "100%"),
      radioButtons("sort", NULL, choiceNames = c("sort first", "sort second"), choiceValues = c(1, 2), inline = TRUE),
      actionButton("load1", "LOAD & SORT", style="padding:8px;font-size:18px;"),
      tabsetPanel(
        tabPanel("Mass Spectrum Plots",
                 br(),
                 div(style="display: inline-block;vertical-align:top; width: 8%;", actionButton("action1", NULL, width = "50px", icon = icon("glyphicon glyphicon-minus"))),
                 div(style="display: inline-block;vertical-align:top;font-size:2em;", textOutput("num"), width = "40px"),
                 div(style="display: inline-block;vertical-align:top; width: 8%;", actionButton("action2", NULL, width = "50px", icon = icon("glyphicon glyphicon-plus"))),
                 div(style="display: inline-block;vertical-align:top; width: 75%;", sliderInput("zoom", label = NULL, min = 0, max = 2000, value = c(0, 2000), width = "90%")),
                 checkboxGroupInput("check", label = NULL, inline = TRUE, choices = list("show mZ diff" = 1, "show intensity diff" = 2), selected = 1),
                 plotOutput("plot1", height = "510"), 
                 plotOutput("plot2", height = "510")),
        tabPanel("Mascot Score Plot", 
                 plotOutput("plot3", height = "600", dblclick = "dbclick", brush = brushOpts(
                   id = "brush",
                   resetOnNew = TRUE))))
      )
    )
  )
)
