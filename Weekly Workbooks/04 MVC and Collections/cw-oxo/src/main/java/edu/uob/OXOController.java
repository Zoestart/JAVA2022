package edu.uob;

import java.util.ArrayList;

public class OXOController {
    OXOModel gameModel;

    private int currentPlayerIndex;

    public OXOController(OXOModel model) {
        gameModel = model;
        currentPlayerIndex = 0;
    }

    public void handleIncomingCommand(String command) throws OXOMoveException {
        System.out.println("handleIncomingCommand called with command: " + command);
        // Call winDetection() for winner state
        if (winDetection()) {
            System.out.println("winDetection called");
            OXOPlayer winner = gameModel.getWinner();
            if (winner != null) {
                return;
            }
            gameModel.setWinner(winner);
            return;
        }
        //Check invalide input length
        if (command.length() != 2) {
            throw new OXOMoveException.InvalidIdentifierLengthException(command.length());
        }

        // Check that the row identifier is a valid letter character
        char rowIdentifier = Character.toUpperCase(command.charAt(0));
        if (rowIdentifier < 'A' || rowIdentifier > 'Z') {
            throw new OXOMoveException.InvalidIdentifierCharacterException(
                    OXOMoveException.RowOrColumn.ROW, rowIdentifier);
        }

        // Check that the column identifier is a valid digit character
        char columnIdentifier = command.charAt(1);
        if (columnIdentifier < '1' || columnIdentifier > '9') {
            throw new OXOMoveException.InvalidIdentifierCharacterException(
                    OXOMoveException.RowOrColumn.COLUMN, columnIdentifier);
        }

        // Convert the row identifier to a zero-indexed row number
        int row = rowIdentifier - 'A';

        // Convert the column identifier to a zero-indexed column number
        int column = columnIdentifier - '1';

        // Check that the row and column numbers are within the grid range
        if (row < 0 || row >= gameModel.getNumberOfColumns() || column < 0 || column >= gameModel.getNumberOfRows()) {
            throw new OXOMoveException.OutsideCellRangeException(
                    row < 0 || row >= gameModel.getNumberOfColumns() ? OXOMoveException.RowOrColumn.ROW : OXOMoveException.RowOrColumn.COLUMN,
                    row < 0 || row >= gameModel.getNumberOfColumns() ? rowIdentifier : columnIdentifier);
        }


        //Check if cell is been taken or not
        if (gameModel.getCellOwner(row, column) == null){
            //Set current player to current cell
            gameModel.setCellOwner(row, column, gameModel.getPlayerByNumber(gameModel.getCurrentPlayerNumber()));
            //Switch to next player
            int nextPlayer = (gameModel.getCurrentPlayerNumber() + 1) % gameModel.getNumberOfPlayers();
            gameModel.setCurrentPlayerNumber(nextPlayer);
        }
        else {
            throw new OXOMoveException.CellAlreadyTakenException(row, column);
        }


        //Check maximum grid is 9
        if (gameModel.getNumberOfRows() == 9) {
            System.out.println("here");
            throw new OXOMoveException("Maximum grid size is 9, can not add new row");
        }
        if (gameModel.getNumberOfColumns() == 9) {
            throw new OXOMoveException("Maximum grid size is 9, can not add new column");
        }

    }
    public void addRow() {
        gameModel.addRow();
    }
    public void removeRow() {
        gameModel.removeRow();
    }
    public void addColumn() {
        gameModel.addColumn();
    }
    public void removeColumn() {
        gameModel.removeColumn();
    }
    public void increaseWinThreshold() {
        int currentThreshold = gameModel.getWinThreshold();
        gameModel.setWinThreshold(currentThreshold + 1);
    }
    public void decreaseWinThreshold() {
        int currentThreshold = gameModel.getWinThreshold();
        if(currentThreshold > 3) {
            gameModel.setWinThreshold(currentThreshold - 1);
        }
    }
    public void reset() {
        //Set player 0
        gameModel.setCurrentPlayerNumber(0);

        //Set flags for winner/gameDraw
        gameModel.setWinner(null);
        gameModel.setGameDrawn();

        //Create new array
        OXOPlayer[][] newCells = new OXOPlayer[gameModel.getNumberOfRows()][gameModel.getNumberOfColumns()];

        //Initialize cell to null
        for ( int i = 0; i < gameModel.getNumberOfRows() ; i++) {
            for ( int j = 0; j < gameModel.getNumberOfColumns() ; j ++) {
                gameModel.setCellOwner(i, j, null);
            }
        }
    }
    public boolean isInBoundaries(int row,int col){
        return row >= 0 && row < gameModel.getNumberOfRows() && col >= 0 && col < gameModel.getNumberOfColumns();
    }

    public boolean winDetection() {
        int rows = gameModel.getNumberOfRows();
        int cols = gameModel.getNumberOfColumns();
        int threshold = gameModel.getWinThreshold();
        ArrayList<ArrayList<OXOPlayer>> cells = gameModel.getCells();

        // Check horizontal
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                OXOPlayer firstPlayer = cells.get(row).get(col);
                if (firstPlayer == null) {
                    continue;
                }

                if (isInBoundaries(row, col + threshold - 1)) {
                    int i = 1;
                    while (i < threshold && firstPlayer==(cells.get(row).get(col + i))) {
                        i++;
                    }
                    if (i == threshold) {
                        gameModel.setWinner(firstPlayer);
                        return true;
                    }
                }
            }
        }

        // Check vertical
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                OXOPlayer firstPlayer = cells.get(row).get(col);
                if (firstPlayer == null) {
                    continue;
                }

                if (isInBoundaries(row + threshold - 1, col)) {
                    int i = 1;
                    while (i < threshold && firstPlayer.equals(cells.get(row + i).get(col))) {
                        i++;
                    }
                    if (i == threshold) {
                        gameModel.setWinner(firstPlayer);
                        return true;
                    }
                }
            }
        }

        // Check diagonal from top left to bottom right
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                OXOPlayer firstPlayer = cells.get(row).get(col);
                if (firstPlayer == null) {
                    continue;
                }

                if (isInBoundaries(row + threshold - 1, col + threshold - 1)) {
                    int i = 1;
                    while (i < threshold && firstPlayer.equals(cells.get(row + i).get(col + i))) {
                        i++;
                    }
                    if (i == threshold) {
                        gameModel.setWinner(firstPlayer);
                        return true;
                    }
                }
            }
        }

        // Check diagonal from bottom left to top right
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                OXOPlayer firstPlayer = cells.get(row).get(col);
                if (firstPlayer == null) {
                    continue;
                }

                if (isInBoundaries(row + threshold - 1, col - threshold + 1)) {
                    int i = 1;
                    while (i < threshold && firstPlayer.equals(cells.get(row + i).get(col - i))) {
                        i++;
                    }
                    if (i == threshold) {
                        gameModel.setWinner(firstPlayer);
                        return true;
                    }
                }
            }
        }

        // Check for draw
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                if (cells.get(row).get(col) == null) {
                    // If any empty cell is found, game is not drawn
                    return false;
                }
            }
        }

        // All cells are filled and nobody won, so it's a draw
        gameModel.setGameDrawn();
        return true;
    }


}