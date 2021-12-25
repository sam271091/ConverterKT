package demo.com.converterkt.utils

import demo.com.converterkt.pojo.Valute

fun getValuteFlagPath(valute:Valute?):String?{
    return "https://flagcdn.com/h80/${getFlagName(valute)}.png"
}

private fun getFlagName(valute:Valute?):String?{
    return valute?.code?.substring(0,2)?.lowercase()
}