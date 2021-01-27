package com.spilkor.webgamesapp.game.carcassonne;

public enum PointOfCompass {

    NORTH,
    EAST,
    SOUTH,
    WEST;

    public PointOfCompass add(PointOfCompass pointOfCompass){
        switch (this){
            case NORTH:
                return pointOfCompass;
            case EAST:
                switch (pointOfCompass){
                    case NORTH:
                        return EAST;
                    case EAST:
                        return SOUTH;
                    case SOUTH:
                        return WEST;
                    case WEST:
                        return NORTH;
                }
            case SOUTH:
                switch (pointOfCompass){
                    case NORTH:
                        return SOUTH;
                    case EAST:
                        return WEST;
                    case SOUTH:
                        return NORTH;
                    case WEST:
                        return EAST;
                }
            case WEST:
                switch (pointOfCompass){
                    case NORTH:
                        return WEST;
                    case EAST:
                        return NORTH;
                    case SOUTH:
                        return EAST;
                    case WEST:
                        return SOUTH;
                }
        }
        return null;
    }

    public PointOfCompass subtract(PointOfCompass pointOfCompass){
        switch (this){
            case NORTH:
                switch (pointOfCompass){
                    case NORTH:
                        return NORTH;
                    case EAST:
                        return WEST;
                    case SOUTH:
                        return SOUTH;
                    case WEST:
                        return EAST;
                }
            case EAST:
                switch (pointOfCompass){
                    case NORTH:
                        return EAST;
                    case EAST:
                        return NORTH;
                    case SOUTH:
                        return WEST;
                    case WEST:
                        return SOUTH;
                }
            case SOUTH:
                switch (pointOfCompass){
                    case NORTH:
                        return SOUTH;
                    case EAST:
                        return EAST;
                    case SOUTH:
                        return NORTH;
                    case WEST:
                        return WEST;
                }
            case WEST:
                switch (pointOfCompass){
                    case NORTH:
                        return WEST;
                    case EAST:
                        return SOUTH;
                    case SOUTH:
                        return EAST;
                    case WEST:
                        return NORTH;
                }
        }
        return null;
    }

}
