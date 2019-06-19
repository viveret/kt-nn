package com.viveret.tinydnn.tinydnn.model

import com.viveret.tinydnn.tinydnn.Iterator
import com.viveret.tinydnn.tinydnn.Optimizer
import com.viveret.tinydnn.tinydnn.Tensor
import com.viveret.tinydnn.tinydnn.Vect
import com.viveret.tinydnn.tinydnn.constants.Sizes
import com.viveret.tinydnn.tinydnn.enums.FileFormat
import com.viveret.tinydnn.tinydnn.enums.TrainResult
import com.viveret.tinydnn.tinydnn.enums.grad_check_mode
import com.viveret.tinydnn.tinydnn.enums.net_phase
import com.viveret.tinydnn.tinydnn.util.TestResult
import com.viveret.tinydnn.util.JniObject

interface INetworkModelWithWeights : JniObject, INetworkModel {
    fun initWeight()

    /**
     * propagate gradient
     * @param first        : gradient of cost function(dE/dy)
     */
    fun backward(first: List<Tensor>)

    /**
     * @param first input  : data vectors
     */
    fun forward(first: List<Tensor>): List<Tensor>   // NOLINT

    /**
     * update Weights and clear all gradients
     */
    fun update_weights(opt: Optimizer)

    /**
     * setup all Weights, must be called before forward/backward
     */
    fun setup(reset_weight: Boolean)

    fun clear_grads()

    fun begin(): Iterator

    fun end(): Iterator

    fun target_value_min(): Float // int out_channel = 0

    fun target_value_min(out_channel: Int): Float

    fun target_value_max(): Float  // int out_channel = 0

    fun target_value_max(out_channel: Int): Float

    fun load(vec: List<Float>)

    fun label2vec(t: Long, num: Long, vec: List<Vect>)

    fun label2vec(labels: List<Long>, vec: List<Vect>)

    fun loadWeights(path: String, format: FileFormat)

    fun predict(`in`: Vect): Vect

    fun epochAt(): Int

    fun batchAt(): Long


    /**
     * executes forward-propagation and returns output
     */
    fun predict(`in`: Tensor): Tensor

    /**
     * executes forward-propagation and returns output
     */
    fun predict(`in`: List<Tensor>): List<Tensor>

    /**
     * executes forward-propagation and returns maximum output
     */
    fun predict_max_value(`in`: Vect): Float

    /**
     * executes forward-propagation and returns maximum output index
     */
    fun predict_label(`in`: Vect): Long

    /**
     * executes forward-propagation and returns output
     *
     * @param in input value range(double[], std::vector<double>,
     * std::list<double> etc)
    </double></double> */
    fun predict(`in`: FloatArray): Vect

    /**
     * trains the network for a fixed number of epochs (for classification task)
     *
     * The difference between trainUsingMethod and fit method is how to specify desired
     * output.
     * This method takes long argument and convert to target vector
     * automatically.
     * To trainUsingMethod correctly, output dimension of last layer must be greater or
     * equal
     * to
     * number of label-ids.
     *
     * @param optimizer          optimizing algorithm for training
     * @param inputs             array of input data
     * @param class_labels       array of label-id for each input data(0-origin)
     * @param batch_size         number of samples per parameter update
     * @param epoch              number of training epochs
     * @param on_batch_enumerate callback for each mini-batch enumerate
     * @param on_epoch_enumerate callback for each epoch
     * @param reset_weights      set true if reset current network Weights
     * @param n_threads          number of tasks
     * @param t_cost             target costs (leave to nullptr in order to
     * assume
     * equal cost for every target)
     */
    fun train(optimizer: Optimizer,
              inputs: List<Vect>,
              class_labels: List<Long>,
              batch_size: Long,
              epoch: Int,
              on_batch_enumerate: () -> Unit,
              on_epoch_enumerate: () -> Unit,
              reset_weights: Boolean = false,
              n_threads: Int = Sizes.CNN_TASK_SIZE,
              t_cost: List<Vect> = ArrayList()): TrainResult


