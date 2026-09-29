package cz.pslib.intent

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import cz.pslib.intent.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var prefs: SharedPreferences

    // 1. Launcher zpracuje výsledek druhé Activity. Musí se zaregistrovat už při vzniku
    // Activity, ne až po kliknutí na tlačítko, proto je to vlastnost třídy.
    private val editNameLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        // Po Zrušit nebo tlačítku Zpět je resultCode RESULT_CANCELED a nic se nemění.
        val newName = result.data?.getStringExtra(EditNameActivity.EXTRA_NEW_NAME)
        if (result.resultCode == RESULT_OK && newName != null) {
            saveName(newName)
            showProfile()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 2. Soubor "profile" vidí jen tato aplikace (MODE_PRIVATE).
        prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE)
        showProfile()

        binding.editNameButton.setOnClickListener {
            // 3. Explicitní Intent: přesně říkáme, kterou třídu spustit. Extras jsou dvojice klíč–hodnota.
            val editIntent = Intent(this, EditNameActivity::class.java)
                .putExtra(EditNameActivity.EXTRA_CURRENT_NAME, loadName())
                .putExtra(EditNameActivity.EXTRA_CHANGES, loadChanges())
            editNameLauncher.launch(editIntent)
        }

        binding.resetButton.setOnClickListener {
            // 4. Smazání všech uložených hodnot; při dalším načtení se použijí výchozí.
            prefs.edit().clear().apply()
            showProfile()
        }
    }

    // Druhý parametr get… metod je hodnota pro případ, že klíč ještě nebyl uložen.
    private fun loadName(): String =
        prefs.getString(KEY_NAME, null) ?: getString(R.string.default_name)

    private fun loadChanges(): Int = prefs.getInt(KEY_CHANGES, 0)

    // Uložení i změna hodnoty jsou stejná operace: put s již existujícím klíčem starou hodnotu přepíše.
    // apply() zapíše změny na pozadí, commit() by na zápis čekal.
    private fun saveName(newName: String) {
        prefs.edit()
            .putString(KEY_NAME, newName)
            .putInt(KEY_CHANGES, loadChanges() + 1)
            .apply()
    }

    private fun showProfile() {
        binding.greetingText.text = getString(R.string.greeting_format, loadName())
        binding.changesText.text = getString(R.string.changes_format, loadChanges())
    }

    companion object {
        private const val PREFS_NAME = "profile"
        private const val KEY_NAME = "name"
        private const val KEY_CHANGES = "changes"
    }
}
