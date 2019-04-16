
import java.util.LinkedList;

public class TTTHandler extends Thread
{
    private volatile TTTGui client;
    private TTTBoard board;
    private TTTComputerPlayer AI;
    int ID;
    private volatile LinkedList<String> instructions = new LinkedList<>();
    public TTTHandler(TTTGui client){
        this.client = client;
    }
    public void run()
    {
        try
        {
            while(!this.isInterrupted())
            {
                try
                {
                    while(board == null) takeClientTurn(false);
                    if(this.isInterrupted()) throw new InterruptedException();
                    for(boolean humanTurn = ID == TTTBoard.X; board.checkWinner()==0; humanTurn = !humanTurn) //Alternate X and O
                    {
                        if(humanTurn)
                            takeClientTurn(true);
                        else
                        {
                            if(TTTBoard.DEBUG)
                            {
                                long stime = System.currentTimeMillis();
                                AI.takeTurn();
                                System.err.println("AI turn took: " + ((System.currentTimeMillis() - stime)/1000.0) + " seconds");
                            }
                            else AI.takeTurn();
                        } 
                        client.updateButtons(board);
                    }
                    client.endGame(board.checkWinner());
                }
                catch(TTTException | InterruptedException e)
                {
                    if(!(e instanceof InterruptedException || e.getMessage().equals("Game exiting to main menu!"))) throw e;
                }
                board = null; AI = null; ID = 0; instructions.clear();
            }
        }
        catch(Exception e)
        {
            try{
                client.handleCrash();
            } catch(Exception ignored){}
            e.printStackTrace();
            System.err.println("Exiting thread...");
        }
    }
    
    private void takeClientTurn(boolean isGameTurn) throws TTTException
    {
        instructions.clear();
        if(isGameTurn) client.turnStart(true);
        while(!this.isInterrupted())
        {
            while(instructions.isEmpty()) //Wait for instructions from either side.
                try {
                    sleep((int)(1000*0.2));
                } catch (InterruptedException e) {return;}
            while(!instructions.isEmpty())
            {
                String instruction = instructions.pollFirst().toLowerCase();
                String params[] = instruction.split("\\s",2);
                instruction = params[0];
                if(params.length > 1)
                    params = params[1].split("\\s");
                else params = null;
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
                            if(isGameTurn) client.turnStart(false);
                            return;
                        }
                        break;
                    case "start":
                        if(isGameTurn) break;
                        ID = Integer.parseInt(params[0]);
                        board = new TTTBoard(Integer.parseInt(params[2]), Integer.parseInt(params[3]));
                        AI = new TTTComputerPlayer(TTTBoard.oppositeID(ID), board, params[1]);
                        return;
                }
            }
        }
    }
    
    public void addInstruction(String instruction)
    {
        instructions.add(instruction);
    }
}