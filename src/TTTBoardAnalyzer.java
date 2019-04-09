public class TTTBoardAnalyzer
{
    /**
     * Values for certain row combinations.
     */
    private static final int
            OOO = 1000, //Victory
            OOB = 10, //2, and a blank
            OOX = -5, //2, and one of the other
            OBB = 4, //One, and 2 blanks
            OBX = 0, //One of each
            BBB = 0; //Blank
    
    /**
     * Scans a TTTBoard, and analyzes the position of each player.
     * 
     * @param board     The board to analyze
     * @return          An integer value representing the advantage. Negative is good for 'X', positive good for 'O'.
     */
    public static int boardHeuristic(TTTBoard board)
    {
        int winner = board.checkWinner();
        if(winner != 0)
        {
            switch(winner)
            {
                case TTTBoard.X:
                    return -OOO;
                    
                case TTTBoard.O:
                    return OOO;
                    
                default:
                    return BBB;
            }
        }
        int totalValue = 0;
        //Rows
        totalValue += scanOneRow(board.getSpace(0, 0), board.getSpace(1, 0), board.getSpace(2, 0));
        totalValue += scanOneRow(board.getSpace(0, 1), board.getSpace(1, 1), board.getSpace(2, 1));
        totalValue += scanOneRow(board.getSpace(0, 2), board.getSpace(1, 2), board.getSpace(2, 2));
        //Columns
        totalValue += scanOneRow(board.getSpace(0, 0), board.getSpace(0, 1), board.getSpace(0, 2));
        totalValue += scanOneRow(board.getSpace(1, 0), board.getSpace(1, 1), board.getSpace(1, 2));
        totalValue += scanOneRow(board.getSpace(2, 0), board.getSpace(2, 1), board.getSpace(2, 2));
        //Diagonals
        totalValue += scanOneRow(board.getSpace(0, 0), board.getSpace(1, 1), board.getSpace(2, 2));
        totalValue += scanOneRow(board.getSpace(2, 0), board.getSpace(1, 1), board.getSpace(0, 2));
        return totalValue;
    }
    
    /**
     * Scans a single row of the board.
     * 
     * @param a     First letter
     * @param b     Second letter
     * @param c     Third letter
     * @return      An integer value representing the advantage. Negative is good for 'X', positive is good for 'O'
     */
    private static int scanOneRow(int a, int b, int c)
    {
        //Count the spaces of each type. Position doesn't matter, as swapped positions within a single row have the same value. -V
        int xcount = (a == TTTBoard.X ? 1 : 0) + (b == TTTBoard.X ? 1 : 0) + (c == TTTBoard.X ? 1 : 0);
        int ocount = (a == TTTBoard.O ? 1 : 0) + (b == TTTBoard.O ? 1 : 0) + (c == TTTBoard.O ? 1 : 0);
        int bcount = 3 - (xcount + ocount);
        //All 3 are the same
        if(xcount == 3) return -OOO;
        if(ocount == 3) return OOO;
        if(bcount == 3) return BBB;
        //
        if(bcount == 1)
        {
            //2 are the same, the third is blank
            if(xcount == 2) return -OOB;
            if(ocount == 2) return OOB;
            //All 3 are different
            return OBX; //Equal position for both sides, so net 0
        }
        //1 and 2 blanks
        if(bcount == 2)
        {
            if(xcount == 1) return -OBB;
            else return OBB;
        }
        //2 of one, blocked by the other
        if(xcount == 2 && ocount == 1) return -OOX;
        if(!(ocount == 2 && xcount == 1)) throw new RuntimeException("Whoops! Unhandled case! (" + a + " " + b + " " + c + ")");
        return -OOX;
    }
}