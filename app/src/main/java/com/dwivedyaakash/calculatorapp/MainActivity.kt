package com.dwivedyaakash.calculatorapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import android.widget.TextView
import com.dwivedyaakash.calculatorapp.databinding.ActivityMainBinding

private const val STATE_FIRST_NUMBER = "FirstNumber"
private const val STATE_RESULT = "Result"
private const val STATE_FIRST_NUMBER_STORED = "FirstNumberStored"

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    private var firstNumber: Double? = null
    private var secondNumber: Double? = null
    private var firstTotalNumber: String = ""
    private var secondTotalNumber: String = ""
    private var operationType: String? = null
    private var result: Double? = null
    private var decimalClicked: Boolean = false
    private var firstNumberAfterDecimal: String = "0."
    private var secondNumberAfterDecimal: String = "0."

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // calculated value reset
        binding.calculatedValue.text = ""

        // clear button
        binding.clearBtn.setOnClickListener {
            binding.calculatedValue.text = ""
            resetAllValues()
        }

        // numbers
        binding.number0.setOnClickListener { onNumberClicked(0) }
        binding.number1.setOnClickListener { onNumberClicked(1) }
        binding.number2.setOnClickListener { onNumberClicked(2) }
        binding.number3.setOnClickListener { onNumberClicked(3) }
        binding.number4.setOnClickListener { onNumberClicked(4) }
        binding.number5.setOnClickListener { onNumberClicked(5) }
        binding.number6.setOnClickListener { onNumberClicked(6) }
        binding.number7.setOnClickListener { onNumberClicked(7) }
        binding.number8.setOnClickListener { onNumberClicked(8) }
        binding.number9.setOnClickListener { onNumberClicked(9) }

        binding.decimal.setOnClickListener {
            decimalClicked = true

            if (firstNumber == null) firstNumber = 0.0
            if (secondNumber == null) secondNumber = 0.0

            if (operationType == null) {
                binding.calculatedValue.text = firstNumber.toString()
            } else {
                binding.calculatedValue.text = checkForDecimal(firstNumber) + operationType.toString() + secondNumber.toString()
            }
        }

        // store operation type
        binding.additionOperation.setOnClickListener {
            firstNumber?.let {
                decimalClicked = false
                operationType = "+"
                binding.calculatedValue.text = checkForDecimal(firstNumber) + operationType.toString()
            }
        }
        binding.negationOperation.setOnClickListener {
            firstNumber?.let {
                decimalClicked = false
                operationType = "-"
                binding.calculatedValue.text = checkForDecimal(firstNumber) + operationType.toString()
            }
        }
        binding.multiplicationOperation.setOnClickListener {
            firstNumber?.let {
                decimalClicked = false
                operationType = "*"
                binding.calculatedValue.text = checkForDecimal(firstNumber) + operationType.toString()
            }
        }
        binding.divisionOperation.setOnClickListener {
            firstNumber?.let {
                decimalClicked = false
                operationType = "/"
                binding.calculatedValue.text = checkForDecimal(firstNumber) + operationType.toString()
            }
        }

        // equal to operation (=)
        binding.equalToOperation.setOnClickListener {
            firstNumber?.let { firstNum ->
                secondNumber?.let { secondNum ->
                    // divide by 0 case handled
                    if (operationType == "/" && secondNumber?.toInt() == 0) {
                        binding.calculatedValue.text = getString(R.string.error_text_message)
                        resetAllValues()
                        return@setOnClickListener
                    }

                    // arithmetic operations
                    when (operationType) {
                        "+" -> result = firstNum + secondNum
                        "-" -> result = firstNum - secondNum
                        "*" -> result = firstNum * secondNum
                        "/" -> result = firstNum / secondNum
                    }

                    if (result.toString().contains(".0"))
                        binding.calculatedValue.text = checkForDecimal(result)
                    else
                        binding.calculatedValue.text = String.format("%.2f", result)

                    resetAllValues()
                    firstNumber = result
                }
            }
        }

    }

    // handling numbers clicked
    private fun onNumberClicked(num: Long) {
        if (operationType == null) {
            if (decimalClicked) {
                firstNumberAfterDecimal += num.toString()
                firstNumber = if (firstTotalNumber == "")
                    firstNumberAfterDecimal.toDouble()
                else
                    firstTotalNumber.toDouble() + firstNumberAfterDecimal.toDouble()
                binding.calculatedValue.text = firstNumber.toString()
                return
            }
            firstTotalNumber += num.toString()
            firstNumber = firstTotalNumber.toDouble()
            binding.calculatedValue.text = checkForDecimal(firstNumber)
        } else {
            if (decimalClicked) {
                secondNumberAfterDecimal += num.toString()
                secondNumber = if (secondTotalNumber == "")
                    secondNumberAfterDecimal.toDouble()
                else
                    secondTotalNumber.toDouble() + secondNumberAfterDecimal.toDouble()
                binding.calculatedValue.text = checkForDecimal(firstNumber) + operationType.toString() + checkForDecimal(secondNumber)
                return
            }
            secondTotalNumber += num.toString()
            secondNumber = secondTotalNumber.toDouble()
            binding.calculatedValue.text = checkForDecimal(firstNumber) + operationType.toString() + checkForDecimal(secondNumber)
        }
    }

    // reset all values
    private fun resetAllValues() {
        firstNumber = null
        secondNumber = null
        operationType = null
        firstTotalNumber = ""
        secondTotalNumber = ""
        decimalClicked = false
        firstNumberAfterDecimal = "0."
        secondNumberAfterDecimal = "0."
    }

    // remove decimal and zero if decimal is not pressed
    private fun checkForDecimal(num: Double?): String {
        if (num.toString().contains(".0"))
            return num.toString().replace(".0", "")
        return num.toString()
    }

//    override fun onSaveInstanceState(outState: Bundle) {
//        super.onSaveInstanceState(outState)
//        if (firstNumber != null) {
//            outState.putLong(STATE_FIRST_NUMBER, firstNumber!!)
//            outState.putBoolean(STATE_FIRST_NUMBER_STORED, true)
//        }
//        outState.putLong(STATE_RESULT, result!!)
//    }
//
//    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
//        super.onRestoreInstanceState(savedInstanceState)
//        if (savedInstanceState.getBoolean(STATE_FIRST_NUMBER_STORED))
//            firstNumber = savedInstanceState.getLong(STATE_FIRST_NUMBER)
//        result = savedInstanceState.getLong(STATE_RESULT)
//        binding.calculatedValue.text = result.toString()
//    }

}