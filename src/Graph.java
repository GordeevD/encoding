import java.util.HashMap;
import java.util.Map;

public class Graph {
    private Map<String, Node> nodes; // id -> Node

    public Graph() {
        this.nodes = new HashMap<>();
    }

    public void addNode(Node node) {
        nodes.put(node.getId(), node);
    }

    public void connectNodes(String nodeId1, String nodeId2) {
        Node node1 = nodes.get(nodeId1);
        Node node2 = nodes.get(nodeId2);
        if (node1 != null && node2 != null) {
            node1.addConnection(node2);
            node2.addConnection(node1); // For undirected graph
        }
    }

    public void sendMessage(Message message) {
        Node sender = nodes.get(message.senderId);
        Node receiver = nodes.get(message.receiverId);
        if (sender != null && receiver != null) {
            String processedMessage = message.processMessage(sender, receiver);
            System.out.println("Received Message from " + sender.getName() + " to " + receiver.getName() + ": " + processedMessage);
        }
    }
}