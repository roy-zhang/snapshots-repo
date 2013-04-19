package sensing.snapShots.model;


public class Profile {

	public int weight;
	public int drinker;
	public boolean smoker;
	public boolean isMale;

	public float elimRateAdjuster = 1.0f;
	public float absRateAdjuster = 1.0f;

	public Profile(int weightInPounds, int drinkingFrequency1to5,
			boolean smokes, boolean male) {
		weight = weightInPounds;
		drinker = drinkingFrequency1to5;
		smoker = smokes;
		isMale = male;

	}

	public Profile(int drinkingFrequency) {
		this(160, drinkingFrequency, false, true);
	}

	public Profile() {
		this(160, 3, false, true);
	}

	public void adjustElimRate(double newRate) {
		elimRateAdjuster = (float) ((elimRateAdjuster + newRate / getElimRate()) / 2.0);

		if (elimRateAdjuster < 0.5)
			elimRateAdjuster = 0.75f;
		if (elimRateAdjuster > 2)
			elimRateAdjuster = 1.7f;
	}

	public void adjustAbsRate(double newRate, double hunger) {
		absRateAdjuster = (float) ((absRateAdjuster + newRate / getAbsRate(hunger)) / 2.0);

		if (absRateAdjuster < 0.5)
			absRateAdjuster = 0.75f;
		if (absRateAdjuster > 2)
			absRateAdjuster = 1.7f;
	}

	// based on hunger level
	public double getAbsRate(double hungerLevel1To5) {
		return (2.6f - (hungerLevel1To5 * 0.35f)) * absRateAdjuster;
	}

	// based on profile settings
	public double getElimRate() { // ml per sec
		double baseElimRate = 0.13f;
		if (smoker)
			baseElimRate = 0.20f;
		else
			baseElimRate = 0.13f + drinker * 0.016f;

		return baseElimRate * elimRateAdjuster;
	}

	public double getMlToElim(double BAC) {
		return lbsToMlBlood(weight) * (BAC / 10.0f);
	}

	public double getBac(double mlToElim) {
		return 10.0 * mlToElim / lbsToMlBlood(weight);
	}

	public double lbsToMlBlood(double lbs) {
		if (isMale)
			return 1000 * (lbs / 2.20462f) / 14.5f;
		else
			return 950 * (lbs / 2.20462f) / 14.5f;
	}
	
	public String toString(){
		return "weight:" + weight + " male:" + isMale + " smoke:" + smoker + " drinker:" + drinker + " " +elimRateAdjuster;
	}
	
}
