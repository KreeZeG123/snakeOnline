package com.github.KreeZeG123.snakeOnline.model;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Serializable;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import com.github.KreeZeG123.snakeOnline.model.dto.snakeGame.GameStartDTO;
import com.github.KreeZeG123.snakeOnline.utils.*;

public class InputMap implements Serializable {

	private static final long serialVersionUID = 1L;

	private int size_x;
	private int size_y;
	private boolean walls[][];
	private ArrayList<FeaturesSnake> start_snakes ;
	private ArrayList<FeaturesItem> start_items ;

	private String filename;
	private BufferedReader buffer;
	ColorSnake[] colorSnake = ColorSnake.values();

	public InputMap(GameStartDTO parsedLoginData) {
		this.size_x = parsedLoginData.width;
		this.size_y = parsedLoginData.height;
		this.walls = new boolean[parsedLoginData.width][parsedLoginData.height];
		for (Position wall : parsedLoginData.walls) {
			this.walls[wall.getX()][wall.getY()] = true;
		}
		this.start_items = parsedLoginData.startItems;
		this.start_snakes = parsedLoginData.startSnakes;
	}

	public InputMap(
			int width,
			int height,
			boolean[][] walls,
			List<FeaturesItem> startItems,
			List<FeaturesSnake> startSnakes
	) {
		this.size_x = width;
		this.size_y = height;
		this.walls = walls;
		this.start_items = new ArrayList<>(startItems);
		this.start_snakes = new ArrayList<>(startSnakes);
	}

	public InputMap(String filename) throws Exception{
		
		this.filename = filename;
		
		try{

			ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
			URL urlFile = classLoader.getResource(filename);

			if (urlFile == null) {
				throw new Exception("Fichier introuvable");
			}

			InputStream flux = new FileInputStream(urlFile.getPath());
			InputStreamReader lecture =new InputStreamReader(flux);
			buffer = new BufferedReader(lecture);

			String ligne;

			int nbX=0;
			int nbY=0;

			while ((ligne = buffer.readLine())!=null)
			{
				ligne = ligne.trim();
				if (nbX==0) {nbX = ligne.length();}
				else if (nbX != ligne.length()) throw new Exception("Toutes les lignes doivent avoir la même longueur");
				nbY++;
			}
			buffer.close();

			size_x = nbX;
			size_y = nbY;

			walls = new boolean [size_x][size_y];

			flux = new FileInputStream(urlFile.getPath());
			lecture = new InputStreamReader(flux);
			buffer = new BufferedReader(lecture);
			int y=0;


			start_snakes = new ArrayList<FeaturesSnake>();
			start_items = new ArrayList<FeaturesItem>();

			int id = 0;

			while ((ligne=buffer.readLine())!=null)
			{
				ligne=ligne.trim();

				for(int x=0;x<ligne.length();x++)
				{

					if (ligne.charAt(x)=='%')
						walls[x][y]=true;

					else walls[x][y]=false;



					if (ligne.charAt(x)=='S' ) {

						ArrayList<Position> pos = new ArrayList<Position>();
						pos.add(new Position(x,y));

						start_snakes.add(new FeaturesSnake(pos, AgentAction.MOVE_DOWN,colorSnake[id%colorSnake.length], false, false));
						id++;
					}

					if (ligne.charAt(x)=='A') {


						start_items.add(new FeaturesItem(x, y, ItemType.APPLE));


					}

					if (ligne.charAt(x)=='B') {


						start_items.add(new FeaturesItem(x, y, ItemType.BOX));


					}

					if (ligne.charAt(x)=='Y') {


						start_items.add(new FeaturesItem(x, y, ItemType.SICK_BALL));


					}

					if (ligne.charAt(x)=='M') {


						start_items.add(new FeaturesItem(x, y, ItemType.INVINCIBILITY_BALL));


					}

				}
				y++;
			}

			buffer.close();

		}catch (Exception e){
			System.out.println("Erreur: "+e.getMessage());
		}

	}

	public int getSizeX() {return(size_x);}

	public int getSizeY() {return(size_y);}
	
	public String getFilename(){
		return filename;
	}

	public boolean[][] get_walls() {
		return walls;
	}

	public ArrayList<FeaturesSnake> getStart_snakes() {
		return start_snakes;
	}

	public ArrayList<FeaturesItem> getStart_items() {
		return start_items;
	}

}
