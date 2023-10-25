package battleship;

import java.io.IOException;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        // Write your code here

        System.out.println("Player 1, place your ships on the game field");
        BattleMap player1Map = new BattleMap(10);
        player1Map.printRevealedMap();
        player1Map.placeAllShips();

        promptEnterKey();

        System.out.println("Player 2, place your ships on the game field");
        BattleMap player2Map = new BattleMap(10);
        player2Map.printRevealedMap();
        player2Map.placeAllShips();

        promptEnterKey();

        Scanner scanner = new Scanner(System.in);

        while(player1Map.checkAnyShipStillLiving() && player2Map.checkAnyShipStillLiving()) {
            player2Map.printRevealedMap();
            System.out.println("---------------------");
            player1Map.printTrueMap();
            System.out.println("Player 1, it's your turn:");

            Coordinates coordinates;
            do {
                coordinates = new Coordinates(scanner.next().toUpperCase());
            } while(!coordinates.checkInputLength() || !coordinates.parseCoordinates());

            if(takeShot(player2Map, coordinates)) {
                System.out.println("You hit a ship!");
                player2Map.checkNewShipSunk();

            } else {
                System.out.println("You missed!");
            }
            if(!player2Map.checkAnyShipStillLiving()) {
                break;
            }
            promptEnterKey();

            player1Map.printRevealedMap();
            System.out.println("---------------------");
            player2Map.printTrueMap();

            System.out.println("Player 2, it's your turn:");

            do {
                coordinates = new Coordinates(scanner.next().toUpperCase());
            } while(!coordinates.checkInputLength() || !coordinates.parseCoordinates());

            if(takeShot(player1Map, coordinates)) {
                System.out.println("You hit a ship!");
                player1Map.checkNewShipSunk();

            } else {
                System.out.println("You missed!");
            }
            if(!player1Map.checkAnyShipStillLiving()) {
                break;
            }
            promptEnterKey();
        }

        System.out.println("You sank the last ship. You won. Congratulations!");
    }

    public static boolean takeShot(BattleMap trueMap, Coordinates coordinates) {
        String hit = trueMap.map[coordinates.y][coordinates.x].getTrueValue();
        if(hit.equals("O") || hit.equals("X")) {
            trueMap.map[coordinates.y][coordinates.x].setTrueValue("X");
            trueMap.map[coordinates.y][coordinates.x].setRevealedValue("X");;
            return true;
        }
        trueMap.map[coordinates.y][coordinates.x].setRevealedValue("M");
        trueMap.map[coordinates.y][coordinates.x].setTrueValue("M");
        return false;
    }

    public static void promptEnterKey() {
        System.out.println("Press Enter and pass the move to another player");
        try {
            System.in.read();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

class BattleMap {
    GridCell[][] map;
    Ship[] ships = new Ship[5];

    BattleMap(int size) {
        map = new GridCell[size + 1][size + 1];

        //label the top row
        for (int i = 0; i < size + 1; i++) {
            map[0][i] = new GridCell(Integer.toString(i), new Coordinates(i,0), false);
        }

        //label the left column
        for (int i = 0; i < size + 1; i++) {
            map[i][0] = new GridCell(Character.toString((char) (i + 64) ), new Coordinates(0,i), false);
        }

        //label the rest with empty water
        for (int i = 1; i < size + 1; i++) {
            for (int j = 1; j < size + 1; j++) {
                map[i][j] = new GridCell("~", new Coordinates(j,i), true);
            }
        }

        //set the top left box to empty.
        map[0][0] = new GridCell(" ", new Coordinates(0,0), false);
    }

    public void printTrueMap() {
        printMap(true);
    }

    public void printRevealedMap() {
        printMap(false);
    }

    void printMap(boolean printTrueMap) {
        for (int i = 0; i < map[1].length; i++) {
            for (int j = 0; j < map[1].length; j++) {
                if(printTrueMap) {
                    System.out.print(map[i][j].getTrueValue() + " ");
                } else {
                    System.out.print(map[i][j].getRevealedValue() + " ");
                }

            }
            System.out.print("\n");
        }
    }

    public void placeAllShips() {
        String[] shipNames = new String[]{"Aircraft Carrier", "Battleship", "Submarine", "Cruiser", "Destroyer"};
        int[] shipSizes = new int[]{5, 4, 3, 3, 2};
        for(int i = 0; i < 5; i++) {
            ships[i] = new Ship(shipSizes[i], shipNames[i]);
        }
        int shipsPlaced = 0;
        while (shipsPlaced < 5) {
            if(placeShip(ships[shipsPlaced])) {
                shipsPlaced++;
                printTrueMap();
            }
        }

    }
    boolean placeShip(Ship ship) {
        Scanner placementScanner = new Scanner(System.in);
        System.out.printf("Enter the coordinates of the %s (%d cells)\n", ship.shipName, ship.size);

        Coordinates coord1 = new Coordinates(placementScanner.next().toUpperCase());
        Coordinates coord2 = new Coordinates(placementScanner.next().toUpperCase());

        if(!coord1.checkInputLength() || !coord2.checkInputLength()) {
            return false;
        }

        if(!coord1.parseCoordinates() || !coord2.parseCoordinates()) {
            return false;
        }

        //make sure placement is geometrically correct - exactly one statement should be true
        if((coord1.x == coord2.x) == (coord1.y == coord2.y)) {
            //something else, like diagonal, that is not allowed.
            System.out.println("Error - diagonal placement not allowed.");
            return false;
        }

        //make sure they are in the right order
        if( (coord1.y == coord2.y && coord1.x > coord2.x) || (coord1.x == coord2.x) && coord1.y > coord2.y ) {
            Coordinates tmp = coord2;
            coord2 = coord1;
            coord1 = tmp;
        }

        GridCell[] cells = new GridCell[ship.size];
        if(coord1.x == coord2.x) { //vertical placement attempt
            if(!checkPlacementSize(coord1.y, coord2.y, ship.size - 1)) {
                System.out.println("Error - wrong size");
                return false;
            }
            for(int i = 0; i < ship.size; i++) {
                cells[i] = map[coord1.y + i][coord1.x];
            }
        }

        if(coord1.y == coord2.y) { //horizontal placement attempt
            if(!checkPlacementSize(coord1.x, coord2.x, ship.size - 1)) {
                System.out.println("Error - wrong size");
                return false;
            }
            for(int i = 0; i < ship.size; i++) {
                cells[i] = map[coord1.y][coord1.x + i];
            }
        }

        for(int i = 0; i < ship.size; i++) {
            if(!cells[i].checkAdjacentCells(this)) {
                System.out.println("Error - overlapping or adjacent ships are not allowed");
                return false;
            }
        }

        ship.cells = cells;
        ship.initializeCellValues();

        return true;
    }

    boolean checkPlacementSize(int coord1, int coord2, int size) {
        return Math.abs(coord1 - coord2) == (size);
    }

    public boolean checkAnyShipStillLiving() {
        for (Ship ship: ships) {
            if(ship.isDestroyed == false) {
                return true;
            }
        }
        return false;
    }

    public void checkNewShipSunk() {
        for (Ship ship: ships) {
            ship.checkShipSunk();
        }
    }
}

class Ship {
    int size;
    GridCell[] cells;

    boolean isDestroyed;

    String shipName;

    Ship(int size, String shipName) {
        this.size = size;
        cells = new GridCell[size];
        this.shipName = shipName;
        isDestroyed = false;
    }

    public void initializeCellValues() {
        for (GridCell cell: cells) {
            cell.trueValue = "O";
        }
    }

    public void checkShipSunk() {
        if(isDestroyed) {
            return;
        }
        for (GridCell cell : cells) {
            if(cell.trueValue.equals("O")) {
                return;
            }
        }
        System.out.println("You sank a ship!");
        isDestroyed = true;
    }
}

class GridCell {
    String trueValue;
    String revealedValue;
    Coordinates coordinate;
    boolean gameCell;

    public GridCell(String trueValue, Coordinates coordinate, boolean gameCell) {
        this.trueValue = trueValue;
        this.coordinate = coordinate;
        this.gameCell = gameCell;

        if(gameCell) {
            this.revealedValue = "~";
        } else {
            this.revealedValue = trueValue;
        }
    }
    public String getTrueValue() {
        return trueValue;
    }
    public void setTrueValue(String trueValue) {
        this.trueValue = trueValue;
    }

    public String getRevealedValue() {
        return revealedValue;
    }

    public void setRevealedValue(String revealedValue) {
        this.revealedValue = revealedValue;
    }

    public boolean checkAdjacentCells(BattleMap battleMap) {
        if (coordinate.x != 10) {
            GridCell rightCell = battleMap.map[coordinate.y][coordinate.x + 1];
            if(!rightCell.getTrueValue().equals("~") && rightCell.gameCell == true) {
                return false;
            }
        }

        if(coordinate.y != 10) {
            GridCell bottomCell = battleMap.map[coordinate.y + 1][coordinate.x];
            if(!bottomCell.getTrueValue().equals("~") && bottomCell.gameCell == true) {
                return false;
            }
        }

        GridCell topCell = battleMap.map[coordinate.y - 1][coordinate.x];
        if(!topCell.getTrueValue().equals("~") && topCell.gameCell == true) {
            return false;
        }

        GridCell leftCell = battleMap.map[coordinate.y][coordinate.x - 1];
        if(!leftCell.getTrueValue().equals("~") && leftCell.gameCell == true) {
            return false;
        }

        return true;
    }
}
class Coordinates {
    Coordinates(String grid) {
        originalInput = grid;
        splitInput = grid.split("(?<=\\D)(?=\\d)");
    }

    Coordinates(int x, int y) {
        this.x = x;
        this.y = y;
        originalInput = "";
    }
    public String originalInput;
    public String[] splitInput;
    public int x = -1;
    public int y = -1;

    public boolean parseCoordinates() {
        try {
            x = Integer.parseInt(splitInput[1]);
        } catch (NumberFormatException e)
        {
            System.out.println("Error - bad X coordinate (Number Format)");
            return false;
        } catch (Exception e){
            System.out.println("Error - bad X coordinate (other)");
            return false;
        }

        try{
            y = (int) splitInput[0].charAt(0) - 65 + 1; //cursed unicode hex value conversion to integer
        } catch (Exception e) {
            System.out.println("Error - bad Y coordinate (other)");
            return false;
        }

        if(x > 10 || x < 0) {
            System.out.println("Error - X coordinate not within boundaries");
            return false;
        }
        if(y > 10 || y < 0) {
            System.out.println("Error - Y coordinate not within boundaries");
            return false;
        }

        return true;
    }

    public boolean checkInputLength()
    {
        if ((originalInput.length() == 2 || originalInput.length() == 3) && splitInput.length == 2) {
            return true;
        }
        System.out.println("Error - incorrect length of coordinates");
        return false;
    }
}
