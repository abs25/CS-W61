package edu.calvin.csw61.finalproject.playercommands;

import edu.calvin.csw61.finalproject.ApertureBehavior;
import edu.calvin.csw61.finalproject.Door;
import edu.calvin.csw61.finalproject.Player;
import edu.calvin.csw61.finalproject.Room;
import edu.calvin.csw61.finalproject.WallBehavior;

/**
 * Walk allows a Player to walk somewhere.
 * (Implements the Command Interface).
 */
public class Walk implements Command{
	
	//Direction to walk in
	private String myDirection;
	//Handle to the Player
	private Player myPlayer;
	//Result of executing the Command
	private String result;
	
	/**
	 * Constructor for the Walk class.
	 * @param: p, a handle to the Player.
	 * @param: direction, a String representing the direction to walk in.
	 */
	public Walk(String direction, Player player){
		myDirection = direction.toLowerCase();  //Lower case it 
		myPlayer = player;
		result = "";
	}

	/**
	 * execute() executes the act of walking in the game.
	 */
	@Override
	public void execute() {
		//Check if we are outside...
		for(int i = 0; i < myPlayer.getRoom().getAperatures().size(); i++) {
			//If the AperatureBehavior is a Door...
			if(myPlayer.getRoom().getAperatures().get(i).getName().equals("door")) {  
				//Typecast to a Door
				Door checker = (Door)myPlayer.getRoom().getAperatures().get(i);
				//Check if the Door is equal to the Player's direction...
				//(THIS WAS WHERE THAT STUPID BUG WAS!)
				//(WE HAD TO FIRST CHECK IF THE DIRECTION OF THE APERTURE
				// WAS EQUAL TO THE DIRECTION OF THE PLAYER. THEN CHECK IF 
				// THE DOOR WAS LOCKED IF IT WAS A DOOR. WE DID THE 
				// OPPOSITE AND SO THAT WAS WHY THE DOOR LOGIC WAS GETTING FUZZY).
				if(checker.getDir().equals(myDirection)) {
					if(!checker.isLocked()) {
						//Unlocked
						//Go through the Door.
						checker.goThrough(myPlayer); 
						//put the room description in result
						result = myPlayer.getRoom().getDescriptor(); 
						//Show ObjectInterfaces and People
						result += myPlayer.getRoom().showObjects();
						result += myPlayer.getRoom().showPeople();
						//Only go through the Door once.
						break; 
					} else {
						result += "The door is locked. Do you have a key?";
						break;
					}
				}
			} else {  //Check for a wall...
				//Typecast to a Wall
				WallBehavior checker2 = (WallBehavior)myPlayer.getRoom().getAperatures().get(i);  
				//If the Wall's direction equals the Player's Direction...
				if(checker2.getDir().equals(myDirection)) {  
					result += "You can't go that way";
					break;
				}
			}
		}
	}
	
	/**
	 * Accessor for the result of executing the command.
	 */
	public String getResult() {
		return result;
	}
}