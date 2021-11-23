import java.awt.Canvas;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;

public class Game extends Canvas implements Runnable, MouseListener, KeyListener{
	
	
	private static final long serialVersionUID = 1L;
	
	public static int WIDTH = 480;
	public static int HEIGHT = 480;
	
	public static List<Crab> crabs;
	public static List<Smoke> smokes;
	
	public Spawner spawner;
	
	public static Spritesheet spritesheet = new Spritesheet("/spritesheet.png");;
	
	private BufferedImage TITLE;
	private BufferedImage HEART;
	private BufferedImage CASTLE;
	
	private BufferedImage[] CRAB = {
			Game.spritesheet.getSprite(0 * 16, 0 * 16, 16, 16),
			Game.spritesheet.getSprite(1 * 16, 0 * 16, 16, 16),
			Game.spritesheet.getSprite(2 * 16, 0 * 16, 16, 16),
			Game.spritesheet.getSprite(3 * 16, 0 * 16, 16, 16)
	};
	
	private BufferedImage[] SMOKE = {
			Game.spritesheet.getSprite(0 * 16, 1 * 16, 16, 16),
			Game.spritesheet.getSprite(1 * 16, 1 * 16, 16, 16),
			Game.spritesheet.getSprite(2 * 16, 1 * 16, 16, 16),
			Game.spritesheet.getSprite(3 * 16, 1 * 16, 16, 16),
			Game.spritesheet.getSprite(4 * 16, 1 * 16, 16, 16),
			Game.spritesheet.getSprite(5 * 16, 1 * 16, 16, 16),
			Game.spritesheet.getSprite(6 * 16, 1 * 16, 16, 16),
			Game.spritesheet.getSprite(7 * 16, 1 * 16, 16, 16)
	};

//	Jframe
	private JFrame frame;
	
//	Crab 
	private Animation crabAnimation;
	private BufferedImage crabSprite;
	private int crabX = 0, crabY = HEIGHT/2;
	private int crabW = 50, crabH = 50;
	private boolean crabAnimating = true;

//	 Title
	private int titleW = 144;
	private int titleH = 112;
	private int titleX = WIDTH/2 - titleW/2;
	private int initialTitleY = 0 - titleH, titleY = initialTitleY;
	private double titleSpeed;
	private double titleGravity = 1.2;
	private boolean titleGravityOn = false;
	private int titleCountTime = 0, titleCountDown = 85;
	private int ground = (HEIGHT/2 - titleH) + crabH;
	
	
//	Smoke
	private Animation smokeAnimation;
	private BufferedImage smokeSprite;
	private boolean smokeAnimating = false;
	
	
//	Mouse 	
	public static int mx, my;
	public static boolean isPressed = false;
	private Cursor cursor;
	Image cursorPoint1;
	Image cursorPoint2;
	private Toolkit toolKit;
	
//	UI
	public static int initialScore = 0, score = initialScore;
	public static int initialLife = 1,  life = initialLife;
	
//	Press Enter
	private boolean pressEnterVisible = true;
	private int visibilityFrames = 0, maxVisibilityFrames = 30;
	
//	Hole
	public static Rectangle maskCastle;
	private int castleW = 20, castleH = 20;
	
//	Game State
	public static final int INTRO_SCREEN = 0, START_SCREEN = 1, PLAYING = 2, GAME_OVER = 3;
	public static int gameState = INTRO_SCREEN;
	
//	Game Over
	private int gameOverFrames = 0, gameOverTime = 60 * 2;
	
	private int counter = 0;
	
	public Game() {
		this.setPreferredSize(new Dimension(WIDTH,HEIGHT));
		this.addMouseListener(this);
		this.addKeyListener(this);
		
		initFrame();
		
//		spritesheet = new Spritesheet("/spritesheet.png");
		crabs = new ArrayList<>();
		smokes = new ArrayList<>();
		spawner = new Spawner();
		
		TITLE = spritesheet.getSprite(0 * 16, 3 * 16, 144, 112);
		
		HEART = spritesheet.getSprite(0 * 16, 2 * 16, 16, 14);
		
		CASTLE = spritesheet.getSprite(4 * 16, 0 * 16, 16, 16);
				
		crabAnimation = new Animation(CRAB, 10);
		
		smokeAnimation = new Animation(SMOKE, 5);
		smokeAnimation.setLoop(false);
		
//		Máscara do buraco
		maskCastle = new Rectangle(WIDTH/2 - castleW/2, HEIGHT/2 - castleH/2, castleW, castleH);
		
	}
	
