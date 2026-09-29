# Intent, Activity Result API, SharedPreferences a AndroidManifest

Aplikace umožnuje

1. spustit druhou **Activity** pomocí explicitního **Intentu**,
2. poslat jí data pomocí **Intent extras**,
3. vrátit výsledek zpět pomocí **Activity Result API**,
4. uložit si jednoduchá data pomocí **SharedPreferences**,
5. pochopit roli souboru **AndroidManifest.xml**.

Stejně jako v [kapitole 04](../04-viewBinding/README.md) používáme **View Binding** (žádné `findViewById`) a klasické XML layouty.

## Struktura projektu

| Soubor | K čemu slouží |
| --- | --- |
| `app/src/main/AndroidManifest.xml` | Seznam Activity, hlavní obrazovka, `exported` |
| `MainActivity.kt` | Hlavní obrazovka, SharedPreferences, spuštění druhé Activity, příjem výsledku |
| `EditNameActivity.kt` | Druhá obrazovka, čtení extras, vrácení výsledku |
| `res/layout/activity_main.xml` | Layout hlavní obrazovky → `ActivityMainBinding` |
| `res/layout/activity_edit_name.xml` | Layout druhé obrazovky → `ActivityEditNameBinding` |
| `res/values/strings.xml` | Všechny texty aplikace |
| `app/build.gradle.kts` | Zapnutý `viewBinding = true` |

## 1. AndroidManifest.xml

Manifest je „průvodní list“ aplikace. Android ho přečte dříve, než spustí jakýkoliv kód, a dozví se z něj například:

- jak se aplikace jmenuje a jakou má ikonu (`android:label`, `android:icon`),
- jaké téma používá (`android:theme`),
- **jaké Activity aplikace obsahuje** a která z nich je hlavní.

Manifest z tohoto projektu (zkráceně):

```xml
<application ...>

    <activity
        android:name=".MainActivity"
        android:exported="true">
        <intent-filter>
            <action android:name="android.intent.action.MAIN" />
            <category android:name="android.intent.category.LAUNCHER" />
        </intent-filter>
    </activity>

    <activity
        android:name=".EditNameActivity"
        android:exported="false" />

</application>
```

### Registrace Activity

**Každá Activity musí být v manifestu zapsaná.** Zápis `.EditNameActivity` (s tečkou na začátku) znamená třídu `EditNameActivity` v balíčku aplikace, tedy `cz.pslib.intent.EditNameActivity`. Pokud záznam chybí, aplikace spadne při pokusu o spuštění s chybou `ActivityNotFoundException`.

### Hlavní Activity: MAIN + LAUNCHER

Blok `<intent-filter>` je „visačka“, která říká, na jaké Intenty Activity reaguje. Dvojice v `MainActivity` má tento význam:

| Prvek | Význam |
| --- | --- |
| `action ... MAIN` | Tato Activity je **vstupní bod** aplikace. |
| `category ... LAUNCHER` | Aplikace se zobrazí v seznamu aplikací a tato Activity se otevře po klepnutí na ikonu. |

Podle této dvojice Android Studio pozná, kterou Activity má spustit tlačítkem **Run**. Ostatní Activity (`EditNameActivity`) `intent-filter` nemají, protože je nespouští ikona, ale naše vlastní kód.

### android:exported

Atribut určuje, zda Activity smí spustit **někdo mimo naši aplikaci** (jiná aplikace nebo systém).

| Hodnota | Význam | V projektu |
| --- | --- | --- |
| `true` | Activity může spustit i cizí aplikace | `MainActivity` – ikonu v launcheru spouští systém, tedy „cizí“ program |
| `false` | Activity může spustit jen naše aplikace | `EditNameActivity` – je to vnitřní obrazovka |

Od Androidu 12 (`targetSdk` 31 a vyšší) je atribut u každé Activity s `<intent-filter>` **povinný**, jinak sestavení skončí chybou `android:exported needs to be explicitly specified`. Pravidlo: nastavujte `false`, pokud není důvod pro `true`. Nevystavujete tak obrazovky, které nejsou určené pro cizí aplikace.

