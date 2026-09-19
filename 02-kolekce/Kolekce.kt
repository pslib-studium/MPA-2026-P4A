package com.example.kolekce

fun main() {

    val seznam = listOf("Jablka", "hrušky")  // Nezměnitelný seznam
    // seznam.add("Banan") // chyba, nelze měnit obsah seznamu
    val menitelnySeznam = mutableListOf("Jablka", "hrusky")
    menitelnySeznam.add("Banán") // Přidání položky do změnitelného seznamu

    menitelnySeznam[0] = "Jablko červené" // / Změna hodnoty na určitém indexu

    val javaList = ArrayList<String>() //  // Vytvoření ArrayListu (z Java standardní knihovny)
    javaList.add("Banan")

    val cisla = listOf<Int>()  // prazdny list císel
    val newListCisel = cisla.toMutableList()  // převod typu listu
    newListCisel.add(10)

    val pole = arrayOf(0, 1, 2, 2, 3,4, 5)   // Vytvoření pole
    // Výpis obsahu pole pomocí metody contentToString()
    println("Obsah pole: " + pole.contentToString())

    // Alternativně lze procházet pole pomocí cyklu foreach
    println("Výpis pole pomocí cyklu:")
    for (prvek in pole) {
        println(prvek)
    }

    // Vytvoření množiny (Set) - obsahuje pouze unikátní hodnoty
    val mnozina = setOf(1, 1, 2, 3) // Výsledná množina: 1, 2, 3
    // Převod pole na množinu (odstraní duplikáty)
    val mnozinaZPole = pole.toSet()

    // pár klíč-hodnota
    val mapa = mapOf(1 to "Jedna", 2 to "Dva")


    val seznamSNull = listOf(0, null, 1, null, null, 2, 3, 15, 30)
    val seznamBezNull = seznamSNull.filterNotNull() // // Filtrování null hodnot

    seznamBezNull.first() // první prvek
    seznamBezNull.take(2) // první dvě čísla
    seznamBezNull.drop(2) // vynechá první dva a získá zbytek seznamu

    val seznamCiselVetsiJakDeset = seznamBezNull.filter { it > 10 }
    val sudaCisla = seznamBezNull.filter { it % 2 == 0 }

    menitelnySeznam.filter { it.contains("B") }


    val mojeAuto = Auto("Škoda Fabie")
    mojeAuto.displaySpeed(100.0)

    val uzivatel = Uzivatel("karel", "karel@example.com")

    val seznamUzivatelu = mutableListOf<Uzivatel>()
    seznamUzivatelu.add(uzivatel)
}

// Definice třídy Auto s jedním atributem
class Auto(private val model: String) {

    val seznam = mutableListOf<String>()

    fun displaySpeed( speed: Double) : String {
        return "$model jede rychlostí $speed km/h"
    }

}
// Datová třída, slouží k ukládání dat bez dodatečné logiky
data class Uzivatel(val login: String, val email: String )