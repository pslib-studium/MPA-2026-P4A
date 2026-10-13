package cz.pslib.jetpack.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

// Typografie pojmenovává styly textu. V UI pak stačí MaterialTheme.typography.bodyLarge.
// Upravujeme jen bodyLarge; zbývající styly (např. headlineMedium) poskytuje Material 3.
val Typography = Typography(
    bodyLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp, // sp na rozdíl od dp zohledňuje také velikost písma v nastavení systému.
        lineHeight = 24.sp,
        letterSpacing = 0.5.sp
    )
)
