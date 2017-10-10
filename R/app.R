library(shiny)

ui <- fluidPage(
  titlePanel("PSM-Difference"),
  sidebarLayout(
    sidebarPanel(textInput("text1", "URL1", "http://fgcz-ms.uzh.ch/~cpanse/R257544/TP_HeLa_200ng_msdeconv.RData"),
                 actionButton("load1", "load"),
                 br(),
                 br(),
                 textInput("text2", "URL2", "http://fgcz-ms.uzh.ch/~cpanse/R257544/TP_HeLa_200ng_msdeconv.RData"),
                 actionButton("load2", "load"),
                 br(),
                 br(),
                 actionButton("action", "+")),
    mainPanel(plotOutput("plot1"), plotOutput("plot2"))
  )
)

server <- function(input, output, session) {
  values <- reactiveValues(n = 0)
  
  button1 <- eventReactive(input$load1, {
    input$text2
  })
  
  button2 <- eventReactive(input$load2, {
    input$text2
  })
  
  observe({
    input$action
    isolate({
      values$n <- values$n + 1
    })
  })
  
  output$plot1 <- renderPlot({
    val <- get(load(con <- url(button1())))[[values$n]]
    plot(x = val$mZ, y = val$intensity, type = "h", xlab = "mZ", ylab = "Intensity", main = val$title)
  })
  
  output$plot2 <- renderPlot({   
    val <- get(load(con <- url(button2())))[[values$n]]
    plot(x = val$mZ, y = val$intensity, type = "h", xlab = "mZ", ylab = "Intensity", main = val$title)  
  })
}

shinyApp(ui = ui, server = server)