package com.spilkor.webgamesapp.game.carcassonne;

public enum HalfSide {

//    nw____ne
//  wn|      |en
//    |      |
//  ws|______|es
//     sw  se

    NORTH_EAST,
    EAST_NORTH,
    EAST_SOUTH,
    SOUTH_EAST,
    SOUTH_WEST,
    WEST_SOUTH,
    WEST_NORTH,
    NORTH_WEST;

    public HalfSide add(PointOfCompass pointOfCompass){
        switch (this){
            case NORTH_EAST:
                switch (pointOfCompass){
                    case NORTH:
                        return NORTH_EAST;
                    case EAST:
                        return EAST_SOUTH;
                    case SOUTH:
                        return SOUTH_WEST;
                    case WEST:
                        return WEST_NORTH;
                }
            case EAST_NORTH:
                switch (pointOfCompass){
                    case NORTH:
                        return EAST_NORTH;
                    case EAST:
                        return SOUTH_EAST;
                    case SOUTH:
                        return WEST_SOUTH;
                    case WEST:
                        return NORTH_WEST;
                }
            case EAST_SOUTH:
                switch (pointOfCompass){
                    case NORTH:
                        return EAST_SOUTH;
                    case EAST:
                        return SOUTH_WEST;
                    case SOUTH:
                        return WEST_NORTH;
                    case WEST:
                        return NORTH_EAST;
                }
            case SOUTH_EAST:
                switch (pointOfCompass){
                    case NORTH:
                        return SOUTH_EAST;
                    case EAST:
                        return WEST_SOUTH;
                    case SOUTH:
                        return NORTH_WEST;
                    case WEST:
                        return EAST_NORTH;
                }
            case SOUTH_WEST:
                switch (pointOfCompass){
                    case NORTH:
                        return SOUTH_WEST;
                    case EAST:
                        return WEST_NORTH;
                    case SOUTH:
                        return NORTH_EAST;
                    case WEST:
                        return EAST_SOUTH;
                }
            case WEST_SOUTH:
                switch (pointOfCompass){
                    case NORTH:
                        return WEST_SOUTH;
                    case EAST:
                        return NORTH_WEST;
                    case SOUTH:
                        return EAST_NORTH;
                    case WEST:
                        return SOUTH_EAST;
                }
            case WEST_NORTH:
                switch (pointOfCompass){
                    case NORTH:
                        return WEST_NORTH;
                    case EAST:
                        return NORTH_EAST;
                    case SOUTH:
                        return EAST_SOUTH;
                    case WEST:
                        return SOUTH_WEST;
                }
            case NORTH_WEST:
                switch (pointOfCompass){
                    case NORTH:
                        return NORTH_WEST;
                    case EAST:
                        return EAST_NORTH;
                    case SOUTH:
                        return SOUTH_EAST;
                    case WEST:
                        return WEST_SOUTH;
                }
        }
        return null;
    }

//    nw____ne
//  wn|      |en
//    |      |
//  ws|______|es
//     sw  se

    public HalfSide subtract(PointOfCompass pointOfCompass){
        switch (this){
            case NORTH_EAST:
                switch (pointOfCompass){
                    case NORTH:
                        return NORTH_EAST;
                    case EAST:
                        return WEST_NORTH;
                    case SOUTH:
                        return SOUTH_WEST;
                    case WEST:
                        return EAST_NORTH;
                }
            case EAST_NORTH:
                switch (pointOfCompass){
                    case NORTH:
                        return EAST_NORTH;
                    case EAST:
                        return NORTH_WEST;
                    case SOUTH:
                        return WEST_SOUTH;
                    case WEST:
                        return SOUTH_EAST;
                }
            case EAST_SOUTH:
                switch (pointOfCompass){
                    case NORTH:
                        return EAST_SOUTH;
                    case EAST:
                        return NORTH_EAST;
                    case SOUTH:
                        return WEST_NORTH;
                    case WEST:
                        return SOUTH_WEST;
                }
            case SOUTH_EAST:
                switch (pointOfCompass){
                    case NORTH:
                        return SOUTH_EAST;
                    case EAST:
                        return EAST_NORTH;
                    case SOUTH:
                        return NORTH_WEST;
                    case WEST:
                        return WEST_SOUTH;
                }
            case SOUTH_WEST:
                switch (pointOfCompass){
                    case NORTH:
                        return SOUTH_WEST;
                    case EAST:
                        return EAST_SOUTH;
                    case SOUTH:
                        return NORTH_EAST;
                    case WEST:
                        return WEST_NORTH;
                }
            case WEST_SOUTH:
                switch (pointOfCompass){
                    case NORTH:
                        return WEST_SOUTH;
                    case EAST:
                        return SOUTH_EAST;
                    case SOUTH:
                        return EAST_NORTH;
                    case WEST:
                        return NORTH_WEST;
                }
            case WEST_NORTH:
                switch (pointOfCompass){
                    case NORTH:
                        return WEST_NORTH;
                    case EAST:
                        return SOUTH_WEST;
                    case SOUTH:
                        return EAST_SOUTH;
                    case WEST:
                        return NORTH_EAST;
                }
            case NORTH_WEST:
                switch (pointOfCompass){
                    case NORTH:
                        return NORTH_WEST;
                    case EAST:
                        return WEST_SOUTH;
                    case SOUTH:
                        return SOUTH_EAST;
                    case WEST:
                        return EAST_NORTH;
                }
        }
        return null;
    }

}
