public class Main {
    public static void main(String[] args) throws InterruptedException {
        System.out.println("Hola, soluciono sudokus");

        Sudoku sudoku = new Sudoku(3);
        try {

            sudoku.recorrerArreglo();
            if (sudoku.iteraciones == 200000) {
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("SOLUCIÓN ENCOTRADA");
        sudoku.imprimirSudoku();

    }


}
