import java.util.Scanner;
import java.lang.StringBuilder;
import java.util.Arrays;
import java.util.PriorityQueue;
import java.util.Random;

public class DNTE_63190001 {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        // Igranje naključnih primerov dolžine 1000
        /*
        for (int i = 0; i < 100; i++) {
            int[] liki = nakljucniLiki(1000);
            Jama jama = odigraj(liki);
            System.out.println(jama.najslabsaVisina);
        }
        */
        
        // Branje vhoda in igranje
        int[] liki = preberiLike(sc);
        Jama jama = odigraj(liki);

        // Izpis potez (za tekmovanje)
        System.out.print(jama.poteze);

        // Izpis rezultata
        //System.out.println(jama.najslabsaVisina);

        // Prikaz igranja
        /*
        Poteza p = jama.poteze;
        Jama j = new Jama(20);
        odigrajPoteze(p, j);
        */

        //rocnoIgranje(sc);

        sc.close();
    }        
    
    // Izpis in izris posameznih potez in stanj v jami
    public static Jama odigrajPoteze(Poteza p, Jama j) {
        if (p.prejsnja == null) {
            j.dodajLik(Lik.getLik(p.lik, p.orientacija), p.koordinata);
            System.out.printf("%d %d %d ", p.lik, p.orientacija, p.koordinata);
            System.out.print(j);
            return j;
        } else {
            j = odigrajPoteze(p.prejsnja, j);
            j.dodajLik(Lik.getLik(p.lik, p.orientacija), p.koordinata);
            System.out.printf("%d %d %d ", p.lik, p.orientacija, p.koordinata);
            System.out.print(j);
            return j;
        }
    }

    // Jerdo algoritma
    public static Jama odigraj(int[] liki) {
        int n = liki.length;
        Jama jama = new Jama(5);

        // Prioritetna vrsta najboljših trenutnih rešitev
        PriorityQueue<Jama> pq = new PriorityQueue<>();
        pq.add(jama);

        while (!pq.isEmpty()) {
            Jama jama1 = pq.poll();

            // Vsi liki so spuščeni
            if (jama1.poteza == n) return jama1;

            int l = liki[jama1.poteza];
            Jama jama2 = jama1.kopija();

            // Spuščanje naslednjega lika na vse možne načine in umeščanje rešitev v vrsto
            for (Lik lik: Lik.moznosti(l)) {
                for (int j = 0; j <= 10 - lik.sirina; j++) {
                    if (jama2.dodajLik(lik, j)) {
                        pq.add(jama2);
                        jama2 = jama1.kopija();
                    }
                }
            }

            // Uničevanje slabih rešitev, ki po nepotrebnem zasedajo pomnilnik
            if (pq.size() > 5000) {
                PriorityQueue<Jama> pq1 = new PriorityQueue<>();
                for (int i = 0; i < 1000; i++) pq1.add(pq.poll());
                pq = pq1;
            }
        }
        return null;
    }

    // Branje vhodnih podatkov
    public static int[] preberiLike(Scanner sc) {
        int n = sc.nextInt();
        int[] liki = new int[n];
        for (int i = 0; i < n; i++) liki[i] = sc.nextInt();
        return liki;
    }

    // Generiranje naključnega zaporedja likov - vhodnih podatkov
    public static int[] nakljucniLiki(int n) {
        Random rnd = new Random();
        int[] liki = new int[n];
        for (int i = 0; i < n; i++) liki[i] = rnd.nextInt(7);
        return liki;
    }

    // Ročno igranje za testiranje delovanja
    public static void rocnoIgranje(Scanner sc) {
        Jama jama = new Jama(100);
        while(sc.hasNextInt()) {
            // lik
            int l = sc.nextInt();
            // orientacija
            int o = sc.nextInt();
            // koordinata - stolpec levega dela
            int k = sc.nextInt();
            jama.dodajLik(Lik.getLik(l, o), k);
            System.out.println(jama);
        }
    }

    // Predtavitev vmesne rešitve kot stanje v jami
    public static class Jama implements Comparable<Jama> {
        // Število zasedenih vrstic, brez praznih na vrhu
        int stVrstic;

        // Zadnjih 10 bitov zapisa števila predstavlja stanje v posamezni vrstici
        // 0 - prosto, 1 - zasedeno z blokom
        int[] jama;

        // Indeksi najvišjih blokov v posameznih stolpcih
        int[] najvisjiProstor;

        // Najslabši vmesni rezultat
        int najslabsaVisina;

        // Vsota višin stolpcev
        int skupnaVisina;

        // Indeks poteze, ki je nasledjna na vrsti
        int poteza;

        // Povezan seznam dosedanjih potez, usmerjen nazaj
        Poteza poteze;

        public Jama(int visina) {
            this.stVrstic = 0;
            this.jama = new int[visina];
            this.najvisjiProstor = new int[10];
            this.najslabsaVisina = 0;
            this.skupnaVisina = 0;
            this.poteza = 0;
        }

        // Ustvarjanje kopije
        public Jama kopija() {
            Jama jama2 = new Jama(this.stVrstic + 4);
            jama2.stVrstic = this.stVrstic;
            jama2.najvisjiProstor = Arrays.copyOf(this.najvisjiProstor, 10);
            jama2.kopirajJamoOd(this);
            jama2.najslabsaVisina = this.najslabsaVisina;
            jama2.skupnaVisina = this.skupnaVisina;
            jama2.poteza = this.poteza;
            jama2.poteze = this.poteze;
            return jama2;
        }

        private void kopirajJamoOd(Jama original) {
            for (int i = 0; i < original.stVrstic; i++) {
                this.jama[i] = original.jama[i];
            }
        }
        
        // Vrstice nad vrstico z indeksom višina se premaknejo za eno dol
        private void premakniDol(int visina) {
            for (int i = visina; i < this.stVrstic - 1; i++) {
                this.jama[i] = this.jama[i + 1];
            }
            this.jama[this.stVrstic - 1] = 0;
            this.stVrstic--;
        }
        
        // Izbriše polne vrstice
        private void odstraniPolne() {
            for (int i = this.stVrstic - 1; i >= 0; i--) {
                if (this.jama[i] == 1023) premakniDol(i);
            }
            this.izracunajNajvisje();
        }
        
        // Izračuna najvišje zasedene položaje v stolpcih
        private void izracunajNajvisje() {
            this.skupnaVisina = 0;
            for (int i = 0; i < 10; i++) {
                int j = this.stVrstic - 1;
                while (j >= 0 && (this.jama[j] & (1 << (9 - i))) == 0) j--;
                this.najvisjiProstor[i] = j + 1;
                this.skupnaVisina += j + 1;
            }
        }
        
        // V jamo spusti želen lik, koordinata predstavlja lokacijo najbolj levega dela lika
        public boolean dodajLik(Lik lik, int koordinata) {
            int a = lik.visina;
            int b = lik.sirina;
            if (b + koordinata > 10) return false;
            int maxVisina = 0;
            for (int i = 0; i < b; i++) {
                int j = 0;
                while (j < a && (lik.oblika[j] & 1 << (b - 1 - i)) == 0) j++;
                int visina = this.najvisjiProstor[i + koordinata] - j;
                if (visina > maxVisina) maxVisina = visina;
            }
            int povecano = maxVisina + a - this.stVrstic;
            if (povecano > 0) this.stVrstic += povecano;

            for (int i = 0; i < a; i++) {
                jama[i + maxVisina] |= lik.oblika[i] << (10 - koordinata - b);
            }
            this.odstraniPolne();
            if (this.stVrstic > this.najslabsaVisina) this.najslabsaVisina = this.stVrstic;
            this.poteza++;
            this.poteze = new Poteza(lik.indeks, lik.orientacija, koordinata, this.poteze);
            return true;
        }

        // Število vseh zasedenih polj
        private int stKock() {
            int stevec = 0;
            for (int i = 0; i < this.stVrstic; i++) {
                for (int j = 1; j < 1024; j<<=1) {
                    if ((this.jama[i] & j) != 0) stevec++;
                }
            }
            return stevec;
        }

        // Kriterij za določanje najboljše jame za nadaljevanje
        @Override
        public int compareTo(Jama jama) {
            if (this.najslabsaVisina == jama.najslabsaVisina) {
                if (this.poteza == jama.poteza) {
                    if (this.skupnaVisina == jama.skupnaVisina) {
                        return this.stKock() - jama.stKock();
                    } else return this.skupnaVisina - jama.skupnaVisina;
                } else return jama.poteza - this.poteza;
            } else return this.najslabsaVisina - jama.najslabsaVisina;
        }

        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append(String.format("Najslabsa: %d\n", this.najslabsaVisina));
            for (int i = this.stVrstic; i >= 0; i--) {
                sb.append("|");
                for (int j = 512; j > 0; j>>=1) {
                    sb.append((this.jama[i] & j) != 0 ? "[]" : "  ");
                }
                sb.append("|\n");
            }
            return sb.toString();
        }
    }

    public static class Lik {
        // Tip lika - glej navodila
        private int indeks;

        private int orientacija;
        private int sirina;
        private int visina;

        // Števila, katerih zadnji biti predstavljajo bloke lika
        // Prvo število je najnižja vrstica
        private int[] oblika;

        // Shranjeni vsi liki z vsemi orientacijami
        private static Lik[][] orientiraniLiki = narediLike();

        private Lik(int indeks, int orientacija) {
            this.indeks = indeks;
            this.orientacija = orientacija;
        }

        private Lik (int indeks, int orientacija, int sirina, int visina, int[] oblika) {
            this.indeks = indeks;
            this.orientacija = orientacija;
            this.sirina = sirina;
            this.visina = visina;
            this.oblika = oblika;
        }

        public static Lik getLik(int indeks, int orientacija) {
            return Lik.orientiraniLiki[indeks][orientacija];
        }

        // Možne orientacije lika brez ponavljanj za simetrične like
        public static Lik[] moznosti(int indeks) {
            int stMoznosti;
            if (indeks == 0 || indeks == 4 || indeks == 6) stMoznosti = 2;
            else if (indeks == 1 || indeks == 2 || indeks == 5) stMoznosti = 4;
            else if (indeks == 3) stMoznosti = 1;
            else return null;
            Lik[] mozniLiki = new Lik[stMoznosti];
            for (int i = 0; i < stMoznosti; i++) {
                mozniLiki[i] = Lik.orientiraniLiki[indeks][i];
            }
            return mozniLiki;
        }

        // Generiranje likov, tabela števil predstavlja vrstice od spodaj navzgor,
        // Vsako število ima v zadnjih bitih zakodirane bloke lika
        private static Lik[][] narediLike() {
            Lik[][] liki = new Lik[7][4];

            // [][][][] -> 1111 -> 15
            liki[0][0] = new Lik(0, 0, 4, 1, new int[] {15});
            // [] -> 1 -> 1 
            // [] -> 1 -> 1 
            // [] -> 1 -> 1 
            // [] -> 1 -> 1 
            liki[0][1] = new Lik(0, 1, 1, 4, new int[] {1, 1, 1, 1});
            liki[0][2] = liki[0][0];
            liki[0][3] = liki[0][1];

            // []     -> 100 4
            // [][][] -> 111 7
            liki[1][0] = new Lik(1, 0, 3, 2, new int[] {7, 4});
            // [][] -> 11 -> 3
            // []   -> 10 -> 2
            // []   -> 10 -> 2
            liki[1][1] = new Lik(1, 1, 2, 3, new int[] {2, 2, 3});
            // [][][] -> 111 -> 7
            //     [] -> 001 -> 1
            liki[1][2] = new Lik(1, 2, 3, 2, new int[] {1, 7});
            //   [] -> 01 -> 1
            //   [] -> 01 -> 1
            // [][] -> 11 -> 3
            liki[1][3] = new Lik(1, 3, 2, 3, new int[] {3, 1, 1});

            liki[2][0] = new Lik(2, 0, 3, 2, new int[] {7, 1});
            liki[2][1] = new Lik(2, 1, 2, 3, new int[] {3, 2, 2});
            liki[2][2] = new Lik(2, 2, 3, 2, new int[] {4, 7});
            liki[2][3] = new Lik(2, 3, 2, 3, new int[] {1, 1, 3});

            liki[3][0] = new Lik(3, 0, 2, 2, new int[] {3, 3});
            liki[3][1] = liki[3][0];
            liki[3][2] = liki[3][0];
            liki[3][3] = liki[3][0];

            liki[4][0] = new Lik(4, 0, 3, 2, new int[] {6, 3});
            liki[4][1] = new Lik(4, 1, 2, 3, new int[] {1, 3, 2});
            liki[4][2] = liki[4][0];
            liki[4][3] = liki[4][1];

            liki[5][0] = new Lik(5, 0, 3, 2, new int[] {7, 2});
            liki[5][1] = new Lik(5, 1, 2, 3, new int[] {2, 3, 2});
            liki[5][2] = new Lik(5, 2, 3, 2, new int[] {2, 7});
            liki[5][3] = new Lik(5, 3, 2, 3, new int[] {1, 3, 1});

            liki[6][0] = new Lik(6, 0, 3, 2, new int[] {3, 6});
            liki[6][1] = new Lik(6, 1, 2, 3, new int[] {2, 3, 1});
            liki[6][2] = liki[6][0];
            liki[6][3] = liki[6][1];

            return liki;
        }
    }

    // Povezan seznam preteklih potez, usmerjen nazaj, torej prva poteza je na koncu seznama
    public static class Poteza {
        int lik;
        int orientacija;
        int koordinata;
        Poteza prejsnja;

        public Poteza(int lik, int orientacija, int koordinata, Poteza prejsnja) {
            this.lik = lik;
            this.orientacija = orientacija;
            this.koordinata = koordinata;
            this.prejsnja = prejsnja;
        }

        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder();
            Poteza p = this;
            while (p != null) {
                sb.insert(0, String.format("%d %d\n", p.orientacija, p.koordinata));
                p = p.prejsnja;
            }
            return sb.toString();
        }
    }
}
