package jonahshader

import kotlin.math.sqrt
import kotlin.math.tanh

class NeuralNetwork() {

    private val mutationScale = 0.1f
    private lateinit var activation: (Float)->Float

    val inputNeurons = ArrayList<Float>()
    val outputNeurons = ArrayList<Float>()
    private val hiddenNeurons = ArrayList<Float>()

    private val inputToHiddenLayer = ArrayList<ArrayList<Float>>()
    private val hiddenToOutputLayer = ArrayList<ArrayList<Float>>()

    constructor(inputs: Int, outputs: Int, hiddenLayer: Int, activation: (Float)->Float) : this() {
        this.activation = activation
        for (i in 0 until inputs) inputNeurons.add(0f)
        for (i in 0 until outputs) outputNeurons.add(0f)
        for (i in 0 until hiddenLayer) hiddenNeurons.add(0f)

        for (i in 0 until (inputs + 1)) {
            inputToHiddenLayer.add(ArrayList())
            for (j in 0 until hiddenLayer) {
                inputToHiddenLayer[i].add(xavierWeight(inputs + 1, hiddenLayer))
            }
        }

        for (i in 0 until hiddenLayer + 1) {
            hiddenToOutputLayer.add(ArrayList())
            for (j in 0 until outputs) {
                hiddenToOutputLayer[i].add(xavierWeight(hiddenLayer + 1, outputs))
            }
        }
    }

    constructor(nn: NeuralNetwork) : this() {
        this.activation = nn.activation
        for (neuron in nn.inputNeurons) {
            this.inputNeurons.add(neuron)
        }

        for (neuron in nn.outputNeurons) {
            this.outputNeurons.add(neuron)
        }

        for (neuron in nn.hiddenNeurons) {
            this.hiddenNeurons.add(neuron)
        }

        for (i in nn.inputToHiddenLayer.indices) {
            this.inputToHiddenLayer.add(ArrayList())
            for (j in nn.inputToHiddenLayer[i].indices) {
                this.inputToHiddenLayer[i].add(nn.inputToHiddenLayer[i][j])
            }
        }

        for (i in nn.hiddenToOutputLayer.indices) {
            this.hiddenToOutputLayer.add(ArrayList())
            for (j in nn.hiddenToOutputLayer[i].indices) {
                this.hiddenToOutputLayer[i].add(nn.hiddenToOutputLayer[i][j])
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
    private fun xavierWeight(fanIn: Int, fanOut: Int) : Float = (Math.random().toFloat() - 0.5f) * 2f * (sqrt(6f) / (sqrt(fanIn.toFloat() + fanOut.toFloat())))
    fun mutate() {
        for (weights in inputToHiddenLayer) {
            for (i in weights.indices) {
                weights[i] += ((Math.random().toFloat() - 0.5f) * 2f) * mutationScale
            }
        }

        for (weights in hiddenToOutputLayer) {
            for (i in weights.indices) {
                weights[i] += ((Math.random().toFloat() - 0.5f) * 2f) * mutationScale
            }
        }
    }
}

val relu: (Float)->Float = { input-> if (input > 0) input else input / 32f}
val tanh: (Float)->Float = { input-> tanh(input) }