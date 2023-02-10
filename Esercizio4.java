import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.Locale;
import java.util.Scanner;

/**
 * Enjun Hu
 * 0000944041
 * enjun.hu@studio.unibo.it
 */

/**
 * Prima implementazione per la risoluzione del problema tramite l'algoritmo
 * Greedy Dijkstra "Modificato".
 * Si considera che Dijkstra viene applicato per trovare i cammini minimi nei
 * grafi in cui ci siano solo archi positivi.
 * Viene usato una Struttura Min-Heap per raggruppare i "nodi" (nel nostro caso
 * caselle). Il termine "Nodo" e "Casella" sono interscambiabili in questo caso.
 * L'array parent[] viene usato per salvare l'indicazione di chi è il nodo
 * predecessore da cui si passa per raggiungere il nodo i-esimo.
 * L'array height[] contiene le altezze della matrice presa in input.
 * 
 * 
 * NOTA POST ESAME: Per grafi molto grandi, bisogna tenere in considerazione il
 * limite superiore del datatype "double", visto che la distanza dal nodo
 * iniziale a quello finale potrebbe essere estremamente elevato.
 * Per tale motivo si è consigliato l'utilizzo di un Long (? conversione ?) o
 * nel modo più semplice un BigDecimal.
 */
public class Esercizio4 {

    double Ccell;
    double Cheight;
    int n;
    int m;
    int[] parent;
    double[] distance;
    double[][] height;
    int endx;
    int endy;
    final static int STARTX = 0;
    final static int STARTY = 0;

    public static void main(String[] args) {
        Locale.setDefault(Locale.US);

        if (args.length != 1) {
            System.err.println("E' richiesto in input il percorso del file da caricare.");
            System.exit(-1);
            return;
        }

        Esercizio4 es4 = new Esercizio4(args[0]);
        es4.minimumCostPath(STARTX, STARTY);
        es4.printResult();

    }

    /**
     * Stampa il percorso necessario da (STARTX, STARTY) per arrivare a (endx,
     * endy).
     * Seguito dal "Costo" per arrivarci.
     */
    private void printResult() {
        printPath(endx, endy);
        System.out.println(distance[(n * m) - 1] + Ccell);

    }

    /**
     * Metodo ricorsivo che stampa il percorso, partendo dall'ultima casella fino
     * alla prima, usando il vettore parent[] per navigare tra le caselle.
     * 
     * @param endx
     * @param endy
     */
    private void printPath(int endx, int endy) {
        final int idCasella = (endx * m) + (endy % m);
        int prevCell = parent[idCasella];
        if (endx == STARTX && endy == STARTY) {
            System.out.println(STARTX + "\t" + STARTY);
        } else if (prevCell < 0) {
            System.out.println("Destinazione non raggiungibile, c'è qualche errore!");

        } else {
            printPath(prevCell / m, prevCell % m);
            System.out.println(endx + "\t" + endy);
        }
    }