## 2. Druhá Activity

Postup v Android Studiu:

1. **Layout:** v `res/layout` vytvořte `activity_edit_name.xml` (*New → Layout Resource File*). Kořenem je svislý `LinearLayout`. Prvky mají ID `currentNameText`, `changesText`, `newNameInput`, `saveButton` a `cancelButton`. Z názvu souboru vznikne třída `ActivityEditNameBinding`.
2. **Třída:** vytvořte `EditNameActivity : AppCompatActivity()` a stejně jako v kapitole 04 načtěte layout přes binding:

   ```kotlin
   binding = ActivityEditNameBinding.inflate(layoutInflater)
   setContentView(binding.root)
   ```

3. **Manifest:** přidejte do `AndroidManifest.xml` záznam `<activity android:name=".EditNameActivity" android:exported="false" />`.

Android Studio umí všechny tři věci vytvořit najednou (*File → New → Activity → Empty Views Activity*), včetně záznamu v manifestu. Vygenerovaný layout ale používá `ConstraintLayout` a kód pro okraje obrazovky, který zde nepotřebujeme. Kdo Activity vytváří ručně, musí na **záznam v manifestu** myslet sám. Je to nejčastější chyba (viz [Časté chyby](#časté-chyby)).

## 3. Explicitní Intent a extras

**Intent** je „záměr“: zpráva systému, že chceme, aby se něco stalo. Je-li v Intentu uvedena přesná cílová třída, jde o **explicitní** Intent.

Odeslání v `MainActivity`:

```kotlin
val editIntent = Intent(this, EditNameActivity::class.java)
    .putExtra(EditNameActivity.EXTRA_CURRENT_NAME, loadName())
    .putExtra(EditNameActivity.EXTRA_CHANGES, loadChanges())
editNameLauncher.launch(editIntent)
```

- `this` je `Context`, ze kterého Intent odchází. Podle něj systém pozná, do které aplikace `EditNameActivity` patří.
- `EditNameActivity::class.java` je cíl.
- `putExtra(klíč, hodnota)` přidá do Intentu data. **Extras** fungují jako malý slovník klíč → hodnota. Zde posíláme `String` (jméno) a `Int` (počet změn).

Příjem v `EditNameActivity`:

```kotlin
val currentName = intent.getStringExtra(EXTRA_CURRENT_NAME).orEmpty()
val changes = intent.getIntExtra(EXTRA_CHANGES, 0)
```

- `intent` je vlastnost každé Activity: Intent, kterým byla spuštěna.
- Extra nemusí v Intentu být, proto `getStringExtra` vrací `String?` (`.orEmpty()` ho změní na prázdný text) a `getIntExtra` má druhý parametr **výchozí hodnotu**.
- Typ musí sedět. Pokud pošlete `Int` a čtete `getStringExtra`, dostanete `null`.
- Klíče jsou konstanty v `companion object` třídy `EditNameActivity`. Obě Activity tak používají stejný text a překlep se projeví jako chyba při překladu, ne až za běhu.

Extras jsou určené pro **malá a jednoduchá data** (text, čísla, `Boolean`). Velké objekty se tudy posílat nemají.

Když výsledek z druhé Activity nepotřebujete, stačí `startActivity(editIntent)`. Pro návrat výsledku slouží launcher popsaný v další části.

### Explicitní × implicitní Intent

| | Explicitní | Implicitní |
| --- | --- | --- |
| Co uvedeme | konkrétní třídu Activity | jen **akci**, kterou chceme provést |
| Příklad | `Intent(this, EditNameActivity::class.java)` | `Intent(Intent.ACTION_VIEW, uri)` – otevřít odkaz |
| Kdo Activity vybere | my | systém (nabídne vhodné aplikace) |
| Použití | obrazovky vlastní aplikace | web, sdílení, mapa, telefon… |

V této kapitole používáme pouze explicitní Intent.

### Zásobník Activity (back stack)

Android drží spuštěné Activity v zásobníku. Nová Activity se položí navrch. Zavolání `finish()` nebo tlačítko **Zpět** ji z vrcholu odstraní a uživatel se vrátí na Activity pod ní. Proto se výsledek vrací právě do `MainActivity`.

## 4. Vrácení výsledku: Activity Result API

Výsledek vrací tři kroky.

**A) V MainActivity zaregistrujeme launcher** (jako vlastnost třídy):

```kotlin
private val editNameLauncher = registerForActivityResult(
    ActivityResultContracts.StartActivityForResult()
) { result ->
    val newName = result.data?.getStringExtra(EditNameActivity.EXTRA_NEW_NAME)
    if (result.resultCode == RESULT_OK && newName != null) {
        saveName(newName)
        showProfile()
    }
}
```

**B) Druhou Activity spustíme přes launcher** místo `startActivity`:

