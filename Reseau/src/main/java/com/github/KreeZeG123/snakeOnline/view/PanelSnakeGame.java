package com.github.KreeZeG123.snakeOnline.view;


import com.github.KreeZeG123.snakeOnline.utils.*;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.image.RescaleOp;

import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import java.net.URL;
import java.util.ArrayList;
import javax.swing.JPanel;


/** 
 * Classe qui permet de charger d'afficher le panneau du jeu à partir d'une carte et de listes d'agents avec leurs positions.
 * 
 */


public class PanelSnakeGame extends JPanel{

	private static final long serialVersionUID = 1L;

	
	protected Color ground_Color= new Color(0,0,0);


	private int sizeX;
	private int sizeY;

	private int fen_x;
	private int fen_y;
	
	private double stepx;
	private double stepy;

	private String skinChoisie;
	
	float[] contraste = { 0, 0, 0, 1.0f };


	protected ArrayList<FeaturesSnake> featuresSnakes = new ArrayList<FeaturesSnake>();
	protected ArrayList<FeaturesItem> featuresItems = new ArrayList<FeaturesItem>();

	
	private boolean[][] walls;
	
	
	int cpt;

	public PanelSnakeGame(int sizeX, int sizeY, boolean[][] walls, ArrayList<FeaturesSnake> featuresSnakes, ArrayList<FeaturesItem> featuresItems, String skinChoisie) {

		this.sizeX = sizeX;
		this.sizeY = sizeY;
		this.walls = walls;	
		this.featuresSnakes = featuresSnakes;
		this.featuresItems = featuresItems;
		this.skinChoisie = skinChoisie;
	}

	public void paint(Graphics g){

		fen_x = getSize().width;
		fen_y = getSize().height;

		this.stepx = fen_x/(double)sizeX;
		this.stepy = fen_y/(double)sizeY;
		
		g.setColor(ground_Color);
		g.fillRect(0, 0,fen_x,fen_y);

		double position_x=0;

		for(int x=0; x<sizeX; x++)
		{
			double position_y = 0 ;
			
			for(int y=0; y<sizeY; y++)
			{
				if (walls[x][y]){

					try {
						// Utiliser le ClassLoader pour charger l'image
						ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
						URL imageUrl = classLoader.getResource("images/wall.png");
						if (imageUrl == null) {
							throw new IOException("Image non trouvée dans le classpath.");
						}
						// Charger l'image
						Image img = ImageIO.read(imageUrl);
						g.drawImage(img, (int)position_x, (int)position_y, (int)stepx, (int)stepy, this);
					} catch (IOException e) {
						e.printStackTrace();
						
					}
				}

				position_y+=stepy;				
			}
			position_x+=stepx;
		}

		for(int i = 0; i < featuresSnakes.size(); i++){
			paint_Snake(g,featuresSnakes.get(i));	
		}

		for(int i = 0; i < featuresItems.size(); i++){
			paint_Item(g,featuresItems.get(i));	
		}
			
		cpt++;
	}


