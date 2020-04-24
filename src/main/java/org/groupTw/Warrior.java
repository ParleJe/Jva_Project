package org.groupTw;

import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;

public class Warrior extends Entity implements iMovable {

    private ArrayList<Point> possible_moves;

    public Warrior(){
        super();
        MovePattern();
        AttackPattern();
    }
    //Constructor
    public Warrior(Point position_, String imagePath_, int health_, int attack_, int defense_, boolean canAttack_, boolean can_Move) throws IOException {
        super(position_, imagePath_, health_, attack_, defense_, canAttack_);
        MovePattern();
        AttackPattern();

    }

    /*
    Attack pattern :
    - - - - -
    - X X X -
    - X 0 X -
    - X X X -
    - - - - -
     */
    @Override
    void AttackPattern() {
        int x = this.position.x;
        int y = this.position.y;
        this.possible_attacks = new ArrayList<>();
        for (int x_ = -1; x_ < 1; x_++) {
            for (int y_ = -1; y_ < 1; y_++) {
                if (x > -1 && x < 8 && y > -1 && x < 8)
                    this.possible_attacks.add(new Point(x + x_, y = y_));
            }
        }
        this.possible_attacks.remove(5);

    }


    /*
    move pattern :
    - - - - -
    - X X X -
    - X 0 X -
    - X X X -
    - - - - -
     */
    @Override
    public void MovePattern() {

        int x = this.position.x;
        int y = this.position.y;
        this.possible_moves = new ArrayList<>();
        for (int x_ = -1; x_ < 1; x_++) {
            for (int y_ = -1; y_ < 1; y_++) {
                if (x > -1 && x < 8 && y > -1 && y < 8) this.possible_moves.add(new Point(x, y));
            }
        }
        this.possible_moves.remove(4); //current position
    }

    @Override
    public void Move(Point position_) {
        this.position = position_;
    }

}
