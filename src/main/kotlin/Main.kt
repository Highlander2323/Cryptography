package com.cybersecurity

import java.io.File
import java.io.FileNotFoundException
import kotlin.random.Random

object FileController {
    private const val INPUT_DIRECTORY = "E:\\Master\\Anul 2" +
            " Sem 1\\Cybersecurity\\Project 1\\Cryptography1\\src\\main\\input_files"
    private const val OUTPUT_DIRECTORY = "E:\\Master\\Anul 2" +
            " Sem 1\\Cybersecurity\\Project 1\\Cryptography1\\src\\main\\output_files"

    fun readFile(fileName: String): String {
        return File("$INPUT_DIRECTORY\\$fileName").readText()
    }

    fun writeFile(fileName: String, text: String) {
        File("$OUTPUT_DIRECTORY\\$fileName").writeText(text)
    }

    fun readFileLines(fileName: String): List<String> {
        return File("$INPUT_DIRECTORY\\$fileName").readLines()
    }

    fun writeFileLines(fileName: String, text: List<String>) {
        File("$OUTPUT_DIRECTORY\\$fileName").writeText(text.joinToString(separator = "\n"))
    }
}

object Cryptography {
    // if encryptMode is false, decrypt the text using this algorithm
    fun caesar(text: List<String>, key: Int = 1, encryptMode: Boolean = true): List<String> {
        try {
            if (key > 25 || key < 1) {
                throw NumberFormatException()
            }
            val encryptedList: MutableList<String> = mutableListOf()
            //make all characters uppercase for ease of use in the algorithm
            for (line in text) {
                val normalizedText = line.uppercase()
                val encrypted = StringBuilder()
                for (i in normalizedText.indices) {
                    if (normalizedText[i] in 'A'..'Z') {
                        var c = normalizedText[i] - 'A'
                        if (encryptMode) {
                            c += key
                            c %= 26
                        } else {
                            c -= key
                            if (c < 0) {
                                c += 26
                            }
                        }
                        encrypted.append('A' + c)
                    } else {
                        encrypted.append(normalizedText[i])
                    }
                }
                encryptedList.add(encrypted.toString())
            }
            return encryptedList
        } catch (e: NumberFormatException) {
            println("For the caesar algorithm, step must be a number between 1 and 25")
            return listOf("")
        }
    }

    fun betterCaesar(text: List<String>, key: List<Int>, encryptMode: Boolean = true): List<String> {
        try {
            for (k in key) {
                if (k > 25 || k < 1) {
                    throw NumberFormatException()
                }
            }
            val encryptedList: MutableList<String> = mutableListOf()
            //make all characters uppercase for ease of use in the algorithm
            for (line in text) {
                val normalizedText = line.uppercase()
                val encrypted = StringBuilder()
                var it = 0
                for (i in 0..normalizedText.length - 1) {
                    if (normalizedText[i] in 'A'..'Z') {
                        var c = normalizedText[i] - 'A'
                        if (encryptMode) {
                            c += key[it]
                            c %= 26
                        } else {
                            c -= key[it]
                            if (c < 0) {
                                c += 26
                            }
                        }
                        encrypted.append('A' + c)
                    } else {
                        encrypted.append(normalizedText[i])
                    }
                    it = if (it + 1 == key.size) 0 else it + 1
                }
                encryptedList.add(encrypted.toString())
            }
            return encryptedList
        } catch (e: NumberFormatException) {
            println("For the caesar algorithm, step must be a number between 1 and 25")
            return listOf("")
        }
    }

    fun vigenere(text: List<String>, key: String, encryptMode: Boolean = true): List<String> {
        val encryptedList: MutableList<String> = mutableListOf()
        //make all characters uppercase for ease of use in the algorithm
        for (line in text) {
            val normalizedText = line.uppercase()
            val encrypted = StringBuilder()
            var it = 0
            for (i in 0..normalizedText.length - 1) {
                if (normalizedText[i] in 'A'..'Z') {
                    var c = normalizedText[i] - 'A'
                    if (encryptMode) {
                        c += (key[it] - 'A')
                        c %= 26
                    } else {
                        c -= (key[it] - 'A')
                        if (c < 0) {
                            c += 26
                        }
                    }
                    encrypted.append('A' + c)
                } else {
                    encrypted.append(normalizedText[i])
                }
                it = if (it + 1 == key.length) 0 else it + 1
            }
            encryptedList.add(encrypted.toString())
        }
        return encryptedList
    }

