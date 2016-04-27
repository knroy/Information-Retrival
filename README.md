# Information-Retrival
Sorting All documents for search Query, based on TF-IDF score and cosine Similarity

#Demo Input:


3
the sun in the sky is bright
2 2 1 2 2 1 2
we can see the shining sun
2 1 2 2 2 2
the sky is blue
2 2 1 2

Now Enter Queries: (enter "#" to stop)
blue sky

#Demo Output:

DOC table with DF and IDF:
--------------------------

------------------------------------------------------------------------------------------------
      | the | sun   | in    | sky   | is    | bright | we    | can   | see   | shining | blue  |
------------------------------------------------------------------------------------------------
 DOC1 | 2   | 2     | 1     | 2     | 1     | 2      | 0     | 0     | 0     | 0       | 0     |
------------------------------------------------------------------------------------------------
 DOC2 | 2   | 2     | 0     | 0     | 0     | 0      | 2     | 1     | 2     | 2       | 0     |
------------------------------------------------------------------------------------------------
 DOC3 | 2   | 0     | 0     | 2     | 1     | 0      | 0     | 0     | 0     | 0       | 2     |
------------------------------------------------------------------------------------------------
 DF   | 3   | 2     | 1     | 2     | 2     | 1      | 1     | 1     | 1     | 1       | 1     |
------------------------------------------------------------------------------------------------
 IDF  | 0.0 | 0.585 | 1.585 | 0.585 | 0.585 | 1.585  | 1.585 | 1.585 | 1.585 | 1.585   | 1.585 |
------------------------------------------------------------------------------------------------

Now Enter Queries: (enter "#" to stop)
blue sky

Query Table:

------------------------
 Query | sky   | blue  |
------------------------
 TF    | 1     | 1     |
------------------------
 DF    | 2     | 1     |
------------------------
 IDF   | 0.585 | 1.585 |
------------------------

Normalizing:

------------

Query( sky ): 0.2925
Query( blue ): 1.585


Cosine Similarity:

(DOC1,Q) : 0.042
(DOC2,Q) : 0.0
(DOC3,Q) : 0.3622
DOC3 > DOC1 > DOC2

Enjoy!
