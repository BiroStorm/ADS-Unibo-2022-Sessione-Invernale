import java.util.Locale;
import java.util.Random;
import java.util.Scanner;

/**
 * Enjun Hu
 * 0000944041
 * enjun.hu@studio.unibo.it
 */

/**
 * Soluzione 2 tramite l'uso di un MinHeap:
 * Tra le varie soluzioni, questa risulta quella più efficiente, dove si ha un
 * C.C. pari a O(log n) per le Operazioni b) e c) garantendo al contempo le
 * condizioni 1) e 2) richieste. Mentre per l'operazione a) si ha un Costo O(1).
 * 
 * A differenza di un MinHeap per le code di priorità, non si avrà l'array pos[]
 * e quindi non si avrà un Overhead di O(K) in quanto non è necessario il metodo
 * DecreseKey().
 * 
 * Questa è la soluzione più efficiente dato il problema ma bisogna notare come
 * esso sia efficiente perchè sappiamo che avremo sempre K o K-1 elementi nella
 * nostra struttura. Quindi se l'ordine delle operazioni fossero cambiate,
 * questa soluzione dovrebbe essere rivista, in quanto potrebbe capitare che
 * si riempia lo spazio disponibile nella struttura, visto che il MinHeap si
 * basa su un vettore statico di K elementi.
 * Bisogna tenere in considerazione anche il fatto che il costo ammortizzato
 * dell'Inserimento e della Cancellazione (dovuto al Raddoppio e Dimezzamento) è
 * pari a O(1), quindi in "teoria" non violerebbe la condizione 1) e 2).
 * 
 * Tempo: O(log K)
 * Spazio: O(K)
 */
public class Esercizio1 {
    MinHeap S;

    public static void main(String[] args) {

        Locale.setDefault(Locale.US);
        Esercizio1 es1 = new Esercizio1();

    }

    /**
     * Chiede in input i valori di K, TSI(1) e TSE(1)
     * per poi popolare la struttura partendo da questi valori.
     * Inoltre esegue per 2K volte le operazioni a), b), c).
     * 
     * Viene stampato i dati nella struttura prima e dopo il ciclo di operazioni.
     */
    public Esercizio1() {
        Scanner sc = new Scanner(System.in);
        final int K = sc.nextInt();
        int TSI = sc.nextInt();
        int TSE = sc.nextInt();
        sc.close();

        assert (K > 15 && K < 27);
        assert (TSE > TSI);

        this.S = new MinHeap(K);

        int i = 1;
        S.insert(i, TSI, TSE);

        Random Z = new Random(944041);
        Random T = new Random(944041);

        for (i = 2; i <= K; i++) {
            TSI = TSI + Z.nextInt(7) + 4;
            TSE = TSI + T.nextInt(6) + 2;
            assert (TSE > TSI);

            S.insert(i, TSI, TSE);
        }

        System.out.println("Lista Prima le 2K operazioni\n");
        printList(S);

        // Eseguire il ciclo di operazioni a), b) e c) 2*K volte.
        for (int j = 0; j < 2 * K; j++) {
            Tripla t = S.min();
            S.deleteMin();
            TSI = TSI + Z.nextInt(7) + 4;
            TSE = TSI + T.nextInt(6) + 2;
            S.insert(i, TSI, TSE);
            i++;
        }

        System.out.println("Lista Dopo le 2K operazioni\n");
        printList(S);

    }

    /**
     * Stampa la lista delle Triple contenute nella struttura S.
     * Nel formato: [i TSI TSE]
     * 
     * @param S
     */
    protected static void printList(MinHeap S) {
        for (Tripla tripla : S.heap) {
            System.out.println(tripla);
        }
    }

    private class Tripla {
        public final int i;
        public final int TSI;
        public final int TSE;

        public Tripla(int i, int TSI, int TSE) {
            this.i = i;
            this.TSI = TSI;
            this.TSE = TSE;
        }

        @Override
        public String toString() {
            return this.i + "\t" + this.TSI + "\t" + this.TSE;
        }
    }

    /**
     * Struttura MinHeap (vista a lezione), modificata e riadattata per soddisfare
     * le operazioni e le condizioni 1) e 2) richieste.
     */
    private class MinHeap {

        Tripla heap[];
        int size, maxSize;

        public MinHeap(int maxSize) {
            this.heap = new Tripla[maxSize];
            this.maxSize = maxSize;
            this.size = 0;
        }

        /**
         * Controlla la validità dell'indice
         */
        private boolean valid(int i) {
            return ((i >= 0) && (i < size));
        }

        /**
         * Inverte l'heap[i] con l'heap[j]
         */
        private void swap(int i, int j) {
            Tripla elemTmp = heap[i];
            heap[i] = heap[j];
            heap[j] = elemTmp;
        }

        /**
         * Ritorna il nodo padre di heap[i]
         */
        private int parent(int i) {
            assert (valid(i));

            return (i + 1) / 2 - 1;
        }

        /**
         * Ritorna l'indice del figlio sinistro dell'heap[i]
         */
        private int lchild(int i) {
            assert (valid(i));

            return (i + 1) * 2 - 1;
        }

