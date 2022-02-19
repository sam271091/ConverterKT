package Apps.com.converterkt.utils

import Apps.com.converterkt.pojo.Valute

fun getValuteFlagPath(valute:Valute?):String?{
    return "https://flagcdn.com/h80/${getFlagName(valute)}.png"
}

private fun getFlagName(valute:Valute?):String?{
    return valute?.code?.substring(0,2)?.lowercase()
}

fun dpToPx(dp: Int,density:Float): Int {
    val density: Float = density
    return Math.round(dp.toFloat() * density)
}