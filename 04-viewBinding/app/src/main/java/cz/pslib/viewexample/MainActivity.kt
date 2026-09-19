package cz.pslib.viewexample

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import cz.pslib.viewexample.databinding.ActivityMainBinding
import cz.pslib.viewexample.databinding.ItemGreetingBinding

class MainActivity : AppCompatActivity() {

    // 1. Třída ActivityMainBinding se automaticky vygeneruje z activity_main.xml.
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 2. inflate vytvoří všechny Views z XML a root je kořenový LinearLayout.
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 3. K Views s ID přistupujeme přímo přes binding, bez findViewById.
        binding.addButton.setOnClickListener {
            val name = binding.nameInput.text.toString().trim().ifEmpty {
                getString(R.string.default_name)
            }

            // 4. Druhý layout item_greeting.xml má vlastní ItemGreetingBinding.
            val itemBinding = ItemGreetingBinding.inflate(
                layoutInflater,
                binding.greetingContainer,
                false
            )
            itemBinding.greetingText.text = getString(R.string.greeting_format, name)

            // 5. greetingContainer je ViewGroup, proto do něj můžeme přidat další View.
            binding.greetingContainer.addView(itemBinding.root)
            binding.nameInput.text.clear()
        }
    }
}