    //we don't care about uppercase necessarily, we leave the option for the user to obfuscate his text as much as
    //he wants
    fun transBox(text: String, box: Map<Int,Int>, encryptionMode:Boolean = true): String{
        val newText = CharArray(box.size)
        var it = 1
        while(it <= box.size){
            if(encryptionMode) {
                newText[box[it]!!-1] = text[it - 1]
            }
            else{
                newText[it-1] = text[box[it]!!-1]
            }
            it++
        }
        val transposed = StringBuilder()
        for(element in newText){
            transposed.append(element)
        }
        return transposed.toString()
    }
}



object Examples {

    // method for generating a transposition box
    private fun generateBox(seed: String ,length: Int): Map<Int, Int>{
        val box: MutableMap<Int, Int> = mutableMapOf()
        var sum = 0
        for(c in seed) sum += c.code
        //randomizer with seed, needed for algorithm
        val randomGenerator = Random(sum)
        val numsLeft: MutableList<Int> = mutableListOf()
        //we need a list with every number that is left. Numbers denote positions
        for(num in 1..length){
            numsLeft.add(num)
        }
        var it = 1
        while(numsLeft.isNotEmpty()){
            //here we build the box.
            //e.g. if randomizer chooses number 11 in the first iteration, the first letter in the original text
            //will be on the 11th position in the transposed text
            val num = randomGenerator.nextInt(0, numsLeft.size)
            box[it] = numsLeft[num]
            numsLeft.removeAt(num)
            it++
        }
        return box
    }

    // simple caesar algorithm
    fun run_caesar(mode: Boolean) {
        var text = FileController.readFileLines("caesar.txt")
        text = Cryptography.caesar(text, 23, mode)
        FileController.writeFileLines("caesar_output.txt", text)
    }

    // better caesar; each letter has another shift step
    fun run_better_caesar(mode: Boolean) {
        val fileContent = FileController.readFileLines("better_caesar.txt")
        val keyString = fileContent[0].split(",")
        val key: MutableList<Int> = mutableListOf()
        for (k in keyString) {
            key.add(k.toInt())
        }
        val text: MutableList<String> = mutableListOf()
        for (it in 1..fileContent.size - 1) {
            text.add(fileContent[it])
        }
        val output: List<String> = Cryptography.betterCaesar(text, key, mode)
        FileController.writeFileLines("better_caesar_output.txt", output)
    }

    // Vigenere algorithm
    fun run_vigenere(mode: Boolean) {
        val fileContent = FileController.readFileLines("vigenere.txt")
        val key = fileContent[0]
        val text: MutableList<String> = mutableListOf()
        for (it in 1..fileContent.size - 1) {
            text.add(fileContent[it])
        }
        val output: List<String> = Cryptography.vigenere(text, key, mode)
        FileController.writeFileLines("vigenere_output.txt", output)
    }

    fun run_trans_box(mode: Boolean) {
        println("Enter text:")
        val text = readln()
        println("Enter key (randomization seed):")
        val seed = readln()
        val key = generateBox(seed, text.length)
        val output: String = Cryptography.transBox(text, key, mode)
        println(output)
    }
}

enum class AlgorithmOption{QUIT, CAESAR, BETTER_CAESAR, VIGENERE, TRANS_BOX}

fun printMenu(): AlgorithmOption?{
    println("Choose the encryption algorithm:")
    println("1. Caesar")
    println("2. Better Caesar")
    println("3. Vigenere")
    println("4. Transposition Box")
    println("0. Exit")
    val option = readln()
    return when(option){
        "1" -> AlgorithmOption.CAESAR
        "2" -> AlgorithmOption.BETTER_CAESAR
        "3" -> AlgorithmOption.VIGENERE
        "4" -> AlgorithmOption.TRANS_BOX
        "0" -> AlgorithmOption.QUIT
        else -> null
    }
}

// Menu for choice of encryption/decryption
fun printChoice(): Int{
    println("Choose option:")
    println("1. Encrypt")
    println("2. Decrypt")
    println("Any. Back")
    val option = readln()
    return when(option){
        "1" -> 1
        "2" -> 2
        else -> 0
    }
}

fun main() {
        while(true) {
            try {
                val algorithm = printMenu()
                if(algorithm != null) {
                    if (algorithm == AlgorithmOption.QUIT)
                        break
                    val choice = printChoice()
                    val mode: Boolean? = when (choice) {
                        1 -> true
                        2 -> false
                        else -> null
                    }
                    if (mode != null) {
                        when (algorithm) {
                            AlgorithmOption.CAESAR -> Examples.run_caesar(mode)
                            AlgorithmOption.BETTER_CAESAR -> Examples.run_better_caesar(mode)
                            AlgorithmOption.VIGENERE -> Examples.run_vigenere(mode)
                            AlgorithmOption.TRANS_BOX -> Examples.run_trans_box(mode)
                            else -> continue
                        }
                    }
                }
            }
            catch (e: FileNotFoundException) {
                println("File not found.")
            }
    }
}