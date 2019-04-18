public class TTTBoardAnalyzer
{
    private static final int INVALID = Integer.MAX_VALUE,
            XVAL = -1000, TIEVAL = 0, OVAL = 1000;
    
    public static int[] analyzeBoard(TTTBoard board, int ID)
    {
        return analyzeBoard(board, ID, XVAL, OVAL, 0, System.currentTimeMillis(), board.isGuaranteedTie());
    }
    
    private static int[] analyzeBoard(TTTBoard board, int ID, int min, int max, int recDepth, long starttime, boolean isTie)
    {
        if(Thread.interrupted()) throw new TTTException("Game exiting to main menu!");
        boolean minimize = ID == TTTBoard.X;
        int size = board.getSize();
        int[][] heuristics = new int[size][size];
        boolean pruned = false, foundOne = false;
        //System.out.println(isTie);
        for(int x = 0; x < size; ++x)
            for(int y = 0; y < size; ++y)
            {
                if(board.getSpace(x,y) != TTTBoard.BLANK || pruned || (isTie && foundOne))
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
                                if(minimize ? TIEVAL < max : TIEVAL > min)
                                    if(minimize)
                                        max = TIEVAL;
                                    else min = TIEVAL;
                                if(min >= max)
                                    pruned = true;
                            }
                        }
                        heuristics[x][y] = TIEVAL;
                        foundOne = true;
                        break;
                    case TTTBoard.X:
                        if(prune)
                        {
                            if(min != INVALID && max != INVALID)
                            {
                                if(minimize ? XVAL < max : XVAL > min)
                                    if(minimize)
                                        max = XVAL;
                                    else min = XVAL;
                                if(min >= max)
                                    pruned = true;
                            }
                        }
                        heuristics[x][y] = XVAL;
                        foundOne = true;
                        break;
                    case TTTBoard.O:
                        if(prune)
                        {
                            if(min != INVALID && max != INVALID)
                            {
                                if(minimize ? OVAL < max : OVAL > min)
                                    if(minimize)
                                        max = OVAL;
                                    else min = OVAL;
                                if(min >= max)
                                    pruned = true;
                            }
                        }
                        heuristics[x][y] = OVAL;
                        foundOne = true;
                        break;
                    case TTTBoard.BLANK:
                        if(recDepth <= board.getRecLimit() && (System.currentTimeMillis() - starttime) < board.getTimeLimit() && !isTie)
                            heuristics[x][y] = analyzeBoard(temp, TTTBoard.oppositeID(ID), minimize ? XVAL : min, minimize ? max : OVAL, recDepth + 1, starttime, temp.isGuaranteedTie())[2];
                        else
                            heuristics[x][y] = quickAnalyze(temp);
                        if(prune)
                        {
                            if(min != INVALID && max != INVALID)
                            {
                                if(minimize ? heuristics[x][y] < max : heuristics[x][y] > min)
                                    if(minimize)
                                        max = heuristics[x][y];
                                    else min = heuristics[x][y];
                                if(min >= max)
                                    pruned = true;
                            }
                        }
                        foundOne = true;
                        break;
                }
            }
        
        int[] data = new int[3];
        data[2] = INVALID;
        for(int x = 0; x < size; ++x)
            for(int y = 0; y < size; ++y)
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
    
    /**
     * Evaluates the position of a given board.
     * 
     * @param board
     * @return 
     */
    private static int quickAnalyze(TTTBoard board)
    {
        int size = board.getSize();
        int retval = 0;
        //Check columns
        for(int x = 0; x < size; ++x)
        {
            int xval = 0, oval = 0, bval = 0;
            for(int y = 0; y < size; ++y)
            {
                switch(board.getSpace(x, y))
                {
                    case TTTBoard.X: ++xval; break;
                    case TTTBoard.O: ++oval; break;
                    case TTTBoard.BLANK: ++bval; break;
                }
            }
            retval += checkVal(oval, xval, bval);
        }
        //Check rows
        for(int y = 0; y < size; ++y)
        {
            int xval = 0, oval = 0, bval = 0;
            for(int x = 0; x < size; ++x)
            {
                switch(board.getSpace(x, y))
                {
                    case TTTBoard.X: ++xval; break;
                    case TTTBoard.O: ++oval; break;
                    case TTTBoard.BLANK: ++bval; break;
                }
            }
            retval += checkVal(oval, xval, bval);
        }
        //Check diagonals
        //Up-Left to Down-Right
        int xval = 0, oval = 0, bval = 0;
        int x = 0, y = 0;
        while(x < size)
        {
            switch(board.getSpace(x, y))
            {
                case TTTBoard.X: ++xval; break;
                case TTTBoard.O: ++oval; break;
                case TTTBoard.BLANK: ++bval; break;
            }
            ++x; ++y;
        }
        retval += checkVal(oval, xval, bval);
        xval = 0; oval = 0; bval = 0;
        //Up-Right to Down-Left
        x = size - 1;
        y = 0;
        while(y < size)
        {
            switch(board.getSpace(x, y))
            {
                case TTTBoard.X: ++xval; break;
                case TTTBoard.O: ++oval; break;
                case TTTBoard.BLANK: ++bval; break;
            }
            --x; ++y;
        }
        retval += checkVal(oval, xval, bval);
        return retval;
    }
    
    private static int checkVal(int oval, int xval, int bval)
    {
        //If the line is not blocked for the given player, give it additional weighting.
        if(oval == 0) return -5 * xval;
        if(xval == 0) return 5 * oval;
        return (oval - xval);
    }
}