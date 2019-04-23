public class TTTBoard
{
    public static final int BLANK = 0, X = 1, O = 2, TIE = -1;
    public static final boolean DEBUG = false;
    private int board[][]; //This is the board. `0` means blank, `1` means X, `2` means O. -V
    private final int size, reclimit;
    private final long timeLimit;
    
    public TTTBoard(int size, int reclimit, long timeLimit)
    {
        this.board = new int[size][size];
        this.size = size;
        this.reclimit = reclimit;
        this.timeLimit = timeLimit;
    }
    
    public TTTBoard cloneBoard()
    {
        TTTBoard dupe = new TTTBoard(size, reclimit, timeLimit);
        dupe.board = new int[size][size];
        for(int x = 0; x < size; ++x)
            for(int y = 0; y < size; ++y)
                dupe.setSpace(x,y,board[x][y]);
        return dupe;
    }
    
    public int getSpace(int x, int y)
    {
        if(x >= size || y >= size) throw new TTTException("Out of Bounds button!");
        return board[x][y];
    }
    
    public void setSpace(int x, int y, int val)
    {
        if(x >= size || y >= size) throw new TTTException("Out of Bounds button!");
        board[x][y] = val;
    }
    
    public int getRecLimit()
    {
        return reclimit;
    }
    
    public long getTimeLimit()
    {
        return timeLimit;
    }
    
    /**
     * Checks if someone has won the TTT game.
     * 
     * @return      -1 if tie, 1 if X, 2 if O, 0 if game continues
     */
    public int checkWinner()
    {
        //Check columns
        for(int x = 0; x < size; ++x)
        {
            int val = board[x][0];
            if(val==0)continue;
            for(int y = 1; y < size; ++y)
            {
                if(board[x][y] != val)
                {
                    val = -1;
                    break;
                }
            }
            if(val!=-1) return val;
        }
        //Check rows
        for(int y = 0; y < size; ++y)
        {
            int val = board[0][y];
            if(val==0)continue;
            for(int x = 1; x < size; ++x)
            {
                if(board[x][y] != val)
                {
                    val = -1;
                    break;
                }
            }
            if(val!=-1) return val;
        }
        //Check diagonals
        //Up-Left to Down-Right
        int x = 1, y = 1;
        int val = board[0][0];
        if(val!=0)
        {
            while(x < size)
            {
                if(board[x][y] != val)
                {
                    val = -1;
                    break;
                }
                ++x; ++y;
            }
            if(val!=-1) return val;
        }
        //Up-Right to Down-Left
        x = size - 2;
        y = 1;
        val = board[size-1][0];
        if(val!=0)
        {
            while(y < size)
            {
                if(board[x][y] != val)
                {
                    val = -1;
                    break;
                }
                --x; ++y;
            }
            if(val!=-1) return val;
        }
        
        return isFull() ? -1 : 0; //-1 if game is over in a tie, 0 if ongoing
    }
    
    public boolean isFull()
    {
        for(int x = 0; x < size; ++x)
        {
            for(int y = 0; y < size; ++y)
            {
                if(board[x][y] == 0) return false;
            }
        }
        return true;
    }
    
    public int getSize()
    {
        return size;
    }
    
    public boolean isGuaranteedTie()
    {
        //Check columns
        for(int x = 0; x < size; ++x)
        {
            int xval = 0, oval = 0;
            for(int y = 0; y < size; ++y)
            {
                switch(board[x][y])
                {
                    case X: ++xval; break;
                    case O: ++oval; break;
                }
            }
            if(xval == 0 || oval == 0) return false;
        }
        //Check rows
        for(int y = 0; y < size; ++y)
        {
            int xval = 0, oval = 0;
            for(int x = 0; x < size; ++x)
            {
                switch(board[x][y])
                {
                    case X: ++xval; break;
                    case O: ++oval; break;
                }
            }
            if(xval == 0 || oval == 0) return false;
        }
        //Check diagonals
        //Up-Left to Down-Right
        int xval = 0, oval = 0;
        int x = 0, y = 0;
        while(x < size)
        {
            switch(board[x][y])
            {
                case X: ++xval; break;
                case O: ++oval; break;
            }
            ++x; ++y;
        }
        if(xval == 0 || oval == 0) return false;
        xval = 0; oval = 0;
        //Up-Right to Down-Left
        x = size - 1;
        y = 0;
        while(y < size)
        {
            switch(board[x][y])
            {
                case X: ++xval; break;
                case O: ++oval; break;
            }
            --x; ++y;
        }
        if(xval == 0 || oval == 0) return false;
        return true;
    }
    
    public static int oppositeID(int ID)
    {
        if(ID == X) return O;
        else return X;
    }
}
