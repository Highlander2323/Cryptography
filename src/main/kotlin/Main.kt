package com.cybersecurity

import java.io.File
import java.io.FileNotFoundException

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
                for (i in 0..normalizedText.length - 1) {
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
            var encryptedList: MutableList<String> = mutableListOf()
            //make all characters uppercase for ease of use in the algorithm
            for (line in text) {
                val normalizedText = line.uppercase()
                var encrypted = StringBuilder()
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
            var encrypted = StringBuilder()
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
}

object Examples {
    fun run_caesar() {
        var text = FileController.readFileLines("caesar.txt")
        text = Cryptography.caesar(text, 23, true)
        FileController.writeFileLines("caesar_output.txt", text)
    }

    fun run_better_caesar() {
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
        val output: List<String> = Cryptography.betterCaesar(text, key, false)
        FileController.writeFileLines("better_caesar_output.txt", output)
    }

    fun run_vigenere() {
        val fileContent = FileController.readFileLines("vigenere.txt")
        val key = fileContent[0]
        val text: MutableList<String> = mutableListOf()
        for (it in 1..fileContent.size - 1) {
            text.add(fileContent[it])
        }
        val output: List<String> = Cryptography.vigenere(text, key, false)
        FileController.writeFileLines("vigenere_output.txt", output)
    }
}

fun main() {
    try {
        Examples.run_caesar()
        Examples.run_better_caesar()
        Examples.run_vigenere()
    } catch (e: FileNotFoundException) {
        println("File not found.")
    }
}