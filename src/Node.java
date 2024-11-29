import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.ArrayList;
import java.util.List;

public class Node {
    private String id; // Unique Identifier
    private String name;
    private PublicKey publicKey; // For RSA Encryption
    private PrivateKey privateKey; // For RSA Decryption/Signing
    private List<Node> connections; // Friends

    /**
     * Constructor to initialize a Node with basic information and cryptographic keys.
     *
     * @param id        Unique Identifier for the Node
     * @param name      Name of the Node (e.g., Person's Name)
     * @param keyPair   KeyPair containing PublicKey and PrivateKey for RSA encryption/decryption
     */
    public Node(String id, String name, KeyPair keyPair) {
        this.id = id;
        this.name = name;
        this.publicKey = keyPair.getPublic();
        this.privateKey = keyPair.getPrivate();
        this.connections = new ArrayList<>();
    }

    // **Getters**

    /**
     * Returns the Unique Identifier of the Node.
     *
     * @return String representing the ID
     */
    public String getId() {
        return id;
    }

    /**
     * Returns the Name of the Node.
     *
     * @return String representing the Name
     */
    public String getName() {
        return name;
    }

    /**
     * Returns the PublicKey of the Node for encryption purposes.
     *
     * @return PublicKey object
     */
    public PublicKey getPublicKey() {
        return publicKey;
    }

    /**
     * Returns the PrivateKey of the Node for decryption/signing purposes.
     *
     * @return PrivateKey object
     */
    public PrivateKey getPrivateKey() {
        return privateKey;
    }

    /**
     * Returns a List of connected Nodes (friends).
     *
     * @return List<Node> representing connections
     */
    public List<Node> getConnections() {
        return connections;
    }

    // **Setters**

    /**
     * Updates the Name of the Node.
     *
     * @param name New Name for the Node
     */
    public void setName(String name) {
        this.name = name;
    }

    // **Connection Management Methods**

    /**
     * Adds a new connection (friend) to the Node's connections list.
     *
     * @param node Node object to be added as a connection
     */
    public void addConnection(Node node) {
        if (!connections.contains(node)) {
            this.connections.add(node);
        }
    }

    /**
     * Removes an existing connection (friend) from the Node's connections list.
     *
     * @param node Node object to be removed from connections
     */
    public void removeConnection(Node node) {
        this.connections.remove(node);
    }

    // **Utility Method for Printing Connections**

    /**
     * Prints out all current connections of the Node.
     */
    public void printConnections() {
        System.out.println(name + "'s Connections:");
        for (Node connection : connections) {
            System.out.println("- " + connection.getName());
        }
    }

    @Override
    public String toString() {
        return "Node{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                '}';
    }

    // **Equals and HashCode for Proper List Management**

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Node node = (Node) o;
        return id.equals(node.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}