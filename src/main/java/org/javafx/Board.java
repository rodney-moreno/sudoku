package org.javafx;

import java.io.File;
import java.io.FileNotFoundException;
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

    public boolean solve(int index) {
        String potentialChoices = "123456789";
        if(board[NUM_OF_ROWS - 1][NUM_OF_COLS - 1] != EMPTY_CELL) {
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

    public void placeNum(int row, int col, char element) {
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

    private boolean isValidSudoku() {

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

    public static void main(String[] args) throws FileNotFoundException {
        Board puzzle = new Board(new File("board-1.txt"));
        System.out.println(puzzle.toString());
        puzzle.solve(0);
        System.out.println(puzzle.toString());
    }
}
