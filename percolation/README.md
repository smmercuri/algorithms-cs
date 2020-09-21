# Percolation

Percolation is an abstract process used to model situations such as: given a porous landscape with surface water, under what conditions will water be able to drain to the bottom?

The percolation system is modelled as an *n*-by-*n* grid of sites, where a site can be one of two states -- open or closed. A system percolates if there is an open site in the bottom row than is connected by a chain of open sites to the top row. To code this efficiently we use the Union-Find data structure.

Suppose sites are indepedently set to open with a probability of *p*; for example, if *p* is 0, then the system does not percolate, whereas if *p* is 1, then it does. Turns out there exists a percolation threshold *p'* for which if *p* is less than *p'* the system hardly ever percolates, whereas if *p* is greater than *p'* the system nearly always percolates.

The program *Percolation* contains the percolation model class. The program *PercolationStats* takes two command-line arguments *n* and *T* and runs *T* independent random trials on an *n*-by-*n* grid, printing out the mean, standard deviation, and 95% confidence interval for the percolation threshold.

See the full specification at [https://coursera.cs.princeton.edu/algs4/assignments/percolation/specification.php](https://coursera.cs.princeton.edu/algs4/assignments/percolation/specification.php)
