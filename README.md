# deisotoper

This package contains a Java implementation of 

* [Features Based Deisotoping Method](https://github.com/protViz/deisotoper/tree/master/java/deisotoper/src/main/java/ch/fgcz/proteomics/fbdm)


## Installation



### from [github](https://github.com/protViz/deisotoper)

install the latest development version

```
install.packages(c('rJava', 'devtools'))
library(devtools)
install_git('https://github.com/protViz/deisotoper', build_vignettes = TRUE, quiet = FALSE, credentials = git2r::cred_user_pass("USERNAME", "PASSWORT"))
```

```
sudo R CMD javareconf
```

### from [fgcz-ms.uzh.ch/~lucas/deisotoper_0.0.1.tar.gz](fgcz-ms.uzh.ch/~lucas/deisotoper_0.0.1.tar.gz)

install the latest JDK and R. Then run in R:

```
install.packages(c('rJava', 'devtools', 'DiagrammeR', 'lattice', 'roxygen2', 'protViz', 'shiny', 'testthat', 'mzR', 'knitr'))
install.packages("~/deisotoper_0.0.1.tar.gz", repos = NULL, type = "source")
```

## Documentation

The package ships with a package vignette (`browseVignettes('deisotoper')` 
and a reference manual (just type `?deisotoper` on the R shell).

Both documents are also available on the package's ???  page.


## Demonstration

```{R}
TODO
```

## Related approaches

