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
        String messageBody = "Hello, Bob!";
        EncryptedMessage encryptedMsg = new EncryptedMessage("1", "2", "RSA", messageBody);
        graph.sendMessage(encryptedMsg);

        // Similarly, create and send other types of messages
    }
}
