# Multi-Threaded Chat Application (Assignment 6)

### Getting Started (IDE used: IntelliJ IDEA)
1. At <code>Server.java</code>, execute the <code>main</code> function.
2. At <code>Client.java</code>, execute the <code>main</code> function to generate the first client.
3. Edit configuration to allow multiple client instances<br>
![image](https://media.github.khoury.northeastern.edu/user/11705/files/b58fc81d-0d14-42ff-8b20-9b0c4bdab499)
![image](https://media.github.khoury.northeastern.edu/user/11705/files/3f1b4a53-2c72-4c2d-89d8-791150ed4dff)
4. At each client command prompt, enter your username and start chatting<br>
![image](https://media.github.khoury.northeastern.edu/user/11705/files/4f4f22b3-f683-4a5a-8007-8e6caa5a04e8)
5. Input <code>?</code> command to see the help manual
![image](https://media.github.khoury.northeastern.edu/user/11705/files/79f8ba84-a24b-494a-ac6a-1e755e3e4d2f)

### Key Classes / Methods
1. <code>Server</code>: To accept connection from clients and instantiate corresponding client handlers
	- <code>startServer</code>: this method will keep detecting and accepting new client connections, and it will pass the generated soket to a new instantiated client handler for futuer communication  		
2. <code>ClientHandler</code>: The server will instantiate a corresponding client handler to handle the new connected client
	- <code>run</code>: this method will handle the message from the corresponding client based on the messge type
3. <code>Client</code>: To send and receive message, and communicate with other clients via the server
	-  <code>SendMessage</code>: this method will send the message and the specified message type to the server based on the user input command
	-  <code>listenForMessage</code>: this method will create another runnable thread to receive the message, so the client program can to the sending and listening concurrently
