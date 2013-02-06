package sensing.snapShots.model;


import java.util.ArrayDeque;
import java.util.Calendar;

public class BacMan {
	// interface for all your bac modeling needs

	private static Profile profile;
	private static ArrayDeque<SnapShot> snapShots;

	public BacMan(Profile drinkerProfile, double startHungerLevel1to5) {
		profile = drinkerProfile;
		snapShots = new ArrayDeque<SnapShot>();

		snapShots.add(new SnapShot(startHungerLevel1to5));
	}

	public SnapShot last() {
		return snapShots.peekLast();
	}

	// add sensor readings to model
	public void addSensorSnapShot(double bac) {
		snapShots.add(SnapShot.birthNewSnapShot(profile, last(), bac,
				Calendar.getInstance()));
	}

	public SnapShot predictCurrent() {
		double secsSince = SnapShot.getSecsBetween(last().timestamp,
				Calendar.getInstance());

		return SnapShot.predictNextSnapShot(profile, last(), secsSince);
	}

	public ArrayDeque<SnapShot> predictTillFlat(int intervalLengthInSecs, int SSLimit) {
		ArrayDeque<SnapShot> returnMe = new ArrayDeque<SnapShot>();

		returnMe.add(last());

		for (int i = 1; i < SSLimit; i++) {
			SnapShot newPred = SnapShot.predictNextSnapShot(profile,
					returnMe.peekLast(), intervalLengthInSecs);
			returnMe.add(newPred);
			
			if (newPred.isFlat())
				break;
		}

		return returnMe;
	}
	


	// add drink

	public void stiffShot() {
		addAlcohol(predictCurrent(), 18, 30);
	}
	
	public void standardDrink() {
		addAlcohol(predictCurrent(), 18, 60*5);
	}

	public void canOfBeer() {
		addAlcohol(predictCurrent(), 10, 60*15);
	}

	public void addAlcohol(SnapShot ss, double mlAdded, int secondsToDrink) {
		ss.addAlcohol(mlAdded, secondsToDrink);
		snapShots.add(ss);
	}
	
	public void addAlcohol(int mlToAdd){
		//default to 5 minutes to drink
		addAlcohol(predictCurrent(), mlToAdd, 60*5);
	}

	// set food level, 1 is empty, 5 is buffet full

	public void changeHunger(int hunger) {
		SnapShot currentSS = predictCurrent();
		currentSS.hunger1to5 = hunger;
		snapShots.add(currentSS);
	}

	public static void printQueue(ArrayDeque<SnapShot> queue) {
		Calendar firstTS = queue.peekFirst().timestamp;

		while (!queue.isEmpty()) {
			System.out.println("hour: "
					+ minutesSinceTimestamp(queue.peek().timestamp, firstTS));
			System.out.println(queue.poll().toString());
		}
	}

	public static int minutesSinceTimestamp(Calendar a, Calendar b) {
		double diff = a.getTimeInMillis() - b.getTimeInMillis();
		int mins = (int) diff / 60000;

		return mins;
	}
}
