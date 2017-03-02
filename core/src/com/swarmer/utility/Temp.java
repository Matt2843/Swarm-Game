package com.swarmer.utility;

/**
 * Created by Matt on 02-03-2017.
 */
public class Temp {

  public Boolean spawn(String PlayerId, int x, int y){
      Vector2 vec = getTileCoordinates(x, y);
    if(gameScreen().graph.nodes[vec.x][vec.y] != null && gameScreen().graph.nodes[vec.x][vec.y].getConnectedEdges().size > 0) {
      gameScreen().ants.add(new Ant(PlayerId, (TiledMapTileLayer) gameScreen().map.getLayers().get(1), gameScreen().graph.nodes[vec.x][vec.y]));
      return true;
    }
    return false;
  }

  private GameScreen gameScreen(){
    return ScreenManager.getInstance().getScreen(com.swarmer.gui.screens.ScreenLib.GAME_SCREEN);
  }
}
