# View Binding, ViewGroup a View

- **View** je jeden prvek uživatelského rozhraní, například `TextView`, `EditText` nebo `Button`.
- **ViewGroup** je View, který může obsahovat další Views. V ukázce je to `LinearLayout` s ID `greetingContainer`.
- **View Binding** vygeneruje typově bezpečnou třídu pro každý XML layout. Díky tomu není potřeba používat `findViewById`.

## Jak fungují názvy binding tříd

Název XML souboru se převede na název Kotlin třídy:

| XML layout | Vygenerovaná třída |
| --- | --- |
| `activity_main.xml` | `ActivityMainBinding` |
| `item_greeting.xml` | `ItemGreetingBinding` |

ID prvků se stanou vlastnostmi bindingu:

- `@+id/nameInput` → `binding.nameInput`
- `@+id/addButton` → `binding.addButton`
- `@+id/greetingContainer` → `binding.greetingContainer`
- `@+id/greetingText` → `itemBinding.greetingText`

## Postup v Android Studiu

### 1. Zapnutí View Binding

Do bloku `android` v souboru `app/build.gradle.kts` přidejte:

```kotlin
buildFeatures {
    viewBinding = true
}
```

Potom klikněte na **Sync Now**. Android Studio vygeneruje binding třídy při sestavení projektu.

### 2. Hlavní layout

V `activity_main.xml` vytvořte svislý `LinearLayout`. Přidejte do něj `EditText`, `Button` a další `LinearLayout` s ID `greetingContainer`. Vnější i vnitřní `LinearLayout` jsou ViewGroups, ostatní prvky jsou jednotlivé Views.

### 3. Layout jedné položky

V adresáři `res/layout` vytvořte soubor `item_greeting.xml`. Jeho kořenem je `LinearLayout` a uvnitř je `TextView` s ID `greetingText`. Z názvu souboru vznikne třída `ItemGreetingBinding`.

### 4. Načtení hlavního layoutu

V `MainActivity` vytvořte binding a jeho kořenový View předejte metodě `setContentView`:

```kotlin
binding = ActivityMainBinding.inflate(layoutInflater)
setContentView(binding.root)
```

`binding.root` je kořenový `LinearLayout` z `activity_main.xml`.

### 5. Reakce na tlačítko

Tlačítko je dostupné přímo podle svého ID:

```kotlin
binding.addButton.setOnClickListener {
    val name = binding.nameInput.text.toString()
}
```

Kompilátor zná typ každého prvku. Překlep v ID se proto projeví jako chyba už při překladu.

### 6. Vytvoření a přidání nového View

Druhý layout vytvořte přes jeho binding. Parametr `false` znamená, že se položka zatím nepřipojí k rodiči automaticky:

```kotlin
val itemBinding = ItemGreetingBinding.inflate(
    layoutInflater,
    binding.greetingContainer,
    false
)

itemBinding.greetingText.text = "Ahoj!"
binding.greetingContainer.addView(itemBinding.root)
```

Metoda `addView` funguje, protože `greetingContainer` je ViewGroup. Každé další kliknutí přidá další instanci layoutu `item_greeting.xml`.

## Vyzkoušení aplikace

1. Spusťte konfiguraci `app` na emulátoru nebo telefonu.
2. Zadejte jméno a stiskněte **Přidat pozdrav**.
3. Zkuste přidat více jmen a sledujte, jak ve ViewGroup přibývají Views.
4. Nechte vstup prázdný. Aplikace použije výchozí text `Ahoj, světe!`.

V aplikačním kódu není použito `findViewById`; přístup ke všem prvkům zajišťuje View Binding.

## Řešení problémů s Binding třídou

Pokud Android Studio hlásí, že binding třída (např. `ActivityMainBinding`) neexistuje nebo neobsahuje očekávané prvky, i když je layout i `viewBinding = true` v pořádku, zkuste postupně:

1. **Build → Clean Project** a poté **Build → Rebuild Project**.
2. Pokud to nepomůže, zkuste **File → Invalidate Caches...** → zaškrtnout vše → **Invalidate and Restart**. Android Studio smaže vnitřní cache a indexy a po restartu projekt znovu vygeneruje binding třídy.
3. Po restartu proveďte znovu **Sync Now** (ikona slona / notifikace nad `build.gradle.kts`).

Tento problém se typicky objeví po přejmenování XML souboru, přidání nového ID nebo přepnutí větve v gitu, kdy Android Studio nestihne správně přegenerovat binding třídy.
