package com.spilkor.webgamesapp.game.carcassonne;

import com.spilkor.webgamesapp.util.MathUtil;

import java.util.ArrayList;
import java.util.List;

public class Deck {

    private List<TileID> tileIDS = new ArrayList<>();

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
}
