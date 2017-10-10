library(shiny)


shinyServer(function(input, output, session) {
  values <- reactiveValues(i = 1)
  
  button1 <- eventReactive(input$load1, {
    input$text2
  })
  
  button2 <- eventReactive(input$load2, {
    input$text2
  })
  
  observe({
    input$action2
    isolate({
      values$i <- values$i + 1
    })
  })
  
  observe({
    input$action1
    isolate(if(values$i != 1) { values$i <- values$i - 1 })
  })
  
  output$num <- renderText({
    values$i
  })
  
  output$plot1 <- renderPlot({
    val <- get(load(con <- url(button1())))[[values$i]]
    plot(x = val$mZ, y = val$intensity, type = "h", xlab = "mZ", ylab = "Intensity", main = val$title)
  })
  
  output$plot2 <- renderPlot({   
    val <- get(load(con <- url(button2())))[[values$i]]
    plot(x = val$mZ, y = val$intensity, type = "h", xlab = "mZ", ylab = "Intensity", main = val$title)  
  })
}
)