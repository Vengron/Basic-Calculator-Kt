package calculator

import java.util.*
import java.util.regex.Pattern

class CalcUtil {

    fun checkParentheses(infix: ArrayList<String>): Boolean {
        val stack = ArrayDeque<String>()
        for (s in infix) {
            if (s == "(") {
                stack.push("(")
            } else if (s == ")") {
                try {
                    stack.pop()
                } catch (e: Exception) {
                    return false
                }
            }
        }
        return stack.isEmpty()
    }

    /**
     * Splits input to elements based on their function in expression
     * (numbers, operators, variables, parentheses)
     *
     * @param input expression inserted by user
     * @return separated elements of expression
     */

    fun split(input: String): ArrayList<String> {
        val chars = input.toCharArray()
        val infix = ArrayList<String>()
        val sb = StringBuilder()
        var i = 0
        if (chars[i] == '-' || chars[i] == '+') {
            infix.add("0")
        }
        while (i < chars.size) {
            when {
                chars[i].toString().matches("\\w") -> {
                    sb.append(chars[i])
                    i++
                    while (i < chars.size && chars[i].toString().matches("\\w")) {
                        sb.append(chars[i])
                        i++
                    }
                    infix.add(sb.toString())
                    sb.setLength(0)
                }
                chars[i].toString().matches("\\d") -> {
                    sb.append(chars[i])
                    i++
                    while (i < chars.size && chars[i].toString().matches("\\d")) {
                        sb.append(chars[i])
                        i++
                    }
                    infix.add(sb.toString())
                    sb.setLength(0)
                }
                chars[i] == '+' -> {
                    i++
                    // for '+' and '-' is not necessary to check the length because these signs shouldn't be at the end
                    while (chars[i] == '+') {
                        i++
                    }
                    infix.add("+")
                }
                chars[i] == '-' -> {
                    sb.append(chars[i])
                    i++
                    while (chars[i] == '-') {
                        sb.append(chars[i])
                        i++
                    }
                    if (sb.length % 2 == 0) {
                        infix.add("+")
                    } else {
                        infix.add("-")
                    }
                    sb.setLength(0)
                }
                !chars[i].toString().matches("\\s+") -> {
                    infix.add(chars[i].toString())
                    i++
                }
                else -> {
                    i++
                }
            }
        }
        return infix
    }

    /**
     * Converts expression in infix form to Polish notation
     * @param expression ArrayList with already separated elements of expression
     * @return ArrayList of elements in postfix notation order
     */

    fun convertToPostfix(expression: ArrayList<String>): ArrayList<String> {
        val stack: Deque<String> = ArrayDeque()
        val result = ArrayList<String>()
        for (symbol in expression) {
            if (isWord(symbol) || isNumber(symbol)) {
                result.add(symbol)
            } else {
                if (stack.isEmpty()) {
                    stack.push(symbol)
                } else if (stack.peek() == "(" || symbol == "(") {
                    stack.push(symbol)
                } else if (symbol == ")") {
                    while (!stack.isEmpty() && stack.peek() != "(") {
                        result.add(stack.pop())
                    }
                    stack.pop()
                } else {
                    when (symbol) {
                        "^" -> stack.push(symbol)
                        "*", "/" -> {
                            while (!stack.isEmpty() && !stack.peek().matches("[(+-]")) {
                                result.add(stack.pop())
                            }
                            stack.push(symbol)
                        }
                        else -> {
                            while (!stack.isEmpty() && stack.peek() != "(") {
                                result.add(stack.pop())
                            }
                            stack.push(symbol)
                        }
                    }
                }
            }
        }
        while (!stack.isEmpty()) {
            result.add(stack.pop())
        }
        return result
    }


    fun isWord(input: String): Boolean {
        return input.matches("[a-zA-Z]+")
    }


    fun isNumber(input: String): Boolean {
        return input.matches("\\d+")
    }


    fun isAssignment(input: String): Boolean {
        return input.matches(".*=.*")
    }


    fun isExpression(input: String): Boolean {
        val numOrVar = "(\\w+|[+-]*\\d+)"
        val operator = "([*/^]|[+-]+)"
        val brackets = "[ ()]*"
        return input.matches(String.format("%s%s%s(%s%s%s%s)*",
                brackets, numOrVar, brackets, operator, brackets, numOrVar, brackets))
    }
}

fun String.matches(regex: String): Boolean {
    val pattern = Pattern.compile(regex)
    val matcher = pattern.matcher(this)
    return matcher.matches()
}