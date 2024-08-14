package de.devbeyer.podcast_sponsorskipper.ui.theme

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb


val StrongGray = Color(162, 162, 162, 255)

val Pink40 = Color(0xFF7D5260)

val StrongBlue = Color(0xff0070c6)
val StrongBlueDark = Color(0xff005da3)


val StrongRed = Color(0xFFEC3C2F)
val StrongRedDark = Color(0xFFFF1200)


val StrongOrange = Color(255, 165, 0)
val StrongOrangeDark = Color(189, 128, 19, 255)

val colorArray = arrayOf(
    Color(255, 0, 0),
    Color(255, 165, 0),
    Color(0, 255, 0),
    Color(0, 0, 255),
    Color(75, 0, 130),
    Color(148, 0, 211),
)

fun shiftHue(color: Color, amount: Float): Color {
    val hsv = FloatArray(3)
    android.graphics.Color.colorToHSV(color.toArgb(), hsv)
    hsv[0] = (hsv[0] + amount) % 360
    return Color.hsv(hsv[0], hsv[1], hsv[2], 1f)
}

fun shiftValue(color: Color, amount: Float): Color {
    val hsv = FloatArray(3)
    android.graphics.Color.colorToHSV(color.toArgb(), hsv)
    hsv[2] = (hsv[2] + amount) % 1f
    return Color.hsv(hsv[0], hsv[1], hsv[2], 1f)
}