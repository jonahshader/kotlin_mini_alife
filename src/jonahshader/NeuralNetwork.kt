package jonahshader

import kotlin.math.tanh

class NeuralNetwork(inputs: Int, outputs: Int, hiddenLayer: Int, val activation: (Float)->Float) {
    val inputNeurons = ArrayList<Float>(inputs)
    val outputNeurons = ArrayList<Float>(outputs)
    private val hiddenNeurons = ArrayList<Float>(hiddenLayer)

    private val inputToHiddenLayer = ArrayList<ArrayList<Float>>()
    private val hiddenToOutputLayer = ArrayList<ArrayList<Float>>()

    init {
        for (i in 0 until inputs) inputNeurons.add(0f)
        for (i in 0 until outputs) outputNeurons.add(0f)
        for (i in 0 until hiddenLayer) hiddenNeurons.add(0f)

        for (i in 0 until (inputs + 1)) {
            inputToHiddenLayer.add(ArrayList())
            for (j in 0 until hiddenLayer) {
                inputToHiddenLayer[i].add(randomWeight())
            }
        }

        for (i in 0 until hiddenLayer + 1) {
            hiddenToOutputLayer.add(ArrayList())
            for (j in 0 until outputs) {
                hiddenToOutputLayer[i].add(randomWeight())
            }
        }
    }

    fun calculateOutputs() {
        // reset neurons
        for (i in hiddenNeurons.indices) hiddenNeurons[i] = 0f
        for (i in outputNeurons.indices) outputNeurons[i] = 0f

        for (i in inputNeurons.indices) {
            for (j in hiddenNeurons.indices) {
                hiddenNeurons[j] += inputNeurons[i] * inputToHiddenLayer[i][j]
            }

            // bias neuron
            for (j in hiddenNeurons.indices) {
                hiddenNeurons[j] += inputToHiddenLayer[inputToHiddenLayer.size - 1][j]
            }
        }

        // run activation function on hidden layer neurons
        for (i in hiddenNeurons.indices) hiddenNeurons[i] = activation(hiddenNeurons[i])


        for (i in hiddenNeurons.indices) {
            for (j in outputNeurons.indices) {
                outputNeurons[j] += hiddenNeurons[i] * hiddenToOutputLayer[i][j]
            }

            // bias neuron
            for (j in outputNeurons.indices) {
                outputNeurons[j] += hiddenToOutputLayer[hiddenToOutputLayer.size - 1][j]
            }
        }
    }

    private fun randomWeight() : Float = ((Math.random() - 0.5) * 2.0).toFloat()
}

val relu: (Float)->Float = { input-> if (input > 0) input else input / 32f}
val tanh: (Float)->Float = { input-> tanh(input) }