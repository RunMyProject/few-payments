/*
 * extractor.js
 * Author: Edoardo Sabatini
 * Date: 2026-05-11
 * Company: few-payments
 * Description: Extracts the private key from the keystore file using the password. 
 * This is used for signing transactions in the blockchain tests.
 */
const keyth = require('keythereum');
const fs = require('fs');

const password = fs.readFileSync('password.txt', 'utf8').trim();
const keystorePath = "keystore/UTC--2026-05-09T16-39-12.065690754Z--7677afeb70f24580f72b2a259b0e956c3cbe4374";
const keyObject = JSON.parse(fs.readFileSync(keystorePath, 'utf8'));

const privateKey = keyth.recover(password, keyObject);
console.log("\n🔑 YOUR PRIVATE KEY IS:");
console.log(privateKey.toString('hex'));
