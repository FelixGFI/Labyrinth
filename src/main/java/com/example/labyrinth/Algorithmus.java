package com.example.labyrinth;

import java.util.ArrayList;

public class Algorithmus {
    static int hoehe = 121;
    static int breite = 151;
    static int[][] labyrinth = new int[breite][hoehe];

    static final int EMPTY = 0;
    static final int WALL = 1;
    static final int SIDE_PATH = 2;
    static final int RIGHT_PATH = 3;
    static final int MIDDLE = 4;
    static final int RAND = 5;
    static final int AKT_PATH = 6;
    static Tile aktTile;

    static char direction;

    static final char DIR_RECHTS = 'r';
    static final char DIR_LINKS = 'l';
    static final char DIR_UNTEN = 'u';
    static final char DIR_OBEN = 'o';

    static ArrayList<Tile> allPathTiles = new ArrayList<>();

    public static void main(String[] args) throws Exception {
        for (int i = 0; i < 1; i++) {
            generateLabyrinth();
            printArray();
        }
    }

    private static void generateLabyrinth() throws Exception {
        generateBorder();
        generateStartAndGoal();
        generateInterior();
    }

    private static void generateInterior() throws Exception {

        boolean middleReached = false;
        Tile tryTile = getNextTile(direction);

        setATileOnRightPath(tryTile);

        int failedTrysToGenerateNextTile = 0;
        boolean triedEverything = false;
        boolean lastCheckInitiated = false;
        boolean aktTileSetToLastIndexForLastCheck = false;

        //unless Borken (by raeching the middle) this loop goes on
        while(!triedEverything) {

            //direction to potentially set the next tile in is generated by chance
            char tryDirection = generateRandomDirection();

            int xv = 0;
            int yv = 0;
            int xv2 = 0;
            int yv2 = 0;

            switch(tryDirection){
                case DIR_UNTEN -> { xv = 0; yv = 1; xv2 = 0; yv2 = 2;}
                case DIR_OBEN -> { xv = 0; yv = -1; xv2 = 0; yv2 = -2;}
                case DIR_RECHTS -> { xv = 1; yv = 0; xv2 = 2; yv2 = 0;}
                case DIR_LINKS -> { xv = -1; yv = 0; xv2 = -2; yv2 = 0;}
            }

            int neuesX = aktTile.getxCord() + xv;
            int neuesY = aktTile.getyCord() + yv;
            int neuesX2 = aktTile.getxCord() + xv2;
            int neuesY2 = aktTile.getyCord() + yv2;

            if(neuesX >= breite-1 || neuesX <= 0 || neuesX2 >= breite-1 || neuesX2 <= 0 ||neuesY >= hoehe-1 || neuesY <= 0 ||neuesY2 >= hoehe-1 || neuesY2 <= 0) {
                failedTrysToGenerateNextTile++;
                continue;
            }

            tryTile = new Tile(neuesX, neuesY, labyrinth[neuesX][neuesY]);
            Tile tryTile2 = new Tile(neuesX2, neuesY2, labyrinth[neuesX2][neuesY2]);

            //TODO try to extract Method to detect if middle reached

            if (isNextToMiddle(tryTile2) && !middleReached) {
                if (isNextToMiddle(tryTile)) {
                    setATileOnRightPath(tryTile);
                    middleReached = true;
                } else {
                    setATileOnRightPath(tryTile);
                    setATileOnRightPath(tryTile2);
                    allPathTiles.add(tryTile2);
                    middleReached = true;
                }
            } else if (isNextToMiddle(tryTile) && !middleReached) {
                setATileOnRightPath(tryTile);
                middleReached = true;
            } else {
                if (!isAllreadySet(tryTile) && !isAllreadySet(tryTile2)) {
                    setATileOnRightPath(tryTile);
                    setATileOnRightPath(tryTile2);
                    allPathTiles.add(tryTile2);
                    failedTrysToGenerateNextTile = 0;
                } else {
                    failedTrysToGenerateNextTile++;
                }
            }

            if(failedTrysToGenerateNextTile >= 250 || lastCheckInitiated) {
                lastCheckInitiated = true;
                if(allPathTiles.indexOf(aktTile) == 0) {
                    triedEverything = true;
                }
                if(!aktTileSetToLastIndexForLastCheck) {
                    aktTile = allPathTiles.get(allPathTiles.size() - 1);
                    aktTileSetToLastIndexForLastCheck = true;
                } else if(failedTrysToGenerateNextTile >= 10) {
                    try{
                        labyrinth[aktTile.getxCord()][aktTile.getyCord()] = RIGHT_PATH;
                            aktTile = allPathTiles.get(allPathTiles.indexOf(aktTile) - 1);
                            failedTrysToGenerateNextTile = 0;
                        labyrinth[aktTile.getxCord()][aktTile.getyCord()] = AKT_PATH;
                    } catch (Exception e) {

                    }
                }
            }

            if(failedTrysToGenerateNextTile > 10 && !lastCheckInitiated) {
                try{
                    labyrinth[aktTile.getxCord()][aktTile.getyCord()] = RIGHT_PATH;
                    if (middleReached){
                        int number = (int) (Math.random() * allPathTiles.size());
                        aktTile = allPathTiles.get(number);
                    } else {
                        aktTile = allPathTiles.get(allPathTiles.indexOf(aktTile) - 1);
                        failedTrysToGenerateNextTile = 0;
                    }
                    labyrinth[aktTile.getxCord()][aktTile.getyCord()] = AKT_PATH;
                } catch (Exception e) {
                    System.out.println("Generation Failed. Please Try again.");
                }
            }
        }
    }
    //TODO >>> -/\---/\---/\---/\---/\---/\- <<<
    //TODO >>> -||---||---||---||---||---||- <<<

