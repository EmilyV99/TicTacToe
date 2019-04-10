public class TTTBoardAnalyzer
{
    private static final int INVALID = Integer.MAX_VALUE,
            XVAL = -1, TIEVAL = 0, OVAL = 1;
    
    public static int[] analyzeBoard(TTTBoard board, int ID)
    {
        return analyzeBoard(board, ID, INVALID, INVALID);
    }
    
    private static int[] analyzeBoard(TTTBoard board, int ID, int min, int max)
    {
        boolean minimize = ID == TTTBoard.X;
        int[][] heuristics = new int[3][3];
        boolean pruned = false;
        for(int x = 0; x < 3; ++x)
            for(int y = 0; y < 3; ++y)
            {
                if(board.getSpace(x,y) != TTTBoard.BLANK || pruned)
                {
                    heuristics[x][y] = INVALID;
                    continue;
                }
                boolean prune = min != INVALID || max != INVALID;
                TTTBoard temp = board.cloneBoard();
                temp.setSpace(x, y, ID);
                switch(temp.checkWinner())
                {
                    case TTTBoard.TIE:
                        if(prune)
                        {
                            if(min != INVALID && max != INVALID)
                            {
                                if(minimize ? TIEVAL > max : TIEVAL < min)
                                    pruned = true;
                                else
                                    if(minimize) max = TIEVAL;
                                    else min = TIEVAL;
                            }
                        }
                        heuristics[x][y] = TIEVAL;
                        break;
                    case TTTBoard.X:
                        if(prune)
                        {
                            if(min != INVALID && max != INVALID)
                            {
                                if(minimize ? XVAL > max : XVAL < min)
                                    pruned = true;
                                else
                                    if(minimize) max = XVAL;
                                    else min = XVAL;
                            }
                        }
                        heuristics[x][y] = XVAL;
                        break;
                    case TTTBoard.O:
                        if(prune)
                        {
                            if(min != INVALID && max != INVALID)
                            {
                                if(minimize ? OVAL > max : OVAL < min)
                                    pruned = true;
                                else
                                    if(minimize) max = OVAL;
                                    else min = OVAL;
                            }
                        }
                        heuristics[x][y] = OVAL;
                        break;
                    case TTTBoard.BLANK:
                        heuristics[x][y] = analyzeBoard(temp, TTTBoard.oppositeID(ID), minimize ? INVALID : min, minimize ? max : INVALID)[2];
                        if(prune)
                        {
                            if(min != INVALID && max != INVALID)
                            {
                                if(minimize ? heuristics[x][y] > max : heuristics[x][y] < min)
                                    pruned = true;
                                else
                                    if(minimize) max = heuristics[x][y];
                                    else min = heuristics[x][y];
                            }
                        }
                        break;
                }
            }
        
        int[] data = new int[3];
        data[3] = INVALID;
        for(int x = 0; x < 3; ++x)
            for(int y = 0; y < 3; ++y)
            {
                if(heuristics[x][y] == INVALID) continue;
                if(data[2] == INVALID)
                {
                    data[0] = x;
                    data[1] = y;
                    data[2] = heuristics[x][y];
                }
                else if(minimize ? heuristics[x][y] < data[2] : heuristics[x][y] > data[2])
                {
                    data[0] = x;
                    data[1] = y;
                    data[2] = heuristics[x][y];
                }
            }
        
        return data;
    }
}