	public void update() {
		countFPS(false);
					
		switch (gameState) {
		case INTRO_SCREEN:
			updateIntro();
			break;
		
		case START_SCREEN:
			switchPressEnterVisibility();
			break;
		
		case PLAYING:
			spawner.update();
			updateCrabs();
			updateSmokes();
			break;
		case GAME_OVER:
			updateGameOver();
			break;
			
			
		}
		
	}
	
	public void render() {
			BufferStrategy bs = this.getBufferStrategy();
			if (bs == null) {
				this.createBufferStrategy(3);
				return;
			}
			
			Graphics g = bs.getDrawGraphics();
			
			renderBackground(g);
			
			switch (gameState) {
			
			case INTRO_SCREEN:
				renderIntro(g);
				break;
			
			case START_SCREEN:
				renderStartScreen(g);
				
				break;
			
			case PLAYING:
				renderCastle(g);
				renderCrabs(g);
				renderSmokes(g);
				
				renderUI(g);
				break;
				
			case GAME_OVER:				
				renderGameOver(g);
				break;
			}
			
						
	//		render mask
	//		g.setColor(Color.green);
	//		g.fillRect(WIDTH/2 - castleW/2, HEIGHT/2 - castleH/2, castleW, castleH);
							
			g.dispose();
			bs.show();
		}
	

	public static void takeDamage(int damage) {
		life -= damage;
		if (life < 0) gameState = GAME_OVER;
	}

	private void switchPressEnterVisibility() {
		visibilityFrames++;
		if (visibilityFrames == maxVisibilityFrames) {
			visibilityFrames = 0;
			pressEnterVisible = !pressEnterVisible;
		}
	}

	private void updateIntro() {
		if (crabAnimating) crabX+=2;
		
		if (titleCountDown()) {
			titleGravityOn = true;
		}
		if (titleGravityOn) {
			dropTitle();
		}

	}

	private void renderIntro(Graphics g) {
		if (crabAnimating) {			
			crabSprite = crabAnimation.animate();
			g.drawImage(crabSprite, crabX, crabY, crabW, crabH, null);
		}
		
		if (crabX >= (WIDTH/2) - 25) {
			crabAnimating = false;
			smokeAnimating = true;
		}
		
		if (smokeAnimating) {
			smokeSprite = smokeAnimation.animate();
			g.drawImage(smokeSprite, crabX, crabY, crabW, crabH, null);
			if (smokeAnimation.isFinished()) {
				smokeAnimating = false;
				gameState = START_SCREEN;
			}
		}
		
//		Render title
		g.drawImage(TITLE, titleX, titleY, titleW, titleH, null);
		
	}

	private boolean titleCountDown() {
		titleCountTime++;
		if (titleCountTime == titleCountDown) {
			return true;
		}
		return false;
	}

	private void dropTitle() {
		if (titleY >= ground) {
			titleY = ground;
			titleSpeed -= 1.5 * titleSpeed;
		}
		titleSpeed += titleGravity;
		titleY += titleSpeed;
						
	}

	private void renderStartScreen(Graphics g) {
		g.drawImage(TITLE, titleX, titleY, titleW, titleH, null);
		
		if (pressEnterVisible) renderText("Arial", Font.BOLD, 25, "<Press Enter to Start>", "center", WIDTH/2, 350, Color.orange, g);

		
	}

	private void updateGameOver() {
		gameOverFrames++;
		if (gameOverFrames == gameOverTime) {
			gameOverFrames = 0;
			gameState = INTRO_SCREEN;
			resetIntro();
			
		}
	}

	private void renderGameOver(Graphics g) {
		renderText("Arial", Font.BOLD, 25, "Your Score", "center", WIDTH/2, 100, Color.green, g);
		renderText("Arial", Font.BOLD, 50, Integer.toString(score), "center", WIDTH/2, 160, Color.green, g);
		renderText("Arial", Font.BOLD, 40, "Game Over", "center", WIDTH/2, HEIGHT/2, Color.red, g);
		
	}

