package Apps.com.converterkt.utils

import java.util.*


fun getCurrentTime() : Date{
   return Calendar.getInstance().time
}

fun getEndOfTheDay(date:Date) : Date {
   var calendar = Calendar.getInstance()
   calendar.setTime(date)
   calendar.set(Calendar.HOUR_OF_DAY, 23)
   calendar.set(Calendar.MINUTE, 59)
   calendar.set(Calendar.SECOND, 59)

   return calendar.time
}

fun getStartOfTheDay(date:Date) : Date {
   var calendar = Calendar.getInstance()
   calendar.setTime(date)
   calendar.set(Calendar.HOUR_OF_DAY, 0)
   calendar.set(Calendar.MINUTE, 0)
   calendar.set(Calendar.SECOND, 0)

   return calendar.time
}
