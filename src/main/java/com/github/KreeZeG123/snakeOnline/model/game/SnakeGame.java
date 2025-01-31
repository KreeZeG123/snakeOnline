package com.github.KreeZeG123.snakeOnline.model.game;

import com.github.KreeZeG123.snakeOnline.model.Item;
import com.github.KreeZeG123.snakeOnline.model.agent.Snake;
import com.github.KreeZeG123.snakeOnline.model.InputMap;
import com.github.KreeZeG123.snakeOnline.model.factory.ItemFactory;
import com.github.KreeZeG123.snakeOnline.model.factory.SnakeFactory;
import com.github.KreeZeG123.snakeOnline.utils.*;

import java.util.*;
import java.util.stream.Collectors;

public class SnakeGame extends Game {

    /**
     * La carte du jeu
     */
    private final InputMap map;

    /**
     * La liste des agents de type snake
     */
    private List<Snake> snakeAgents;

    /**
     * La liste des items
     */
    private List<Item> items;

    /**
     * Le générateur aléatoire
     */
    public Random random = new Random();

    /**
     * Booléan qui indique si la map chargé n'a aucun mur
     */
    private Boolean aucunMur = null;

    /**
     * La liste des stratégies pour chaque snake
     */
    private final ArrayList<String> snakeStrategies;

    /**
     * Gestion des input des joueurs pour la stratégie controlable
     */
    private final Map<String, PlayerInput> playerInputs;

    /**
     * Nombre de joeurs (snake controlable) sans la partie
     */
    private int nbPlayers;

    /**
     * Constructeur de la classe Game
     *
     * @param maxturn le nombre de tours maximum
     * @param time    le temps de pause entre chaque tour en ms
     */
    public SnakeGame(int maxturn, long time, InputMap map, ArrayList<String> snakeStrategies) {
        super(maxturn, time);
        this.map = map;
        this.nbPlayers = 0;
        this.snakeStrategies = snakeStrategies;

        this.initializeGame();

        // Gestiopn des inputs
        this.playerInputs = new HashMap<>();
        this.playerInputs.put("Player1", new PlayerInput("", true));
        this.playerInputs.put("Player2", new PlayerInput("", true));
    }

    /**
     * Méthode qui initialise le jeu
     */
    public void init() {
        this.isRunning = false;
        this.turn = 0;
        this.nbPlayers = 0;
        for (PlayerInput playerInput : this.playerInputs.values()) {
            playerInput.setUsed(true);
        }
        initializeGame();
        this.propertyChangeSupport.firePropertyChange("turns", -1, 0);
    }

    @Override
    public void initializeGame() {
        // Gère le cas où aucune stratégie n'est spécifié
        if (this.snakeStrategies == null) {
            ArrayList<String> snakeStrategies = new ArrayList<>();
        }
        int nbOfSnake = this.map.getStart_snakes().size();
        if (this.snakeStrategies.size() < nbOfSnake) {
            this.snakeStrategies.clear();
            for (int i = 0; i < nbOfSnake; i++) {
                // Utilise des strategie composite (focus pomme quand petit, focus snake quand plus grand et evite sickness)
                snakeStrategies.add("CompositeA*");
            }
        }

        // Initialization des snakes
        this.snakeAgents = new ArrayList<>();
        ArrayList<FeaturesSnake> startSnakes = this.map.getStart_snakes();
        for (int i = 0; i < nbOfSnake; i++) {
            FeaturesSnake featuresSnake = startSnakes.get(i);
            String strategie = this.snakeStrategies.get(i);
            snakeAgents.add(SnakeFactory.createSnake(featuresSnake, strategie, this));
        }
        // Initialization des items
        this.items = new ArrayList<>();
        for (FeaturesItem featuresItem : this.map.getStart_items()) {
            items.add(ItemFactory.createItem(featuresItem));
        }
    }

    @Override
    public void takeTurn() {
        System.out.println("Tour "+this.turn+" du jeu en cours");
        for (Snake snake : snakeAgents) {
            snake.makeAction();
            Position newPos = Utils.movePosition(
                    snake.getPositions().get(0),
                    snake.getLastAction(),
                    map.getSizeX(),
                    map.getSizeY()
            );
            snake.move(newPos);
            snake.decreaseStatusTime();
        }
        checkInteraction();
    }

    public void checkInteraction() {
        checkCollisionWall();
        checkCollisionSnake();
        removeDeadSnakes();
        checkItems();
    }

