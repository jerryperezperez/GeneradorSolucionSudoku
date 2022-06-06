import javax.swing.*;
import java.sql.SQLOutput;
import java.util.*;

public class Sudoku {
    public int[][] sudoku[][];
    private Integer[] NUMEROS;
    private int cantidad;
    boolean banderaReinicio = false;
    private HashSet<Integer> listaFilaColumnaActual;
    int iteraciones = 1;

    public Sudoku(int cantidad) {
        this.generarListaNumeros(cantidad);
        this.crearArreglo(cantidad);
        this.cantidad = cantidad;
        this.listaFilaColumnaActual = new HashSet<>();
    }

    private void generarListaNumeros(int cantidad) {
        NUMEROS = new Integer[(cantidad * cantidad)];
        for (int i = 0; i < (cantidad * cantidad); i++) {
            NUMEROS[i] = i + 1;
        }
    }

    private void crearArreglo(int cantidad) {
        this.sudoku = new int[cantidad][cantidad][cantidad][cantidad];
        for (int i = 0; i < sudoku.length; i++) {
            for (int j = 0; j < sudoku[i].length; j++) {
                for (int k = 0; k < sudoku[i][j].length; k++) {
                    for (int l = 0; l < sudoku[i][j][k].length; l++) {
                        sudoku[i][j][k][l] = 0;
                    }
                }
            }
        }
    }

    public void recorrerArreglo() throws Exception {
        try {
            for (int i = 0; i < sudoku.length; i++) {
                for (int j = 0; j < sudoku[i].length; j++) {
//                    System.out.println("SE LIMPIA LA LISTA PORQUE ESTÁ EN NUEVO SUBARREGLO");
                    this.listaFilaColumnaActual.clear();
                    this.recorrerSubArreglo(i, j);
                }
            }
        } catch (Exception e) {
//            JOptionPane.showMessageDialog(null, "DEBE REINICIAR ALGÚN ARREGLO PERO NO SÉ CÓMO");
        }
    }

    //TODO cambiar nombre de insertarNumeroAleatorio a recorrerSubArreglo
    //TODO cambiar el nombre de metodoPrueba por el de insertarNumeroAleatorio
    public void recorrerSubArreglo(int i, int j) throws Exception {
        List<Integer> lista = Arrays.asList(NUMEROS);
        ArrayList<Integer> numeros = new ArrayList<>(lista);
        Collections.shuffle(numeros);
//        System.out.println("numeros disponibles: " + numeros.size());

        for (int k = 0; k < cantidad; k++) {
            for (int l = 0; l < cantidad; l++) {
//                Thread.sleep(500);
                this.listaFilaColumnaActual.clear();
                try {
                    this.insertarNumero(numeros, i, j, k, l);
                } catch (Exception e) {
//                    System.out.println("HA ENTRADO EN EL CACTH DE  RECORRER SUBARREGLO");
                    if (banderaReinicio == true) {
                        throw new Exception("reconfigurar subarreglo");
                    } else {
                        k = 0;
                        l = -1;
                        lista = Arrays.asList(NUMEROS);
                        numeros = new ArrayList<>(lista);
                        Collections.shuffle(numeros);
                    }
                }
            }
        }
    }

    public void insertarNumero(ArrayList<Integer> numeros, int i, int j, int k, int l) throws Exception {
        if (numeros.size() == 1) {
//            JOptionPane.showMessageDialog(null, "LE QUEDA UN NÚMERO, REVISAR EN CASO DE AGREGAR OTRO EXTRA");
        }
        while (!numeros.isEmpty()) { // loops forever until break
//            System.out.println("NUMEROS RESTANTES: " + numeros.toString());
//            System.out.println("-----------");

            int numero = numeros.remove(0);

            try {
                sudoku[i][j][k][l] = this.validarNumeroActual(numero, i, j, k, l);
//                this.imprimirSudoku();
                break;
            } catch (Exception e) {
                numeros.add(numero);
                iteraciones ++;
                if (iteraciones == 200000){
                    JOptionPane.showMessageDialog(null, "NO ENCUENTRA LA RESPUESTA");
                }
                System.out.println("iteraciones " + iteraciones);
                this.imprimirSudoku();
                if (iteraciones % 10000 == 0){
                    System.out.println("iteraciones:" + iteraciones);
                }
//                System.out.println("HA AGREGADO AL NÚMERO " + numero);
                Collections.shuffle(numeros);
//                System.out.println(e.getMessage());
//                System.out.println("NUMERO QUE QUISO PONER " + numero + " ARREGLO DE VALIDACION: " + this.listaFilaColumnaActual.toString() + " Y SU LONGITUD ES: " + this.listaFilaColumnaActual.size());
                if (this.listaFilaColumnaActual.size() == (cantidad * cantidad)) {
                    if ((k == 0 && l == 0)) { //determina si desde la primera posición ya no puede colocar algún número
//                        System.out.println("NO HAY MÁS POSIBILIDAD DE PONER AQUÍ. ES EL PRINCIPIO DEL ARREGLO");
                        this.banderaReinicio = true;
                        throw new Exception("DESDE LA PRIMERA POSICIÓN YA MUERE");
                    }
                }
//                    else { //en caso de que no, solo vuelve a comenzar el subarreglo actual
//                        System.out.println("NO ES EL PRINCIPIO PERO YA NO HAY DISPONIBILIDAD DE NÚMEROS");
//                        List<Integer> lista = Arrays.asList(NUMEROS);
//                        numeros = new ArrayList<>(lista);
//                        this.reiniciarSubArreglo(i, j);
//                        this.recorrerSubArreglo(i, j);
//                    }
            }
            if ((k == cantidad - 1 && l == cantidad - 1)) {
                System.out.println("SE ATORÓ EN EL ÚLTIMO LUGAR. LANZARÁ EXCEPCIÓN DE SEGURIDAD");
                this.reiniciarSubArreglo(i, j);
//                this.recorrerSubArreglo(i, j);
                throw new Exception("REGRESAR");
            }
            if (!hayNumeroDisponible(numeros)) {
                this.reiniciarSubArreglo(i, j);
                throw new Exception("REGRESAR");

            }
        }

    }


