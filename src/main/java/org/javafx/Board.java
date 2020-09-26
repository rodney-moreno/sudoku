package org.javafx;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.ArrayList;

public class Board {
    private char[][] board;
    private final int NUM_OF_ROWS = 9;
    private final int NUM_OF_COLS = 9;
    private final char EMPTY_CELL = '.';
    private final int NUM_OF_GROUPS = 27;
    private List<int[]> openSpaces;
    private int numOfSol = 0;

    public Board(File input) throws FileNotFoundException {
        Scanner boardParser = new Scanner(input);
        this.board = new char[9][9];
        this.openSpaces = new ArrayList<int[]>();
        int row = 0;

        while(row < 9) {
            String line = boardParser.nextLine();
            for(int i = 0; i < line.length(); i++) {
                board[row][i] = line.charAt(i);
                if(line.charAt(i) == '.') {
                    int[] openSpace = {row, i};
                    openSpaces.add(openSpace);
                }
            }
            row++;
        }
    }

    public Board(char[][] board) {
        this.board = board;
    }

    public boolean solve(int index) {
        String potentialChoices = "123456789";
        if(!containsEmpty()) {
            return true;
        } else {
            int row = openSpaces.get(index)[0];
            int col = openSpaces.get(index)[1];
            for(int i = 0; i < potentialChoices.length(); i++) {
                placeNum(row, col, potentialChoices.charAt(i));
                if(isValidSudoku()) {
                    if(solve(index + 1)) return true;
                    removeNum(row, col);
                } else {
                    removeNum(row, col);
                }
            }
        }

        return false;
    }

    private void placeNum(int row, int col, char element) {
        board[row][col] = element;
    }

    private void removeNum(int row, int col) {
        board[row][col] = EMPTY_CELL;
    }

    public String toString() {
        String stringBoard = "";
        for(int row = 0; row < NUM_OF_ROWS; row++) {
            for(int col = 0; col < NUM_OF_COLS; col++) {
                stringBoard += "| " + board[row][col] + " ";
            }
            stringBoard += "|\n";
        }
        return stringBoard;
    }

    public boolean isValidSudoku() {

        HashSet<Character>[] sets = new HashSet[NUM_OF_GROUPS];
        for(int i = 0; i < sets.length; i++) {
            HashSet<Character> set = new HashSet<Character>();
            sets[i] = set;
        }

        for(int i = 0; i < board.length; i++) {
            for(int j = 0; j < board[0].length; j++) {

                char element = board[i][j];
                HashSet<Character> row = sets[i];
                HashSet<Character> col = sets[j + 9];
                HashSet<Character> block = sets[(3 * (i / 3) + j / 3) + 18];

                if(element == '.') {
                    continue;
                } else if(row.contains(element) || col.contains(element) ||
                        block.contains(element)) {
                    return false;
                } else {
                    row.add(element);
                    col.add(element);
                    block.add(element);

                    sets[i] = row;
                    sets[j + 9] = col;
                    sets[(3 * (i / 3) + j / 3) + 18] = block;
                }
            }
        }

        return true;
    }

    private boolean containsEmpty() {
        for(int i = 0; i < NUM_OF_ROWS; i++) {
            for(int j = 0; j < NUM_OF_COLS; j++) {
                if(board[i][j] == EMPTY_CELL) {
                    return true;
                }
            }
        }

        return false;
    }

    public void solveAll(int index) {
        String potentialChoices = "123456789";
        if(!containsEmpty()) {
            numOfSol++;
            System.out.println(numOfSol);
        } else {
            int row = openSpaces.get(index)[0];
            int col = openSpaces.get(index)[1];
            for(int i = 0; i < potentialChoices.length(); i++) {
                placeNum(row, col, potentialChoices.charAt(i));
                if(isValidSudoku()) {
                    solveAll(index + 1);
                }
                removeNum(row, col);
            }
        }

    }

    public void printToFile(String name) throws IOException {
        File puzzle = new File(name);
        puzzle.createNewFile();
        String oneLine = "";
        try (FileWriter writer = new FileWriter(name)) {
            for (int i = 0; i < 9; i++) {
                String row = "";
                for (int j = 0; j < 9; j++) {
                    row += board[i][j];
                }
                oneLine += row;
            }
            writer.write(oneLine);
        }
    }


    public void createPuzzle() {
        board = new char[NUM_OF_ROWS][NUM_OF_COLS];
        for(int i = 0; i < NUM_OF_ROWS; i++) {
            for(int j = 0; j < NUM_OF_COLS; j++) {
                board[i][j] = '.';
            }
        }



        int numOfClues = 35;
        List<List<Integer>> set = new ArrayList<List<Integer>>();
        while(set.size() < numOfClues) {
            int row = (int) (Math.random() * 9);
            int col = (int) (Math.random() * 9);
            List<Integer> cell = new ArrayList<>();
            cell.add(row);
            cell.add(col);
            if(!set.contains(cell)) {
                set.add(cell);
            }
        }

        openSpaces = new ArrayList<int[]>();
        for(int i = 0; i < 9; i++) {
            for(int j = 0; j < 9; j++) {
                int[] pos = {i, j};
                if(!set.contains(pos)) {
                    openSpaces.add(pos);
                }
            }
        }

        System.out.println(fillNewBoard(0, numOfClues, set));
    }

    public boolean fillNewBoard(int index, int numOfCells, List<List<Integer>> set) {
        String potentialChoice = "123456789";
        if(index == set.size() - 1) {
            return true;
        } else {
            int row = set.get(index).get(0);
            int col = set.get(index).get(1);
            for(int i = 0; i < 9; i++) {
                placeNum(row, col, potentialChoice.charAt(i));
                int[] openPos = {row, col};
                if(solve(0)) {
                    if(fillNewBoard(index + 1, numOfCells + 1, set)){
                        removeNum(row, col);
                        return true;
                    }
                } else {
                    removeNum(row, col);
                }
            }
        }

        return false;
    }


    public static void main(String[] args) throws IOException {
        Board puzzle = new Board(new File("puzzles/board-1.txt"));
        puzzle.createPuzzle();
        System.out.println(puzzle.toString());
        puzzle.printToFile("puzzle-13.txt");

    }
}