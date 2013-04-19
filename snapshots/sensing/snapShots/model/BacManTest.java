package sensing.snapShots.model;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class BacManTest {

	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void testPredictTillFlat() {
		fail("Not yet implemented");
	}

	@Test
	public void normalManStandardDrink() {
		Profile profile = new Profile(160, 3, false, true);
		BacMan model = new BacMan(profile, 3); // med full

		model.stiffShot();
		// model.canOfBeer();

		System.out.println("normal male 4 standard drinks");
		model.printQueue(model.predictTillFlat(900, 50));
	}

	@Test
	public void hungryManStandardDrink() {
		Profile profile = new Profile(160, 1, false, true);
		BacMan model = new BacMan(profile, 3); // med full

		model.canOfBeer();
		model.canOfBeer();

		System.out.println("hungry male 2 cans of beer");
		model.printQueue(model.predictTillFlat(900, 50));
	}
}