```kotlin
editNameLauncher.launch(editIntent)
```

**C) V EditNameActivity výsledek nastavíme a Activity zavřeme:**

```kotlin
setResult(RESULT_OK, Intent().putExtra(EXTRA_NEW_NAME, newName))
finish()
```

Vysvětlení:

- `StartActivityForResult()` je **kontrakt**: říká, *co* se spustí (Activity podle Intentu) a *jaký výsledek* se vrátí (`ActivityResult`). Stejný princip mají i další kontrakty, například `TakePicture` nebo `RequestPermission`.
- Blok `{ result -> ... }` se zavolá **až po návratu** z druhé Activity.
- `result.resultCode` říká, jak Activity skončila:

  | Kód | Význam |
  | --- | --- |
  | `RESULT_OK` | Uživatel akci dokončil, výsledek je v `result.data`. |
  | `RESULT_CANCELED` | Uživatel akci zrušil. Stejný kód se vrací, když stiskne systémové **Zpět**. |

- `result.data` je `Intent?`. Po zrušení bývá `null`, proto se čte přes `?.`.
- `setResult` výsledek jen **připraví**. Předá se až po `finish()`, kdy se Activity zavře.
- **Proč je launcher vlastnost třídy?** Registrace musí proběhnout dřív, než je Activity ve stavu STARTED. Kdybyste `registerForActivityResult` zavolali až v `setOnClickListener`, aplikace spadne s `IllegalStateException`.

### Starší řešení: startActivityForResult

Ve starších návodech a projektech najdete dvojici `startActivityForResult(intent, REQUEST_CODE)` a `override fun onActivityResult(requestCode, resultCode, data)`. Toto řešení je **zastaralé (deprecated)**, v nových aplikacích ho nepoužívejte. Princip zůstává stejný (Intent, `setResult`, `finish`, `resultCode`). Liší se místo zpracování výsledku:

| | Starší | Současné |
| --- | --- | --- |
| Spuštění | `startActivityForResult(intent, 1)` | `launcher.launch(intent)` |
| Příjem výsledku | jedna metoda `onActivityResult` pro všechny návraty | lambda u každého launcheru zvlášť |
| Rozlišení návratů | ručně přes číslo `requestCode` | není potřeba, každý launcher má vlastní callback |

## 5. SharedPreferences

**SharedPreferences** je malé úložiště dvojic klíč → hodnota. Hodí se pro jednoduchá data, která si má aplikace pamatovat: jméno uživatele, zvolený jazyk, zapnutý tmavý režim, poslední skóre. Data se ukládají do souboru v soukromém úložišti aplikace a **přežijí vypnutí aplikace i restart telefonu**.

Nehodí se pro velké množství dat (tam patří databáze) ani pro hesla a další citlivé údaje.

Objekt získáme v `onCreate`:

```kotlin
prefs = getSharedPreferences("profile", MODE_PRIVATE)
```

`"profile"` je název souboru, `MODE_PRIVATE` znamená, že k němu má přístup pouze naše aplikace.

### Čtyři operace