        /**
         * Ritorna l'indice del figlio destro dell'heap[i]
         */
        private int rchild(int i) {
            assert (valid(i));

            return lchild(i) + 1;
        }

        /**
         * Controlla se l'Heap è vuoto.
         */
        public boolean isEmpty() {
            return (size == 0);
        }

        /**
         * Controlla se l'Heap è pieno.
         */
        public boolean isFull() {
            return (size > maxSize);
        }

        /**
         * Operazione A.
         * Costo O(1).
         * 
         * @return La Tripla caratterizzata dal
         *         valore minimo della differenza tra TSE(i) – TSI(i).
         * 
         */
        public Tripla min() {
            assert (!isEmpty());
            return heap[0];
        }

        /**
         * 
         * Scambia la tripla i-esima con il suo padre finchè non arriva alla posizione
         * corretta nell'Heap.
         * 
         * Ha un Costo pari a O(log n).
         */
        private void moveUp(int i) {
            assert (valid(i));

            int p = parent(i);

            while ((p >= 0) && ((heap[i].TSE - heap[i].TSI < heap[p].TSE - heap[p].TSI)
                    || ((heap[i].TSE - heap[i].TSI == heap[p].TSE - heap[p].TSI) && heap[i].i < heap[p].i))) {

                swap(i, p);
                i = p;
                p = parent(i);

            }
        }

        /**
         * 
         * Ritorna la posizione del figlio con la priorità minore.
         * In caso di due figli con stessa priorità, ritorna quello con l'identificativo
         * "i" minore.
         * 
         * Se non ha figli, ritorna -1.
         */
        private int minChild(int i) {
            assert (valid(i));

            final int l = lchild(i);
            final int r = rchild(i);
            int result = -1;
            if (valid(l)) {
                result = l;
                if (valid(r)) {
                    if (heap[r].TSE - heap[r].TSI < heap[l].TSE - heap[l].TSI) {
                        result = r;
                    }
                    // Si controlla inoltre, nel caso fossero uguali la differenza tra TSE e TSI,
                    // l'indice i, che come richiesto, bisogna dare "priorità a quello minore".
                    else if ((heap[r].TSE - heap[r].TSI == heap[l].TSE - heap[l].TSI) && (heap[r].i < heap[l].i)) {
                        result = r;
                    }
                }
            }
            return result;
        }

        /**
         * Inverte la tripla i-esima con il figlio con "priorità" inferiore, se esiste,
         * finchè non raggiunge la corretta posizione nell'heap.
         * Costo O(log n).
         * 
         * Tiene in considerazione anche l'identificativo "i" nella Tripla, in caso di
         * Priorità uguale.
         * In caso di priorità uguale, ci sarà sempre quello con il valore di "i" minore
         * in cima, grazie al while() che si ferma solo quando la Priorità della Tripla
         * in cima è minore o uguale a quelle dei figli ().
         * 
         * Non ci potrà mai essere una Tripla con identificativo "i" maggiore sopra ad
         * una Tripla con stessa priorità ma uno con identificiativo "i" minore.
         * Esso è dimostrabile per Assurdo, in quanto affinchè venga messo una Tripla X
         * più in alto rispetto ad una Tripla Z, con stessa priorità ma quest'ultima con
         * "i" minore, occorrerebbe che tra le due triple ci sia almeno un'altra Tripla
         * Y in mezzo, quest'ultima con priorità maggiore rispetto alla priorità di X.
         * Il che è assurdo visto che X e Z hanno la stessa priorità (X <= Y <= Z).
         * 
         * Gli "if statement" possono essere compattati, ma si lascia diviso per
         * questioni di leggibilità del codice.
         * 
         * Costo O(log n)
         */
        private void moveDown(int i) {
            assert (valid(i));

            boolean done = false;
            do {
                int dst = minChild(i);
                if (valid(dst)) {
                    if ((heap[dst].TSE - heap[dst].TSI < heap[i].TSE - heap[i].TSI)) {
                        swap(i, dst);
                        i = dst;
                    } else if ((heap[dst].TSE - heap[dst].TSI == heap[i].TSE - heap[i].TSI)
                            && (heap[dst].i < heap[i].i)) {
                        swap(i, dst);
                        i = dst;
                    } else {
                        done = true;
                    }
                } else {
                    done = true;
                }
            } while (!done);
        }

        /**
         * Inserisce una nuova Tripla (i, TSE, TSI) nel MinHeap.
         * Costo O(log n).
         */
        public void insert(int id, int TSI, int TSE) {
            assert (!isFull());

            final int i = size++;
            heap[i] = new Tripla(id, TSI, TSE);
            moveUp(i);
        }

        /**
         * 
         * Elimina l'elemento con priorità minore.
         * Riordina l'heap dopo l'eliminazione.
         * 
         * Tempo O(log n).
         */
        public void deleteMin() {
            assert (!isEmpty());

            swap(0, size - 1);
            size--;
            if (size > 0)
                moveDown(0);
        }
    }
}
