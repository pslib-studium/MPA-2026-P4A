package cz.pslib.intent

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import cz.pslib.intent.databinding.ActivityEditNameBinding

class EditNameActivity : AppCompatActivity() {

    private lateinit var binding: ActivityEditNameBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditNameBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 1. Vlastnost intent je Intent, kterým nás spustila MainActivity. Druhý parametr
        // je výchozí hodnota pro případ, že extra v Intentu chybí.
        val currentName = intent.getStringExtra(EXTRA_CURRENT_NAME).orEmpty()
        val changes = intent.getIntExtra(EXTRA_CHANGES, 0)
        binding.currentNameText.text = getString(R.string.current_name_format, currentName)
        binding.changesText.text = getString(R.string.changes_format, changes)

        binding.saveButton.setOnClickListener {
            val newName = binding.newNameInput.text.toString().trim()
            if (newName.isEmpty()) {
                binding.newNameInput.error = getString(R.string.name_required)
                binding.newNameInput.requestFocus()
                return@setOnClickListener
            }

            // 2. Výsledek je nový Intent, který nese data zpět. Do SharedPreferences
            // tady nic nezapisujeme, uložení je věc MainActivity.
            setResult(RESULT_OK, Intent().putExtra(EXTRA_NEW_NAME, newName))
            // Výsledek se předá až ve chvíli, kdy se Activity zavře.
            finish()
        }

        // 3. Tlačítko Zpět v systému vrací RESULT_CANCELED automaticky, tady to jen děláme viditelně.
        binding.cancelButton.setOnClickListener {
            setResult(RESULT_CANCELED)
            finish()
        }
    }

    // Klíče jsou konstanty, aby se obě Activity nemohly rozejít kvůli překlepu v textu.
    companion object {
        const val EXTRA_CURRENT_NAME = "current_name"
        const val EXTRA_CHANGES = "changes"
        const val EXTRA_NEW_NAME = "new_name"
    }
}
