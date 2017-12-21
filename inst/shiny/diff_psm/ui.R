#R


shinyUI(fluidPage(
  titlePanel(
    "PSM-Difference",
    windowTitle = "PSM-Difference"),
  fluidRow(
    column(
      12,
      align="left",
      hr(),
      textInput("text1", "First URL", "", width = "100%", placeholder = "Insert URL to RData-file here..."),
      textInput("text2", "Second URL", value = "", width = "100%", placeholder = "Insert URL to RData-file here..."),
      radioButtons("sort", NULL, choiceNames = c("sort first", "sort second"), choiceValues = c(1, 2), inline = TRUE),
      actionButton("load1", "LOAD & SORT"),
      hr(),
      tabsetPanel(
        tabPanel("Mass Spectrum Plots",
                 br(),
                 div(style="display: inline-block;vertical-align:top", uiOutput("slider")),
                 div(style="display: inline-block;vertical-align:top", sliderInput("itol", label = "Tolerance", min = 0, max = 10, value = 0.6, width = "400", step = 0.05)),
                 sliderInput("zoom", label = "Zoom", min = 0, max = 2000, value = c(0, 2000), width = "1900"),
                 checkboxGroupInput("check", label = NULL, inline = TRUE, choices = list("show mZ diff" = 1, "show intensity diff" = 2, "show ion diff" = 3,"show delta" = 4)),
                 textOutput("textout1"),
                 plotOutput("plot1", height = height * 0.4), 
                 textOutput("textout2"),
                 plotOutput("plot2", height = height * 0.4)),
        tabPanel("Mascot Score Plot", 
                 plotOutput("plot3", height = height * 0.6, dblclick = "dbclick", brush = brushOpts(
                   id = "brush",
                   resetOnNew = TRUE))))
      )
    )
  )
)