| Operace | Kód v projektu |
| --- | --- |
| **Načtení** | `prefs.getString(KEY_NAME, null)`, `prefs.getInt(KEY_CHANGES, 0)` |
| **Uložení** | `prefs.edit().putString(KEY_NAME, newName).apply()` |
| **Změna** | totéž: `put` se stejným klíčem starou hodnotu přepíše |
| **Smazání** | `prefs.edit().clear().apply()` (jeden klíč: `.remove(KEY_NAME)`) |

Uložení a změna jsou v SharedPreferences stejná operace. Když klíč neexistuje, vznikne. Když existuje, přepíše se jeho hodnota.

Načtení s výchozí hodnotou:

```kotlin
private fun loadName(): String =
    prefs.getString(KEY_NAME, null) ?: getString(R.string.default_name)

private fun loadChanges(): Int = prefs.getInt(KEY_CHANGES, 0)
```

Druhý parametr je hodnota, která se vrátí, dokud klíč nebyl uložen. Proto aplikace při prvním spuštění zobrazí `host` a `0`. `getString` v Kotlinu vrací `String?`, takže `null` nahradíme výchozím textem operátorem `?:`.

Uložení a změna:

```kotlin
private fun saveName(newName: String) {
    prefs.edit()
        .putString(KEY_NAME, newName)
        .putInt(KEY_CHANGES, loadChanges() + 1)
        .apply()
}
```

- Zápis vždy začíná `edit()`, který vrátí `Editor`. Do něj lze zřetězit více `put…` volání.
- **Bez `apply()` se nic neuloží.** `apply()` změnu okamžitě promítne do paměti a na disk ji zapíše na pozadí. Varianta `commit()` na zápis počká a vrátí `Boolean`. Pro běžné použití stačí `apply()`.
- Počet změn se zvyšuje tak, že se hodnota **načte, zvýší o 1 a uloží zpět**.

### Kde jsou data uložena

V emulátoru je v *Device Explorer* (*View → Tool Windows → Device Explorer*) soubor `/data/data/cz.pslib.intent/shared_prefs/profile.xml`:

```xml
<map>
    <string name="name">Petr</string>
    <int name="changes" value="2" />
</map>
```

Data zmizí po odinstalaci aplikace nebo po *Nastavení → Aplikace → Úložiště → Vymazat data*.

## Vyzkoušení aplikace

Spusťte konfiguraci `app` na emulátoru nebo telefonu a projděte tabulku.

| # | Akce | Co uvidíte | Co se tím ukazuje |
| --- | --- | --- | --- |
| 1 | První spuštění | `Ahoj, host!`, počet změn `0` | klíče ještě nejsou uloženy, platí výchozí hodnoty |
| 2 | **Změnit jméno** | `Aktuální jméno: host`, `Počet změn jména: 0` | extras dorazily do druhé Activity |
| 3 | **Uložit** s prázdným polem | chyba u pole, Activity zůstane otevřená | výsledek se vrací až po kontrole vstupu |
| 4 | Zadejte `Petr` → **Uložit** | `Ahoj, Petr!`, počet změn `1` | `RESULT_OK`, uložení do SharedPreferences |
| 5 | Změňte jméno na `Anna` | `Ahoj, Anna!`, počet změn `2` | změna hodnoty přepsáním klíče |
| 6 | **Změnit jméno** → **Zrušit** (nebo **Zpět**) | nic se nezmění | `RESULT_CANCELED` |
| 7 | Aplikaci úplně ukončete a znovu spusťte | jméno i počet zůstaly | SharedPreferences přežijí vypnutí |
| 8 | **Obnovit výchozí** | `Ahoj, host!`, počet `0` | smazání hodnot pomocí `clear()` |

### Experimenty

Každou změnu vyzkoušejte a poté vraťte zpět:

1. **Odeberte `<activity>` pro `EditNameActivity` z manifestu.** Po kliknutí na **Změnit jméno** aplikace spadne. V *Logcatu* najděte `ActivityNotFoundException`.
2. **Odeberte `.apply()` v `saveName`.** Jméno se nikdy neuloží.
3. **V `EditNameActivity` změňte `RESULT_OK` na `RESULT_CANCELED`.** Jméno se nezmění, protože `MainActivity` výsledek zahodí.


