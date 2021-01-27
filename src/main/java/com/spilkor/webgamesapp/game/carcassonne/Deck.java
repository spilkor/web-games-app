package com.spilkor.webgamesapp.game.carcassonne;

import com.spilkor.webgamesapp.util.MathUtil;

import java.util.ArrayList;
import java.util.List;

import static com.spilkor.webgamesapp.game.carcassonne.TileID.*;
import static com.spilkor.webgamesapp.game.carcassonne.TileID.TILE_5;

public class Deck {

    private List<TileID> tileIDS = new ArrayList<>();

    public Deck() {
        put(TILE_0, 3);
        put(TILE_1, 9);
        put(TILE_2, 8);
        put(TILE_3, 1);
        put(TILE_4, 4);
        put(TILE_5, 4);
        //TODO
    }

    public TileID draw(){
        if (tileIDS.isEmpty()){
            return null;
        }
        TileID tileID = MathUtil.selectRandom(tileIDS);
        tileIDS.remove(tileID);
        return tileID;
    }

    public void put(TileID tileID){
        tileIDS.add(tileID);
    }

    public void put(TileID tileID, int howMany){
        for(int x = 0; x < howMany; x++){
            tileIDS.add(tileID);
        }
    }
}
