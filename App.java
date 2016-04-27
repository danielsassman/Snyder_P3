import java.util.*;

class Player extends Thread{
	int myId;
	
	public Player(int x){
		myId = x;
		this.start();
	} 

	public void run() {
		while(App.getplayerRunning()){
			try {
				Random rand = new Random();
				int randomNum = rand.nextInt(3500);
				Thread.sleep(randomNum);
			}
			catch (Exception e) {
				e.printStackTrace();
			}	
		}
	findChair();
	}

	public synchronized void findChair(){
			if(getAvailableChairs()!=0) {
				sitDown();
				decrement();
			}
			else{
				eliminated();
			}
		
	}

	public void decrement() {
		if(App.availableChairs == 0) {
			eliminated();
		}
		else
			App.decChairs();
	}

	public int getAvailableChairs(){
		return App.getavailableChairs();
	}

	public synchronized void sitDown(){
		App.satDown++;
		if(App.availableChairs > 0 && App.satDown < (App.people-1)) {
			System.out.print("P"+myId +", ");
		}
		else if(App.availableChairs > 0 && App.satDown < App.people) {
			System.out.print("P"+myId);
		}
	}
	public void eliminated(){
		App.people--;
		App.satDown = 0;
		System.out.println("]");
		System.out.println("P"+myId + " could not find an available chair\n\n");
		App.stillStanding.remove(new Integer(myId));
		App.initializeMusic(App.stillStanding);
	}
	public int getMyId(){
		return this.myId;
	}
}

public class App {
	static int satDown;
	static int id;
	static int chairNumber;
	static int seatNumber;
	static boolean playerRunning = true;
	static int availableChairs;
	static ArrayList<Integer> stillStanding = new ArrayList<Integer>();
	static int people;
	public static void main(String args[]) {
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
				initializeMusic(stillStanding);
			}
		}
	}
	public static void initializeMusic(ArrayList x) {
		if(x.size()>1){
			setplayerRunning(true);
			availableChairs = (x.size()-1);
			if(availableChairs > 1)
				System.out.println(availableChairs + " chairs remaining");
			else
				System.out.println(availableChairs + " chair remaining");
			System.out.print("Found seats: [");
			int k = 0;
			for(int i = 0; i< x.size(); i++){
				int myValue = (int)x.get(i);
				new Player(myValue);
			}
			setplayerRunning(false);
		}
		else{
			System.out.println("P"+x.get(0) + " is the winner");
			return;
		}
	}
	public synchronized static int getavailableChairs(){
		return availableChairs;
	}
	public synchronized static int decChairs() {
		return availableChairs--;
	}
	public static int getId() {
		return id;
	}

	public static void  setId() {
		 id++;
	}
	public static boolean getplayerRunning() {
		return playerRunning;
	}

	public static void setplayerRunning(boolean x) {
		 playerRunning = x;
	}
}
