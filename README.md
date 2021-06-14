## TransferByte
Simple app to Transfer Media and Text from computer to mobile.Written in kotlin.

Recycler view used for listing the media posted and downloded

## Ktor Server

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

## Contributing
Pull requests are welcome. For major changes, please open an issue first to discuss what you would like to change.

## License
[APACHE 2.0](https://www.apache.org/licenses/LICENSE-2.0)
