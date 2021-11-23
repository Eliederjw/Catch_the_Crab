import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

public class Crab {
	
	private BufferedImage[] CRAB_SPRITES = {
			Game.spritesheet.getSprite(0 * 16, 0 * 16, 16, 16),
			Game.spritesheet.getSprite(1 * 16, 0 * 16, 16, 16),
			Game.spritesheet.getSprite(2 * 16, 0 * 16, 16, 16),
			Game.spritesheet.getSprite(3 * 16, 0 * 16, 16, 16)
	};

	private BufferedImage sprite;
	
	private Animation crabAnimation;
	private double x, y, dx, dy;
	private int width = 40, height = 40;
	private int maskW = 10, maskH = 10;
	private double speed = 4;
	
	
	
	public Crab(double x, double y) {
		this.x = x;
		this.y = y;
		
		double radius = Math.atan2((Game.HEIGHT/2 - 20) - y, (Game.WIDTH/2 - 20) - x);
		this.dx = Math.cos(radius);
		this.dy = Math.sin(radius);
		
		crabAnimation = new Animation(CRAB_SPRITES, 8);
		this.sprite = CRAB_SPRITES[0];
		
//		Calcular até o buraco
	}
	
	public void update() {
		x += dx*speed;
		y += dy*speed;
		
		if (Game.isPressed)	onMousePressed();
		
		checkHoleCollision();	
	}
	
	public void render(Graphics g) {
		
		g.drawImage(animate(),(int)x, (int)y, width, height, null);
		
//		renderMask(g);
	}

	private void onMousePressed() {
		Game.isPressed = false;
		if (Game.mx >= x && Game.mx <= x + width) {
			if (Game.my >= y && Game.my <= y + height) {
				Game.crabs.remove(this);
				Game.score++;
				
				Game.smokes.add(new Smoke((int)x, (int)y, width, height));
				return;
			}
	
		}
	}

	private void renderMask(Graphics g) {
		int centerX = ((int)x + width/2) - maskW/2;
		int centerY = ((int)y + height/2) - maskH/2 + 5;
		
//		render mask
		g.setColor(Color.blue);
		g.fillRect(centerX, centerY, maskW, maskH);

	}

	private void checkHoleCollision() {
		int centerX = ((int)x + width/2) - maskW/2;
		int centerY = ((int)y + height/2) - maskH/2 + 5;
		if (new Rectangle(centerX, centerY, maskW, maskH).intersects(Game.maskCastle)) {
			Game.crabs.remove(this);
			Game.takeDamage(1);;
			return;
		}
	}

	private BufferedImage animate() {
		sprite = crabAnimation.animate();
		return sprite;
	}
}
