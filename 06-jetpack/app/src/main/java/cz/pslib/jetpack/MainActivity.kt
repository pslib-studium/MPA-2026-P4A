package cz.pslib.jetpack

import android.content.res.Configuration
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import cz.pslib.jetpack.ui.theme.JetpackTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Obsah může sahat pod systémové lišty. Jejich odsazení řeší Scaffold níže.
        enableEdgeToEdge()

        // Místo XML layoutu a setContentView popíšeme celé UI funkcemi v Kotlinu.
        setContent {
            // Téma poskytuje vnořeným prvkům barvy a styly písma Material Design 3.
            JetpackTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    // innerPadding zohledňuje systémové lišty; aplikujeme ho právě jednou.
                    Counter(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}

// @Composable označuje funkci, která popisuje UI. Voláme ji z jiné composable funkce
// nebo ze setContent. Při změně sledovaného stavu může Compose její kód spustit znovu.
@Composable
fun Counter(modifier: Modifier = Modifier) {
    // mutableStateOf vytvoří stav, jehož změny Compose sleduje. Obyčejné var by UI
    // samo neaktualizovalo. Delegace by dovolí číst a zapisovat count místo count.value;
    // k tomu patří importy getValue a setValue nahoře.
    // rememberSaveable zachová stav při rekompozici i při znovuvytvoření Activity,
    // například po otočení telefonu. Není to trvalé úložiště jako SharedPreferences.
    var count by rememberSaveable { mutableStateOf(0) }

    // Stav vlastní tato funkce. Potomkovi předá hodnotu a lambdy pro jeho události.
    // Lambdy se provedou až po kliknutí, nikoli při sestavování UI.
    CounterContent(
        count = count,
        onIncrement = { count++ },
        onReset = { count = 0 },
        modifier = modifier
    )
}

// Tato funkce nemá vlastní stav počítadla. Z parametrů vždy popíše odpovídající UI.
// Přesunutí stavu k rodiči se nazývá state hoisting: data dolů, události nahoru.
@Composable
fun CounterContent(
    count: Int,
    onIncrement: () -> Unit,
    onReset: () -> Unit,
    modifier: Modifier = Modifier
) {
    // Modifier popisuje velikost, odsazení a další vlastnosti prvku. Řetězení má pořadí:
    // nejprve přijmeme nastavení rodiče, vyplníme prostor a uvnitř přidáme 24 dp.
    Box(
        modifier = modifier.fillMaxSize().padding(24.dp),
        contentAlignment = Alignment.Center
    ) {
        // Box umístí obsah doprostřed; Column skládá prvky svisle pod sebe.
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = stringResource(R.string.counter_title),
                style = MaterialTheme.typography.headlineMedium
            )
            // stringResource načítá text z res/values/strings.xml a dosadí count za %1$d.
            Text(
                text = stringResource(R.string.counter_value, count),
                style = MaterialTheme.typography.bodyLarge
            )
            // Row skládá tlačítka vedle sebe. dp je jednotka nezávislá na hustotě displeje.
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                Button(onClick = onIncrement) {
                    Text(stringResource(R.string.increment))
                }
                // enabled se odvodí z aktuálního stavu; při nule není co resetovat.
                OutlinedButton(onClick = onReset, enabled = count > 0) {
                    Text(stringResource(R.string.reset))
                }
            }
        }
    }
}

// Každá dvojice @Preview zobrazí stejný stav ve světlém a tmavém tématu.
// Náhled nepotřebuje spustit Activity. Prázdné lambdy stačí pro statickou ukázku;
// dynamické barvy vypínáme, aby vzhled náhledů nezávisel na tapetě zařízení.
@Preview(name = "Nula – světlé", showBackground = true)
@Preview(name = "Nula – tmavé", showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun CounterZeroPreview() {
    JetpackTheme(dynamicColor = false) {
        Surface {
            CounterContent(count = 0, onIncrement = {}, onReset = {})
        }
    }
}

@Preview(name = "Pět – světlé", showBackground = true)
@Preview(name = "Pět – tmavé", showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun CounterNonZeroPreview() {
    JetpackTheme(dynamicColor = false) {
        Surface {
            CounterContent(count = 5, onIncrement = {}, onReset = {})
        }
    }
}
