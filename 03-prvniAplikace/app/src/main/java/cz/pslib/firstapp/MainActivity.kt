package cz.pslib.firstapp

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        // ID tlacitko je v activity_main.xml přiřazeno tlačítku pomocí android:id="@+id/tlacitko".
        findViewById<Button>(R.id.tlacitko).setOnClickListener {
            // ID textview je v activity_main.xml přiřazeno textovému poli pomocí android:id="@+id/textview".
            findViewById<TextView>(R.id.textview).text = "Ahoj světe!"
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
}
