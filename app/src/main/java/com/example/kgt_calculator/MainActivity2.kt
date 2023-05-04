package com.example.kgt_calculator

import android.graphics.Path.Op
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import android.widget.Toast
import com.example.kgt_calculator.databinding.ActivityMain2Binding
import java.math.BigDecimal

class MainActivity2 : AppCompatActivity() {
    // 전역변수(binding)
    private var nullbinding: ActivityMain2Binding? = null
    private val binding get() = nullbinding!!

    // 결과값 저장
    private var save: String = ""

    // 입력잠금 역할 & 체크
    private var OpCheck: Boolean = false
    private var numCheck: Boolean = false


    // 작성 뷰
    private val InPuttextview: TextView by lazy { binding.text1 }


    // 결과 뷰
    private val OutPuttextview: TextView by lazy { binding.text2 }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        nullbinding = ActivityMain2Binding.inflate(layoutInflater)
        setContentView(binding.root)

    }
    // 버튼
    fun BtnClick(v: View) {
        when (v.id) {
            // 숫자
            binding.btn0.id -> numberClicked("0")
            binding.btn1.id -> numberClicked("1")
            binding.btn2.id -> numberClicked("2")
            binding.btn3.id -> numberClicked("3")
            binding.btn4.id -> numberClicked("4")
            binding.btn5.id -> numberClicked("5")
            binding.btn6.id -> numberClicked("6")
            binding.btn7.id -> numberClicked("7")
            binding.btn8.id -> numberClicked("8")
            binding.btn9.id -> numberClicked("9")
//=================================================================================================
            // 연산자
            binding.btnShare.id -> OpButtonClick("%")
            binding.btnPlus.id -> OpButtonClick("+")
            binding.btnMinus.id -> OpButtonClick("-")
            binding.btnMult.id -> OpButtonClick("X")
            binding.btnDivis.id -> OpButtonClick("/")
            binding.btnSquare.id -> squareBtn("^")
        }
    }


    // 버튼 입력 값 함수
    private fun numberClicked(number: String) {
        if (numCheck) {
            InPuttextview.append(" ")
        }
        numCheck = false
        val InputText = InPuttextview.text.split(" ")
        if (InputText.isNotEmpty() && InputText.last().length >= 15) {
            Toast.makeText(this, "15자리 까지만 사용할수 있습니다.", Toast.LENGTH_SHORT).show()
            return
        } else if (InputText.last().isEmpty() && number == "0") {
            Toast.makeText(this, "현재 식에서 0은 입력되지 않습니다.", Toast.LENGTH_SHORT).show()
            return
        }
        InPuttextview.append(number)

        // 실시간 결과 값
        OutPuttextview.text = ion()
        save = OutPuttextview.text.toString()
    }

    // 연산자 입력 함수
    private fun OpButtonClick(operator: String) {
        if (InPuttextview.text.isEmpty()) {

            return
        }
        when {
            numCheck -> {
                val text = InPuttextview.text.toString()
                InPuttextview.text = text.dropLast(1) + operator
            }
            // 연산자 연속 입력 예외 처리 & 결과 값 누적(결과 값으로 연속 계산)
            OpCheck -> {
                if (save != null || InPuttextview.text == save) {
                    InPuttextview.text = ""
                    InPuttextview.append("$save")
                    InPuttextview.append(" ")
                    InPuttextview.append("$operator")
                    InPuttextview.append(" ")
                    OutPuttextview.text = ""
                    numCheck = false

                } else{
                    Toast.makeText(this, "연속적으로 연산자 사용은 안됩니다.", Toast.LENGTH_SHORT).show()
                return}
            }

            else -> {
                InPuttextview.append(" $operator")
            }
        }
        numCheck = false
        OpCheck = true
    }

    // 계산하는 함수
    private fun ion(): String {
        if (InPuttextview.text.isEmpty()) {
            return ""
        }
        val InputText = InPuttextview.text.split(" ")
        if (OpCheck.not() || InputText.size != 3) {
            return ""
        } else if (InputText[0].Num().not() || InputText[2].Num().not()) {
            return ""
        }
        var num1: BigDecimal
        var num2: BigDecimal
        if (save == InputText[0] || InputText.size == 6) {
            num1 = save.toBigDecimal()
            num2 = InputText[2].toBigDecimal()
        }
         else {
            num1 = InputText[0].toBigDecimal()
            num2 = InputText[2].toBigDecimal()
        }
        val op = InputText[1]
        return when (op) {
            "+" -> (num1 + num2).toString()
            "-" -> (num1 - num2).toString()
            "X" -> (num1 * num2).toString()
            "%" -> (num1 * num2 * 0.01.toBigDecimal()).toString()
            "/" -> (num1.toDouble() / num2.toDouble()).toString()
            else -> ""
        }
    }
    // 최종 합계 결과 값 출력
    fun sumBntClick(v: View) {
        val InputTexts = InPuttextview.text.split(" ")
        if (InPuttextview.text.isEmpty() || InputTexts.size == 1) {
            Toast.makeText(this, "계산이 정확하지 않습니다.", Toast.LENGTH_SHORT).show()
            return
        }
        if (InputTexts.size != 3 && OpCheck && InPuttextview.equals("^").not()) {
            Toast.makeText(this, "수식을 완성해주세요", Toast.LENGTH_SHORT).show()
            return
        }
        if (InputTexts[0].Num().not() || InputTexts[2].Num().not()) {
            Toast.makeText(this, "오류가 발생했습니다.", Toast.LENGTH_SHORT).show()
            return
        }
        val resultText = ion()
        OutPuttextview.text = resultText
        InPuttextview.text = ""
        save = resultText
        !numCheck
        !OpCheck
        if (OpCheck == false || OutPuttextview.text == save){
            InPuttextview.append("$save")
            OutPuttextview.text = ""
        }


    }
    // 입력값, 결과값 전체 삭제
    fun clearButtonClicked(v: View) {
        InPuttextview.text = ""
        OutPuttextview.text = ""
        save = null.toString()
        numCheck = false
        OpCheck = false
    }
    fun squareBtn(v: String){
        if (InPuttextview.text.isEmpty()) {
            return
        }
        val InputText = InPuttextview.text.toString()
        InPuttextview.text = InputText.toBigDecimal().pow(2).toString()
    }
    fun DeleteBtn(v:View){
        if (InPuttextview.text.isEmpty()) {
            return
        }
        val text = InPuttextview.text.toString()
//       if (InPuttextview){
//            Toast.makeText(this, "연산자는", Toast.LENGTH_SHORT).show()
//            return
//        }
        InPuttextview.text = text.dropLast(1)
        save = ion()
        OutPuttextview.text = save
        !numCheck
        OpCheck = true
        if(OpCheck){
            !numCheck
        }
    }
}
// 계산 확인
fun String.Num(): Boolean {
    return try {
        this.toBigInteger()
        true
    } catch (e: NumberFormatException) {
        false
    }
}

