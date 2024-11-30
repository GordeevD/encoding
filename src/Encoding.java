import java.security.KeyPair;
import java.security.KeyPairGenerator;

public class Encoding {
    public static void main(String[] args) throws Exception{
        // Generate Key Pairs for RSA
        KeyPairGenerator kpg = KeyPairGenerator.getInstance("RSA");
        kpg.initialize(2048);
        KeyPair keyPairAlice = kpg.generateKeyPair();
        KeyPair keyPairBob = kpg.generateKeyPair();

        Graph graph = new Graph();

        Node alice = new Node("1", "Alice", keyPairAlice);
        Node bob = new Node("2", "Bob", keyPairBob);

        graph.addNode(alice);
        graph.addNode(bob);
        graph.connectNodes("1", "2");

        // Example: Send Encrypted Message
        String messageBody1 = "Hello, Bob!";
        // Example: Send Compressed Message
        CompressedMessage compressedMsg = new CompressedMessage("1", "2", "run-length", messageBody1);

        graph.sendMessage(compressedMsg);

        // =======================================================================================================
        // Define the Message Body for Lossy Compression
        String messageBody2 = "Hello, Bob! This message will be lossy compressed using FFT.";

        // Create a LossyCompressedMessage Instance
        LossyCompressedMessage lossyMsg = new LossyCompressedMessage("1", "2", "fft", messageBody2, 2);

        //  Send the Lossy Compressed Message
        graph.sendMessage(lossyMsg);
        // =======================================================================================================
        
        // Similarly, create and send other types of messages
    }
}
