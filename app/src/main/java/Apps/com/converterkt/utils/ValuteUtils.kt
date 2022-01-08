package Apps.com.converterkt.utils

import Apps.com.converterkt.pojo.Valute

fun getAZN() : Valute{
    return Valute(code = "AZN", name = "Azerbaijan manat", nominal = "1", value = 1.0)
}




fun getValutePresentation() : HashMap<String,String>{

    var presentation = HashMap<String,String>()
    presentation.put("BYN","Belarusian ruble");
    presentation.put("AZN","Azerbaijani manat");
    presentation.put("EUR","Euro");
    presentation.put("CAD","Canadian dollar");
    presentation.put("KGS","Kyrgyz Som");
    presentation.put("ARS","Argentine Peso");
    presentation.put("BRL","Brazilian real");
    presentation.put("NZD","New Zealand Dollar");
    presentation.put("AUD","Australian dollar");
    presentation.put("SEK","Swedish Krona");

    presentation.put("LBP","Lebanese pound");
    presentation.put("EGP","Egyptian pound");
    presentation.put("JPY","Japanese yen");
    presentation.put("KZT","Kazakhstan tenge");
    presentation.put("HKD","Hong Kong Dollar");
    presentation.put("GBP","British pound sterling");
    presentation.put("CZK","Czech Koruna");
    presentation.put("MYR","Malaysian Ringgit");
    presentation.put("DKK","Danish krone");
    presentation.put("GEL","Georgian lari");

    presentation.put("INR","Indian rupees");
    presentation.put("TWD","Taiwanese dollar");
    presentation.put("AED","UAE Dirham");
    presentation.put("PLN","Polish zloty");
    presentation.put("ZAR","South african rand");
    presentation.put("CNY","Chinese yuan");
    presentation.put("CHF","Swiss frank");
    presentation.put("TRY","Turkish lira");
    presentation.put("BYR","Belarusian ruble");
    presentation.put("UAH","Ukrainian hryvnia");

    presentation.put("ILS","Israeli Shekel");
    presentation.put("KWD","Kuwaiti dinar");
    presentation.put("NOK","Norwegian Krone");
    presentation.put("TJS","Tajik somoni");
    presentation.put("SAR","Saudi riyal");
    presentation.put("RUB","Russian ruble");
    presentation.put("TMT","Turkmen manat");
    presentation.put("IRR","Iranian Rial");
    presentation.put("SGD","Singapore Dollar");
    presentation.put("MDL","Moldovan leu");


    presentation.put("UZS","Uzbek Sum");
    presentation.put("KRW","South Korean Won");
    presentation.put("CLP","Chilean Peso");
    presentation.put("USD","U.S. dollar");
    presentation.put("IDR","Indonesian Rupee");
    presentation.put("MXN","Mexican peso");
    presentation.put("SDR","DR (Special IMF Drawing Rights)");

    return presentation
}