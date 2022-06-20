package com.example.labyrinth;

public class Algorithmus {
    static int hoehe = 20;
    static int breite = 50;
    static int[][] labyrinth = new int[breite][hoehe];

    public static void main(String[] args) {
        for (int i = 0; i < 1; i++) {
            generateLabyrinth();
            printArray();
        }

    }

    private static void generateLabyrinth() {
        generateBorder();
        generateStartAndGoal();
        generateLayers();
    }

    private static void generateLayers() {
        int xstart = 1;
        int ystart = 1;
        int xstop = breite - 2;
        int ystop = hoehe - 2;
        boolean xstartModify = true;
        boolean ystartModify = true;
        boolean xstopModify = true;
        boolean ystopModify = true;

        while(xstartModify || ystartModify || xstartModify || ystopModify) {
            for (int i = xstart; i <= xstop; i++) {
                setTile(i, ystart);
                labyrinth[i][ystart] = 1;
            }
            for (int i = xstart; i <= xstop; i++) {
                labyrinth[i][ystop] = 1;
            }
            for (int i = ystart; i <= ystop; i++) {
                labyrinth[xstart][i] = 1;
            }
            for (int i = ystart; i <= ystop; i++) {
                labyrinth[xstop][i] = 1;
            }

            if(!((ystop - 1) - (ystart + 1) > 3)) {
                if(labyrinth[breite/2][ystop - 1] == 4) {
                    ystopModify = false;
                }
                if(labyrinth[breite/2][ystart + 1] == 4) {
                    ystartModify = false;
                }
            }
            if(!((xstop - 1) - (xstart + 1) > 3)) {
                if (labyrinth[xstop - 1][hoehe/2] == 4) {
                    xstopModify = false;
                }
                if (labyrinth[xstart + 1][hoehe/2] == 4) {
                    xstartModify = false;
                }
            }
            if(xstartModify) {
                xstart++;
            }
            if(ystartModify) {
                ystart++;
            }
            if(xstopModify) {
                xstop--;
            }
            if(ystopModify) {
                ystop--;
            }

        }


    }

    private static void setTile(int xCord, int yCord) {
         Tile diesesTile = new Tile(xCord, yCord, labyrinth[xCord][yCord]);
         if(diesesTile.tileStatus == 0) {

             Tile links         = new Tile(xCord-1, yCord, labyrinth[xCord-1][yCord]);
             Tile oben          = new Tile(xCord, yCord-1, labyrinth[xCord][yCord-1]);
             Tile rechts        = new Tile(xCord+1, yCord, labyrinth[xCord+1][yCord]);
             Tile unten         = new Tile(xCord, yCord+1, labyrinth[xCord][yCord+1]);

             Tile linksOben     = new Tile(xCord-1, yCord-1, labyrinth[xCord-1][yCord-1]);
             Tile rechtsOben    = new Tile(xCord+1, yCord-1, labyrinth[xCord+1][yCord-1]);
             Tile linksUnten    = new Tile(xCord-1, yCord+1, labyrinth[xCord-1][yCord+1]);
             Tile rechtsUnten   = new Tile(xCord+1, yCord+1, labyrinth[xCord+1][yCord+1]);
             

             }
         }
    }

    private static void generateStartAndGoal() {
        int xmitte = breite/2;
        int ymitte = hoehe/2;
        labyrinth[xmitte][ymitte] = 4;
        labyrinth[xmitte + 1][ymitte] = 4;
        labyrinth[xmitte - 1][ymitte] = 4;
        labyrinth[xmitte + 1][ymitte + 1] = 4;
        labyrinth[xmitte - 1][ymitte + 1] = 4;
        labyrinth[xmitte + 1][ymitte - 1] = 4;
        labyrinth[xmitte - 1][ymitte - 1] = 4;
        labyrinth[xmitte][ymitte + 1] = 4;
        labyrinth[xmitte][ymitte - 1] = 4;

        int side = (int) (Math.random() * 4);
        int x = -1;
        int y = -1;
        if(side == 0) {
            x = 0;
        } else if(side == 1) {
            y = 0;
        } else if(side == 2) {
            x = breite - 1;
        } else if(side == 3) {
            y = hoehe - 1;
        }


        if(y == -1) {
            y = (int)( Math.random() * (hoehe - 2)) + 1;
            System.out.println("y = " + y);
        }
        if(x == -1) {
            x = (int)( Math.random() * (breite - 2)) + 1;
            System.out.println("x = " + x);
        }
        labyrinth[x][y] = 3;

    }

    private static void generateBorder() {
        for(int y = 0; y < hoehe; y++) {
            labyrinth[0][y] = 5;
            labyrinth[breite-1][y] = 5;
        }
        for(int x = 0; x < breite; x++) {
            labyrinth[x][0] = 5;
            labyrinth[x][hoehe-1] = 5;
        }
    }

    public static void printArray() {
        for(int y = 0; y < hoehe; y++){
            for (int x = 0; x < breite; x++) {
                if(labyrinth[x][y] == 1) {
                    System.out.print("+");
                } else if (labyrinth[x][y] == 2) {
                    System.out.print(" ");
                } else if (labyrinth[x][y] == 3) {
                    System.out.print("p");
                }else if (labyrinth[x][y] == 4) {
                    System.out.print("z");
                } else if (labyrinth[x][y] == 5) {
                    System.out.print("r");
                } else {
                    System.out.print("e");
                }
            }
            System.out.println();
        }
    }
}
