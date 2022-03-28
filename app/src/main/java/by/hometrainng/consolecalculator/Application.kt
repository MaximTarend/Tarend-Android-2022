package by.hometrainng.consolecalculator

import by.hometrainng.consolecalculator.service.CalcService
import by.hometrainng.consolecalculator.utils.Patterns

class Application {

    private val calcService: CalcService = CalcService()

    fun run() {
        println("App start (type \"end\" to finish)")
        while (true) {
            val line : String? = readLine()
            if (line == Patterns.END) {
                println("App finished")
                break
            } else {
                val result = line?.let { calcService.calc(it) } // notNull добавить
                println(result)
            }
        }
    }
}