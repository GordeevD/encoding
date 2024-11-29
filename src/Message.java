public abstract class Message {
    protected String senderId;
    protected String receiverId;
    protected String metadata; // Compression type, lossiness, etc.
    protected String messageBody;

    public Message(String senderId, String receiverId, String metadata, String messageBody) {
        this.senderId = senderId;
        this.receiverId = receiverId;
        this.metadata = metadata;
        this.messageBody = messageBody;
    }

    // Abstract method for processing (e.g., decompress, decrypt, verify)
    public abstract String processMessage(Node sender, Node receiver);
}

// Concrete Message Classes

class CompressedMessage extends Message {
    public CompressedMessage(String senderId, String receiverId, String metadata, String messageBody) {
        super(senderId, receiverId, metadata, messageBody);
    }

    @Override
    public String processMessage(Node sender, Node receiver) {
        // Implement run-length decoding based on metadata
        // Not finished method yet.
        return "success";
    }
}

class LossyCompressedMessage extends Message {
    public LossyCompressedMessage(String senderId, String receiverId, String metadata, String messageBody) {
        super(senderId, receiverId, metadata, messageBody);
    }

    @Override
    public String processMessage(Node sender, Node receiver) {
        // Implement fast Fourier transform decoding based on lossiness in metadata
        // Not finished method yet.
        return "success";
    }
}

class EncryptedMessage extends Message {
    public EncryptedMessage(String senderId, String receiverId, String metadata, String messageBody) {
        super(senderId, receiverId, metadata, messageBody);
    }

    @Override
    public String processMessage(Node sender, Node receiver) {
        // Decrypt using receiver's privateKey
        // Not finished method yet.
        return "success";
    }
}

class SignedMessage extends Message {
    private byte[] signature;

    public SignedMessage(String senderId, String receiverId, String metadata, String messageBody, byte[] signature) {
        super(senderId, receiverId, metadata, messageBody);
        this.signature = signature;
    }

    @Override
    public String processMessage(Node sender, Node receiver) {
        // Verify signature using sender's publicKey and then return the original messageBody
        // Not finished method yet.
        return "success";
    }
}

class ConfirmationMessage extends SignedMessage {
    private String originalSignature;
    private String originalHash;

    public ConfirmationMessage(String senderId, String receiverId, String metadata,
                               String messageBody, byte[] signature, String originalSignature, String originalHash) {
        super(senderId, receiverId, metadata, messageBody, signature);
        this.originalSignature = originalSignature;
        this.originalHash = originalHash;
    }

    @Override
    public String processMessage(Node sender, Node receiver) {
        // Specific handling for confirmation messages
        // Not finished method yet.
        return "success";
    }
}