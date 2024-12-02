import javax.crypto.Cipher;
import java.security.Signature;
import java.util.Base64;

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
        if (metadata.equals("run-length")) {
            return runLengthEncode(messageBody);
        }
        return "Unsupported compression type";
    }

    // Method to perform run-length encoding
    public static String runLengthEncode(String input) {
        StringBuilder encoded = new StringBuilder();
        int count = 1;
        for (int i = 1; i < input.length(); i++) {
            if (input.charAt(i) == input.charAt(i - 1)) {
                count++;
            } else {
                encoded.append(input.charAt(i - 1)).append(count);
                count = 1;
            }
        }
        encoded.append(input.charAt(input.length() - 1)).append(count);
        return encoded.toString();
    }

    // Method to perform run-length decoding
    private String runLengthDecode(String encoded) {
        StringBuilder decoded = new StringBuilder();
        for (int i = 0; i < encoded.length(); i += 2) {
            char character = encoded.charAt(i);
            int count = Character.getNumericValue(encoded.charAt(i + 1));
            for (int j = 0; j < count; j++) {
                decoded.append(character);
            }
        }
        return decoded.toString();
    }
}

class LossyCompressedMessage extends Message {
    private int lossLevel;
    
    public LossyCompressedMessage(String senderId, String receiverId, String metadata, String messageBody, int lossLevel) {
        super(senderId, receiverId, metadata, messageBody);
        this.lossLevel = lossLevel;
    }

    @Override
    public String processMessage(Node sender, Node receiver) {
        // Implement fast Fourier transform decoding based on lossiness in metadata
        // Example of FFT decoding based on metadata
        if (metadata.equals("fft")) {
            return fftDecode(messageBody, lossLevel);
        }
        return "Unsupported compression type";
    }

    private String fftDecode(String encoded, int lossLevel) {
        // Convert the encoded string to an array of doubles
        String[] parts = encoded.split(",");
        double[] input = new double[parts.length];
        for (int i = 0; i < parts.length; i++) {
            input[i] = Double.parseDouble(parts[i]);
        }

        // Perform the inverse FFT
        Complex[] result = inverseFFT(input, lossLevel);

        // Convert the result to a decoded string
        StringBuilder decoded = new StringBuilder();
        for (Complex c : result) {
            decoded.append((char) Math.round(c.real));
        }

        return decoded.toString();
    }

    private Complex[] inverseFFT(double[] input, int losslevel) {
        int n = input.length;
        Complex[] x = new Complex[n];
        for (int i = 0; i < n; i++) {
            x[i] = new Complex(input[i], 0);
        }
        
        Complex[] fftResult = fft(x, false);
        int threshold = fftResult.length / (lossLevel + 1);  
        for (int i = threshold; i < fftResult.length; i++) {
            fftResult[i] = new Complex(0, 0);  
        }
        
        return fft(fftResult, true);
    }

    private Complex[] fft(Complex[] x, boolean inverse) {
        int n = x.length;
        if (n == 1) return new Complex[]{x[0]};

        if (n % 2 != 0) throw new IllegalArgumentException("Length of x must be a power of 2");

        Complex[] even = new Complex[n / 2];
        Complex[] odd = new Complex[n / 2];
        for (int i = 0; i < n / 2; i++) {
            even[i] = x[i * 2];
            odd[i] = x[i * 2 + 1];
        }

        Complex[] q = fft(even, inverse);
        Complex[] r = fft(odd, inverse);

        Complex[] y = new Complex[n];
        for (int k = 0; k < n / 2; k++) {
            double kth = -2 * k * Math.PI / n;
            if (inverse) kth = -kth;
            Complex wk = new Complex(Math.cos(kth), Math.sin(kth));
            y[k] = q[k].plus(wk.times(r[k]));
            y[k + n / 2] = q[k].minus(wk.times(r[k]));
        }
        return y;
    }

    private class Complex {
        private final double real;
        private final double imag;

        public Complex(double real, double imag) {
            this.real = real;
            this.imag = imag;
        }

        public Complex plus(Complex b) {
            return new Complex(this.real + b.real, this.imag + b.imag);
        }

        public Complex minus(Complex b) {
            return new Complex(this.real - b.real, this.imag - b.imag);
        }

        public Complex times(Complex b) {
            return new Complex(this.real * b.real - this.imag * b.imag, this.real * b.imag + this.imag * b.real);
        }
    }
}

class EncryptedMessage extends Message {
    public EncryptedMessage(String senderId, String receiverId, String metadata, String messageBody) {
        super(senderId, receiverId, metadata, messageBody);
    }

    @Override
    public String processMessage(Node sender, Node receiver) {
        try {
            // Initialize the cipher for decryption using the receiver's private key
            Cipher cipher = Cipher.getInstance("RSA");
            cipher.init(Cipher.DECRYPT_MODE, receiver.getPrivateKey());

            // Decode the base64 encoded message body
            byte[] encryptedBytes = Base64.getDecoder().decode(messageBody);

            // Decrypt the message
            byte[] decryptedBytes = cipher.doFinal(encryptedBytes);

            // Convert the decrypted bytes to a string
            return new String(decryptedBytes);
        } catch (Exception e) {
            e.printStackTrace();
            return "Decryption failed";
        }
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
        try {
            // Initialize the signature object with the sender's public key
            Signature sig = Signature.getInstance("SHA256withRSA");
            sig.initVerify(sender.getPublicKey());
            sig.update(messageBody.getBytes());

            // Verify the signature
            boolean isVerified = sig.verify(signature);
            if (isVerified) {
                return messageBody;
            } else {
                return "Signature verification failed";
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "Verification process failed";
        }
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
        try {
            // Verify the original signature using the sender's public key
            Signature sig = Signature.getInstance("SHA256withRSA");
            sig.initVerify(sender.getPublicKey());
            sig.update(originalHash.getBytes());

            boolean isOriginalVerified = sig.verify(Base64.getDecoder().decode(originalSignature));
            if (!isOriginalVerified) {
                return "Original signature verification failed";
            }

            // Verify the current message's signature
            boolean isCurrentVerified = super.processMessage(sender, receiver).equals(messageBody);
            if (!isCurrentVerified) {
                return "Current signature verification failed";
            }

            return "Confirmation successful: " + messageBody;
        } catch (Exception e) {
            e.printStackTrace();
            return "Confirmation process failed";
        }
    }
}
