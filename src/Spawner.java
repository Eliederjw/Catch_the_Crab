import java.util.Random;

public class Spawner {

	private int curTime = 0, targetTime = 60 * 1;
	private Random random;
	
	public Spawner() {
		random = new Random();
	}
	
	public void update() {
		curTime++;
		if (curTime == targetTime) {
			curTime = 0;
			
			int side = random.nextInt(4) + 1;

			double xInitial = random.nextInt(Game.WIDTH - 40);
			double yInitial = random.nextInt(Game.HEIGHT - 40);
			
			if (side == 1) Game.crabs.add(new Crab(xInitial, 0));
			else if (side == 2) Game.crabs.add(new Crab(0, yInitial));
			else if (side == 3) Game.crabs.add(new Crab(xInitial, Game.HEIGHT - 40));
			else if (side == 4) Game.crabs.add(new Crab(Game.WIDTH - 40, yInitial));
		}
	}
}
