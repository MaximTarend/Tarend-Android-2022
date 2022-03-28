package by.hometrainng.consolecalculator.service

import by.hometrainng.consolecalculator.utils.Patterns
import java.util.*
import java.util.regex.Pattern

class CalcService {
    private val priorityMap: Map<String, Int> = mapOf(
        "+" to 0,
        "-" to 0,
        "*" to 1,
        "/" to 1
    )

    fun calc(expression: String): String {
        //   5+7
        //   5+5/3+7*9
        //   (5+5)/3+7*9
        var result: String = expression.replace(Patterns.SPACES, "")
        if (result.contains(Patterns.BRACES)) {
            result = calcBracedExpression(result)
        }
        result = calcExpression(result)
        return result
    }

    private fun calcExpression(expression: String): String {
        val listOfOperands =
            expression.split("+", "-", "*", "/")    // 5 5 3 7 9 .. regex не сработал - выяснить
        val operands = listOfOperands.toMutableList()
        val operations = mutableListOf<String>() //  + / + *
        val matcher = Pattern.compile(Patterns.OPERATIONS).matcher(expression)
        while (matcher.find()) {
            operations.add(matcher.group())
        }
        while (operations.isNotEmpty()) {
            val index: Int = getIndex(operations)
            val firstOperand = operands.removeAt(index)
            val secondOperand = operands.removeAt(index)
            val operation = operations.removeAt(index)
            val result = calcOneOperation(firstOperand, secondOperand, operation)
            operands.add(index, result)
        }
        return operands[0]
    }

    private fun calcOneOperation(
        firstOperand: String,
        secondOperand: String,
        operation: String
    ): String {
        val first = firstOperand.toDouble()
        val second = secondOperand.toDouble()
        val result = when (operation) {
            "+" -> first + second
            "-" -> first - second
            "*" -> first * second
            "/" -> first / second
            else -> {
                "unknown operation"
            }
        }
        return String.format(Locale.ENGLISH, "%4.2f", result)
    }

    private fun calcBracedExpression(expressionWithBraces: String): String {
        var expression = expressionWithBraces
        while (expression.contains(Patterns.BRACES)) {
            var braceCounter = 0
            val openBraceIndex = expression.indexOf("(")
            var closeBraceIndex = 0
            for (i in openBraceIndex until expression.length) {
                if (expression[i] == '(') {
                    braceCounter++
                }
                if (expression[i] == ')') {
                    braceCounter--
                }
                if (braceCounter == 0) {
                    closeBraceIndex = i
                    break
                }
            }
            val bracedExpression = expression.substring(openBraceIndex + 1, closeBraceIndex)
            val tempExpression = if (bracedExpression.contains("(")) {
                calcBracedExpression(bracedExpression)
            } else {
                bracedExpression
            }
            expression = expression.replace("($tempExpression)", calcExpression(tempExpression))
        }
        return expression
    }

        private fun getIndex(operations: MutableList<String>): Int {
            var index = -1
            var priority = -1
            for (i in 0 until operations.size) {
                val operation = operations[i]
                if (priority < priorityMap.getValue(operation)) {
                    priority = priorityMap.getValue(operation)
                    index = i
                }
            }
            return index
        }
    }