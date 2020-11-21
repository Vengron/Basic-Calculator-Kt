package calculator

import java.math.BigInteger
import java.util.*
import kotlin.collections.ArrayList


class Calculator {
    private val savedVariables = mutableMapOf<String, String>()
    private val calcUtil = CalcUtil()

    fun printVariable(input: String) {
        println(savedVariables.getOrDefault(input, "Unknown variable"))
    }

    fun save(input: String) {
        val split = input.split("=").toTypedArray()
        split[0] = split[0].trim()
        split[1] = split[1].trim()
        if (!calcUtil.isWord(split[0])) {
            println("Invalid identifier")
        } else if (!calcUtil.isNumber(split[1]) && !calcUtil.isWord(split[1]) || split.size != 2) {
            println("Invalid assignment")
        } else if (calcUtil.isNumber(split[1])) {
            savedVariables[split[0]] = determineValue(split[1])
        } else if (calcUtil.isWord(split[1])) {
            savedVariables[split[0]] = determineValue(split[1])
        } else {
            println("Unknown variable")
        }
    }

    fun calculate(input: String) {
        val infix = calcUtil.split(input)
        if (!calcUtil.checkParentheses(infix)) {
            println("Invalid expression")
            return
        }
        val postfix = calcUtil.convertToPostfix(infix)
        if (!areAllVariablesKnown(postfix)) {
            println("Unknown variable")
            return
        }
        println(calcBigInteger(postfix))
    }

    private fun calcBigInteger(postfix: ArrayList<String>): BigInteger {
        val stack = ArrayDeque<BigInteger>()
        for (s in postfix) {
            when (s) {
                "-" -> stack.push(stack.pop().negate().add(stack.pop()))
                "+" -> stack.push(stack.pop().add(stack.pop()))
                "*" -> stack.push(stack.pop().multiply(stack.pop()))
                "/" -> {
                    val divisor = stack.pop()
                    val dividend = stack.pop()
                    stack.push(dividend.divide(divisor))
                }
                "^" -> {
                    val exponent = stack.pop().toString().toInt()
                    val number = stack.pop()
                    stack.push(number.pow(exponent))
                }
                else -> stack.push(BigInteger(determineValue(s)))
            }
        }
        return stack.pop()
    }

    private fun determineValue(value: String): String {
        return if (calcUtil.isWord(value)) {
            savedVariables[value]!!
        } else value
    }

    private fun areAllVariablesKnown(expression: ArrayList<String>): Boolean {
        for (variable in expression) {
            if (calcUtil.isWord(variable) && !savedVariables.containsKey(variable)) {
                return false
            }
        }
        return true
    }

}