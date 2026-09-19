# 02 – Kolekce v Kotlinu

Tato složka obsahuje jednoduchou ukázku práce s kolekcemi v Kotlinu. Zdrojový soubor [`Kolekce.kt`](Kolekce.kt) představuje seznamy, pole, množiny, mapy, filtrování hodnot a ukládání vlastních objektů do kolekce.

## Přehled kolekcí

| Kolekce | Vytvoření | Vlastnosti |
| --- | --- | --- |
| `List` | `listOf(...)` | Seřazený seznam určený pouze pro čtení. |
| `MutableList` | `mutableListOf(...)` | Seznam, do kterého lze přidávat položky a měnit je. |
| `ArrayList` | `ArrayList<T>()` | Měnitelný seznam známý také z Javy. |
| `Array` | `arrayOf(...)` | Pole s pevně daným počtem prvků. |
| `Set` | `setOf(...)` | Množina, která obsahuje pouze unikátní hodnoty. |
| `Map` | `mapOf(klic to hodnota)` | Kolekce dvojic klíč–hodnota. |

## Neměnný a měnitelný seznam

`listOf` vytvoří seznam určený pouze pro čtení:

```kotlin
val seznam = listOf("Jablka", "Hrušky")
```

Do takového seznamu nelze přidávat další položky. Pro změny slouží `mutableListOf`:

```kotlin
val menitelnySeznam = mutableListOf("Jablka", "Hrušky")
menitelnySeznam.add("Banán")
menitelnySeznam[0] = "Jablko červené"
```

Indexy začínají od nuly, proto zápis `[0]` pracuje s první položkou.

## Pole a procházení prvků

Pole vytvoříme pomocí `arrayOf`. Celé pole lze vypsat metodou `contentToString`, nebo jeho prvky projít cyklem `for`:

```kotlin
val pole = arrayOf(0, 1, 2, 2, 3, 4, 5)

println(pole.contentToString())

for (prvek in pole) {
    println(prvek)
}
```

## Set a Map

`Set` automaticky odstraní duplicitní hodnoty. Z pole jej lze vytvořit pomocí `toSet`:

```kotlin
val mnozina = setOf(1, 1, 2, 3) // obsahuje 1, 2, 3
val mnozinaZPole = pole.toSet()
```

`Map` ukládá hodnoty pod klíči:

```kotlin
val mapa = mapOf(1 to "Jedna", 2 to "Dva")
```

## Práce s `null` a filtrování

Metoda `filterNotNull` vytvoří nový seznam bez hodnot `null`:

```kotlin
val seznamSNull = listOf(0, null, 1, null, 2, 3, 15, 30)
val seznamBezNull = seznamSNull.filterNotNull()
```

Další použité operace:

- `first()` vrátí první prvek,
- `take(2)` vrátí první dva prvky,
- `drop(2)` první dva prvky vynechá,
- `filter { podmínka }` vytvoří nový seznam prvků splňujících podmínku,
- `toMutableList()` převede seznam na měnitelnou variantu.

Například:

```kotlin
val vetsiNezDeset = seznamBezNull.filter { it > 10 }
val sudaCisla = seznamBezNull.filter { it % 2 == 0 }
```

Tyto operace původní kolekci nemění. Výsledek vracejí jako novou kolekci, kterou je potřeba uložit do proměnné nebo vypsat.

## Vlastní objekty v kolekci

Ukázka obsahuje také běžnou třídu `Auto` a datovou třídu `Uzivatel`. Objekty lze ukládat do kolekce stejně jako čísla nebo text:

```kotlin
data class Uzivatel(val login: String, val email: String)

val seznamUzivatelu = mutableListOf<Uzivatel>()
seznamUzivatelu.add(Uzivatel("karel", "karel@example.com"))
```

Datová třída je vhodná především pro uchovávání dat. Kotlin jí automaticky vytvoří například metody `toString`, `equals` a `copy`.

## Spuštění ukázky

Složka není samostatný Android ani Gradle projekt. Kód lze nejrychleji vyzkoušet jedním z těchto způsobů:

1. Otevřete [Kotlin Playground](https://play.kotlinlang.org/), vložte obsah souboru `Kolekce.kt` a spusťte funkci `main`.
2. V IntelliJ IDEA nebo Android Studiu vytvořte Kotlin/JVM projekt, vložte do něj soubor `Kolekce.kt` a spusťte zelenou šipkou funkci `main`.

Pro lepší pochopení si zkuste pomocí `println` vypsat proměnné `mnozinaZPole`, `mapa`, `vetsiNezDeset` a `sudaCisla`.

Zpět na [hlavní repozitář MPA-2026-P4A](https://github.com/pslib-studium/MPA-2026-P4A/tree/main).
