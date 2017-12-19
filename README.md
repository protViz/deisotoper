[![Build Status](https://travis-ci.org/protViz/deisotoper.svg)](https://travis-ci.org/protViz/deisotoper) 


# deisotoper

This package contains a Java implementation of 

* [Features Based Deisotoping Method](https://github.com/protViz/deisotoper/tree/master/java/deisotoper/src/main/java/ch/fgcz/proteomics/fbdm)


## Installation


### from [github](https://github.com/protViz/deisotoper)

Install the latest development version:

```
install.packages(c('rJava', 'devtools', 'DiagrammeR', 'lattice', 'roxygen2',
          'protViz', 'shiny', 'testthat', 'knitr'))
library(devtools)
install_git('https://github.com/protViz/deisotoper', build_vignettes = TRUE, quiet = FALSE)
```

```
sudo R CMD javareconf
```


### from [deisotoper_0.0.3.tar.gz](http://fgcz-ms.uzh.ch/~lucas/deisotoper_0.0.3.tar.gz)

Install the latest **JDK**(>= 8.0) and **R**(>=3.3). Then run in R:

```
install.packages(c('rJava', 'devtools', 'DiagrammeR', 'lattice', 'roxygen2',
          'protViz', 'shiny', 'testthat', 'knitr'))
install.packages("~/deisotoper_0.0.3.tar.gz", repos = NULL, type = "source")
```

```
sudo R CMD javareconf
```


### from [CRAN](https://CRAN.R-project.org/package=deisotoper)

Install the latest **JDK**(>= 8.0) and **R**(>=3.3). Then run in R:

```
install.packages(c('rJava', 'devtools', 'DiagrammeR', 'lattice', 'roxygen2',
          'protViz', 'shiny', 'testthat', 'knitr'))
install.packages("deisotoper")
```

```
sudo R CMD javareconf
```


## Documentation

The package ships with a package vignette `browseVignettes('deisotoper')` (>=0.0.2) and a reference manual (just type `?deisotoper` on the R shell).
