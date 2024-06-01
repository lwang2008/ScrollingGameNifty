import java.util.Random;


public class Game

{
  private Grid grid;
  private int userRow;
  private int msElapsed;
  private int timesGet;
  private int timesAvoid;

 
  public Game()
  {
    grid = new Grid(5, 10);
    userRow = 0;
    msElapsed = 0;
    timesGet = 0;
    timesAvoid = 0;
    updateTitle();
    grid.setImage(new Location(userRow, 0), "user.gif");
  }

  public void play()
  {
    while (!isGameOver())
    {
      Grid.pause(100);
      handleKeyPress();
      if (msElapsed % 300 == 0)
      {
        scrollLeft();
        populateRightEdge();
      }
      updateTitle();
      msElapsed += 100;
    }
  }
  
  public void handleKeyPress()
  {
    int key = grid.checkLastKeyPressed();
    Location current =new Location(userRow, 0);
    if(key==40 && userRow !=grid.getNumRows()-1){
        userRow+=1;                                                 //increments userRow by 1
        Location newLocation = new Location(userRow, 0);         //creates new Location with incremented userRow
        handleCollision(newLocation);
        grid.setImage(current, null);                //sets current position as null
        grid.setImage(newLocation, "user.gif");      //puts the user gif at the new position
    }
    else if(key==38 && userRow !=0){    
        userRow-=1;                                               //decreases userRow by 1
        Location newLocation = new Location(userRow, 0);        //creates new Location with decremented userRow
        handleCollision(newLocation);
        grid.setImage(current, null);                //deletes user.gif is at current position
        grid.setImage(newLocation, "user.gif");      //sets user.gif to new position
        
    }

  }
  
  public void populateRightEdge()
  {
    Random x = new Random();
    int col = grid.getNumCols()-1;
	  int ran = x.nextInt(4);     //generates random number from 1 to 3
	  if (ran==1) {
		  int row = x.nextInt(grid.getNumRows());     
		  Location spawn = new Location(row, col);          
		  grid.setImage(spawn, "avoid.gif");      //spawns avoid.gif at a random row, in the lsat column
	  }
	  if (ran==2) {
		  int row = x.nextInt(grid.getNumRows());
		  Location spawn1 = new Location(row, col);             //spawns get.gif at a random row, in the last column
		  grid.setImage(spawn1, "get.gif");
	  }
	  if (ran==3) {
		  int row = x.nextInt(grid.getNumRows());
      int row1 = x.nextInt(grid.getNumRows());     
		  Location spawn = new Location(row, col);     
      Location spawn1 = new Location(row1, col);     
		  grid.setImage(spawn, "avoid.gif");
		  grid.setImage(spawn1, "get.gif");       //spawns both avoid.gif and get.gif at seperate ranodm rows, in the last column

	  }
  }
  
  public void scrollLeft()
  {
    int col = grid.getNumCols();
    int row = grid.getNumRows();
    Location user = new Location(userRow, 0);
    for (int y = 0; y<row; y++) {
      for (int x = 0; x<col; x++) {                                    //iterates through entire grid
        Location before = new Location(y, x);                          
        if (!before.equals(user)) {                                    //checks if the current position is not equal to the location of user
        Location after = new Location(y, x-1);                         //creates new Location that is the old one shifted one left
          if (x<1) {
            grid.setImage(before, null);                 //if image is at leftmost column already, delete it
          } 
          else if (!before.equals(new Location(userRow, 1))) {        
            grid.setImage(after, grid.getImage(before));               //shifts image one left
          }
          if (x==col-1) {
            grid.setImage(before, null);                 //clear last column
          }
        }
        if (before.equals(new Location(userRow, 1))){                //handleCollision if position in question is at the location of user.gjf
          handleCollision(before);
        }
      }
    }
  }
    public void handleCollision(Location loc)
  {
    if (grid.getImage(loc)=="get.gif"){
        timesGet++;                                                     //increase timeGet if the image at the location is get.gif
    }
    if (grid.getImage(loc)=="avoid.gif"){                               //increase timesAvoid if the image at the location is avoid.gif
      timesAvoid++;
    }
    grid.setImage(loc, null);
  }
  
  public int getScore()
  {
    return timesGet - timesAvoid;                                        //score is timesGet - timesAvoid
  }
  
  public void updateTitle()
  {
    grid.setTitle("Game:  " + getScore());
  }
  
  public boolean isGameOver()
  {
    if (timesAvoid > 5){
      System.out.println("Game over! You scored: " + getScore());        //game over if player hits avoid.gif more than 5 times
      return true;
    }
    else if(timesGet >20){                                                // game over and player wins if they reach a score of 20 
      System.out.println("You reached a score of 20 and won!");
      grid.setTitle("YOU WIN!");
      return true;
    }
    return false;
  }
  
  public static void test()
  {
    Game game = new Game();
    game.play();
  }
  
  public static void main(String[] args)
  {
    Game.test();
  }
}