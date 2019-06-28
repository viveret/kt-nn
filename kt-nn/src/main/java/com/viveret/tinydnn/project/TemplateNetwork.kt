package com.viveret.tinydnn.project

import com.viveret.tinydnn.data.formats.CMUMovieSummaryCorpusFormat
import com.viveret.tinydnn.layer.*
import com.viveret.tinydnn.model.INetworkModelWithWeights
import com.viveret.tinydnn.network.SequentialNetworkModelWithWeights

interface TemplateNetwork {
    val name: String
    val description: String
    fun gen(name: String): INetworkModelWithWeights
    fun apply(n: SequentialNetworkModelWithWeights)

    class MultiLayerPerceptron : TemplateNetwork {
        override val name: String
            get() = "Multi-Layer Perceptron (MLP)"

        override val description: String
            get() = "Intended for linear classification (yes or no, cold to hot, 0-9)"

        override fun gen(name: String): INetworkModelWithWeights {
            val n = SequentialNetworkModelWithWeights(name)
            this.apply(n)
            return n
        }

        override fun apply(n: SequentialNetworkModelWithWeights) {
            n.addLayer(FullyConnectedLayer((32 * 32).toLong(), 300, true))
            n.addLayer(SigmoidLayer())
            n.addLayer(FullyConnectedLayer(300, 10, true))
        }
    }

    class MnistRnn : TemplateNetwork {
        override val name: String
            get() = "MNIST RNN"

        override val description: String
            get() = "Convolutional neural network for MNIST"

        override fun gen(name: String): INetworkModelWithWeights {
            val n = SequentialNetworkModelWithWeights(name)
            this.apply(n)
            return n
        }

        override fun apply(n: SequentialNetworkModelWithWeights) {
            // in:32x32x1, 5x5conv, 6fmaps
            n.addLayer(ConvolutionalLayer(32, 32, 5, 1, 6, Padding.None, true))
            n.addLayer(TanhLayer())
            // in:28x28x6, 2x2pooling
            n.addLayer(AveragePoolingLayer(28, 28, 6, 2))
            n.addLayer(TanhLayer())
            // in:14x14x6, out:120
            n.addLayer(FullyConnectedLayer((14 * 14 * 6).toLong(), 120, true))
            n.addLayer(TanhLayer())
            // in:120,     out:10
            n.addLayer(FullyConnectedLayer(120, 10, true))
        }
    }

    class MnistRnn2 : TemplateNetwork {
        override val name: String
            get() = "MNIST RNN 2"

        override val description: String
            get() = "Better convolutional neural network for MNIST"

        override fun gen(name: String): INetworkModelWithWeights {
            val n = SequentialNetworkModelWithWeights(name)
            this.apply(n)
            return n
        }

        override fun apply(n: SequentialNetworkModelWithWeights) {
            val O = true
            val X = false
            val tbl = booleanArrayOf(
                    O, X, X, X, O, O, O, X, X, O, O, O, O, X, O, O,
                    O, O, X, X, X, O, O, O, X, X, O, O, O, O, X, O,
                    O, O, O, X, X, X, O, O, O, X, X, O, X, O, O, O,
                    X, O, O, O, X, X, O, O, O, O, X, X, O, X, O, O,
                    X, X, O, O, O, X, X, O, O, O, O, X, O, O, X, O,
                    X, X, X, O, O, O, X, X, O, O, O, O, X, O, O, O
            )

            // in:32x32x1, 5x5conv, 6fmaps
            n.addLayer(ConvolutionalLayer(32, 32, 5, 1, 6, Padding.None, true))
            n.addLayer(TanhLayer())
            n.addLayer(AveragePoolingLayer(28, 28, 6, 2))
            //6@14x14-in, 16@10x10-out
            n.addLayer(ConvolutionalLayer(14, 14, 5, 6, 16, Padding.None, true))
            n.addLayer(TanhLayer())
            // in:28x28x6, 2x2pooling
            n.addLayer(AveragePoolingLayer(10, 10, 16, 2))
            n.addLayer(TanhLayer())

            n.addLayer(
                ConvolutionalLayer(5, 5, 5, 16, 120,
                    ConnectionTable(tbl, 6, 16),
                    Padding.None, true)
            )
            n.addLayer(TanhLayer())
            // in:120,     out:10
            n.addLayer(FullyConnectedLayer(120, 10, true))
            n.addLayer(TanhLayer())
        }
    }

    class BinaryGate : TemplateNetwork {
        override val name: String
            get() = "Binary Gate"