    public boolean hayNumeroDisponible(ArrayList<Integer> numeros) {
//        System.out.println("ENTRA EN MÉTODO HAY NUMEROS DISPONIBLE");
        for (int numero : numeros) {
            if (!this.listaFilaColumnaActual.contains(numero)) {
//                System.out.println("REGRESA TRUE PORQUE HA AGREGADO ALGÚN NÚMERO");
                return true;
            }
        }
//        System.out.println("REGRESA Y VA A HACER ALGO PORQUE FUE FALSO, NO HUBO NINGUNO NUEVO PARA AGREGAR");
        return false;
    }

    //TODO crear método que devuelva la copia de los números para evitar estar instanciando por más de una vez el arraylist
    //TODO cambiar de reiniciar a limpiar y crear un método que sea el que reinicie el subArreglo
    public void reiniciarSubArreglo(int i, int j) {
//        System.out.println("REINICIANDO SUBARREGLO");
        for (int k = 0; k < cantidad; k++) {
            for (int l = 0; l < cantidad; l++) {
                sudoku[i][j][k][l] = 0;
            }
        }
//        System.out.println("REINICIA LA LISTA");
        this.listaFilaColumnaActual.clear();// se limpia este arreglo porque va a comenzar a insertar desde nuevo
    }

    public int validarNumeroActual(int numeroAleatorio, int i, int j, int k, int l) throws Exception {
        if (!validarColumna2(j, l, numeroAleatorio)) {
            this.listaFilaColumnaActual.add(numeroAleatorio);
            throw new Exception("no cumple en columna");
        }
        if (!validarFila(i, k, numeroAleatorio)) {
            this.listaFilaColumnaActual.add(numeroAleatorio);
            throw new Exception("no cumple en fila");
        }

        return numeroAleatorio;
    }

    //TODO tratar de adaptar que solo haya una línea para agregarDatoListaFilaColumna
    public boolean validarColumna2(int j, int l, int numeroAleatorio) {
        for (int i = 0; i < cantidad; i++) {
            for (int k = 0; k < cantidad; k++) {
                if (sudoku[i][j][k][l] == numeroAleatorio) {
                    return false;
                }
            }
        }
        return true;
    }

    public boolean validarFila(int i, int k, int numeroAleatorio) {
        for (int j = 0; j < cantidad; j++) {
            for (int l = 0; l < cantidad; l++) {
                if (sudoku[i][j][k][l] == numeroAleatorio) {
                    return false;
                }
            }
        }
        return true;
    }


    public void intercambiarSubArreglo(int i, int j) throws Exception {
        this.reiniciarSubArreglo(i, j);
//        System.out.println("INTERCAMBIANDO SUBARREGLO, NO BASTÓ CON REINICIAR EL ACTUAL");
        if (i > 0) {
            this.reiniciarSubArreglo(i - 1, j);
            this.recorrerSubArreglo(i - 1, j);
        } else {
            if (j > 0) {
                this.reiniciarSubArreglo(i, j - 1);
                this.recorrerSubArreglo(i, j - 1);
            } else {
                throw new Exception("IMPOSIBLE QUE OCURRA PERO SE CONSERVA LA EXCEPCIÓN");
            }
        }
    }

    public void imprimirSudoku() {
        for (int i = 0; i < sudoku.length; i++) {
            for (int j = 0; j < sudoku[i].length; j++) {
                for (int k = 0; k < sudoku[i][j].length; k++) {
                    for (int l = 0; l < sudoku[i][j][k].length; l++) {
                        System.out.print(sudoku[i][k][j][l] + "  ");
                    }
                }
                System.out.println();
            }
        }
    }
}
