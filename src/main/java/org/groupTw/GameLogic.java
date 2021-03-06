package org.groupTw;

import org.groupTw.MapEnitites.Entity;
import org.groupTw.MapEnitites.MovingUnit;

import java.awt.*;
import java.util.ArrayList;

/**
 * Class that implements iLogic interface. Handles all user input in game
 */
public class GameLogic implements iLogic {
    /**
     * Player who won the game.
     */
    static protected Player winner;
    /**
     * number of rounds played.
     */
    private int roundCounter;
    /**
     * possible attacks of current selected unit.
     */
    private ArrayList<MapPanel> possibleAttacks;
    /**
     * possible moves of current selected unit.
     */
    private ArrayList<MapPanel> possibleMoves;
    /**
     * previous selected MapPanel.
     */
    private MapPanel selected;
    /**
     * Array of currently playing players.
     */
    private Player[] playersArr;
    /**
     * Player who can play in this turn.
     */
    private Player currentPlayer;
    /**
     * Entity which is requested to highlight their statistics in statistics tab.
     */
    private Entity toShow;


    /**
     * creates GameLogic instance with given players
     *
     * @param playersArr_ players who play game
     */
    public GameLogic(Player[] playersArr_) {
        playersArr = playersArr_;
        possibleAttacks = new ArrayList<>();
        possibleMoves = new ArrayList<>();
        currentPlayer = playersArr[roundCounter % 2];
        selected = null;
        winner = null;
    }


    /**
     * action method handles all action invoked by mouse click, checks if it's second click on the same tile, if emptny
     * tile was clicked, or if unit of current active player was clicked.
     *
     * @param tile_     clicked tile on game map - MapPanel type
     * @param mapTiles_ whole map, double-dim array of MapPanels
     */
    public void action(MapPanel tile_, MapPanel[][] mapTiles_) {
        currentPlayer = playersArr[roundCounter % 2];
        ArrayList<Entity> currentArmy = currentPlayer.getArmy();
        toShow = null;
        //first click on unit, which is in your army
        if (selected == null && currentArmy.contains(tile_.getEntity_on_tile())) {
            tile_.setBorder(Color.RED, 2);
            tileNotSelected(tile_, mapTiles_);
            selected = tile_;
            //second click on a tile other than selected
        } else if (selected != null && !selected.equals(tile_) ) {
            tileSelected(tile_);
            selected.setBorder(Color.BLACK, 0);
            selected = null;


            //second click on the same tile
        } else if (selected != null && selected.equals(tile_)) {
            toShow = selected.getEntity_on_tile();
            selected.setBorder(Color.BLACK, 0);
            selected = null;
            clearArrMoves();
            clearArrAttacks();
            //first click on a tile without unit, or on a tile which contains inactive player unit
        } else {
            selected = null;
            clearArrAttacks();
            clearArrMoves();
        }

    }

    /**
     * This method checks if the game ended, updates turn counter
     */
    private void updateBoardState() {
        if (playersArr[(roundCounter + 1) % 2].getArmy().size() == 0) {
            winner = currentPlayer;
        }
        for (Player ply : playersArr) {
            ply.getGoldPerTurn();
        }
        roundCounter++;
    }

    /**
     * Method updates map state, when no tile was selected
     * @param Tile_ currently selected tile
     * @param mapTiles_ map of game
     */
    private void tileNotSelected(MapPanel Tile_, MapPanel[][] mapTiles_) {
        if (Tile_.getEntity_on_tile() instanceof iMovable) {
            setMoveBorders(Tile_, mapTiles_, Color.MAGENTA);
        }
        setAttackBorders(Tile_, mapTiles_, Color.ORANGE);
    }

    /**
     * Updates map state, when tile was selected before. Checks if unit can move or attack tile
     *
     * @param Tile_ currently selected tile
     */
    private void tileSelected(MapPanel Tile_) {
        //checks if unit can move, if it can, checks if its possible to move to the selected tile
        if (this.selected.getEntity_on_tile() instanceof iMovable && this.possibleMoves.contains(Tile_) && Tile_ != selected) {
            moveEntity(Tile_, selected);
            updateBoardState();
            //every entity can attack, so it only checks if tile is possible to attack
        } else if (this.possibleAttacks.contains(Tile_) && Tile_ != selected) {
            attackEntity(Tile_);
            updateBoardState();
        }
        //clear highlights
        this.clearArrAttacks();
        this.clearArrMoves();
    }

