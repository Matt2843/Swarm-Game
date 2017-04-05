package com.swarmer.gui.screens.lobby;

import com.badlogic.gdx.scenes.scene2d.ui.List;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.swarmer.gui.StyleSheet;

import java.util.HashMap;

public class GameList extends Table {

    private ScrollPane scrollPane;
    private HashMap<String, GameListEntry> gameList;
    private List<HashMap> games;

    public GameList(float parentWidth, float parentHeight) {

        System.out.println(parentWidth + " " + parentHeight);
        setSize((float) (parentWidth*0.5), (float) (parentHeight*0.35));

        createFields();
    }

    private void createFields() {

        gameList = new HashMap<>();
        gameList.put("asdasd", new GameListEntry("test1", "3", getWidth(), getHeight()));
        gameList.put("asdasd", new GameListEntry("test2", "2", getWidth(), getHeight()));

        games = new List<>(StyleSheet.defaultSkin);
        games.setItems(gameList);
        scrollPane = new ScrollPane(games);

        addActor(scrollPane);
    }

    public void addGameListEntry(String id, GameListEntry entry) {
        gameList.put(id, entry);
    }
}
