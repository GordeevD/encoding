const { generateRSAKeys, encryptMessage, decryptMessage } = require('./crypto');

// Example for the sender side
function senderProcess() {
  // Sender generates a message and the receiver's public key
  const { publicKey } = generateRSAKeys(); // Normally, you'd retrieve this from the receiver
  const message = 'Hello Receiver!';

  // Encrypt the message
  const encryptedMessage = encryptMessage(message, publicKey);
  console.log('Encrypted Message:', encryptedMessage);

  return encryptedMessage;
}

// Example for the receiver side
function receiverProcess(encryptedMessage) {
  // Receiver generates their private key
  const { privateKey } = generateRSAKeys(); // Receiver's private key
  const decryptedMessage = decryptMessage(encryptedMessage, privateKey);
  console.log('Decrypted Message:', decryptedMessage);
}

// Simulate sending and receiving
const encryptedMessage = senderProcess();
receiverProcess(encryptedMessage);