    /**
     * Method updates possibleAttacks array, changes borders of tiles possible to attack
     * @param Tile_ currenty selected tile
     * @param mapTiles_ map of game
     * @param color_ color to which border color will be changed
     */
    private void setAttackBorders(MapPanel Tile_, MapPanel[][] mapTiles_, Color color_) {
        Entity entityOnTile = Tile_.getEntity_on_tile();
        this.possibleAttacks = new ArrayList<>();
        for (Point point : entityOnTile.getPossible_attacks()) {
            for (int i = 0; i < AppFrame.MAPSIZE; i++) {
                for (int j = 0; j < AppFrame.MAPSIZE; j++) {
                    Point tilePosition = (Point) mapTiles_[i][j].getClientProperty("Position");
                    if (tilePosition.equals(point) && mapTiles_[i][j].isOccupied() && mapTiles_[i][j].getOwner() != Tile_.getOwner()) {
                        mapTiles_[i][j].setBorder(color_, 1);
                        this.possibleAttacks.add(mapTiles_[i][j]);
                    }
                }
            }
        }
    }

    /**
     * Method updates possibleAttacks array, changes borders of tiles possible to move
     * @param Tile_ currently selected tile
     * @param mapTiles_ map of game
     * @param color_ color to which border color will be changed
     */
    private void setMoveBorders(MapPanel Tile_, MapPanel[][] mapTiles_, Color color_) {
        iMovable entityOnTile = (iMovable) Tile_.getEntity_on_tile();
        this.possibleMoves = new ArrayList<>();
        for (Point point : entityOnTile.getPossible_moves()) {
            for (int i = 0; i < AppFrame.MAPSIZE; i++) {
                for (int j = 0; j < AppFrame.MAPSIZE; j++) {
                    Point tilePosition = (Point) mapTiles_[i][j].getClientProperty("Position");
                    if (tilePosition.equals(point) && !mapTiles_[i][j].isOccupied()) {
                        mapTiles_[i][j].setBorder(color_, 1);
                        this.possibleMoves.add(mapTiles_[i][j]);
                    }
                }
            }
        }
    }


    /**
     * Moves entity which is positioned on MapPanel selected_ to MapPanel tile_ .
     * Asserts unit can move.
     *
     * @param tile_     currently selected mapPanel.
     * @param selected_ previous selected mapPanel.
     */
    private void moveEntity(MapPanel tile_, MapPanel selected_) {
        MovingUnit entity = (MovingUnit) selected_.getEntity_on_tile();
        entity.Move((Point) tile_.getClientProperty("Position"));
        tile_.setEntity_on_tile(entity);
        tile_.setOwner(selected_.getOwner());
        selected_.removeAll();
        selected_.setEntity_on_tile(null);
        selected_.setOwner(null);
        clearArrMoves();
        clearArrAttacks();

    }

    /**
     * Unit on a selected tile attacks and, if HP drops to 0 kills, Entity on MapPanel tile_ .
     * All Entities can attack .
     *
     * @param tile_ attacked MapPanel
     */
    public void attackEntity(MapPanel tile_) {

        int attackValue = selected.getEntity_on_tile().getAttack();
        if (!tile_.getEntity_on_tile().getDamage(attackValue)) {

            Player ply = this.playersArr[(roundCounter + 1) % 2]; //get owner of killed unit
            ply.getArmy().remove(tile_.getEntity_on_tile());

            tile_.removeAll();
            tile_.setEntity_on_tile(null);
            tile_.setOwner(null);

        }
        clearArrMoves();
        clearArrAttacks();


    }

    /**
     * Clears possibleAttacks array and change borders to normal (black) state.
     */
    private void clearArrAttacks() {
        for (MapPanel panel : possibleAttacks) {
            panel.setBorder(Color.BLACK, 0);
        }
        this.possibleAttacks.clear();
    }

    /**
     * Clears possibleMoves array and change borders to normal (black) state.
     */
    private void clearArrMoves() {
        for (MapPanel panel : possibleMoves) {
            panel.setBorder(Color.BLACK, 0);
        }
        this.possibleMoves.clear();
    }


    //GETTERS AND SETTERS____________________________________________________

    public Player[] getPlayersArr() {
        return playersArr;
    }

    public Player getWinner() {
        return winner;
    }

}
