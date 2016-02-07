# Crypto Example

![Screenshot](http://i.imgur.com/zw5KLnJ.png?1)

I wrote this quick application to demonstrate to people how to leverage client-side data encryption in Android. I am astounded by the amount of security breaches in the news that result from storing private client data in plain text. This example shows just how easy it is to implement encryption of sensitive data before it is sent/stored somewhere. Even though this is a modest implementation, it's still far more effective than storing data in plain text. It's certainly worth implementing, given the modest time investment.

The meat of the relevant code can be found inside `CryptoUtils.java` for doing typical encrypt/decrypt operations on strings of text.

The application itself is a simple input/output application where you can see the cryptographic functions in action, and how to use them.

One could easily extend this to use with `ContentProvider`s to store data in a SQLite database or elsewhere.