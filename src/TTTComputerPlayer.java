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
		// MAKE A TURN LEFT AI 
    }
	//Take the AI's turn, using the `board` object.
	//If you need a copy of the board to modify for some reason, you can do:
	//TTTBoard tempBoard = board.cloneBoard();
	//This will give you a copy of the current state of the board.
	//`ID` is 1 or 2 (X or O); the one the AI is using
	//`opponentID` is 1 or 2 (X or O); the one the human is using
	//You can make whatever helper methods you need to accomplish this task. You can even make other classes, if you think it would help.
	//The only condition is that it all needs to run from THIS method, as though this were `main()`
	//-V
		
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
