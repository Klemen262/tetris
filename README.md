# Tekmovanje pri predmetu programiranje 1
Kot študent tretjega letnika sem za izziv rešil nalogo, ki so jo dobili študentje prvega letnika za tekmovanje pri predmetu programiranje 1.

# Naloga: reševanje tetrisa z vnaprej podanim zaporedjem likov
Dobimo zaporednje likov ki padajo v jamo pri tetrisu. Namesto standardnega tetris točkovanja se tukaj upošteva samo višina blokov v jami. Cilj je like v jamo spustiti tako, da je tekom spuščanja likov najvišja dosežena višina blokov v jami čimnižja. Upošteva se višina po brisanju polnih vrstic. Glej priložena navodila.
Moja rešitev za veliko večino naključno ustvarjenih testnih primerov doseže rezultat 3, tudi pri testiranju z milijon liki.

### Izsek iz profesorjevega maila ob razglasitvi rezultatov z dobro pojasnjenim delovanjem algoritma:
Absolutni zmagovalec tekmovanja je nastopal izven konkurence, saj obiskuje tretji letnik. Kljub temu pa ga velja izpostaviti, saj je dosegel povprečno višino 3,00! Prav ste prebrali — pri nobenem testnem primeru ni nikoli presegel višine 3. To mu je uspelo z razmeroma preprostim algoritmom, v katerem glavno vlogo igra prioritetna vrsta (PriorityQueue). Gre za vsebovalnik, ki podpira učinkovito dodajanje elementov in izločanje najmanjšega elementa. Prioritetna vrsta hrani posamezne "jame", pri čemer vsaka jama podaja vsebino igralne površine po določenem številu spustov. Vrsta na začetku vsebuje zgolj prazno jamo (jamo po 0 spuščenih likih), nato pa se v vsakem obhodu glavne zanke iz vrste izloči najugodnejša jama (recimo ji J), v vrsto pa se dodajo vse naslednice jame J. (Če jama J predstavlja stanje po k spustih, potem so njene naslednice vse jame, ki nastanejo, ko v jamo J na vse mogoče načine spustimo lik z zaporedno številko k + 1.) Zanka se zaključi, ko se iz prioritetne vrste izloči jama, ki predstavlja stanje po vseh n spustih. Ker sta čas in prostor omejena, algoritem občasno umetno zmanjša velikost prioritetne vrste: v trenutku, ko število jam v vrsti preseže 5000, ohrani le tisoč najugodnejših, ostale pa zavrže.

Kako pa se določi najugodnejša jama? To je jama, ki ima najmanjšo maksimalno doseženo višino. Če je več takih, je najugodnejša med njimi tista, pri kateri je dotedanje število spustov največje (pri takšni jami bomo hitreje prišli do cilja, torej do vseh n spustov). Če imamo še vedno več možnosti, je najugodnejša jama tista, ki ima najmanjšo trenutno skupno višino, če je tudi po tem filtru še kaj izbire, pa tista, ki ima najmanjše število kvadratov 1 x 1.

Algoritem se odlično obnese na naključnih vhodih, "uniči" pa ga zaporedje Z-jev (likov z indeksom 6), toda ne zato, ker bi tam dosegal slabše rezultate (pri n = 25 doseže maksimalno višino 4, zmagovalec prvega letnika pa 9), ampak zato, ker že pri n = 26 krepko preseže časovno omejitev.

## Uporaba
Odkomentirajte/zakomentirajte želene vrstice v metodi main<br>
`javac DNTE_6319001.java`

`java DNTE_63190001` in vnašanje vhoda ročno ali<br>
`java DNTE_63190001 < vhod.txt [> izhod.txt]`

## Možna časovna optimizacija
Recikliranje objektov razreda Jama, nametso da vedno generiramo nove, stare pa prepuščamo usodi samevanja v pomnilniku, dokler jih ne pobere smetar.