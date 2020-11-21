package calculator

fun main() {
    val calc = Calculator()
    val calcUtil = CalcUtil()
    println("Calculator with ops: + - * / ^ ()")
    println("Write your expression:")
    var input = readLine()!!.trim()
    while (input != "/exit") {
        when {
            input.matches("/.+") -> {
                println("Unknown command")
            }
            calcUtil.isAssignment(input) -> {
                calc.save(input)
            }
            calcUtil.isExpression(input) -> {
                calc.calculate(input)
            }
            calcUtil.isWord(input) -> {
                calc.printVariable(input)
            }
            input.isNotEmpty() -> {
                println("Invalid expression")
            }
        }
        input = readLine()!!.trim()
    }
    println("Bye!")
}
