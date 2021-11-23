import java.awt.image.BufferedImage;

public class Animation {

	private BufferedImage[] bfImage;
	private BufferedImage sprite;
	private int framesAnimation = 0, maxFramesAnimation;
	private int minSprites, maxSprites, curSprite;
	private boolean loop = true;
	private boolean finished = false;
	
	
	public Animation(BufferedImage[] bfImage, int maxFramesAnimation) {
		this.bfImage = bfImage;
		this.maxFramesAnimation = maxFramesAnimation;
		this.minSprites = 0;
		this.maxSprites = bfImage.length;
		this.curSprite = 0;

	}
	
	public BufferedImage animate() {
		if (!finished) {
			 
			framesAnimation++;
			if(framesAnimation == maxFramesAnimation) {
				framesAnimation = 0;
				curSprite++;
					if (curSprite == maxSprites) {
						if (loop) {curSprite = minSprites;}
						else {
							finished = true;
							curSprite = maxSprites - 1;
						}
					}
					
			}
		}
		
		sprite = bfImage[curSprite];
		return sprite;
	}
	
	public void setLoop(boolean state) {
		loop = state;
	}
	
	public void resetAnimation() {
		finished = false;
		framesAnimation = 0;
		curSprite = 0;
	}
	
	public void setRange(int minSprites, int maxSprites) {
		this.minSprites = minSprites;
		this.maxSprites = maxSprites + 1;
	}
	
	public void setRangeDefaut(){		
		this.minSprites = 0;
		this.maxSprites = bfImage.length;
	}

	public boolean isFinished() {
		return finished;
	}
}
