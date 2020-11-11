# WordNet

WordNet is a semantic lexicon for the English language (often used by computational linguists). It groups words into sets of synonyms called *synsets* and it describes relationships between synsets. For example, the *is-a* relationship which connects a more specific synset (called a *hyponym*) to a more general synset (called a *hypernym*). For example, the synset {action} is a hyponym of the synset {act, human_action, human_activity}. Or {dash, sprint} is a hyponym of {run, running}.

Here we represent WordNet as a digraph in which each vertex *v* is an integer that represents a synset, and each directed edge *v->w* represents the relationship that *w* is a hypernym of *v* (this is not necessarily a tree). Closeness in this digraph gives a notion of relatedness between synsets, that is, closer synsets will be closer related in meaning that those further apart. This data type is created by reading in two text files *synsets.txt* and *hypernyms.txt* to give the synsets and relationships respectively. This data is created in the file *WordNet*.

The aim here is to consider two things – shortest ancestral paths and outcast detection. Given any two nouns, nounA and nounB, we would like to find the shortest ancestral path (SAP) between the synsets containing these nouns. The SAP is the shortest path from nounA to nounB that passes through a common ancestor (this will be a path, but not a directed path). We generalise this notion to find the SAP between two sets of nouns. This is done in *SAP*.

Finally, we do outcast detection. Given a list of WordNet nouns, which noun is least related to the others? For this we define a distance function based on the shortest ancestral path of two nouns. For each word in the list, we sum up all of its distances to the other words. The word that has the largest such sum is the least related. This is done in *Outcast*.

See the full specification and definitions at [https://coursera.cs.princeton.edu/algs4/assignments/wordnet/specification.php](https://coursera.cs.princeton.edu/algs4/assignments/wordnet/specification.php)
