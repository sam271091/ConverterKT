package Apps.com.converterkt.utils

import Apps.com.converterkt.pojo.ValuteInfo

fun getCalculatedResult(sumValue:String,firstValute:ValuteInfo?,secondValute:ValuteInfo?) : Double{


    var Res = 0.0

    if (!sumValue.equals("")){
        val sumDouble : Double = sumValue.toDouble()

        var valuteInfoForCalc: ValuteInfo? = null

        var crossCal = false
        if (firstValute?.valute?.code.equals(getAZN().code) ){
            valuteInfoForCalc = secondValute
        } else if (secondValute?.valute?.code.equals(getAZN().code)){
            valuteInfoForCalc = firstValute
        } else if (!firstValute?.valute?.code.equals(getAZN().code) &&
            !secondValute?.valute?.code.equals(getAZN().code)){
            valuteInfoForCalc = firstValute
            crossCal = true
        }


        valuteInfoForCalc.let {
            val value = it?.value
            val nominal = it?.nominal
            if (value != null && nominal != null ){
                if (crossCal){

                    val secvalue = secondValute?.value
                    val secnominal = secondValute?.nominal

                    if (secvalue != null && secnominal != null){
                        Res =  (sumDouble * value/nominal)  / secvalue*secnominal
                    } else {
                        Res = 0.0
                    }

                }
                else if (valuteInfoForCalc == firstValute ){
                    Res =  sumDouble * value/nominal
                } else if (valuteInfoForCalc == secondValute) {
                    Res =  sumDouble / value*nominal
                }

            }

        }
    }



    return Res
}