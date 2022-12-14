package Progetto2022;

import java.io.FileReader;
import java.io.IOException;
import java.util.Locale;
import java.util.Scanner;

/**
 * ! Rimuovere il package prima di consegnarlo
 * ! Testato correttamente !
 * Enjun Hu
 * 0000944041
 * enjun.hu@studio.unibo.it
 */

public class Esercizio2 {
    int n; // numero di nodi
    int m; // numero di archi
    Edge[] E; // lista degli archi

    public static void main(String args[]) {

        Locale.setDefault(Locale.US);

        if (args.length != 2) {
            System.out.println("E' richiesto l'input di due file [circuito] [coppie di pin]");
            System.exit(-1);
            return;
        }

        Esercizio2 es2 = new Esercizio2();
        es2.readGraph(args[0]);
        es2.checkConnection(args[1]);

    }

    private void readGraph(String inputf) {
        try {
            Scanner f = new Scanner(new FileReader(inputf));
            n = f.nextInt();
            m = f.nextInt();
            E = new Edge[m];

            for (int i = 0; i < m; i++) {
                final int src = f.nextInt();
                final int dst = f.nextInt();
                E[i] = new Edge(src, dst);
            }
        } catch (IOException ex) {
            System.err.println(ex);
            System.exit(1);
        }
    }

    /*
     * Dato in input il nome di un file (o il suo percorso), crea una struttura
     * UnionFind e lo popola con i dati del grafo.
     * Controlla se dato due Pin (Nodi) sono connessi.
     * Stampa a video i Pin seguito da "C" se sono connessi, "NC" se non sono
     * connessi.
     * 
     * Il costo del popolamento della stuttura UnionFind è pari a O(n + m)
     * Il costo del controllo di q coppie di Pin usando la struttura UnionFind è
     * pari a O(q * 2f()), dove f() è il costo di un'operazione di find() della
     * struttura UnionFind, ma dato che il costo è O(1*) allora tutto il controllo
     * sarà pari a O(q).
     * 
     * In complessivo il metodo ha costo O(n + m + q)
     */
    private void checkConnection(String inputfile) {
        UnionFind UF = new UnionFind(n);

        for (int k = 0; k < E.length; k++) {
            final int src = E[k].src;
            final int dst = E[k].dst;
            if (UF.find(src) != UF.find(dst)) {
                UF.union(src, dst);
            }
        }

        try {
            Scanner f = new Scanner(new FileReader(inputfile));
            int q = f.nextInt();
            assert (q >= 1);

            for (int i = 0; i < q; i++) {
                final int start = f.nextInt();
                final int end = f.nextInt();
                final boolean isConnected = UF.find(start) == UF.find(end) ? true : false;

                System.out.println(start + " " + end + " " + (isConnected ? "C" : "NC"));
            }
        } catch (IOException ex) {
            System.err.println(ex);
            System.exit(1);
        }
    }

    /*
     * Struttura Union-Find (Merge-Find) Quick-union con Euristica sul Rango con
     * compressione dei cammini
     * p[] è l'array dei parenti dei nodi.
     * rank[] è l'array contenente i ranghi dei nodi, che indica il limite superiore
     * dell'altezza del nodo.
     * 
     * Con l'Euristica sul Rango insieme alla compressione dei cammini, le
     * operazioni di Union e Find hanno un costo pari a O(1*).
     * * moltiplicato per funzione inversa di Ackermann.
     */
    private class UnionFind {

        final int n;
        int[] p;
        int[] rank;

        /**
         * UFSet ha un costo pari a O(n)
         * 
         * @param n
         */
        public UnionFind(int n) {
            assert (n >= 0);

            this.p = new int[n];
            this.rank = new int[n];
            this.n = n;
            for (int i = 0; i < n; i++) {
                p[i] = i;
                rank[i] = 0;
            }
        }

        /**
         * Unisce l'insieme conentente l'i-esimo elemento, con l'insieme che contiene
         * l'j-esimo elemento, collegando uno dei rappresentanti all'altro.
         * 
         * @param i
         * @param j
         */
        public void union(int i, int j) {

            assert ((i >= 0) && (i < n));
            assert ((j >= 0) && (j < n));
            link(find(i), find(j));
        }

        /**
         * Collega 2 insiemi, dati i rappresentanti i e j.
         * Unisce l'albero con altezza (rango) minore a quello con altezza maggiore.
         * Se il rango è uguale, unisce il j ad i, incrementando l'altezza dell'albero
         * i.
         * Costo O(1)
         * 
         * @param i rappresentante del primo insieme
         * @param j rappresentante del secondo insieme
         */
        private void link(int i, int j) {
            assert (i != j);
            assert ((p[i] == i) && (p[j] == j));

            if (rank[i] < rank[j]) {
                p[i] = p[j];
            } else {
                p[j] = p[i];
                if (rank[i] == rank[j]) {
                    rank[i] = rank[i] + 1;
                }
            }
        }

        /**
         * Trova il rappresentante di i.
         * Questo metodo applica la compressione dei cammini per trovare il
         * rappresentante.
         * 
         * @param i
         * @return il rappresentante dell'insieme che contiene l'i-esimo elemento
         */
        public int find(int i) {
            assert ((i >= 0) && (i < n));

            if (i != p[i]) {
                p[i] = find(p[i]);
            }
            return p[i];
        }
    }

    /**
     * Classe Edge
     */
    private class Edge {
        public final int src;
        public final int dst;

        public Edge(int src, int dst) {
            this.src = src;
            this.dst = dst;
        }
    }

}