    public void checkCollisionWall() {
        for (Snake snake : snakeAgents) {
            if ( snake.getInvincibleTime() <= 0 ) {
                Position snakeHeadPos = snake.getPositions().get(0);
                if (this.map.get_walls()[snakeHeadPos.getX()][snakeHeadPos.getY()]) {
                    snake.setDead(true);
                }
            }
        }
    }

    public void checkCollisionSnake() {
        // Récupère toutes les positions des corps des snakes (sans la tête)
        Map<Position, Snake> positionsBodySnakes = new HashMap<>();
        for (Snake snake : snakeAgents) {
            List<Position> body = snake.getPositions().subList(1, snake.getPositions().size());
            for (Position pos : body) {
                positionsBodySnakes.put(pos, snake);
            }
        }

        // Prépare une map pour gérer les collisions entre têtes
        Map<Position, List<Snake>> headCollisions = new HashMap<>();

        // Vérifie chaque snake
        for (Snake snake : snakeAgents) {
            // Ignore les snakes invincibles
            if (snake.getInvincibleTime() > 0) {
                continue;
            }

            Position snakeHeadPos = snake.getPositions().get(0);

            // Collision avec son propre corps
            if (positionsBodySnakes.containsKey(snakeHeadPos) && positionsBodySnakes.get(snakeHeadPos) == snake) {
                snake.setDead(true);
                continue;
            }

            // Collision avec un autre snake (tête ou corps)
            for (Snake otherSnake : snakeAgents) {
                if (snake == otherSnake) {
                    continue;
                }

                Position otherSnakeHeadPos = otherSnake.getPositions().get(0);

                // Collision de la tête du snake avec la tête d'un autre snake
                if (snakeHeadPos.equals(otherSnakeHeadPos)) {
                    if (snake.getPositions().size() >= otherSnake.getPositions().size()) {
                        otherSnake.setDead(true);
                    } else {
                        snake.setDead(true);
                    }
                }
                // Collision avec le corps d'un autre snake
                else if (otherSnake.getPositions().subList(1, otherSnake.getPositions().size()).contains(snakeHeadPos)) {
                    if (snake.getPositions().size() > otherSnake.getPositions().size()) {
                        otherSnake.setDead(true);
                    } else {
                        snake.setDead(true);
                    }
                }
            }
        }
    }


    public void checkItems() {
        int nbAppleEaten = 0;
        for (Snake snake : snakeAgents) {
            if ( snake.getSickTime() <= 0) {
                Position snakeHeadPos = snake.getPositions().get(0);
                for (Item item : items) {
                    if (item.getPosition().equals(snakeHeadPos) && !item.isUsed()) {
                        item.setUsed(true);
                        snake.setEffect(item.getItemType());
                        if ( item.getItemType().equals(ItemType.APPLE)) {
                            nbAppleEaten++;
                        }
                    }
                }
            }
        }
        this.items.removeIf(Item::isUsed);
        for (int i = 0; i < nbAppleEaten; i++) {
            summonApple();
            summonBonus();
        }
    }

    public void removeDeadSnakes() {
        this.snakeAgents.removeIf(Snake::isDead);
    }

    @Override
    public boolean gameContinue() {
        return !this.snakeAgents.isEmpty();
    }

    @Override
    public void gameOver() {
        System.out.println("Game Over");
    }

    public boolean isLegalMove(Snake snake, AgentAction action) {
        List<Position> positions = snake.getPositions();
        Position newPose = Utils.movePosition(
                positions.get(0),
                action,
                this.map.getSizeX(),
                this.getMap().getSizeY()
        );
        if ( positions.size() >= 2 ) {
            return
                    action.compareTo(Utils.invertAction(snake.getLastAction())) != 0 &&
                    !newPose.equals(snake.getPositions().get(1))
            ;
        }
        return true;
    }

    public void moveAgent(Snake snake, AgentAction action) {
        if ( isLegalMove(snake, action) ) {
            snake.setLastAction(action);
        }
    }

    public void summonBonus() {
        if ( this.random.nextDouble() < Item.PROBA_BONUS ) {
            Position emptyPosition = findEmptySpace();
            // Si de la place est disponible
            if (emptyPosition != null) {
                items.add(
                    new Item(
                        emptyPosition.getX(),
                        emptyPosition.getY(),
                        // Item aléatoire qui n'est pas une pomme
                        Arrays.stream(ItemType.values())
                            .filter(item -> item != ItemType.APPLE)
                            .toList()
                            .get(new Random().nextInt(ItemType.values().length - 1))
                    )
                );
            }
        }
    }

    public void summonApple() {
        Position emptyPosition = findEmptySpace();
        // Si de la place est disponible
        if (emptyPosition != null) {
            items.add(new Item(
                    emptyPosition.getX(),
                    emptyPosition.getY(),
                    ItemType.APPLE
            ));
        }
    }

