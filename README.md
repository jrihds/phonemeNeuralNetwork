phonemeNeuralNetwork
====================

Neural network implemented in java that processes voice audio data and identifies specific phonemes

Originally developed as part of a EE University Project by James Richards and Dr Paul Leonard (University of Bath, UK)

There is a pre-trained neural network already in the project:
 - To use a microphone as a real-time input execute the Java Application from at phonemeNeuralNetwork/src/speech/MainApp.java
 - To read in patient voice files enter the 'fileName' of the wav and execute the Java Application from at phonemeNeuralNetwork/src/speech/MainAppFile.java


If you want to train a new neural network:
 - Load your .wav files into the directory src/speech/wavFiles/trainingSplice
 - Update the training script with the new parameters
 - Execute the training script at src/speech/neuralNetwork/WavTraining.java
 - Wait for a while until the network is trained
 - Execute the Java Application as described above
