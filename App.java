import java.util.*;


//Creates player threads
class Player extends Thread{
	int myId;
	
	public Player(int x){
		myId = x;
		this.start();
	} 
	
	//All player threads are released at the same time via getPlayerRunning variable
	public void run() {
		while(App.getplayerRunning()){
			try {
				Random rand = new Random();
				int randomNum = rand.nextInt(App.randCount - 200) + 200;
				Thread.sleep(randomNum);
			}
			catch (Exception e) {
				e.printStackTrace();
			}	
		}
	sitDown();
	}

	public void sitDown(){
		getsatDown();
		if(App.chairs.get(App.satDown).getEmpty()){
			if(stillStanding() > (getsatDownInc())) {
				System.out.print("P"+myId +", ");
			}
			else{
				System.out.print("P"+myId);
			}
	}
		else {
			eliminated();	
		}
	}

	public void eliminated(){
		decPeople();
		App.satDown = -1;
		System.out.println("]");
		System.out.println("P"+myId + " could not find an available chair\n\n");
		App.stillStanding.remove(new Integer(myId));
		try{
			Thread.sleep(100);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		App.initializeMusic(App.stillStanding);
	}
//Internal methods that call synchronized methods inside the APP class
	public int decPeople() {
		return App.decPeople();
	}

	public int stillStanding() {
		return App.getstandingSize();
	}

	public int getsatDown(){
		return App.getsatDown();
	}

	public int getsatDownInc() {
		return App.getsatDownInc();
	}
}

//Class that contains seperate chair resources
class Chairs extends Thread{
	
	boolean isEmpty;
		
	public Chairs(boolean x){
		App.chairs.add(this);
		setEmpty(x);
	}

	public void setEmpty(boolean x) {
		this.isEmpty = x;
	}

	public boolean getEmpty(){
		return this.isEmpty;
	}
}

//Main class. Provides setup and breakdown of all methods
public class App extends Thread{
	static ArrayList<Chairs> chairs = new ArrayList<Chairs>();
	static int satDown= -1;
	static int id;
	static boolean playerRunning = true;
	static int availableChairs;
	static ArrayList<Integer> stillStanding = new ArrayList<Integer>();
	static int people;
	static int randCount = 1000;
	public static final Object LOCK_1 = new Object() {};
	public static final Object LOCK_2 = new Object() {};
	public static void main(String args[]) {
		
		//Confirms inputs and initializes arrays
		if (args.length < 1 ) {
			for (int i = 0; i < 10; i++) {
				stillStanding.add(i);
			}
			people = 10;
			initializeMusic(stillStanding);
		}
		else {
			people = Integer.parseInt(args[0]);
			if(people < 2){ 
				System.out.println("Need more than one");
				return;
			}
			else {
				for(int i = 0; i<people; i++) {
					stillStanding.add(i);
				}
				try{
					Thread.sleep(100);
				}
				catch (Exception e) {
					e.printStackTrace();
				}
				initializeMusic(stillStanding);
			}
		}
	}
	
	//Method that gets the arrays running properly
	public static void initializeMusic(ArrayList x) {
		if(x.size()>1){
			setplayerRunning(true);
			availableChairs = (x.size()-1);
			chairs = new ArrayList<Chairs>();
			if(availableChairs > 1)
				System.out.println(availableChairs + " chairs remaining");
			else
				System.out.println(availableChairs + " chair remaining");
			System.out.print("Found seats: [");
			int myValue = 0;
			for(int i = 0; i<(x.size()-1); i++) {
				new Chairs(true);
			}
			new Chairs(false);
			for(int i = 0; i< x.size(); i++){
				myValue = (int)x.get(i);
				new Player(myValue);
			}	
			try{
				Thread.sleep(100);
			}
			catch (Exception e) {
				e.printStackTrace();
			}
			setplayerRunning(false);
		}
		else{
			System.out.println("P"+x.get(0) + " is the winner");
			return;
		}
	}

	//Synchronized methods to provide proper concurrency
	public static int getstandingSize() {
		synchronized(LOCK_1) {
			return stillStanding.size();
		}
	}

	public static int getsatDown() {
		synchronized(LOCK_1) {
			return satDown++;
		}
	}

	public static int getsatDownInc() {
		synchronized(LOCK_1) {
			return satDown + 2;
		}
	}
	
	public static int getcurrentSatDown() {
		synchronized(LOCK_1) {
			return satDown;
		}
	}

	public static int decPeople() {
		synchronized(LOCK_2) {
			return people--;
		}
	}
	
	//Holds and releases the Players
	public static boolean getplayerRunning() {
		return playerRunning;
	}

	public static void setplayerRunning(boolean x) {
		 playerRunning = x;
	}
}
