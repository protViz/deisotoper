# deisotoper

This package contains a Java implementation of 

* [Features Based Deisotoping Method](https://github.com/protViz/deisotoper/tree/master/java/deisotoper/src/main/java/ch/fgcz/proteomics/fbdm)
* [mMass](http://www.mmass.org/) (mspy)


## Installation



### from [github](https://github.com/protViz/deisotoper)

Install the latest development version:

```
install.packages(c('rJava', 'devtools'))
library(devtools)
install_git('https://github.com/protViz/deisotoper', build_vignettes = TRUE, quiet = FALSE, credentials = git2r::cred_user_pass("USERNAME", "PASSWORT"))
```

```
sudo R CMD javareconf
```

### from [deisotoper_0.0.1.tar.gz](http://fgcz-ms.uzh.ch/~lucas/deisotoper_0.0.1.tar.gz)

Install the latest **JDK** and **R**. Then run in R:

```
install.packages(c('rJava', 'devtools', 'DiagrammeR', 'lattice', 'roxygen2', 'protViz', 'shiny', 'testthat', 'mzR', 'knitr'))
install.packages("~/deisotoper_0.0.1.tar.gz", repos = NULL, type = "source")
```

## Documentation

The package ships with a package vignette `browseVignettes('deisotoper')` and a reference manual (just type `?deisotoper` on the R shell).

Both documents are also available on the package's ???  page.


## Demonstration

```{R}
# Creation of MSM Java-Object
load(system.file("extdata", name = 'TP_HeLa_200ng_filtered_pd21.RData', package = "deisotoper"))
MSM <- jCreateMSM(TP_HeLa_200ng_filtered_pd21)

# Creation of properties file
CreateProperties("test.properties")
AddToProperties("test.properties", key = "F1", value = 1.0)
AddToProperties("test.properties", key = "F2", value = 0.3)
AddToProperties("test.properties", key = "F3", value = 0.15)
AddToProperties("test.properties", key = "DELTA", value = 0.004)
AddToProperties("test.properties", key = "GLY", value = 57.02146)
AddToProperties("test.properties", key = "HIS", value = 137.05891)
AddToProperties("test.properties", key = "ILE", value = 113.08406)
AddToProperties("test.properties", key = "LYS", value = 128.09496)

# Deisotoping process
MSMd <- jDeisotopeMSM(MSM, configfile = "test.properties")

# Save the deisotoped MSM on the disk 
jWriteMGF("test.mgf", MSMd)
```


## Related approaches