    public Position findEmptySpace() {
        // Génère les positions vides
        Set<Position> emptyPositions = new HashSet<>();
        for (int x = 0; x < this.map.getSizeX(); x++) {
            for (int y = 0; y < this.map.getSizeY(); y++) {
                if (!this.map.get_walls()[x][y]) {
                    emptyPositions.add(new Position(x, y));
                }
            }
        }
        for (Snake snake : snakeAgents) {
            emptyPositions.removeAll(snake.getPositions());
        }
        for (Item item : items) {
            emptyPositions.remove(item.getPosition());
        }

        // Choix de la position
        if (! emptyPositions.isEmpty()) {
            return new ArrayList<>(emptyPositions).get(this.random.nextInt(emptyPositions.size()));
        }
        return null;
    }

    public List<Snake> getSnakeAgents() {
        return snakeAgents;
    }

    public List<Item> getItems() {
        return items;
    }

    public InputMap getMap() {
        return map;
    }

    public List<Position> voisinValide(Position pos, Set<Position> avoid) {
        // Gérer le cas où avoid est null en créant un ensemble vide
        Set<Position> positionsToAvoid;
        if ( avoid == null ) {
            positionsToAvoid = new HashSet<>();
        }
        else {
            positionsToAvoid = new HashSet<>(avoid);
        }


        // Positions des snakes et leurs corps
        Set<Position> positionsSerpent = new HashSet<>();
        for (Snake snake : snakeAgents) {
            positionsSerpent.addAll(snake.getPositions());
        }

        // Informations de la map
        boolean[][] murs = this.map.get_walls();
        int[] maxXY = {this.map.getSizeX(), this.map.getSizeY()};

        // Liste des voisins possibles
        List<Position> voisins = new ArrayList<>();
        for (AgentAction a : AgentAction.values()) {
            voisins.add(Utils.movePosition(pos,a,maxXY[0],maxXY[1]));
        }

        // Filtre les voisins valides
        return voisins.stream()
                .filter(position ->
                        !positionsSerpent.contains(position) &&
                        !murs[position.getX()][position.getY()] &&
                        !positionsToAvoid.contains(position)
                )
                .collect(Collectors.toList());
    }

    public boolean shouldUseManhattanWrap(Snake snake) {
        // Renvoie vrai si le snake est invincible car il peut passer au travers des murs
        if (snake.getInvincibleTime() > 0) {
            return true;
        }
        
        // Sinon on regarde si c'est une map sans aucun murs pour globaliser
        if ( this.aucunMur == null ) {
            boolean sansWall = true;
            for (boolean[] row : this.getMap().get_walls()) {
                for (boolean wall : row) {
                    if (wall) {
                        sansWall = false;
                        break;
                    }
                }
                if (!sansWall) break;
            }
            this.aucunMur = sansWall;
        }
        return this.aucunMur;
    }

    public Set<Position> getSnakesPositions() {
        Set<Position> snakesPositions = new HashSet<>();
        for ( Snake snake : snakeAgents ) {
            snakesPositions.addAll(snake.getPositions());
        }
        return snakesPositions;
    }

    public Set<Position> getWallsPositions() {
        Set<Position> wallsPositions = new HashSet<>();
        for (int x = 0; x < this.map.getSizeX(); x++) {
            for (int y = 0; y < this.map.getSizeY(); y++) {
                if (this.map.get_walls()[x][y]) {
                    wallsPositions.add(new Position(x, y));
                }
            }
        }
        return wallsPositions;
    }

    public Set<Position> getItemsPositions(ItemType itemType) {
        return getItemsPositions(Collections.singletonList(itemType));
    }

    public Set<Position> getItemsPositions(List<ItemType> itemTypes) {
        Set<Position> itemsPositions = new HashSet<>();
        for (Item item : items) {
            if (itemTypes.contains(item.getItemType())) {
                itemsPositions.add(item.getPosition());
            }
        }
        return itemsPositions;
    }

    // Méthode pour mettre à jour les entrées
    public void updatePlayerInput(String player, String key, boolean used) {
        if (playerInputs.containsKey(player)) {
            playerInputs.get(player).setLastKey(key);
            playerInputs.get(player).setUsed(used);
        }
    }

    // Méthode pour récupérer l'entrée d'un joueur
    public PlayerInput getPlayerInput(String player) {
        return playerInputs.get(player);
    }

    public int getNbPlayers() {
        return this.nbPlayers;
    }

    public void addPlayer() {
        this.nbPlayers++;
    }
}
