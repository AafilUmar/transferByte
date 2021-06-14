# transferByte
Simple app to Transfer Media and Text from computer to mobile.

##Ktor Server

Ktor is an asynchronous kotlin framework for creating microservices.Used to create local server for sending files and text.

```
	embeddedServer(Netty, port = 8000) {
		routing {
			get ("/") {
				call.respondText("Hello, world!")
			}
		}
	}.start(wait = true)
```