    /**
     * Inizializza la classe prendendo i dati dal file in input, inizializzando
     * inoltre 3 vettori grandi n*m.
     * 
     * Costo asintotico O(n*m)
     * Costo di memoria O(n*m)
     * 
     * @param filepath percorso del file di input
     */
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

        } catch (IOException ex) {
            System.err.println(ex);
            System.exit(1);
        }
    }

    /**
     * Metodo che prende in input le coordinate della casella di partenza e
     * costruisce il percorso necessario per arrivare alla casella indicata con
     * coordinate (endx, endy).
     * Viene usato un Min-Heap inizializzato con grandezza n*m, insieme ad un
     * vettore di booleani visited[] che mantiene traccia delle caselle già visitate
     * dall'algoritmo di Dijkstra.
     * Il metodo funziona come un normale algoritmo di Dijkstra leggermente
     * migliorato per questo specifico problema (implementando alcuni accorgimenti),
     * visitando i nodi più vicini finchè non visita anche il nodo destinazione.
     * 
     * Costo asintotico delle Operazioni:
     * 3 Arrays.fill che avranno un costo di O(n*m) ciascuno.
     * Un while che ha massimo un costo di O(n*m).
     * Modifiche ai dati nel Min-heap dentro al while O(log n*m)
     * (n*m dovuto alla grandezza stabilita all'inizio per il Min-Heap)
     * 
     * Sia k = n*m
     * Per un costo complessivo O(3k + k*log(k)) = O(k*log(k)).
     * 
     * 
     * Spiegazione del concetto per l'uso del Min-Heap su una matrice
     * bidimensionale, tramite un unico identificativo i:
     * 
     * Visto che il Min-Heap identifica il dato tramite un numero intero (utile nel
     * caso di grafi), ma nel nostro caso abbiamo una matrice, dove le celle sono
     * identificate dalle coordiante x e y, è stato necessario trovare una funzione
     * iniettiva di conversione che sia pure invertibile, tale da associare due
     * coordinate (x,y) ad un numero e allo stesso tempo, che da quel numero sia
     * possibile risalire nuovamente alle coordinate x e y.
     * 
     * Quindi:
     * 
     * Data una matrice con celle identificate da 0 a (n*m - 1), con n= numero di
     * righe, m = numero di colonne.
     * L'identificativo i-esimo corrisponde alla cella di coordinate (i/m, i%m)
     * Mentre data una cella in posizione (riga, colonna), i = riga*m + colonna%m
     *
     * Inoltre:
     * 
     * Dato una cella in coordinata x, y con identificativo i, la cella a destra
     * sarà i+1, la cella a sinistra i-1, la cella in alto i-m, la cella in basso
     * i-n.
     * Le celle nella prima riga avranno i/n = 0. (non hanno una cella in alto)
     * le celle nell'ultima riga avranno i/n = n-1. (non avranno una cella in basso)
     * le celle nella prima colonna avranno i%m = 0. (non avranno una cella a
     * sinistra)
     * le celle nell'ultima colonna avranno i%m = m-1 (non avranno una cella a
     * destra).
     * < alla fine questo non è servito visto che ci potevamo ricavare x e y >
     * 
     * Sapendo questo, possiamo finalmente usare il Min-Heap identificando ogni
     * singola cella con un identificatore univoco nella matrice, senza dover usare
     * le coordiante x e y.
     * 
     * 
     * @param STARTX
     * @param STARTY
     */
    private void minimumCostPath(int STARTX, int STARTY) {
        MinHeap mh = new MinHeap(n * m);

        boolean[] visited = new boolean[n * m];
        Arrays.fill(visited, false);
        Arrays.fill(parent, -1);
        Arrays.fill(distance, Double.POSITIVE_INFINITY);

        distance[0] = 0;
        parent[0] = 0;

        // A differenza dell'algoritmo di Dijkstra visto a lezione, non è necessario
        // passare da tutti i nodi, in quanto abbiamo l'obiettivo di trovare solo il
        // percorso minimo che va dal nodo iniziale a quello finale.
        // Deleghiamo l'inserimento dei vari nodi, solo quando li incontriamo.
        mh.insert(0, 0);

        while (!mh.isEmpty()) {
            final int i = mh.min();
            mh.deleteMin();
            visited[i] = true;

            final int row = i / m;
            final int col = i % m;

            // se siamo all'ultima casella allora abbiamo finito.
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
                }
            }
            // Casella Sotto
            if (row != n - 1) {
                final int idDown = (row + 1) * m + col % m;
                final double costToGoUp = distance[i] + Ccell
                        + (Cheight * Math.pow(height[row][col] - height[row + 1][col], 2));
                if (!visited[idDown] && (costToGoUp < distance[idDown])) {
                    distance[idDown] = costToGoUp;
                    mh.changePrio(idDown, costToGoUp);
                    parent[idDown] = i;
                }
            }
        }
    }

    /******************* MIN HEAP VISTO A LEZIONE ********************/
    // Viene modificato solo il metodo .changePrio() dove nel caso in cui venga
    // passato un |data| che non è stato inizializzato, allora lo inserisce
    // direttamente.
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
         * Modificato la parte in cui, se si prova a modificare la priorità associato ad
         * un |data| che non esiste, allora esso viene solamente inserito.
         * 
         * Chenage the priority associated to |data|. This method requires
         * time O(log n).
         */
        public void changePrio(int data, double newprio) {
            int j = pos[data];
            if (j < 0) {
                // new |data| so insert directly.
                this.insert(data, newprio);
            } else {
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
    }

}
