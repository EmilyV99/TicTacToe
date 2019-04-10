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
        int[] data = TTTBoardAnalyzer.analyzeBoard(board, ID);
        board.setSpace(data[0], data[1], ID);
    }
		
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