	void paint_Snake(Graphics g, FeaturesSnake featuresSnake)
	{
		ArrayList<Position> positions = featuresSnake.getPositions();
		
		AgentAction lastAction = featuresSnake.getLastAction();

		
		BufferedImage img = null;
		

		double pos_x;
		double pos_y;
		
		int cpt_img = -1;
		
		String skin = featuresSnake.getColorSnake().toString().toLowerCase();
		if ( skinChoisie != null && !skinChoisie.isBlank() ) {
			skin = "skin_" + skinChoisie;
		}
		
		for(int i = 0; i < positions.size(); i++) {
			
			pos_x=positions.get(i).getX()*stepx;
			pos_y=positions.get(i).getY()*stepy;


			if(i == 0) {
				switch (lastAction) {
					case MOVE_UP:
						cpt_img = 0;
						break;
					case MOVE_DOWN:
						cpt_img = 1;
						break;
					case MOVE_RIGHT:
						cpt_img = 2;
						break;
					case MOVE_LEFT:
						cpt_img = 3;
						break;

					default:
						break;
				}
				
			} else {	
				cpt_img = 4;
			}
			
			try {
				// Utiliser le ClassLoader pour charger l'image
				ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
				System.out.println("skin : " + skin);
				System.out.println("cpt_img : " + cpt_img);
				String imagePath = "images/snake_" + skin + "_" + cpt_img + ".png";
				System.out.println("img : " + imagePath);
				URL imageUrl = classLoader.getResource(imagePath);
				if (imageUrl == null) {
					throw new IOException("Image non trouvée dans le classpath : " + imagePath);
				}
				// Charger l'image
				img = ImageIO.read(imageUrl);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			
			float [] scales = new float[]{1 ,1, 1, 1.0f };
			
			if (featuresSnake.isInvincible())
		
				scales = new float[]{3 ,0.75f, 3, 1.0f };
	
			if (featuresSnake.isSick())
				scales = new float[]{1.5f ,1.5f, 0.75f, 1.0f };
			
			
			RescaleOp op = new RescaleOp(scales, contraste, null);
			img = op.filter( img, null);
			
			
			if(img != null) {
				g.drawImage(img, (int)pos_x, (int)pos_y, (int)stepx, (int)stepy, this);
			}
			
			
			
		}
			
		
	}

	

	void paint_Item(Graphics g, FeaturesItem featuresItem){



		int x = featuresItem.getX();
		int y = featuresItem.getY();

		double pos_x=x*stepx;
		double pos_y=y*stepy;

		if (featuresItem.getItemType() == ItemType.APPLE) {
			try {
				// Utiliser le ClassLoader pour charger l'image
				ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
				URL imageUrl = classLoader.getResource("images/apple.png");
				if (imageUrl == null) {
					throw new IOException("Image non trouvée dans le classpath.");
				}
				// Charger l'image
				Image img = ImageIO.read(imageUrl);
				g.drawImage(img, (int)pos_x, (int)pos_y, (int)stepx, (int)stepy, this);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		if (featuresItem.getItemType() == ItemType.BOX) {
			try {
				// Utiliser le ClassLoader pour charger l'image
				ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
				URL imageUrl = classLoader.getResource("images/mysteryBox.png");
				if (imageUrl == null) {
					throw new IOException("Image non trouvée dans le classpath.");
				}
				// Charger l'image
				Image img = ImageIO.read(imageUrl);
				g.drawImage(img, (int)pos_x, (int)pos_y, (int)stepx, (int)stepy, this);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		
		if (featuresItem.getItemType() == ItemType.SICK_BALL) {
			try {
				// Utiliser le ClassLoader pour charger l'image
				ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
				URL imageUrl = classLoader.getResource("images/sickBall.png");
				if (imageUrl == null) {
					throw new IOException("Image non trouvée dans le classpath.");
				}
				// Charger l'image
				Image img = ImageIO.read(imageUrl);
				g.drawImage(img, (int)pos_x, (int)pos_y, (int)stepx, (int)stepy, this);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		if (featuresItem.getItemType() == ItemType.INVINCIBILITY_BALL) {
			try {
				// Utiliser le ClassLoader pour charger l'image
				ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
				URL imageUrl = classLoader.getResource("images/invicibleBall.png");
				if (imageUrl == null) {
					throw new IOException("Image non trouvée dans le classpath.");
				}
				// Charger l'image
				Image img = ImageIO.read(imageUrl);
				g.drawImage(img, (int)pos_x, (int)pos_y, (int)stepx, (int)stepy, this);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		
	}

	public void updateInfoGame( ArrayList<FeaturesSnake> featuresSnakes , ArrayList<FeaturesItem> featuresItems) {

		if (featuresSnakes != null) {
			this.featuresSnakes = featuresSnakes;
		}
		if (featuresItems != null) {
			this.featuresItems = featuresItems;
		}

	}

	public int getSizeX() {
		return sizeX;
	}

	public int getSizeY() {
		return sizeY;
	}


	public ArrayList<FeaturesItem> getFeaturesItems() {
		return featuresItems;
	}
}
