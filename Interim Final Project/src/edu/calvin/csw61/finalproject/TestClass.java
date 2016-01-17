/**
 * We implement these patterns:
 * 	- Strategy
 * 	- Command
 *  - Singleton? (For the Map and Player).
 * 
 * Making a Map:
 * 	- Do it in the TestClass; It makes it easier to test things out and not bloat the Driver.
 *  - Notes:
 *     + If a Player is Outside, they are in the outsideRoom whose Room number is -2.
 *     + If a Player goes inside of a HashMap of Rooms from the Outside,
 *       there MUST be two Doors for the first Room of the HashMap (Room 0):
 *       One for the direction of the HashMap of Rooms (north, south, east, or west)
 *       and a second for the direction of the Outside from the perspective of the
 *       HashMap of Rooms. (Example: At the start of the game, the Player is Outside.
 *       North Hall is north of the Player. When the Player types "walk north",
 *       then they should be in the first room of North Hall. If the Player types
 *       "walk south" while in the first Room of the Science Building, then 
 *       he/she should be back Outside.)
 *     + If a Room goes to an adjacent Room, the adjacent Room must have a Door
 *       in the direction of the previous Room. (Example: If a Player is in the first
 *       Room of the Science Building and there is a Room to the east, when the Player
 *       types "walk east" then he/she should be in the next Room. If the Player types
 *       "walk west" from the next Room, then he/she should be back in the first Room.)
 *     + Each Room in the HashMap takes an array of Integers as a parameter. This array 
 *       of Integers determines what Rooms are connected to the Room being made.
 *       Like this:
 *       
 *       Integer[] nextRoomNumbers = {0, 1, 2, 3};
 *       Room room = new Room(hasNorthDoor, hasSouthDoor, hasEastDoor, hasWestDoor, 
 *       					   hasNPC, hasMonster, nextRoomNumbers);
 *       
 *       The nextRoomNumbers array is the keys in the HashMap of Rooms.
 *       Each Room has an Integer key in the HashMap of Rooms.
 *       The first Room in the HashMap has 0 as its key. 
 *       In order to "connect" the Rooms together, we need to figure out
 *       which Room to give the Player when he/she goes through a Door.
 *       That is where the array of next Room numbers comes in.
 *       The room numbers in the array are organized by direction, so:
 *       
 *       Integer[] nextRoomNumbers = {northRoom, southRoom, eastRoom, westRoom};
 *       
 *       If there is a Room to the north of the Player's current Room, we use the
 *       Integer in the northRoom slot of the array in order to get it from the 
 *       HashMap. We then give it the Player. 
 *       
 *       The Doors of the Room must reflect this as well:
 *       
 *       Integer[] nextRoomNumbers = {0, 1, 2, 3};
 *       Room room = new Room(true, true, true, true, 
 *       					   false, false, nextRoomNumbers);
 *       
 *       If there is no Room in a certain direction, put -1 as the next Room number.
 *       (This makes it easy to see where's a Wall. If there is no Door, then there
 *        has to be a Wall.)
 *       
 *       Integer[] nextRoomNumbers = {-1, 2, -1, 3};
 *       Room room = new Room(false, true, false, true, 
 *       					   false, false, nextRoomNumbers);
 *    	(Check out the example Map in loadRooms()).
 *    
 *    + Lastly, don't worry about making any NPCs and Monsters. Set the boolean flags
 *      when you create a Room to false for both. (loadNPCAndMonsters() isn't done yet).
 *      
 */
