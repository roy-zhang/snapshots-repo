package sensing.snapShots.model;


import java.util.Calendar;

public class SnapShot {

	public Calendar timestamp;

	public double mlInGlass;
	double mlToAbs;
	double mlToElim;
	double mlElimed;
	double hunger1to5;
	
	double secondsLeftToDrink = 0.0;


	public double bac;

	// holds a picture of a person's bac status

	// default Snapshot starting from zero BAC
	public SnapShot(double hunger) {
		this(0.0, 0.0, 0.0, hunger, 0.0, Calendar.getInstance(), 0.0, 0.0);
	}

	public SnapShot(double inGlass, double toAbs, double toElim, double hunger, double Bac,
			Calendar time, double secondsToDrink, double elimed) {
		mlInGlass = inGlass;
		mlToAbs = toAbs;
		mlToElim = toElim;
		hunger1to5 = hunger;
		secondsLeftToDrink = secondsToDrink;
		mlElimed = elimed;

		bac = Bac;

		timestamp = time;

		if (mlInGlass < 0.0)
			mlInGlass = 0.0;
		if (mlToAbs < 0.0)
			mlToAbs = 0.0;
		if (mlToElim < 0.0)
			mlToElim = 0.0;
		if (hunger1to5 < 1.0)
			hunger1to5 = 1.0;
		if (bac < 0.0)
			bac = 0.0;
		if (secondsLeftToDrink < 0.0)
			secondsLeftToDrink = 0.0;
	}

	// instance one from sensor readings
	public static SnapShot birthNewSnapShot(Profile p, SnapShot lastSensor,
			double BAC, Calendar timestamp) {

		double secsSince = getSecsBetween(lastSensor.timestamp, timestamp);

		SnapShot predicted = predictNextSnapShot(p, lastSensor, secsSince);

		// absRate elimRate adjustment disabled for now

		// rising significantly means absorbin
		if (significantBacRise(lastSensor.bac, secsSince, BAC)) {
			// double actualMlChange = -1.0 * p.getMlToElim(lastSensor.bac -
			// BAC);
			// double desiredRate = actualMlChange/(secsSince/3600.0);
			// p.adjustAbsRate(desiredRate, lastSensor.hunger1to5);

		}
		// once BAC starts falling, then we can adjust eliminationRate
		else if (significantBacDrop(lastSensor.bac, secsSince, BAC)) {
			// only adjust abs rate if actualy thinks has alchohol to abs
			// if (predicted.mlToAbs > 1.0){
			// double actualMlChange = -1.0 * p.getMlToElim(lastSensor.bac -
			// BAC);
			// double desiredRate = actualMlChange/(secsSince/3600.0) +
			// p.getElimRate();
			// p.adjustElimRate(desiredRate);
			// }
			// else
			// predicted.mlToAbs = 18; //standard drink

		} else { // in nearly all absorbed zone
					// must have some to absorb, some to elim
			// double overallSlope = p.getMlToElim(BAC - lastSensor.bac)/
			// (secsSince / 3600.0);
			// if (overallSlope > 0.5 && predicted.mlToAbs < 1.0)
			// predicted.mlToAbs = 10.0;

		}

		predicted.bac = BAC;
		predicted.mlToElim = p.getMlToElim(BAC);

		return predicted;
	}

	public static SnapShot predictNextSnapShot(Profile p, SnapShot parent, double secondsAfter) {

		if (secondsAfter <= 0.0)
			return parent;
		
		
		double drank      = getDrank(parent.mlInGlass, secondsAfter, parent.secondsLeftToDrink);
		double absorbed   = getAbsorbed(p.getAbsRate(parent.hunger1to5), parent.hunger1to5, parent.mlToAbs + drank, secondsAfter);
		double eliminated = getEliminated(p.getElimRate(), parent.mlToElim + absorbed, secondsAfter);
		double hunger     = adjustHunger(parent.hunger1to5, secondsAfter);
		
		double mlInBlood = parent.mlToElim + absorbed - eliminated;

		return new SnapShot( parent.mlInGlass-drank ,parent.mlToAbs + drank - absorbed, mlInBlood, hunger,
				p.getBac(mlInBlood), addSecs(parent.timestamp, secondsAfter), parent.secondsLeftToDrink - secondsAfter, parent.mlElimed + eliminated );
	}


	private static double getDrank(double mlInGlass, double secondsAfter, double secondsLeftToDrink) {
		if (secondsLeftToDrink <= 0)
			return mlInGlass;
		
		if (secondsLeftToDrink < secondsAfter)
			return mlInGlass;
		
		return mlInGlass * secondsAfter/secondsLeftToDrink;	
		}

	public static Calendar addSecs(Calendar timestamp, double secs) {
		Calendar stamp = Calendar.getInstance();
		stamp.setTimeInMillis(timestamp.getTimeInMillis() + (long) secs * 1000);
		return stamp;
	}

	public static double getAbsorbed(double absRate, double hungerLevel,
			double ToAbs, double secondsPassed) {

		hungerLevel = adjustHunger(hungerLevel, secondsPassed / 2);

		double secsToEmpty = 3600.0 / absRate; // proportion eliminated in one
												// hour

		if (secondsPassed > secsToEmpty)
			return ToAbs;
		else
			return ToAbs * (secondsPassed / secsToEmpty);

	}

	public static double getEliminated(double elimRate, double ToElim,
			double secondsPassed) {
		double eliminated = secondsPassed * (elimRate / 60.0);
		if (eliminated > ToElim)
			return ToElim;
		return eliminated;

	}

	
	public void addAlcohol(double ml, int secondsToDrink) {
		mlInGlass += ml;
		secondsLeftToDrink += secondsToDrink;
	}

	// utils

	public boolean isFlat() {
		return mlToAbs < 1 && mlToElim < 1 && mlInGlass == 0 && secondsLeftToDrink == 0;
	}

	public static boolean significantBacRise(double last, double seconds,
			double recent) {
		// > .02 in 60 min
		return (seconds > 300) && (last > recent - 0.02 * (seconds / 3600.0));
	}

	public static boolean significantBacDrop(double last, double seconds,
			double recent) {
		// > .015 in 60 min
		return (seconds > 600) && (last > recent + 0.015 * (seconds / 3600.0));

	}

	public static double adjustHunger(double hunger, double seconds) {
		hunger = hunger - ((double) seconds / 10800.0); // down 1 hunger per 3
														// hours
		if (hunger < 1.0)
			hunger = 1.0;
		return hunger;
	}

	public static double getSecsBetween(Calendar a, Calendar b) {
		return 1000.0f * (b.getTimeInMillis() - a.getTimeInMillis());

	}


	public String toString(){
		 return "glass:" + mlInGlass 
				+" stomach:" + mlToAbs
				+ " blood:" + mlToElim
				+ " elimed:" +  mlElimed
				+ " hunger:" + hunger1to5
				+ " secondsToDrink" +  secondsLeftToDrink;

	}

}
