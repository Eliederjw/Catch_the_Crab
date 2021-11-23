import java.awt.Graphics;
import java.awt.image.BufferedImage;

public class Smoke {

	private BufferedImage[] SMOKE_SPRITES = {
			Game.spritesheet.getSprite(0 * 16, 1 * 16, 16, 16),
			Game.spritesheet.getSprite(1 * 16, 1 * 16, 16, 16),
			Game.spritesheet.getSprite(2 * 16, 1 * 16, 16, 16),
			Game.spritesheet.getSprite(3 * 16, 1 * 16, 16, 16),
			Game.spritesheet.getSprite(4 * 16, 1 * 16, 16, 16),
			Game.spritesheet.getSprite(5 * 16, 1 * 16, 16, 16),
			Game.spritesheet.getSprite(6 * 16, 1 * 16, 16, 16)
	};
	
	private Animation smokeAnimation;
	
	private BufferedImage sprite;
	
	private int x, y, width, height;
	
	public Smoke (int x, int y, int width, int height) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		
		smokeAnimation = new Animation(SMOKE_SPRITES, 5);
		smokeAnimation.setLoop(false);
				
	}
	
	public void update() {
		if (smokeAnimation.isFinished()) Game.smokes.remove(this);
	}
	
	public void render(Graphics g) {
		g.drawImage(animate(), x, y, width, height, null);
	}

	private BufferedImage animate() {
		sprite = smokeAnimation.animate();
		
		return sprite;
	}
	
	
	
}
