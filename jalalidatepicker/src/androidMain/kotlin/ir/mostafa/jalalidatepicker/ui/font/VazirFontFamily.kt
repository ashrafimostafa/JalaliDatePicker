package ir.mostafa.jalalidatepicker.ui.font

import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import ir.mostafa.jalalidatepicker.R

/**
 * Bundled [Vazirmatn](https://github.com/rastikerdar/vazirmatn) typefaces (OFL).
 *
 * - [Default] — standard glyphs
 * - [FarsiDigits] — UI Farsi-digit variant (۰–۹ shaped in the font)
 */
object VazirFontFamily {
    val Default: FontFamily = FontFamily(
        Font(R.font.vazirmatn_regular, FontWeight.Normal),
        Font(R.font.vazirmatn_medium, FontWeight.Medium),
        Font(R.font.vazirmatn_bold, FontWeight.Bold),
    )

    val FarsiDigits: FontFamily = FontFamily(
        Font(R.font.vazirmatn_fd_regular, FontWeight.Normal),
        Font(R.font.vazirmatn_fd_medium, FontWeight.Medium),
        Font(R.font.vazirmatn_fd_bold, FontWeight.Bold),
    )
}
