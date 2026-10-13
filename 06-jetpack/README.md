# Základy Jetpack Compose

Jetpack Compose vytváří uživatelské rozhraní pomocí funkcí v Kotlinu. V [lekci 04](../04-viewBinding/README.md) jsme načítali XML layout a měnili vlastnosti Views přes binding. V Compose popisujeme, **jak má obrazovka vypadat pro aktuální stav**.

Ukázkou je počítadlo: tlačítko **Přičíst 1** zvýší hodnotu, **Vynulovat** ji vrátí na nulu. Reset je při nule zakázaný. Hlavní kód najdete v [MainActivity.kt](app/src/main/java/cz/pslib/jetpack/MainActivity.kt).

## Rozhraní tvoří funkce

`setContent { ... }` v `MainActivity` určuje obsah obrazovky. Uvnitř se volají funkce označené anotací `@Composable`. Ty popisují jednotlivé části UI: `Text` zobrazí text, `Button` tlačítko a naše `CounterContent` celé počítadlo.

Když se sledovaný stav změní, Compose znovu vyhodnotí potřebné části popisu a aktualizuje obrazovku. Tomu říkáme **rekompozice**. Nový text tedy vzniká z nové hodnoty počítadla.

## Stav počítadla

Funkce `Counter` uchovává počet kliknutí:

```kotlin
var count by rememberSaveable { mutableStateOf(0) }
```

`mutableStateOf(0)` vytvoří stav s počáteční hodnotou nula, jehož změny Compose sleduje. Díky delegaci `by` můžeme psát `count++` místo práce s `count.value`. Obyčejná proměnná `var count = 0` by sama aktualizaci UI nevyvolala.

`remember` uchovává hodnotu mezi rekompozicemi. `rememberSaveable` ji navíc dokáže obnovit při znovuvytvoření Activity, například po otočení telefonu. Trvalé uložení pro pozdější nové spuštění aplikace řeší úložiště, například SharedPreferences z [lekce 05](../05-intent/README.md).

## Data dolů, události nahoru

`Counter` předává hodnotu a obsluhu událostí funkci `CounterContent`:

```kotlin
CounterContent(
    count = count,
    onIncrement = { count++ },
    onReset = { count = 0 },
    modifier = modifier
)
```

Zápis `{ count++ }` je **lambda**, tedy funkce předaná jako hodnota. Spustí se až po kliknutí. `CounterContent` zobrazuje předaná data a přes `onIncrement` nebo `onReset` oznamuje události rodiči. Stav vlastní `Counter`; tomuto uspořádání říkáme **state hoisting**.

Tok změny je jednoduchý: **kliknutí → změna `count` → rekompozice → aktualizované UI**.

Stejně se ze stavu odvozuje dostupnost resetu:

```kotlin
OutlinedButton(onClick = onReset, enabled = count > 0) {
    Text(stringResource(R.string.reset))
}
```

Při nule je podmínka nepravdivá, takže tlačítko nereaguje na kliknutí.

## Rozložení a vzhled

- **`Box`** v ukázce vystředí obsah obrazovky.
- **`Column`** skládá nadpis, počet kliknutí a tlačítka pod sebe.
- **`Row`** umístí obě tlačítka vedle sebe.
- **`Modifier`** nastavuje například velikost a odsazení prvku. Na pořadí jeho úprav záleží.

Řetězec `modifier.fillMaxSize().padding(24.dp)` vyplní dostupný prostor a uvnitř vytvoří odsazení. Jednotka `dp` vyjadřuje rozměr nezávislý na hustotě displeje. `Scaffold` poskytuje odsazení od systémových lišt jako `innerPadding`.

`stringResource` načítá texty ze `strings.xml`. U textu počítadla dosadí aktuální `count` do připraveného formátu. `JetpackTheme` poskytuje barvy a styly písma pro světlý i tmavý režim.

## Náhledy

Anotace `@Preview` umožňuje zobrazit UI v editoru. Na konci souboru jsou náhledy hodnot **0** a **5** ve světlém i tmavém tématu. Používají `CounterContent` s pevnou hodnotou a prázdnými obsluhami událostí, takže ukazují vzhled, ale klikání v nich počet nemění.
