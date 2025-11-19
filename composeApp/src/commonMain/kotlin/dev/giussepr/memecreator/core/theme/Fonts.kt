package dev.giussepr.memecreator.core.theme

import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import memecreator.composeapp.generated.resources.Res
import memecreator.composeapp.generated.resources.impact
import memecreator.composeapp.generated.resources.manrope
import org.jetbrains.compose.resources.Font

object Fonts {
    val Manrope
        @Composable get() = FontFamily(
            Font(
                resource = Res.font.manrope,
                weight = FontWeight.Normal
            )
        )

    val Impact
        @Composable get() = FontFamily(
            Font(
                resource = Res.font.impact,
                weight = FontWeight.Normal
            )
        )
}

val Typography: Typography
    @Composable get() = Typography(
        displayLarge = TextStyle(
            fontFamily = Fonts.Manrope,
            fontWeight = FontWeight.Medium,
            fontSize = 24.sp,
            lineHeight = 28.sp
        ),
        displayMedium = TextStyle(
            fontFamily = Fonts.Manrope,
            fontWeight = FontWeight.SemiBold,
            fontSize = 22.sp,
        ),
        displaySmall = TextStyle(
            fontFamily = Fonts.Manrope,
            fontWeight = FontWeight.SemiBold,
            fontSize = 18.sp,
        ),
        bodyLarge = TextStyle(
            fontFamily = Fonts.Manrope,
            fontWeight = FontWeight.Normal,
            fontSize = 20.sp,
        ),
        bodyMedium = TextStyle(
            fontFamily = Fonts.Manrope,
            fontWeight = FontWeight.Normal,
            fontSize = 16.sp,
        ),
        bodySmall = TextStyle(
            fontFamily = Fonts.Manrope,
            fontWeight = FontWeight.Normal,
            fontSize = 12.sp,
        ),
    )
