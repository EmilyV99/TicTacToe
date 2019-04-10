public class TTTBoard
{
    public static final int BLANK = 0, X = 1, O = 2, TIE = -1;
    private int board[][] = new int[3][3]; //This is the board. `0` means blank, `1` means X, `2` means O. -V
    
    public TTTBoard(){}
    private TTTBoard(int board[][])
    {
        this.board = board.clone();
    }
    
    public TTTBoard cloneBoard()
    {
        return new TTTBoard(board);
    }
    
    public int getSpace(int x, int y)
    {
        return board[x][y];
    }
    
    public void setSpace(int x, int y, int val)
    {
        board[x][y] = val;
    }
    
    /*
        Returns -1 if tie, 1 if X, 2 if O, or 0 if game is not over -V
    */
    public int checkWinner()
    {
        for(int x = 0; x < 3; ++x) //Check columns
        {
            if(board[x][0] == 0) continue;
            if(board[x][1] != board[x][0]) continue;
            if(board[x][2] != board[x][0]) continue;
            return board[x][0];
        }
        for(int y = 0; y < 3; ++y) //Check rows
        {
            if(board[0][y] == 0) continue;
            if(board[1][y] != board[0][y]) continue;
            if(board[2][y] != board[0][y]) continue;
            return board[0][y];
        }
        //Check diagonals
        if(board[0][0] != 0 && board[0][0] == board[1][1] && board[0][0] == board[2][2]) return board[0][0];
        if(board[2][0] != 0 && board[2][0] == board[1][1] && board[2][0] == board[0][2]) return board[2][0];
        
        return isFull() ? -1 : 0; //-1 if game is over in a tie, 0 if ongoing
    }
    
    public boolean isFull()
    {
        for(int x = 0; x < 3; ++x)
        {
            for(int y = 0; y < 3; ++y)
            {
                if(board[x][y] == 0) return false;
            }
        }
        return true;
    }
    
    public static int oppositeID(int ID)
    {
        if(ID == X) return O;
        else return X;
    }
}
