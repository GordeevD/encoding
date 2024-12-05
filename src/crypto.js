const crypto = require('crypto');

// Function to generate RSA keys
function generateRSAKeys() {
  const { publicKey, privateKey } = crypto.generateKeyPairSync('rsa', {
    modulusLength: 2048,
    publicKeyEncoding: {
      type: 'spki',
      format: 'pem',
    },
    privateKeyEncoding: {
      type: 'pkcs8',
      format: 'pem',
    },
  });

  return { publicKey, privateKey };
}

// Function to encrypt a message using a public key
function encryptMessage(message, publicKey) {
  const encryptedMessage = crypto.publicEncrypt(publicKey, Buffer.from(message));
  return encryptedMessage.toString('base64');
}

// Function to decrypt a message using a private key
function decryptMessage(encryptedMessage, privateKey) {
  const decryptedMessage = crypto.privateDecrypt(privateKey, Buffer.from(encryptedMessage, 'base64'));
  return decryptedMessage.toString('utf8');
}

module.exports = { generateRSAKeys, encryptMessage, decryptMessage };
