package com.spilkor.webgamesapp.game.carcassonne;

import com.spilkor.webgamesapp.util.MathUtil;

import java.util.ArrayList;
import java.util.List;

import static com.spilkor.webgamesapp.game.carcassonne.TileID.*;

public class Deck {

    private List<TileID> tileIDS = new ArrayList<>();

    public Deck() {
        put(TILE_0,3);
        put(TILE_1,9);
        put(TILE_2,8);
        put(TILE_3,1);
        put(TILE_4,4);
        put(TILE_5,4);
        put(TILE_6,2);
        put(TILE_7,3);
        put(TILE_8,1);
        put(TILE_9,1);
        put(TILE_10,3);
        put(TILE_11,2);
        put(TILE_12,3);
        put(TILE_13,2);
        put(TILE_14,2);
        put(TILE_15,5);
        put(TILE_16,3);
        put(TILE_17,3);
        put(TILE_18,3);
        put(TILE_19,3);
        put(TILE_20,1);
        put(TILE_21,2);
        put(TILE_22,1);
        put(TILE_23,2);
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

    public boolean isEmpty(){
        return tileIDS.isEmpty();
    }

    public boolean isNotEmpty(){
        return !isEmpty();
    }

    public int getSize() {
        return tileIDS.size();
    }
}
