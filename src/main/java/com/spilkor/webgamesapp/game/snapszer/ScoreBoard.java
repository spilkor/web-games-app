package com.spilkor.webgamesapp.game.snapszer;

import java.util.*;

public class ScoreBoard {

    private Map<Player, Integer> pointMap = new HashMap<>();

    ScoreBoard(Collection<Player> players) {
        players.forEach(player -> pointMap.put(player, 0));
    }

    ScoreBoard(Player player) {
        this(Collections.singleton(player));
    }

    void addPlayer(Player player) {
        pointMap.put(player, 0);
    }

    void removePlayer(Player player) {
        pointMap.remove(player);
    }

    int get(Player player){
        return pointMap.get(player);
    }

    void score(Player player, int i) {
        pointMap.replace(player, pointMap.get(player) + i);
    }

    public int max() {
        return pointMap.values().stream().filter(Objects::nonNull).max(Integer::compareTo).orElse(0);
    }

    List<Integer> toIntegerList(List<Player> players) {
        List<Integer> result = new ArrayList<>();
        players.forEach(player -> result.add(get(player)));
        return result;
    }

    public void clear() {
        for(Player player: pointMap.keySet()){
            pointMap.replace(player, 0);
        }
    }
}
