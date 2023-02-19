package edu.uob;
import java.util.ArrayList;
public class OXOModel {

    private ArrayList<ArrayList<OXOPlayer>> cells;
    private OXOPlayer[] players;
    private int currentPlayerNumber;
    private int numberOfPlayers;

    private OXOPlayer winner;
    private boolean gameDrawn;
    private int winThreshold;

    public OXOModel(int numberOfRows, int numberOfColumns,
                    int winThresh) {
        winThreshold = winThresh;
        cells = new ArrayList<>();
        for (int i = 0; i < numberOfRows; i++) {
            ArrayList<OXOPlayer> row = new ArrayList<>();
            for (int j = 0; j < numberOfColumns; j++) {
                row.add(null);
            }
            cells.add(row);
        }

        players = new OXOPlayer[2];

    }

    public int getNumberOfPlayers() {
        return players.length;
    }

    public void addPlayer(OXOPlayer player) {
        for (int i = 0; i < players.length; i++) {
            if (players[i] == null) {
                players[i] = player;
                return;
            }
        }
    }

    public OXOPlayer getPlayerByNumber(int number) {
        return players[number];
    }

    public OXOPlayer getWinner() {
        return winner;
    }

    public void setWinner(OXOPlayer player) {
        winner = player;
    }

    public int getCurrentPlayerNumber() {
        return currentPlayerNumber;
    }

    public void setCurrentPlayerNumber(int playerNumber) {
        currentPlayerNumber = playerNumber;
    }


    public int getNumberOfRows() {
        return cells.size();
    }

    public int getNumberOfColumns() {
        return cells.get(0).size();
    }

    public OXOPlayer getCellOwner(int rowNumber, int colNumber) {
        return cells.get(rowNumber).get(colNumber);
    }

    public void setCellOwner(int rowNumber, int colNumber, OXOPlayer player) {
        cells.get(rowNumber).set(colNumber,player);
    }

    public void setWinThreshold(int winThresh) {
        winThreshold = winThresh;
    }

    public int getWinThreshold() {
        return winThreshold;
    }

    public void setGameDrawn() {
        gameDrawn = true;
    }

    public boolean isGameDrawn() {
        return gameDrawn;
    }

    public void addColumn() {
        for (ArrayList<OXOPlayer> row : cells) {
            row.add(null);
        }
    }

    public void addRow() {
        int numberOfColumns = cells.get(0).size();
        ArrayList<OXOPlayer> row = new ArrayList<>();
        for (int i = 0; i < numberOfColumns; i++) {
            row.add(null);
        }
        cells.add(row);
    }

    public void removeColumn() {
        for (ArrayList<OXOPlayer> row : cells) {
            row.remove(row.size() - 1);
        }
    }

    public void removeRow() {
        cells.remove(cells.size() - 1);
    }

    public ArrayList<ArrayList<OXOPlayer>> getCells() {
        return cells;
    }

    public void setNumberOfPlayers(int numberOfPlayers) {
        this.numberOfPlayers = numberOfPlayers;
        players = new OXOPlayer[numberOfPlayers];
    }

}
