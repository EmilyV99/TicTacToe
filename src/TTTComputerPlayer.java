public class TTTComputerPlayer
{
    private final int ID, size;
    private TTTBoard board;
    private byte difficulty;

    public TTTComputerPlayer(int ID, TTTBoard board, String diff)
    {
        this.ID = ID;
        this.board = board;
        this.size = board.getSize();
        switch(diff.toLowerCase())
        {
            case "easy":
                difficulty = 40;
                break;
            case "normal":
                difficulty = 60;
                break;
            case "hard":
                difficulty = 80;
                break;
            case "impossible":
                difficulty = 100;
                break;
        }
    }

    public void takeTurn()
    {
        if(((int)(Math.random() * 100) + 1) <= difficulty)
        {
            int[] data = TTTBoardAnalyzer.analyzeBoard(board.cloneBoard(), ID);
            board.setSpace(data[0], data[1], ID);
        }
        else
        {
            int numblanks = 0;
            for(int x = 0; x < size; ++x)
                for(int y = 0; y < size; ++y)
                    if(board.getSpace(x,y) == TTTBoard.BLANK) ++numblanks;
            int choice = ((int)(Math.random() * numblanks) + 1);
            numblanks = 0;
            for(int x = 0; x < size; ++x)
                for(int y = 0; y < size; ++y)
                {
                    if(board.getSpace(x,y) == TTTBoard.BLANK)
                    {
                        ++numblanks;
                        if(numblanks == choice)
                        {
                            board.setSpace(x, y, ID);
                            return;
                        }
                    }
                }
        }
    }
		
    //Driver method. This will not exist in the final form of the project.
    //Running this will pit 2 AIs against each other, since there is no coded human input at this time.
    //You can modify this however you like, this is just an example test.
    //When the AI is done (save for difficulty settings) this should ALWAYS end in a tie!
    //-V
    /*
    public static void main(String[] args)
    {
        TTTBoard theboard = new TTTBoard();
        TTTComputerPlayer AI1 = new TTTComputerPlayer(1, theboard);
        TTTComputerPlayer AI2 = new TTTComputerPlayer(2, theboard);
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
    }*/
}