package edu.calvin.csw61.finalproject;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class TestClass {
	//Maps of Rooms (Static and anyone can refer to them as long as we call Driver.mapOfRooms
	//This makes it a LOT easier to determine where we are
	
	//Each room will instead be numbered. This allows us to add NPCs and Monsters in the 
	//order in which we add the Rooms. (Effectively giving us control over adding them
	//to specific Rooms).
	//The reason why it wasn't working before was because Rooms are not Comparable.
	//There is no way to compare them as a Key in the HashMap.
	//Therefore, we need to number them.
	
	static Map<Integer, Room> mySBRooms = new HashMap<Integer, Room>();  //Science Building, East.	
	static Map<Integer, Room> myHHRooms = new HashMap<Integer, Room>();  //Hiemenga Hall, West
	static Map<Integer, Room> myCPRooms = new HashMap<Integer, Room>();  //Chapel, North
	static Map<Integer, Room> myCMRooms = new HashMap<Integer, Room>();  //Commons, South
	
	//Only one outside Room
	static Integer[] outsideBuildings = {0, 0, 0, 0}; //The outside Room will have the first Room from the HashMap of Rooms.
	static Room outsideRoom = new Room(true, true, true, true, true, false, outsideBuildings);
	
	
	public static void main(String[] args) {
		System.out.println("Testbed for LostInKnightdale");
		//Load the game
		loadGame();
		story();
		String verb, noun;  //Holders for the words of the command
		outsideRoom.setDescriptor(makeDescriptor("centerRoomDescription.txt"));  //For now. We may want to implement a loadDescriptions() method that sets the descriptions for every Room in the HashMap.
		
		Player p = new Player(outsideRoom);  //Player
		
		//Test: Poisoning the Player (WORKS)
//		ObjectInterface bidoof = new Food("bidoof");
//		Food.addPoisonedFood("bidoof");  
		
//		p.addObject(bidoof.getName(), bidoof);
				
		//Test: Can I eat a Monster? (NOPE) 
//		ObjectInterface m = new Monster("Harry");
		//Since we don't have Rooms (yet), I have to add him to the backpack and see if I can eat him
//		p.addObject(m.getName(), m);
		
		//How about an NPC? (ALSO NOPE)
//		ObjectInterface n = new NPC("Mary");
		//Since we don't have Rooms (yet), I have to add her to the backpack and see if I can eat her
//		p.addObject(n.getName(), n);
		
		verb = "";
		noun = "";
		
		//Since we are adding the food items ourselves, we don't have to worry about
		//checking if the Food object's name is valid or not :D
		
		//Non-poisoned item
		ObjectInterface ob = new Food("apple");		
		ObjectInterface ob2 = new Key("key");
		ObjectInterface ob3 = new Food("pear");
		ObjectInterface ob4 = new Food("orange");
		ObjectInterface ob5 = new Food("orange");
		ObjectInterface ob6 = new Treasure("ring");
		ObjectInterface ob7 = new Treasure("bracelet");
		ObjectInterface ob8 = new Treasure("gauntlet");
		ObjectInterface ob9 = new Food("pineapple");
		ObjectInterface ob10 = new Food("blueberry");
		ObjectInterface ob11 = new Food("watermelon");
		
		p.addObject(ob.getName(), ob); //Has to be the name of the object and the object itself
										//Reason is because we remove objects by name
										//Just because two objects have the same name does NOT mean they are the same object
										//At least in memory
		p.addObject(ob2.getName(), ob2);
		p.addObject(ob3.getName(), ob3);
		p.addObject(ob4.getName(), ob4);
		p.addObject(ob5.getName(), ob5);
		p.addObject(ob6.getName(), ob6);
		p.addObject(ob7.getName(), ob7);
		p.addObject(ob8.getName(), ob8);
		p.addObject(ob9.getName(), ob9);
		p.addObject(ob10.getName(), ob10);
		p.addObject(ob11.getName(), ob11);
		
//		ObjectInterface ob12 = new Treasure("emerald");
		
//		p.addObject(ob12.getName(), ob12);
		
		//Test: Get the NPC from a Room. (Works)
//		ObjectInterface checker = mySBRooms.get(0).getNPC();
//		System.out.println();
//		System.out.println("Room 0 should have " + checker.getName());
		
		//Test: No NPC in the Room (Works).
//		ObjectInterface checker2 = mySBRooms.get(4).getNPC();
//		System.out.println();
//		System.out.println("Room 4 = " + checker2);  //No NPC. Throws a NullPointerException if I try to get the name.
		
		//Test: Get the Monster from a Room. (Works).
//		ObjectInterface checker3 = mySBRooms.get(5).getMonster();
//		System.out.println();
//		System.out.println("Room 5 should have " + checker3.getName());
		
		//Test: No Monster in the Room. (Works).
//		ObjectInterface checker4 = mySBRooms.get(1).getMonster();
//		System.out.println();  
//		System.out.println("Room 1 = " + checker4);  //No Monster.
		
		//MULTI-Test: Rooms, Doors, and the Interactions between them.
		
		//Test 1: Outside Room. (WORKS).		
		System.out.println(p.getRoom().getDescriptor());
		
		//Test 2: Transferring the Player to a new Room. (WORKS).
//		System.out.println("Transferring to a new Room...");
//		p.setCurrentRoom(mySBRooms.get(0));
//		System.out.println(p.getRoom().getDescriptor());
		
		//Test 3: Going inside of a building.
		//Fire up the Game.
		//Type, "walk north"
		//Should tell you that you are now in North Hall, first Room. (WORKS)
		
		//Test 4: In a Building, now you have to go Outside (WORKS).
		
		//Final Test: Navigate through a Building and then go Outside (OH. MY. NUGGETS. IT WORKS!).
		//(Now you just have to incorporate Walls).
		
		System.out.println("You have an " + ob.getName() + " in your backpack.");
		System.out.println("\n");
		
		//Test: Food gives health to Players (WORKS)
//		p.subtractHealth();
//		p.subtractHealth();
//		p.subtractHealth();
		//eat apple, pear, and orange. Should be back to 100 hit points. (WORKS)
				
		//infinite loop
		while(true){
			//new scanner to get user input
			Scanner scanner = new Scanner(System.in);

			//read in the line
			String string = scanner.nextLine();
			
			//get the number of words in the string
			int wordCount = wordCount(string);
			
			if(wordCount >= 3){ //if the count is 3 or more
				System.out.println("Too many words"); //too many words
			} else if(wordCount == 1){ //else if the count is one,
				verb = string.trim();			//set the verb to the one word
				noun = "";			//and make the noun null
				p.setCommand(verb, noun);
			} else { //else split the string and put the first part in the noun and the second part in the verb.
				String[] parts = string.split("\\W+");
				verb = parts[0];
				noun = parts[1];
				p.setCommand(verb, noun);
			}

		} 
	} 
	
	/*
	 * This method counts the number of words in a string.
	 * (Adapted from StackOverflow)
	 */
	public static int wordCount(String s){
		String trim = s.trim();
		if (trim.isEmpty()){
			return 0;
		}
		return trim.split("\\W+").length; // separate string around spaces
	}

	//This will print the story...
	public static void story() {
		ReadFile readFile = new ReadFile("story.txt"); //new readFile with story.txt as the file to be read
		readFile.readAndPrint(); //read in the file and print it
	}
	
	public static void loadGame() {
		loadRooms();  //Rooms, NPCs, and Monsters in that order.
		loadNPCAndMonster();
	}
	
	public static void loadRooms() {
		
		//EXAMPLE MAP:
		
		//-2 = Outside, for 0th Room.
		//We could read these from a text file, that way we don't have to keep
		//creating an array of Integers...
	
		//South
		Integer[] chapelRooms = {-2, -1, -1, -1};  //Rooms to the North, South, and East.
		
		//North
		Integer[] commonsRooms = {-1, -2, -1, -1}; //Rooms to the North, South, East, and West.
		
		//West
		Integer[] scienceBuildRooms0 = {-1, -1, -2, 6};  //Room 0
		Integer[] scienceBuildRooms1 = {-1, -1, -1, 7};  //Room 1
		Integer[] scienceBuildRooms2 = {-1, 3, -1, 5};  //Room 2
		Integer[] scienceBuildRooms3 = {2, -1, -1, 4};  //Room 3
		Integer[] scienceBuildRooms4 = {-1, -1, 3, 11};  //Room 4
		Integer[] scienceBuildRooms5 = {-1, -1, 2, 10};  //Room 5
		Integer[] scienceBuildRooms6 = {7, -1, 0, 9};  //Room 6
		Integer[] scienceBuildRooms7 = {-1, 6, 1, 8};  //Room 7
		Integer[] scienceBuildRooms8 = {-1, -1, 7, 15};  //Room 8
		Integer[] scienceBuildRooms9 = {-1, 10, 6, 14};  //Room 9
		Integer[] scienceBuildRooms10 = {9, 11, 5, -1};  //Room 10
		Integer[] scienceBuildRooms11 = {10, -1, 4, 12};  //Room 11
		Integer[] scienceBuildRooms12 = {-1, -1, 11, -1};  //Room 12
		Integer[] scienceBuildRooms13 = {14, -1, -1, -1};  //Room 13
		Integer[] scienceBuildRooms14 = {15, 13, 9, -1};  //Room 14
		Integer[] scienceBuildRooms15 = {-1, 14, 8, -1};  //Room 15
		
		//East
		Integer[] hiemengaHallRooms = {-1, -1, -1, -2}; //Rooms to the North, South, East, and West.
		
		//Create the Rooms in the HashMap.
		//North
		myCPRooms.put(0, new Room(true, true, true, false, false, false, chapelRooms));  
		myCPRooms.get(0).setDescriptor("First Room in Chapel.");
		
		//South
		myCMRooms.put(0, new Room(true, true, true, true, false, false, commonsRooms));
		myCMRooms.get(0).setDescriptor("First Room in the Commons Annex.");
		
		//East
		//Room 0
		mySBRooms.put(0, new Room(false, false, true, true, false, false, scienceBuildRooms0));
		mySBRooms.get(0).setDescriptor(makeDescriptor("sb0.txt"));
		
		//Room1
		mySBRooms.put(1, new Room(false, false, false, true, false, false, scienceBuildRooms1));
		mySBRooms.get(1).setDescriptor(makeDescriptor("sb1.txt"));
		
		//Room 2
		mySBRooms.put(2, new Room(false, true, false, true, false, false, scienceBuildRooms2));
		mySBRooms.get(2).setDescriptor(makeDescriptor("sb2.txt"));
		
		//Room 3
		mySBRooms.put(3, new Room(true, false, false, true, false, false, scienceBuildRooms3));
		mySBRooms.get(3).setDescriptor(makeDescriptor("sb3.txt"));
		//Room 4
		mySBRooms.put(4, new Room(false, false, true, true, false, false, scienceBuildRooms4));
		mySBRooms.get(4).setDescriptor(makeDescriptor("sb4.txt"));
		//Room 5
		mySBRooms.put(5, new Room(false, false, true, true, false, false, scienceBuildRooms5));
		mySBRooms.get(5).setDescriptor(makeDescriptor("sb5.txt"));
		//Room 6
		mySBRooms.put(6, new Room(true, false, true, true, false, false, scienceBuildRooms6));
		mySBRooms.get(6).setDescriptor(makeDescriptor("sb6.txt"));
		//Room 7
		mySBRooms.put(7, new Room(false, true, true, true, false, false, scienceBuildRooms7));
		mySBRooms.get(7).setDescriptor(makeDescriptor("sb7.txt"));
		//Room 8
		mySBRooms.put(8, new Room(false, false, true, true, false, false, scienceBuildRooms8));
		mySBRooms.get(8).setDescriptor(makeDescriptor("sb8.txt"));
		//Room 9
		mySBRooms.put(9, new Room(false, true, true, true, false, false, scienceBuildRooms9));
		mySBRooms.get(9).setDescriptor(makeDescriptor("sb9.txt"));
		//Room 10
		mySBRooms.put(10, new Room(true, true, true, false, false, false, scienceBuildRooms10));
		mySBRooms.get(10).setDescriptor(makeDescriptor("sb10.txt"));
		//Room 11
		mySBRooms.put(11, new Room(true, false, true, true, false, false, scienceBuildRooms11));
		mySBRooms.get(11).setDescriptor(makeDescriptor("sb11.txt"));
		//Room 12
		mySBRooms.put(12, new Room(false, false, true, false, false, false, scienceBuildRooms12));
		mySBRooms.get(12).setDescriptor(makeDescriptor("sb12.txt"));
		//Room 13
		mySBRooms.put(13, new Room(true, false, false, false, false, false, scienceBuildRooms13));
		mySBRooms.get(13).setDescriptor(makeDescriptor("sb13.txt"));
		//Room 14
		mySBRooms.put(14, new Room(true, true, true, false, false, false, scienceBuildRooms14));
		mySBRooms.get(14).setDescriptor(makeDescriptor("sb14.txt"));
		//Room 15
		mySBRooms.put(15, new Room(false, true, true, false, false, false, scienceBuildRooms15));
		mySBRooms.get(15).setDescriptor(makeDescriptor("sb15.txt"));
		
		//West
		myHHRooms.put(0, new Room(true, true, true, true, false, false, hiemengaHallRooms));
		myHHRooms.get(0).setDescriptor("First Room in Hiemenga Hall.");
				
		System.out.println("Rooms created.");
		
	}
	
	//Test: Adding NPCs and Monsters (NOT DONE)
	public static void loadNPCAndMonster() {
		String[] names = {"V. Norman", "Yolanda", "Andrew", "Austin", "Professor Porpoise", "Dr. Professor Patrick", "Bubba" };
		String[] monsterNames = {"Hope student", "A. Bickle", "H. Plantinga", "Dr. Squiggles" };
		
		int npcI = 0, monsterI = 0;
	   // Iterator<Map.Entry<Integer, Room>> it = mySBRooms.entrySet().iterator();
	
		System.out.println("Size of SB Rooms: " + mySBRooms.size()); //Get the size of SBRooms
		for(int i = 0; i < mySBRooms.size(); i++) {
			if(mySBRooms.get(i).needNPC()) {  //Do I need an NPC?
				if(mySBRooms.get(i).needMonster()) {  //How about a Monster?
					mySBRooms.get(i).setMonster(monsterNames[monsterI]);  //Yes. Set the name of it.
					System.out.println("Room " + i + " has " + mySBRooms.get(i).getMonster().getName());
					monsterI++;
				} else {  //No, I only need an NPC
					System.out.println("Room " + i + " doesn't need a Monster."); //Nope.
				}
 				mySBRooms.get(i).setNPC(names[npcI]);  //Set the name of the NPC.
				System.out.println("Room " + i + " has " + mySBRooms.get(i).getNPC().getName());
				npcI++;
			} else {
				System.out.println("Room " + i + " doesn't need an NPC."); //Nope.
				if(mySBRooms.get(i).needMonster()) {  //How about a Monster?
					mySBRooms.get(i).setMonster(monsterNames[monsterI]);  //Yes. Set the name of it.
					System.out.println("Room " + i + " has " + mySBRooms.get(i).getMonster().getName());
					monsterI++;
				} else {  //I only need objects 
					System.out.println("Room " + i + " doesn't need a Monster."); //Nope.
				}
			}
			
		}	
	}
	
	public static Map<Integer, Room> getInstance() {
		return mySBRooms;
	}
	
	public static String makeDescriptor(String fileName){
		ReadFile rf = new ReadFile(fileName);
		rf.read();
		return rf.output;
	}
		
}