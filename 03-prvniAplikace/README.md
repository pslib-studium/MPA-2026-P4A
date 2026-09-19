# První Android aplikace – Hello World

Jednoduchá Android aplikace v Kotlinu. Po spuštění zobrazí text **Hello World!** a tlačítko **Klikni sem**. Po stisknutí tlačítka se text změní na **Ahoj světe!**.

## Jak je aplikace složená

- `app/src/main/res/layout/activity_main.xml` definuje vzhled obrazovky. Obsahuje:
  - `TextView` s ID `@+id/textview`, ve kterém se zobrazuje pozdrav.
  - `Button` s ID `@+id/tlacitko`, na který uživatel klepne.
- `app/src/main/java/cz/pslib/firstapp/MainActivity.kt` načte layout pomocí `setContentView(R.layout.activity_main)` a nastaví reakci na kliknutí.

## Co se stane po kliknutí

1. `findViewById<Button>(R.id.tlacitko)` najde tlačítko podle ID z XML layoutu.
2. `setOnClickListener` nastaví kód, který se provede po kliknutí.
3. `findViewById<TextView>(R.id.textview)` najde textové pole a jeho vlastnost `text` se nastaví na `"Ahoj světe!"`.

## Spuštění

1. Otevřete složku projektu v Android Studiu.
2. Vyberte emulátor nebo připojený telefon.
3. Spusťte konfiguraci `app` tlačítkem **Run**.
