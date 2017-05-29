package com.swarmer.server.game;

import com.swarmer.server.game.logic.Game;

public class Swarmer {
    public static void main(String[] args) {

        Game g = new Game(500, 500);

        while(true) {
            g.render(1);
        }
    }
}
