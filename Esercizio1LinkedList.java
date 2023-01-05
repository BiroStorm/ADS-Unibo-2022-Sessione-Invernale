package Progetto2022;

import java.util.Random;
import java.util.Scanner;

/**
 * ! Rimuovere il package prima di consegnarlo
 * Enjun Hu
 * 0000944041
 * enjun.hu@studio.unibo.it
 */

/**
 * Nota: Si suppone che per "Sensore" si intenda l'utente o la classe main().
 * * Considerazioni Implementazione:
 * * Caso 1:
 * Implementazione tramite Albero Binario di Ricerca Bilanciato
 * ? Bilanciato perchè l'inserimento deve essere Log(n) e quindi se non fosse
 * bilanciato costa di più.
 * Il problema sta poi nel ribilanciamento dell'albero.
 * 
 * * Implementazione 2:
 * Tramite una Linked-List. Bidirezionale, con puntatore alla testa e coda.
 * 
 * * Implementazione 3:
 * Heap con Priorità (proprità -> TSE - TSI) min-heap
 */
public class Esercizio1LinkedList {

    private static class LinkedStructure {
        Node head;
        Node tail;

        public LinkedStructure() {
            head = null;
            tail = null;
        }

        /**
         * Aggiunta di un elemento nella LinkedList.
         * Costo O(1)
         * 
         * @param i
         * @param TSI
         * @param TSE
         */
        public void addData(int i, int TSI, int TSE) {
            Node newNode = new Node(i, TSI, TSE);
            if (head == null) {
                // first element
                assert (tail == null);
                head = newNode;
                tail = newNode;
                return;
            }
            assert (newNode.TSI > tail.TSI); // precondition 2: TSI(i) > TSI(i-1)
            tail.next = newNode;
            newNode.prev = tail;
            tail = newNode;
        }

        /**
         * Operazione A.
         * Costo Teta(K).
         * 
         * @return Il nodo contenente la Tripla caratterizzata dal
         *         valore minimo della differenza tra TSE(i) – TSI(i)
         * Se la struttura è vuota ritorna null.
         * 
         */
        public Node getMinTriple() {
            if (head == null) {
                return null;
            }
            Node min = head;
            int minValue = head.TSE - head.TSI;
            Node currentNode = head.next;
            while (currentNode != null) {
                if (minValue > currentNode.TSE - currentNode.TSI) {
                    minValue = currentNode.TSE - currentNode.TSI;
                    min = currentNode;
                }
                currentNode = currentNode.next;
            }
            return min;
        }

        /**
         * Operazione b (senza passargli il nodo direttamente).
         * Elimina la Tripla con la differenza tra TSE(i) - TSI(i) minima.
         * Costo Teta(K) dovuto alla chiamata di getMinTriple();
         */
        public void deleteMin() {
            Node min = getMinTriple();
            if (min == null)
                return;

            if (min.prev == null && min.next == null) {
                // case 0: Single Node
                head = null;
                tail = null;
            } else {
                if (min.prev == null) {
                    // case 1: Head node
                    head = min.next;
                    min.next.prev = null;
                } else if (min.next == null) {
                    // case 2: Tail node
                    tail = min.prev;
                    min.prev.next = null;
                }
            }
        }

        /**
         * Operazione b (passandogli il nodo trovato con l'operazione a).
         * Elimina la Tripla con la differenza tra TSE(i) - TSI(i) minima.
         * Costo O(1) visto che gli viene passato già la tripla con la differenza
         * minima.
         */
        public void deleteMin(Node min) {
            if (min == null)
                return;

            if (min.prev == null && min.next == null) {
                // case 0: Single Node
                head = null;
                tail = null;
            } else {
                if (min.prev == null) {
                    // case 1: Head node
                    head = min.next;
                    min.next.prev = null;
                } else if (min.next == null) {
                    // case 2: Tail node
                    tail = min.prev;
                    min.prev.next = null;
                } else {
                    // case default: Middle node
                    min.next.prev = min.prev;
                    min.prev.next = min.next;
                }
            }
        }

    }

    private static class Node {
        Node prev;
        Node next;
        int i;
        int TSI;
        int TSE;

        public Node(int i, int TSI, int TSE) {
            this.i = i;
            this.TSI = TSI;
            this.TSE = TSE;
            this.prev = null;
            this.next = null;
            assert (TSE > TSI); // precondition 1
        }

        public String toString() {
            return this.i + "\t" + this.TSI + "\t" + this.TSE;
        }
    }

    public static void main(String[] args) {
        final int K = 20;
        LinkedStructure S = new LinkedStructure();

        Scanner sc = new Scanner(System.in);
        int TSI = sc.nextInt();
        int TSE = sc.nextInt();
        sc.close();

        /**
         * ! Attendere la Risposta sul forum per il Typo, in quanto viola la precondition 1.
         */
        assert (TSI > TSE);
        int i = 1;
        S.addData(i, TSI, TSE);
        Random Z = new Random(944041);
        Random T = new Random(944041);

        /*
         ! Da cancellare
         * TSI = TSI precedente + un numero casuale [0, 7) shiftato di 4, quindi [4, 11)
         * TSE = TSI attuale + un numero casuale [0, 6) shiftato di 2, quindi [2, 8)
         */
        for (i = 2; i <= K; i++) {
            TSI = TSI + Z.nextInt(7) + 4;
            TSE = TSI + T.nextInt(6) + 2;
            S.addData(i, TSI, TSE);
        }

        System.out.println("Lista Prima le 2K operazioni\n");
        printList(S);

        // Eseguire il ciclo di operazioni a), b) e c) 2*K volte.
        for (int j = 0; j < 2 * K; j++) {
            Node min = S.getMinTriple();
            S.deleteMin(min);
            TSI = TSI + Z.nextInt(7) + 4;
            TSE = TSI + T.nextInt(7) + 2;
            S.addData(i, TSI, TSE);
            i++;
        }

        System.out.println("Lista Dopo le 2K operazioni\n");
        printList(S);

    }

    /**
     * Stampa la lista delle Triple contenute nella struttura S.
     * Nel formato: [i    TSI    TSE]
     * @param LS
     */
    protected static void printList(LinkedStructure S) {
        Node tripla = S.head;
        System.out.println("i\tTSI\tTSE");
        while (tripla != null) {
            System.out.println(tripla);
            tripla = tripla.next;
        }
    }

}