    private static char generateRandomDirection() {
        char tryDirection = ' ';
        int directionInt = (int) (Math.random()* 4);
        if(directionInt == 0) {
            tryDirection = DIR_LINKS;
        } else if(directionInt == 1) {
            tryDirection = DIR_UNTEN;
        } else if(directionInt == 2) {
            tryDirection = DIR_RECHTS;
        } else if(directionInt == 3) {
            tryDirection = DIR_OBEN;
        }
        return tryDirection;
    }

    private static boolean isPath(Tile givenTile) {
        if(givenTile == null) {
            return true;
        } else {
            return givenTile.getTileStatus() == RIGHT_PATH || givenTile.getTileStatus() == SIDE_PATH || givenTile.getTileStatus() == AKT_PATH || givenTile.getTileStatus() == MIDDLE;
        }
    }

    private static boolean isAllreadySet(Tile givenTile) {
        if(givenTile == null) {
            return true;
        } else {
            return givenTile.getTileStatus() == RIGHT_PATH || givenTile.getTileStatus() == SIDE_PATH || givenTile.getTileStatus() == AKT_PATH || givenTile.getTileStatus() == MIDDLE || givenTile.getTileStatus() == RAND || givenTile.getTileStatus() == MIDDLE;
        }
    }

    private static boolean isNextToMiddle(Tile tileZuPruefen) {

        int xCord = tileZuPruefen.getxCord();
        int yCord = tileZuPruefen.getyCord();

        Tile links         = new Tile(xCord-1, yCord, labyrinth[xCord-1][yCord]);
        Tile oben          = new Tile(xCord, yCord-1, labyrinth[xCord][yCord-1]);
        Tile rechts        = new Tile(xCord+1, yCord, labyrinth[xCord+1][yCord]);
        Tile unten         = new Tile(xCord, yCord+1, labyrinth[xCord][yCord+1]);

        return links.getTileStatus() == MIDDLE || rechts.getTileStatus() == MIDDLE || oben.getTileStatus() == MIDDLE || unten.getTileStatus() == MIDDLE;
    }

    private static Tile getNextTile(char tryDirection) throws Exception {
        Tile nextTile = null;
        if(tryDirection == DIR_RECHTS) {
            nextTile = new Tile(aktTile.getxCord() + 1, aktTile.getyCord(), labyrinth[aktTile.getxCord() + 1][aktTile.getyCord()]);
        } else if(tryDirection == DIR_LINKS) {
            nextTile = new Tile(aktTile.getxCord() - 1, aktTile.getyCord(), labyrinth[aktTile.getxCord() - 1][aktTile.getyCord()]);
        } else if(tryDirection == DIR_UNTEN) {
            nextTile = new Tile(aktTile.getxCord(), aktTile.getyCord() + 1, labyrinth[aktTile.getxCord()][aktTile.getyCord() + 1]);
        } else if(tryDirection == DIR_OBEN) {
            nextTile = new Tile(aktTile.getxCord(), aktTile.getyCord() - 1, labyrinth[aktTile.getxCord()][aktTile.getyCord()-1]);
        } else {

            Exception e = new Exception("generationError! Please try again!");
            throw e;
        }
        return nextTile;
    }

