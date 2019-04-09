public class TTTBoardAnalyzer
{
    /**
     * Scans a TTTBoard, and analyzes the position of each player.
     * 
     * @param board     The board to analyze
     * @return          An integer value representing the advantage. Negative is good for 'X', positive good for 'O'.
     */
    public static int boardHeuristic(TTTBoard board)
    {
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
        if(xcount == 3) return -1000;
        if(ocount == 3) return 1000;
        if(bcount == 3) return 0;
        //
        if(bcount == 1)
        {
            //2 are the same, the third is blank
            if(xcount == 2) return -10;
            if(ocount == 2) return 10;
            //All 3 are different
            return 0; //Equal position for both sides, so net 0
        }
        //1 and 2 blanks
        if(bcount == 2)
        {
            if(xcount == 1) return -4;
            else return 4;
        }
        //2 of one, blocked by the other
        if(xcount == 2 && ocount == 1) return 5;
        if(!(ocount == 2 && xcount == 1)) throw new RuntimeException("Whoops! Unhandled case! (" + a + " " + b + " " + c + ")");
        return -5;
    }
}