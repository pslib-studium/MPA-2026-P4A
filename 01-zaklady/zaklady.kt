package cz.panerlebach.myapplication

fun main(args: Array<String>) {

// -----------------------------------------
// 1. Proměnné a datové typy
// -----------------------------------------

    // Proměnná, kterou lze měnit
    var promennaMenitelna: Int = 10
    promennaMenitelna = 20  // Hodnotu lze změnit

    // Neměnná proměnná (konstanta)
    val konstantaNemennitelna: String = "Toto je konstanta"
    // konstantaNemennitelna = "Nová hodnota"  // Toto by způsobilo chybu

    // Typy lze odvodit automaticky
    var automatickyTyp = 30  // Kotlin odvodí, že jde o Int

    // unsigned integer - nezáporné celé číslo
    var cislo: UInt = 10U

    // Převod UInt na Double (desetinné číslo)
    var desetinneCislo: Double = cislo.toDouble()
    println(desetinneCislo)
    // -----------------------------------------
    // 3. Vstup od uživatele
    // -----------------------------------------

    println("Zadej své jméno")
    /* Pokud používáte online nástroj https://play.kotlinlang.org/,
     je potřeba definovat vlastní funkci readln() pro simulaci vstupu.
    Pokud používáte Android Studio nebo jiná vývojová prostředí,
    funkce readln() je již k dispozici a není potřeba ji definovat. */

    val jmeno = readln()
    println("Ahoj, $jmeno!")

    // Alternativní způsob získání jména z argumentů příkazové řádky

    if (args.isNotEmpty()) {
        val jmeno = args[0]
        println("Ahoj, $jmeno!")
    } else {
        println("Nezadal jste žádné jméno.")
    }

    // -----------------------------------------
    // 4. Podmínky (if - else)
    // -----------------------------------------
    if (jmeno.isEmpty()) {
        println("Jméno nebylo zadáno.")
    } else if (jmeno.length > 5) {
        println("Máte dlouhé jméno!")
    } else {
        println("Máte kratší jméno!")
    }

    // -----------------------------------------
    // 5. Příkaz when (ekvivalent switch)
    // -----------------------------------------
    val denVTydnu = 3
    when (denVTydnu) {
        1 -> println("Pondělí")
        2 -> println("Úterý")
        3 -> println("Středa")
        4 -> println("Čtvrtek")
        5 -> println("Pátek")
        6, 7 -> println("Víkend")
        else -> println("Neplatný den v týdnu")
    }

    // -----------------------------------------
    // 6. Cyklus
    // -----------------------------------------

    // For cyklus - iterace v rozsahu
    for (i in 1..5) {
        println("Cyklus číslo $i")
    }

    // For cyklus - iterace přes pole
    val cisla = arrayOf(1, 2, 3, 4, 5)
    for (cislo in cisla) {
        println("Číslo z pole: $cislo")
    }

    // While cyklus
    var i = 0
    while (i < 5) {
        println("Hodnota i ve while cyklu: $i")
        i++
    }

    // Do-while cyklus
    var j = 0
    do {
        println("Hodnota j v do-while cyklu: $j")
        j++
    } while (j < 5)

    // -----------------------------------------
    // 7. Kolekce: seznamy (list)
    // -----------------------------------------

    // Neměnitelný seznam (immutable)
    val nemenitelnySeznam = listOf("Jablko", "Banán")
    // nemenitelnySeznam.add("Hruška") // Chyba – nelze měnit

    // Měnitelný seznam (mutable list)
    val menitelnyList = mutableListOf("Jablko", "Banán")
    menitelnyList.add("Hruška")

    for (polozka in menitelnyList) {
        println(polozka)
    }

    // Převod neměnitelného seznamu na měnitelný
    val seznam = nemenitelnySeznam.toMutableList()
    seznam.add("Kiwi")
    val prvniPolozkaSeznamu = seznam[0] // Jablko
    seznam[0] = "Brambora"               // změna prvku

    // -----------------------------------------
    // 8. ArrayList (Java kompatibilita)
    // -----------------------------------------

    val javaList = ArrayList<String>()
    javaList.add("Jablko")

    // -----------------------------------------
    // 9. Pole (array)
    // -----------------------------------------

    val pole = arrayOf(0, 2, 2, 5, 6)
    println(pole.contentToString())

    // -----------------------------------------
    // 10. Množina (set)
    // -----------------------------------------

    val mnozina = setOf(0, 2, 2, 2, 5, 9, 10)
    println(mnozina.contains(2))  // true
    println(mnozina.size)          // velikost bude 5 (množina odstraní duplicity)

    // -----------------------------------------
    // 11. Mapa (map)
    // -----------------------------------------

    val mapa = mutableMapOf(1 to "Jedna", 2 to "Dva")
    println(mapa[1])
    mapa.remove(1)

    // -----------------------------------------
    // 12. Nullable hodnoty v listech
    // -----------------------------------------

    val seznamSNull = listOf(1, 2, 3, null, 15, 88, 411, null, null, 15)
    val seznamBezNull = seznamSNull.filterNotNull()

    println(seznamBezNull.first())   // první prvek
    println(seznamBezNull.take(2))   // první dva prvky
    println(seznamBezNull.drop(2))   // vynechá první dva prvky a vezme zbytek

    // -----------------------------------------
    // 13. Filtrace seznamu
    // -----------------------------------------

    val cislaVetsiNezDeset = seznamBezNull.filter { it > 10 }
    val sudaCisla = seznamBezNull.filter { it % 2 == 0 }

    // -----------------------------------------
    // 14. Datové třídy a objekty
    // -----------------------------------------

    val uzivatel = Uzivatel("karel", "karel@seznam.cz")

    val auto = Auto("Skoda")
    println(auto.vypisRychlost(102.0))
}

// Definice datové třídy - jednoduchý kontejner dat
data class Uzivatel(val login: String, val email: String)

// Třída
class Auto(private val model: String) {

    fun vypisRychlost(rychlost: Double): String {
        return "$model jede rychlostí $rychlost km/h"
    }

}
