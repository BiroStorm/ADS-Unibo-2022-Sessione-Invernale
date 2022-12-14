package Progetto2022;

import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.Locale;
import java.util.Scanner;

/**
 * ! Rimuovere il package prima di consegnarlo
 * ! Non ancora Testato !
 * Enjun Hu
 * 0000944041
 * enjun.hu@studio.unibo.it
 */

/**
 * Prima implementazione per la risoluzione del problema tramite l'algoritmo
 * Greedy Dijkstra "Modificato"
 */
public class Esercizio4 {

    double Ccell;
    double Cheight;
    int n;
    int m;
    int[] parent;
    double[] distance;
    double[][] height;
    Direction[] directions;
    int endx;
    int endy;
    final static int startx = 0;
    final static int starty = 0;

    public static void main(String[] args) {
        Locale.setDefault(Locale.US); 

        /*
        if (args.length != 1) {
            System.err.println("E' richiesto l'input del file da caricare.");
            System.exit(-1);
            return;
        } */

        Esercizio4 es4 = new Esercizio4(args[0]);
        es4.minimumCostPath(startx, starty);
        es4.printResult();

    }

    private void printResult() {
        printPath(endx, endy);
        System.out.println(distance[(n * m) - 1]);

    }

    private void printPath(int endx, int endy) {
        final int idCasella = (endx * m) + (endy % m);
        if (endx == startx && endy == starty) {
            System.out.println(startx + "\t" + starty);
        } else if (parent[idCasella] < 0) {
            System.out.println("Destinazione non raggiungibile, c'è qualche errore!");
            
        } else {
            switch (this.directions[idCasella]) {
                case DOWN:
                    printPath(endx - 1, endy);
                    break;
                case LEFT:
                    printPath(endx, endy + 1);
                    break;
                case RIGHT:
                    printPath(endx, endy - 1);
                    break;
                case UP:
                    printPath(endx + 1, endy);
                    break;
            }
            System.out.println(endx + "\t" + endy);
        }
    }

    public Esercizio4(String filepath) {
        try {
            Scanner f = new Scanner(new FileReader(filepath));

            this.Ccell = f.nextDouble();
            this.Cheight = f.nextDouble();
            this.n = f.nextInt();
            this.m = f.nextInt();
            this.endx = n - 1;
            this.endy = m - 1;

            assert (n > 0 && m > 0);

            this.height = new double[n][m];
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < m; j++) {
                    this.height[i][j] = f.nextDouble();
                }
            }

            this.parent = new int[n * m];
            this.distance = new double[n * m];

