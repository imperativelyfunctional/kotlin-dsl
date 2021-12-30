package me.imperativelyfunctional

import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import java.util.*

object 帳單 {
    private val calendar = Calendar.getInstance()
    private val items = mutableListOf<Triple<String, Int, Int>>()

    infix fun Int.杯(drink: String) = drink to this

    infix fun Int.場(movie: String) = movie to this

    infix fun Int.頓(movie: String) = movie to this

    infix fun Pair<String, Int>.單價(amount: Int) {
        items.add(Triple(first, second, amount))
    }

    val Int.元
        get() = this

    infix fun 於(calendar: Calendar) = apply {
        this@帳單.calendar.apply {
            this.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DATE))
        }
    }

    operator fun invoke(init: 帳單.() -> Unit) = apply {
        init()
    }

    fun 共計() {
        println(
            DateTimeFormatter.ofLocalizedDate(FormatStyle.FULL).withLocale(Locale.CHINESE)
                .withZone(ZoneId.systemDefault())
                .format(calendar.toInstant())
        )
        items.also {
            it.sortBy { triple -> triple.second }
            it.forEach { triple ->
                println("(${triple.second}) ${triple.first} : 單價 \$${triple.third}")
            }
        }

        println("合計 : \$${items.sumOf { it.third * it.second }}")
    }
}

infix fun Int.年(month: Int) = Calendar.getInstance().apply {
    set(this@年, month, 1)
}

infix fun Calendar.月(date: Int) = Calendar.getInstance().apply {
    val calendar = this@月
    set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), date)
}

infix fun Calendar.日(hour: Int) = Calendar.getInstance().apply {
    val calendar = this@日
    set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DATE), hour, 0)
}

infix fun Calendar.點(minute: Int) = Calendar.getInstance().apply {
    val calendar = this@點
    set(
        calendar.get(Calendar.YEAR),
        calendar.get(Calendar.MONTH),
        calendar.get(Calendar.DATE),
        calendar.get(Calendar.HOUR),
        minute
    )
}

val 咖啡 = "美味咖啡"
val 午餐 = "營養套餐"
val 電影 = "蜘蛛俠"

fun main() {
    (帳單 於 (2022 年 11 月 12 日 11 點 58)) {
        3 杯 咖啡 單價 2.元
        1 場 電影 單價 20.元
        1 頓 午餐 單價 10.元
    }.共計()
}