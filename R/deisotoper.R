#R

# author: Lucas Schmidt
# date: 2017-08-15

library(rJava)
library(protViz)
.jinit()
.jaddClassPath("/srv/lucas1/eclipse-workspace/deisotoper/bin")
.jclassPath()
findpattern <- .jnew("findpattern")

# Loading Data
url386248 <- "http://fgcz-ms.uzh.ch/~cpanse/data/386248.RData"
con <- url(url386248)
load(con)
S <- as.psmSet(F225712)

# Data
mZ <- S[[1]]$mZ
pattern <- c(124.3, 149.5, 251.4)
eps <- 0.5

# Running Java-Programm with data
.jcall(findpattern, "V", "setmZ", mZ)
.jcall(findpattern, "V", "setpattern", pattern)
.jcall(findpattern, "V", "seteps", eps)
index <- .jcall(findpattern, "[D", "getIndex")

# Print result
print(index)