    private static void setATileToWall(Tile diesesTile) {
        diesesTile.setTileStatus(WALL);
        setGivenTileInLabyrinth(diesesTile);
    }

    private static void setATileOnRightPath(Tile diesesTile) {
        aktTile.setTileStatus(RIGHT_PATH);
        diesesTile.setTileStatus(AKT_PATH);
        setGivenTileInLabyrinth(aktTile);
        setGivenTileInLabyrinth(diesesTile);
        aktTile= diesesTile;
    }

    private static void setGivenTileInLabyrinth (Tile tile){
        labyrinth = tile.setTileInLabyrinth(labyrinth);
    }
    private static void generateStartAndGoal() {
        int xmitte = breite/2;
        int ymitte = hoehe/2;
        labyrinth[xmitte][ymitte] = MIDDLE;
        labyrinth[xmitte + 1][ymitte] = MIDDLE;
        labyrinth[xmitte - 1][ymitte] = MIDDLE;
        labyrinth[xmitte + 1][ymitte + 1] = MIDDLE;
        labyrinth[xmitte - 1][ymitte + 1] = MIDDLE;
        labyrinth[xmitte + 1][ymitte - 1] = MIDDLE;
        labyrinth[xmitte - 1][ymitte - 1] = MIDDLE;
        labyrinth[xmitte][ymitte + 1] = MIDDLE;
        labyrinth[xmitte][ymitte - 1] = MIDDLE;

        int side = (int) (Math.random() * 4);
        int x = -1;
        int y = -1;
        if(side == 0) {
            x = 0;
            direction = DIR_RECHTS;
        } else if(side == 1) {
            y = 0;
            direction = DIR_UNTEN;
        } else if(side == 2) {
            x = breite - 1;
            direction = DIR_LINKS;
        } else if(side == 3) {
            y = hoehe - 1;
            direction = DIR_OBEN;
        }


        if(y == -1) {
            boolean foundUnevenCord = false;
            while(!foundUnevenCord) {
                int num = (int)( Math.random() * (hoehe - 2)) + 1;
                if(num % 2 != 0) {
                    y = num;
                    foundUnevenCord = true;
                }
            }
        }
        if(x == -1) {
            boolean foundUnevenCord = false;
            while(!foundUnevenCord) {
                int num = (int)( Math.random() * (breite - 2)) + 1;
                if(num % 2 != 0) {
                    x = num;
                    foundUnevenCord = true;
                }
            }
        }
        labyrinth[x][y] = AKT_PATH;
        aktTile = new Tile(x, y, labyrinth[x][y]);


    }

    private static void generateBorder() {
        for(int y = 0; y < hoehe; y++) {
            labyrinth[0][y] = RAND;
            labyrinth[breite-1][y] = RAND;
        }
        for(int x = 0; x < breite; x++) {
            labyrinth[x][0] = RAND;
            labyrinth[x][hoehe-1] = RAND;
        }
    }

    public static void printArray() {
        for(int y = 0; y < hoehe; y++){
            for (int x = 0; x < breite; x++) {
                if(labyrinth[x][y] == WALL) {
                    System.out.print("x");
                } else if (labyrinth[x][y] == SIDE_PATH) {
                    System.out.print("s");
                } else if (labyrinth[x][y] == RIGHT_PATH) {
                    System.out.print(" ");
                }else if (labyrinth[x][y] == MIDDLE) {
                    System.out.print(" ");
                } else if (labyrinth[x][y] == RAND) {
                    System.out.print("+");
                } else if(labyrinth[x][y] == AKT_PATH) {
                    System.out.print(" ");
                } else {
                    System.out.print("+");
                }
            }
            System.out.println();
        }
    }
}
