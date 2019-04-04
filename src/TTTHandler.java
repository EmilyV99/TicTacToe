
import java.util.LinkedList;

public class TTTHandler extends Thread
{
    TTTGui client;
    TTTBoard board;
    int ID;
    volatile LinkedList<String> instructions = new LinkedList<>();
    public TTTHandler(TTTGui client){
        this.client = client;
    }
    public void run()
    {
        takeClientTurn();
        for(int curID = 1; board.checkWinner()==0; curID = (curID % 2) + 1) //Alternate 1 and 2
        {
            if(this.ID == curID)
                takeClientTurn();
            else ;//Take AI turn
            if(board.checkWinner()!=0)break;
            if(this.ID != curID)
                takeClientTurn();
            else ;//Take AI turn
        }
        client.endGame(board.checkWinner());
        board = null; ID = 0; instructions.clear();
    }
    
    private void takeClientTurn()
    {
        instructions.clear();
        client.turnStart(true);
        while(true)
        {
            while(instructions.isEmpty()) //Wait for instructions from either side.
                try {
                    sleep(1000*5);
                } catch (InterruptedException ignored) {}
            while(!instructions.isEmpty())
            {
                String instruction = instructions.pollFirst().toLowerCase();
                String params[] = instruction.split("\\s",2);
                instruction = params[0];
                params = params[1].split("\\s");
                //`instruction` is the head command
                //`params` are the sub-arguments
                switch(instruction)
                {
                    case "place":
                        if(board==null)break; //No placing when no board exists
                        int x = Integer.parseInt(params[0]);
                        int y = Integer.parseInt(params[1]);
                        if(board.getSpace(x, y)==0)
                        {
                            board.setSpace(x,y,ID);
                            client.turnStart(false);
                            return;
                        }
                        break;
                    case "start":
                        ID = Integer.parseInt(params[0]);
                        board = new TTTBoard();
                        //Add AI client, with opposing ID
                        //opposingID = 3 - ID;
                        client.turnStart(false);
                        return;

                }
            }
        }
    }
}