package com.swarmer.gui.screens.lobby;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.ui.List;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

import java.util.HashMap;

public class GameList extends Table {

    private Skin defaultSkin;
    private ScrollPane scrollPane;
    private HashMap<String, GameListEntry> gameList;
    private List<HashMap> games;

    public GameList(float parentWidth, float parentHeight) {

        System.out.println(parentWidth + " " + parentHeight);
        setSize((float) (parentWidth*0.5), (float) (parentHeight*0.35));

        createFields();
    }

    private void createFields() {
        defaultSkin = new Skin(Gdx.files.internal("default/skin/uiskin.json"));

        gameList = new HashMap<>();
        gameList.put("asdasd", new GameListEntry("test1", "3", getWidth(), getHeight()));
        gameList.put("asdasd", new GameListEntry("test2", "2", getWidth(), getHeight()));

        games = new List<>(defaultSkin);
        games.setItems(gameList);
        scrollPane = new ScrollPane(games);

        addActor(scrollPane);
    }

    public void addGameListEntry(String id, GameListEntry entry) {
        gameList.put(id, entry);
    }
}