            this.directions = new Direction[n * m];

        } catch (IOException ex) {
            System.err.println(ex);
            System.exit(1);
        }
    }

    /**
     * Dato una cella in coordinata x, y con identificativo i, la cella a destra
     * sarà i+1, la cella a sinistra i-1, la cella in alto i-m, la cella in basso
     * i-n.
     * Le celle nella prima riga avranno i/n = 0. (non hanno una cella in alto)
     * le celle nell'ultima riga avranno i/n = n-1. (non avranno una cella in basso)
     * le celle nella prima colonna avranno i%m = 0. (non avranno una cella a
     * sinistra)
     * le celle nell'ultima colonna avranno i%m = m-1 (non avranno una cella a
     * destra)
     * 
     * @param startx
     * @param starty
     */
    private void minimumCostPath(int startx, int starty) {
        MinHeap mh = new MinHeap(n * m);

        boolean[] visited = new boolean[n * m];
        Arrays.fill(visited, false);
        Arrays.fill(parent, -1);
        Arrays.fill(distance, Double.POSITIVE_INFINITY);

        distance[0] = 0;
        parent[0] = 0;
        for (int v = 0; v < n * m; v++) {
            // inserisco tutte le celle e le loro distanze nel minheap
            // ? è possibile evitare ciò visto che non per forza, visitiamo tutti i nodi.
            mh.insert(v, distance[v]);
        }

        while (!mh.isEmpty()) {
            final int i = mh.min();
            mh.deleteMin();
            visited[i] = true;

            final int row = i / m;
            final int col = i % m;

            // se siamo all'ultima cella allora abbiamo finito.
            if (row == endx && col == endy)
                return;

            // Casella Sopra
            if (row != 0) {
                final int idUp = (row - 1) * m + col % m;
                final double costToGoUp = distance[i] + Ccell
                        + (Cheight * Math.pow(height[row][col] - height[row - 1][col], 2));
                if (!visited[idUp] && (costToGoUp < distance[idUp])) {
                    distance[idUp] = costToGoUp;
                    mh.changePrio(idUp, costToGoUp);
                    parent[idUp] = i;
                    directions[idUp] = Direction.UP;
                }
            }
            // Casella a Sinistra
            if (col != 0) {
                final int idLeft = row * m + (col - 1) % m;
                final double costToGoUp = distance[i] + Ccell
                        + (Cheight * Math.pow(height[row][col] - height[row][col - 1], 2));
                if (!visited[idLeft] && (costToGoUp < distance[idLeft])) {
                    distance[idLeft] = costToGoUp;
                    mh.changePrio(idLeft, costToGoUp);
                    parent[idLeft] = i;
                    directions[idLeft] = Direction.LEFT;
                }
            }
            // Casella a Destra
            if (col != m - 1) {
                final int idRight = row * m + (col + 1) % m;
                final double costToGoUp = distance[i] + Ccell
                        + (Cheight * Math.pow(height[row][col] - height[row][col + 1], 2));
                if (!visited[idRight] && (costToGoUp < distance[idRight])) {
                    distance[idRight] = costToGoUp;
                    mh.changePrio(idRight, costToGoUp);
                    parent[idRight] = i;
                    directions[idRight] = Direction.RIGHT;
                }
            }
            // Casella Sotto
            if (row != n - 1) {
                if(row == 4 && col == 4){
                    System.out.println("ciao");
                }
                final int idDown = (row + 1) * m + col % m;
                final double costToGoUp = distance[i] + Ccell
                        + (Cheight * Math.pow(height[row][col] - height[row + 1][col], 2));
                if (!visited[idDown] && (costToGoUp < distance[idDown])) {
                    distance[idDown] = costToGoUp;
                    mh.changePrio(idDown, costToGoUp);
                    parent[idDown] = i;
                    directions[idDown] = Direction.DOWN;
                }
            }
        }
    }

    /*
     * Sostitudo della Classe Edges nel metodo con i grafi
     */

    private enum Direction {
        UP,
        DOWN,
        LEFT,
        RIGHT;
    }

    /******************* MIN HEAP VISTO A LEZIONE ********************/
    private class MinHeap {

        heapElem heap[];
        /*
         * pos[id] is the position of "id" inside the heap. Specifically,
         * heap[pos[id]].key == id. This array is required to make
         * decreaseKey() run in log(n) time.
         */
        int pos[];
        int size, maxSize;

        /**
         * An heap element is a pair (id, priority), where
         * id is an integer in 0..(maxSize-1)
         */
        private class heapElem {
            public final int data;
            public double prio;

            public heapElem(int data, double prio) {
                this.data = data;
                this.prio = prio;
            }
        }

        /**
         * Build an empty heap with at most maxSize elements
         */
        public MinHeap(int maxSize) {
            this.heap = new heapElem[maxSize];
            this.maxSize = maxSize;
            this.size = 0;
            this.pos = new int[maxSize];
            Arrays.fill(this.pos, -1);
        }

        /**
         * Return true iff index i is a valid index in the heap,
         * i.e., i>=0 and i<size
         */
        private boolean valid(int i) {
            return ((i >= 0) && (i < size));
        }

        /**
         * swap heap[i] with heap[j]
         */
        private void swap(int i, int j) {
            assert (pos[heap[i].data] == i);
            assert (pos[heap[j].data] == j);

            heapElem elemTmp = heap[i];
            heap[i] = heap[j];
            heap[j] = elemTmp;
            pos[heap[i].data] = i;
            pos[heap[j].data] = j;
        }

        /**
         * Return the index of the parent of heap[i]
         */
        private int parent(int i) {
            assert (valid(i));

            return (i + 1) / 2 - 1;
        }

        /**
         * Return the index of the left child of heap[i]
         */
        private int lchild(int i) {
            assert (valid(i));

            return (i + 1) * 2 - 1;
        }

        /**
         * Return the index of the right child of heap[i]
         */
        private int rchild(int i) {
            assert (valid(i));

            return lchild(i) + 1;
        }

        /**
         * Return true iff the heap is empty
         */
        public boolean isEmpty() {
            return (size == 0);
        }

        /**
         * Return true iff the heap is full, i.e., no more available slots
         * are available.
         */
        public boolean isFull() {
            return (size > maxSize);
        }

        /**
         * Return the data of the element with lowest priority
         */
        public int min() {
            assert (!isEmpty());
            return heap[0].data;
        }

        /**
         * Return the position of the child of i (if any) with minimum
         * priority. If i has no childs, return -1.
         */
        private int minChild(int i) {
            assert (valid(i));

            final int l = lchild(i);
            final int r = rchild(i);
            int result = -1;
            if (valid(l)) {
                result = l;
                if (valid(r) && (heap[r].prio < heap[l].prio)) {
                    result = r;
                }
            }
            return result;
        }

        /**
         * Exchange heap[i] with the parent element until it reaches the
         * correct position into the heap. This method requires time O(log n).
         */
        private void moveUp(int i) {
            assert (valid(i));

            int p = parent(i);
            while ((p >= 0) && (heap[i].prio < heap[p].prio)) {
                swap(i, p);
                i = p;
                p = parent(i);
            }
        }

        /**
         * Exchange heap[i] with the child with lowest priority, if any
         * exists, until it reaches the correct position into the heap.
         * This method requires time O(log n).
         */
        private void moveDown(int i) {
            assert (valid(i));

            boolean done = false;
            do {
                int dst = minChild(i);
                if (valid(dst) && (heap[dst].prio < heap[i].prio)) {
                    swap(i, dst);
                    i = dst;
                } else {
                    done = true;
                }
            } while (!done);
        }

        /**
         * Insert a new pair (data, prio) into the queue.
         * This method requires time O(log n).
         */
        public void insert(int data, double prio) {
            assert ((data >= 0) && (data < maxSize));
            assert (pos[data] == -1);
            assert (!isFull());

            final int i = size++;
            pos[data] = i;
            heap[i] = new heapElem(data, prio);
            moveUp(i);
        }

        /**
         * Delete the element with minimum priority. This method requires
         * time O(log n).
         */
        public void deleteMin() {
            assert (!isEmpty());

            swap(0, size - 1);
            pos[heap[size - 1].data] = -1;
            size--;
            if (size > 0)
                moveDown(0);
        }

        /**
         * Chenage the priority associated to |data|. This method requires
         * time O(log n).
         */
        public void changePrio(int data, double newprio) {
            int j = pos[data];
            assert (valid(j));
            final double oldprio = heap[j].prio;
            heap[j].prio = newprio;
            if (newprio > oldprio) {
                moveDown(j);
            } else {
                moveUp(j);
            }
        }
    }

    /*
     * Data una matrice con celle identificate da 0 a (n*m - 1), con n= numero di
     * righe,
     * m = numero di colonne.
     * L'identificativo i-esimo è la cella (i/m, i%m)
     * Mentre data una cella in posizione (riga, colonna), i = riga*m + colonna%m
     * 
     * 
     */
}
