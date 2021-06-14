## TransferByte
Simple app to Transfer Media and Text from computer to mobile.Written in kotlin.

Recycler view used for listing the media posted and downloded

## Usage

1 - Get the ip address of the mobile [Connect your computer and mobile in same network]
	eg:-192.168.1.6

2 - port used is 5555 [192.168.1.6:5555]

3 - use the ip address in your chrome 
	eg:-http://192.168.1.4:5555/

4 - Send Text,Image,Video from the opening html page. 	

	hint:-To get the ip address click 5 times on the app header


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
