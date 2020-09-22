# 8 Puzzle

8 puzzle is a sliding puzzle played on a 3-by-3 grid. It contains 8 square tiles numbered 1 through 8 in some random order and one blank square. The task is to arrange the tiles so that they are in order (telephone key pad order, top row 1-2-3, then 4-5-6, then 7-8, with the blank square in place of 9). You can move a tile only horizontally or vertically into the blank square.

In this code, we implement the A*-search algorithm to solve the puzzle, and generalisations to larger *n*-by-*n* grids. In *Board* we implement the board data type to represent the 8-puzzle grid.

In *Solver* we implement the A*-search algorithm.
