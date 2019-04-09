public class TTTComputerPlayer
{
    private final int ID;
    private final int opponentID;
    private final TTTBoard board;

    public TTTComputerPlayer(int ID, int opponentID, TTTBoard board)
    {
        this.ID = ID;
        this.opponentID = opponentID;
        this.board = board;
    }

    public void takeTurn()
    {
        boolean minimize = ID == TTTBoard.X;
        int[][] heuristics = new int[3][3];
        int bestSoFar = Integer.MAX_VALUE;
        for(int x = 0; x < 3; ++x)
            for(int y = 0; y < 3; ++y)
            {
                if(board.getSpace(x,y) == TTTBoard.BLANK) continue;
                TTTBoard temp = board.cloneBoard();
                temp.setSpace(x, y, ID);
                //Alpha-beta pruning needs to go here?
                heuristics[x][y] = analyzeBoard(temp, TTTBoard.oppositeID(ID));
                if(bestSoFar == Integer.MAX_VALUE) bestSoFar = heuristics[x][y];
                else
                {
                    if(minimize)
                        if(bestSoFar < heuristics[x][y])
                }
            }
        int best = 0, bestx = -1, besty = -1;
        for(int x = 0; x < 3; ++x)
            for(int y = 0; y < 3; ++y)
            {
                if(board.getSpace(x,y) == TTTBoard.BLANK) continue;
                if(heuristics[x][y] == Integer.MAX_VALUE) continue; //Alpha-beta pruned spot
                if(bestx == -1) //Start at the first blank space, in case there is only 1!
                {
                    best = heuristics[x][y];
                    bestx = x;
                    besty = y;
                    continue;
                }
                if(minimize)
                {
                    if(heuristics[x][y] < best)
                    {
                        best = heuristics[x][y];
                        bestx = x;
                        besty = y;
                    }
                }
                else
                {
                    if(heuristics[x][y] > best)
                    {
                        best = heuristics[x][y];
                        bestx = x;
                        besty = y;
                    }
                }
            }
        board.setSpace(bestx, besty, ID);
    }
    
    /* This is wrong and needs to be totally redone. Need to re-evaluate how I do this. -V
    private int analyzeBoard(TTTBoard board, int ID)
    {
        //Starts just after the AI has moved, calculating the best move for the opponent. Returns a heuristic total for this board state, to the end of the game. 
        boolean minimize = ID == TTTBoard.X;
        int[][] heuristics = new int[3][3];
        for(int x = 0; x < 3; ++x)
            for(int y = 0; y < 3; ++y)
            {
                if(board.getSpace(x,y) == TTTBoard.BLANK) continue;
                TTTBoard temp = board.cloneBoard();
                temp.setSpace(x, y, ID);
                //Alpha-beta pruning needs to go here?
                heuristics[x][y] = analyzeBoard(temp, TTTBoard.oppositeID(ID));
            }
        
    }
    
    private int analyzeBoard(TTTBoard board, int ID, int max, int min)
    {
        boolean minimize = ID == TTTBoard.X;
         
    }*/
		
    //Driver method. This will not exist in the final form of the project.
    //Running this will pit 2 AIs against each other, since there is no coded human input at this time.
    //You can modify this however you like, this is just an example test.
    //When the AI is done (save for difficulty settings) this should ALWAYS end in a tie!
    //-V
    public static void main(String[] args)
    {
        TTTBoard theboard = new TTTBoard();
        TTTComputerPlayer AI1 = new TTTComputerPlayer(1, 2, theboard);
        TTTComputerPlayer AI2 = new TTTComputerPlayer(2, 1, theboard);
        while(theboard.checkWinner() == 0)
        {
            AI1.takeTurn();
            if(theboard.checkWinner() == 0)
                AI2.takeTurn();
        }
        int win = theboard.checkWinner();
        if(win < 0)
                System.out.println("Tie! No one wins!");
        else System.out.println("The winner is: " + win);
    }
}