	private void updateSmokes() {
		for (int i = 0; i < smokes.size(); i++) {
			smokes.get(i).update();
		}	
	}

	private void countFPS(boolean on) {
		if (on) {
			counter ++;
			if (counter%60 == 0) {
				System.out.println(counter);
				counter = 0;
			}		
		}
			
	}

	private void updateCrabs() {
		for (int i = 0; i < crabs.size(); i++) {
			crabs.get(i).update();
		}		
	}

	private void renderSmokes(Graphics g) {
		for (int i = 0; i < smokes.size(); i++) {
			smokes.get(i).render(g);
		}
	}

	private void renderUI(Graphics g) {
		g.setColor(Color.black);
		g.setFont(new Font("arial", Font.BOLD, 22));
		g.drawString("Score: " + score, 10, 27);
		
//		Hearts		
		for (int i = 0; i < life; i++) {
			
			g.drawImage(HEART, 390 + (25 * i), 8, 16 * 3, 14 * 3, null);
		}
		
	}
	
	private void renderText(String font, int style, int size, String text, String align, int x, int y, Color color, Graphics g) {
		int textAlignment = 0;
		g.setColor(color);
		g.setFont(new Font(font, style, size));
		
		Rectangle2D stringBound = g.getFontMetrics().getStringBounds(text, g);
		
		switch (align) {
		case "center":
			textAlignment = (int) stringBound.getCenterX();
			break;
		case "right":
			textAlignment = 0;
			break;
		case "left":
			textAlignment = (int) stringBound.getWidth();
		}
		
		g.drawString(text, x - textAlignment, y);
	}

	private void renderCrabs(Graphics g) {
		for (int i = 0; i < crabs.size(); i++) {
			crabs.get(i).render(g);
		}		
	}

	private void renderCastle(Graphics g) {
		
		g.drawImage(CASTLE, WIDTH/2 - 21, HEIGHT/2 - 21, 42, 42, null);
		
	}

	private void renderBackground(Graphics g) {
		g.setColor(new Color(255,255,127));
		g.fillRect(0, 0, WIDTH, HEIGHT);		
	}

	private void resetIntro() {
		crabX = 0;
		crabAnimating = true;
		smokeAnimation.resetAnimation();
		titleY = initialTitleY;
		titleSpeed = 0;
		titleCountTime = 0;
		titleGravityOn = false;
		
	}
	
	private void startGame() {
		life = initialLife;
		score = initialScore;
	}

	public static void main(String[] args) {
		Game game = new Game();
		new Thread(game).start();
		
	}
	
	public void initFrame() {
		frame = new JFrame();
		frame.setTitle("Catch the Crab");
		frame.add(this);
		frame.pack();
		
//		Definindo cursor do mouse
		toolKit = Toolkit.getDefaultToolkit();
		cursorPoint1 = toolKit.getImage(getClass().getResource("/Shovel_Idle.png"));
		cursorPoint2 = toolKit.getImage(getClass().getResource("/Shovel_Down.png"));
		cursor = toolKit.createCustomCursor(cursorPoint1, new Point(0, 0), "img");
		
		frame.setCursor(cursor);
		frame.setResizable(false);
		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		frame.setVisible(true);
	}
		
	@Override
	public void run() {
		double fps = 60.0;
		while(true) {
			update();
			render();
			
			requestFocus();
			
//			Define o FPS. 1000 milessegundos = 1 segundo. 1000 milessegundos / 60 = 60 Frames Por Segundos
			try {
				Thread.sleep((int)(1000/fps));
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed(MouseEvent e) {
		switch (gameState) {
		case PLAYING:
			isPressed = true;
			mx = e.getX();
			my = e.getY();
			break;
		}
		
		cursor = toolKit.createCustomCursor(cursorPoint2, new Point(0, 0), "img");
		frame.setCursor(cursor);
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		isPressed = false;
		cursor = toolKit.createCustomCursor(cursorPoint1, new Point(0, 0), "img");
		frame.setCursor(cursor);

	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyPressed(KeyEvent e) {
		switch (gameState) {
		case START_SCREEN:
			if (e.getKeyCode() == KeyEvent.VK_ENTER) {
				startGame();
				gameState = PLAYING;
			}
			break;
		}
	}
	
	@Override	
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}
}