        override val description: String
            get() = "Mimic a Binary gate (xor, and, or, etc...)"

        override fun gen(name: String): INetworkModelWithWeights {
            val n = SequentialNetworkModelWithWeights(name)
            this.apply(n)
            return n
        }

        override fun apply(n: SequentialNetworkModelWithWeights) {
            n.addLayer(FullyConnectedLayer(2, 3, true))
            n.addLayer(SigmoidLayer())
            n.addLayer(FullyConnectedLayer(3, 1, true))
            n.addLayer(SigmoidLayer())
        }
    }

    class MovieCategorizer: TemplateNetwork {
        override val name: String
            get() = "Movie Categorizer"

        override val description: String
            get() = "Find a category that best fits a movie description"

        override fun gen(name: String): INetworkModelWithWeights {
            val n = SequentialNetworkModelWithWeights(name)
            this.apply(n)
            return n
        }

        override fun apply(n: SequentialNetworkModelWithWeights) {
            n.addLayer(FullyConnectedLayer(300, 900, true))
            n.addLayer(SigmoidLayer())
            n.addLayer(ConvolutionalLayer(30, 30, 3, 1, 6, Padding.None, true))
            n.addLayer(TanhLayer())
            n.addLayer(MaxPoolingLayer(28, 28, 6, 7, 7, 0, 0, Padding.None))
            n.addLayer(TanhLayer())
            n.addLayer(FullyConnectedLayer((4 * 4 * 6).toLong(), CMUMovieSummaryCorpusFormat.outputLabels.size.toLong(), true))
        }
    }

    class ReviewSentimentAnalysis: TemplateNetwork {
        override val name: String
            get() = "Review Sentiment Analysis"

        override val description: String
            get() = "Discover what the sentiment is behind a review"

        override fun gen(name: String): INetworkModelWithWeights {
            // love,joy, surprise, anger, sadnessand fear
            val n = SequentialNetworkModelWithWeights(name)
            this.apply(n)
            return n
        }

        override fun apply(n: SequentialNetworkModelWithWeights) {
            n.addLayer(ConvolutionalLayer(312, 1, 20, 1, 1, 6, Padding.None, true, 20, 1))
            n.addLayer(ReLuLayer(15, 1, 6))
            n.addLayer(ConvolutionalLayer(90, 1, 10, 1, 1, 2, Padding.None, true, 3, 1))
            //n.addLayer(MaxPoolingLayer(27, 1, 2, 2, 1, 1, 1, LayerBase.Padding.None))
            //n.addLayer(SigmoidLayer())
            n.addLayer(FullyConnectedLayer(54, 1, true))
        }
    }

    class Cifar10Classification: TemplateNetwork {
        override val name: String
            get() = "Cifar-10 Classification"

        override val description: String
            get() = "Classify color images as airplane, automobile, bird, cat, deer, dog, frog, horse, ship, or truck"

        override fun gen(name: String): INetworkModelWithWeights {
            val n = SequentialNetworkModelWithWeights(name)
            this.apply(n)
            return n
        }

        override fun apply(n: SequentialNetworkModelWithWeights) {
            val n_fmaps = 32L
            val n_fmaps2 = 64L
            val n_fc = 64L
            n.addLayer(ConvolutionalLayer(32, 32, 5, 3, n_fmaps, Padding.Fill, true))          // C1
            n.addLayer(MaxPoolingLayer(32, 32, n_fmaps, 2))                            // P2
            n.addLayer(ReLuLayer(16, 16, n_fmaps))                               // activation
            n.addLayer(ConvolutionalLayer(16, 16, 5, n_fmaps, n_fmaps, Padding.Fill, true)) // C3
            n.addLayer(MaxPoolingLayer(16, 16, n_fmaps, 2))                            // P4
            n.addLayer(ReLuLayer(8, 8, n_fmaps))                                 // activation
            n.addLayer(ConvolutionalLayer(8, 8, 5, n_fmaps, n_fmaps2, Padding.Fill, true))     // C5
            n.addLayer(MaxPoolingLayer(8, 8, n_fmaps2, 2))                             // P6
            n.addLayer(ReLuLayer(4, 4, n_fmaps2))                                // activation
            n.addLayer(FullyConnectedLayer(4 * 4 * n_fmaps2, n_fc, true))                          // FC7
            n.addLayer(FullyConnectedLayer(n_fc, 10, true))
            n.addLayer(SoftMaxLayer(10, 10))                  // FC10
        }
    }
}