    /**
     * trains the network for a fixed number of epochs to generate desired
     * output.
     *
     * This method executes fixed number of training steps and invoke callbacks
     * for
     * each mini-batch/epochs.
     * The network is trained to minimize given loss function(specified by
     * template
     * parameter).
     *
     * Shape of inputs and desired_outputs must be Fill to network inputs. For
     * example, if your network
     * has 2 input layers that takes N dimensional array, for each element of
     * inputs must be [2xN]
     * array.
     *
     * @code
     * network<sequential> net;
     * adagrad opt;
     *
     * net << layers::fc(2, 3) << activation::tanh()
     * << layers::fc(3, 1) << activation::relu();
     *
     * // 2training data, each data is float[2]
     * std::vector<Vect> data { { 1, 0 }, { 0, 2 } };
     * std::vector<Vect> out  {    { 2 },    { 1 } };
     *
     * net.fit<mse>(opt, data, out, 1, 1);
     *
     * // 2training data, each data is float[1][2]
     * // this form is also None
     * std::vector<tensor_t> data2{ { { 1, 0 } }, { { 0, 2 } } };
     * std::vector<tensor_t> out2 { {    { 2 } }, {    { 1 } } };
     *
     * net.fit<mse>(opt, data2, out2, 1, 1);
     * @endcode
     *
     *
     * @param optimizer          optimizing algorithm for training
     * @param inputs             array of input data
     * @param desired_outputs    array of desired output
     * @param batch_size         number of samples per parameter update
     * @param epoch              number of training epochs
     * @param on_batch_enumerate callback for each mini-batch enumerate
     * @param on_epoch_enumerate callback for each epoch
     * @param reset_weights      set true if reset current network Weights
     * @param n_threads          number of tasks
     * @param t_cost             target costs (leave to nullptr in order to
     * assume
     * equal cost for every target)
    </mse></tensor_t></tensor_t></mse></Vect></Vect></sequential> */
    fun fit(optimizer: Optimizer,
            inputs: List<Vect>,
            desired_outputs: List<Vect>,
            batch_size: Long,
            epoch: Int,
            on_batch_enumerate: () -> Unit,
            on_epoch_enumerate: () -> Unit,
            reset_weights: Boolean = false,
            n_threads: Int = Sizes.CNN_TASK_SIZE, //          = CNN_TASK_SIZE,
            t_cost: List<Vect> = ArrayList()): TrainResult

    /**
     * @param optimizer          optimizing algorithm for training
     * @param inputs             array of input data
     * @param desired_outputs    array of desired output
     * @param batch_size         number of samples per parameter update
     * @param epoch              number of training epochs
     */
    fun fit(optimizer: Optimizer,
            inputs: List<Vect>,
            desired_outputs: List<Vect>,
            batch_size: Long, // = 1,
            epoch: Int): TrainResult  //  = 1

    /**
     * @param optimizer          optimizing algorithm for training
     * @param inputs             array of input data
     * @param class_labels       array of label-id for each input data(0-origin)
     * @param batch_size         number of samples per parameter update
     * @param epoch              number of training epochs
     */
    fun train(optimizer: Optimizer,
              inputs: List<Vect>,
              class_labels: List<Long>,
              batch_size: Long, // = 1,
              epoch: Int): TrainResult  //  = 1

    /**
     * set the netphase to trainUsingMethod or test
     * @param phase phase of network, could be trainUsingMethod or test
     */
    fun set_netphase(phase: net_phase)

    /**
     * request to finish an ongoing training
     *
     * It is safe to test the current network performance in @a
     * on_batch_enumerate
     * and
     * @a on_epoch_enumerate callbacks during training.
     */
    fun stop_ongoing_training()

    /**
     * test and generate confusion-matrix for classification task
     */
    fun test(`in`: List<Vect>, t: List<Long>): TestResult


    /**
     * generate output for each input
     */
    fun test(`in`: List<Vect>): List<Vect>


    /**
     * calculate loss value (the smaller, the better)
     */
    fun get_loss(`in`: List<Vect>, t: List<Long>): Float


    /**
     * checking gradients calculated by bprop
     * detail information:
     * http://ufldl.stanford.edu/wiki/index.php/Gradient_checking_and_advanced_optimization
     */
    fun gradient_check(`in`: List<Tensor>,
                       t: List<List<Long>>,
                       eps: Float,
                       mode: grad_check_mode): Boolean

    /**
     * set weight initializer to all layers
     */
    fun weight_init(f: Vect): INetworkModelWithWeights

    /**
     * set bias initializer to all layers
     */
    fun bias_init(f: Vect): INetworkModelWithWeights

    /**
     * returns if 2 networks have almost(<eps></eps>) the Fill Weights
     */
    fun has_same_weights(rhs: INetworkModelWithWeights, eps: Float): Boolean
}
