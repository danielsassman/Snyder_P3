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
				int randomNum = rand.nextInt(100);
				Thread.sleep(randomNum);
			}
			catch (Exception e) {
				e.printStackTrace();
			}	
		}
	findChair();
	}

	public  synchronized void findChair(){
		
			if(getAvailableChairs()!=0) {
				sitDown();
				decrement();
			}
			else{
				eliminated();
			}
		
	}

	public synchronized void decrement() {
		if(App.availableChairs == 0) {
			eliminated();
		}
		else
			App.availableChairs--;
	}

	public synchronized int getAvailableChairs(){
		return App.availableChairs;
	}

	public void sitDown(){
		if(App.availableChairs > 0)
			System.out.println(myId + " sat down");
	}
	public void eliminated(){
		System.out.println(myId + " could not find an available chair\n\n\n");
		App.stillStanding.remove(new Integer(myId));
		App.initializeMusic(App.stillStanding);
	}
	public int getMyId(){
		return this.myId;
	}

}

class Runner extends Thread {
	boolean running = true;
	public void run() {
		while(running){
			//System.out.println("music playing...... ");
			try {
				Thread.sleep(1);
			}
			catch (Exception e) {
				e.printStackTrace();
			}	
		}
	}
	public void stopMusic(){
		running = false;
	}
}

public class App {
	static int id;
	static int chairNumber;
	static int seatNumber;
	static boolean playerRunning = true;
	static int availableChairs;
	static ArrayList<Integer> stillStanding = new ArrayList<Integer>();
	public static void main(String args[]) {
		
		if (args.length < 1 ) {
			System.out.println("Enter number of participants");
			return;
		}
		else {
			int people = Integer.parseInt(args[0]);
			if(people < 2){ 
				System.out.println("Need more than one");
				return;
			}
			else
				for(int i = 0; i<people; i++) {
					stillStanding.add(i);
				}
				initializeMusic(stillStanding);
		}
	}
	public static void initializeMusic(ArrayList x) {
		if(x.size()>1){
			setplayerRunning(true);
			availableChairs = (x.size()-1);
			System.out.println(availableChairs + " remaining");
			int k = 0;
			for(int i = 0; i< x.size(); i++){
				int myValue = (int)x.get(i);
				new Player(myValue);
			}
			Runner runner = new Runner();
			runner.start();
			try{
				Thread.sleep(150);
			}
			catch (Exception e){
				e.printStackTrace();
			}

			setplayerRunning(false);
			runner.stopMusic();
			try{
				Thread.sleep(150);
			}
			catch (Exception e) {
				e.printStackTrace();
			}
		}
		else{
			System.out.println(x.get(0) + " is the winner");
			return;
		}
	}
	//synchronized (this) {
		public static int getavailableChairs(){
			return availableChairs;
		}
	//}
	public static int getId() {
		return id;
	}

	public static void  setId() {
		 id++;
	}
//	public static int getseatNumber() {
//		return seatNumber;
//	}

//	public static void setseatNumber() {
//		 seatNumber++;
//	}
//	public static int getchairNumber() {
//		return chairNumber;
//	}

//	public static void setchairNumber() {
//		 chairNumber++;
//	}
	public static boolean getplayerRunning() {
		return playerRunning;
	}

	public static void setplayerRunning(boolean x) {
		 playerRunning = x;
	}
}
