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

public class Esercizio3 {
    int[] coins;
    int n;

    public static void main(String[] args) {
        Locale.setDefault(Locale.US);

        if (args.length != 1) {
            System.err.println("E' richiesto di passare in input il percorso del file da cui prendere i dati");
            System.exit(-1);
            return;
        }

        Esercizio3 es3 = new Esercizio3(args[0]);
        int restoNonErogabile = es3.minRestoNonErogabile();
        System.out.println(restoNonErogabile);

    }

    /**
     * Prende in input il nome del file in cui è contenuto l'input con cui
     * inizializza il vettore di monete su cui calcolare il resto
     * 
     * @param filename
     */
    public Esercizio3(String filename) {
        try {
            Scanner f = new Scanner(new FileReader(filename));
            this.n = f.nextInt();
            assert (n >= 1);
            this.coins = new int[n];
            for (int i = 0; i < n; i++) {
                coins[i] = f.nextInt();
            }
        } catch (IOException ex) {
            System.err.println(ex);
            System.exit(1);
        }
    }

    /**
     * Programmazione Dinamica.
     * 
     * 1) Sottoproblema S*: Trovare il resto più piccolo, non erogabile usando solo
     * i primi "i"
     * elementi del vettore.
     * 2) Soluzione del sottoproblema S*(i): Il resto più piccolo, non erogabile
     * usando solo i primi
     * "i" elementi.
     * 3) Soluzione del problema iniziale S: Il resto più piccolo, non erogabile
     * usando tutti gli n
     * elementi. (quando i=n)
     * 
     * 4) Algoritmo per calcolare la soluzione del sottoproblema:
     * Viene ordinato il vettore delle monete dal più piccolo al più grande,
     * 
     * Proposizione:
     * a) Un resto è erogabile solo da monete di taglia uguale o inferiore a esso.
     * Quando abbiamo solo taglie più grandi del resto, esso per definizione non è
     * erogabile.
     * 
     * La proposizione a) è necessaria ma non sufficiente a garantire un'erogabilità
     * di un resto.
     * 
     * 
     * Caso Base (i=1):
     * S*(1) = 1 if coins(1) > 1 else 2;
     * 
     * Con i > 1:
     * Si può definire come S*(i) =
     * if S*(i-1) < coins(i) then S*(i-1)
     * else S(i-1) + coins(i)
     * 
     * La condizione nell'if è dimostrabile tramite la negazione della proposizione
     * a) vista sopra.
     * La condizione dell'else è dimostrabile in quanto se S*(i-1) è minore di
     * coins(i), allora tutti i numeri interi r, 0 < r < S*(i-1) possono essere
     * prodotti da una combinazione dei primi (i-1)-esimi elementi, quindi è
     * possibile dire che
     * S*(i) = coins(i) + max{r} + 1, dove max{r} + 1 è esattamente S*(i-1).
     * 
     * 
     * Inoltre visto che il vettore di monete è stato ordinato, quando si incontra
     * la condizione S*(i-1) < coins(i) sappiamo anche che per i* > i, coins(i*) >
     * coins(i), quindi sicuramente non è erogabile dalle prossime monete contenuti
     * in coins(i*) sempre per proposizione a).
     * 
     * Costo O(n log n) dovuto alla necessità di ordinare l'array.
     * 
     * @return Resto non erogabile con le monete presenti in coins()
     */
    private int minRestoNonErogabile() {

        Arrays.sort(coins);

        int restoNonErogabile = 1;
        for (int i = 0; i < n; i++) {
            if (coins[i] > restoNonErogabile) {
                return restoNonErogabile;
            } else {
                restoNonErogabile += coins[i];
            }
        }
        return restoNonErogabile;
    }

}
