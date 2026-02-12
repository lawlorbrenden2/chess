# ♕ BYU CS 240 Chess

This project demonstrates mastery of proper software design, client/server architecture, networking using HTTP and WebSocket, database persistence, unit testing, serialization, and security.

## 10k Architecture Overview

The application implements a multiplayer chess server and a command line chess client.

[![Sequence Diagram](10k-architecture.png)](https://sequencediagram.org/index.html#initialData=C4S2BsFMAIGEAtIGckCh0AcCGAnUBjEbAO2DnBElIEZVs8RCSzYKrgAmO3AorU6AGVIOAG4jUAEyzAsAIyxIYAERnzFkdKgrFIuaKlaUa0ALQA+ISPE4AXNABWAexDFoAcywBbTcLEizS1VZBSVbbVc9HGgnADNYiN19QzZSDkCrfztHFzdPH1Q-Gwzg9TDEqJj4iuSjdmoMopF7LywAaxgvJ3FC6wCLaFLQyHCdSriEseSm6NMBurT7AFcMaWAYOSdcSRTjTka+7NaO6C6emZK1YdHI-Qma6N6ss3nU4Gpl1ZkNrZwdhfeByy9hwyBA7mIT2KAyGGhuSWi9wuc0sAI49nyMG6ElQQA)

## Modules

The application has three modules.

- **Client**: The command line program used to play a game of chess over the network.
- **Server**: The command line program that listens for network requests from the client and manages users and games.
- **Shared**: Code that is used by both the client and the server. This includes the rules of chess and tracking the state of a game.

## Starter Code

As you create your chess application you will move through specific phases of development. This starts with implementing the moves of chess and finishes with sending game moves over the network between your client and server. You will start each phase by copying course provided [starter-code](starter-code/) for that phase into the source code of the project. Do not copy a phases' starter code before you are ready to begin work on that phase.

## IntelliJ Support

Open the project directory in IntelliJ in order to develop, run, and debug your code using an IDE.

## Maven Support

You can use the following commands to build, test, package, and run your code.

| Command                    | Description                                     |
| -------------------------- | ----------------------------------------------- |
| `mvn compile`              | Builds the code                                 |
| `mvn package`              | Run the tests and build an Uber jar file        |
| `mvn package -DskipTests`  | Build an Uber jar file                          |
| `mvn install`              | Installs the packages into the local repository |
| `mvn test`                 | Run all the tests                               |
| `mvn -pl shared test`      | Run all the shared tests                        |
| `mvn -pl client exec:java` | Build and run the client `Main`                 |
| `mvn -pl server exec:java` | Build and run the server `Main`                 |

These commands are configured by the `pom.xml` (Project Object Model) files. There is a POM file in the root of the project, and one in each of the modules. The root POM defines any global dependencies and references the module POM files.

## Running the program using Java

Once you have compiled your project into an uber jar, you can execute it with the following command.

```sh
java -jar client/target/client-jar-with-dependencies.jar

♕ 240 Chess Client: chess.ChessPiece@7852e922
```

## Server Program Architecture
https://sequencediagram.org/index.html?presentationMode=readOnly#initialData=IYYwLg9gTgBAwgGwJYFMB2YBQAHYUxIhK4YwDKKUAbpTngUSWDABLBoAmCtu+hx7ZhWqEUdPo0EwAIsDDAAgiBAoAzqswc5wAEbBVKGBx2ZM6MFACeq3ETQBzGAAYAdAHZM9qBACu2AMQALADMABwATACcIDD+yPYAFmA6CD6GAEoo9kiqFnJIEGiYiKikALQAfOSUNFAAXDAA2gAKAPJkACoAujAA9D4GUAA6aADeAEQDlGjAALYo43XjMOMANCu46gDu0ByLy2srKLPASAj7KwC+mMK1MJWs7FyUDRNTUDPzF4fjm6o7UD2SxW63Gx1O52B42ubE43FgD1uogaUCyOTAlAAFJlsrlKJkAI5pXIAShuNVE9yqsnkShU6ga9hQYAAqoNMe9PigyTTFMo1KoqUYdHUAGJITgwNmUXkwHSWGCcuZiHSo4AAaylgxgWyQYASisGXJgwAQao4CpQAA90RpeXSBfdERSVA1pVBeeSRConVVbi8YAozShgBaOhr0ABRK0qbAEQpeu5lB4lcwNQJOYIjCbzdTAJmLFaRqDeeqG6bKk3B0MK+Tq9DQsycTD2-nqX3Vb0oBpoHwIBCJykPVv01R1EBqjHujmDXk87QO9sPYx1BQcDhamXaQc+4cLttjichjEKHz6zHAM8JOct-ejoUrtcb0-6z1I3cPWHPMs49H4tR9lgX7wh2-plm8RrKssDQHKCl76h0ED1mg0ErFciaUB2qYYA04ROE42aTJBXwwDBIIrPBCSIchqEHNc6AcKYXi+AE0DsEysSinAkbSHACgwAAMhA2RFNhzDOtQAYtO03R9AY6gFGg2ZKvM6x-ACHDXGBQrAQGEEVl8UKgupuzfDCTwgRJVDIjACDCRKmJCSJhLEmAZLvoYe60gejLMtOKncre3n3suIripK7qyvK5YfMqmCqiGmrusaaAQMwABmvjNiOjrJn6LrdjAAByfYDh5oE1AGLIzFe0BIAAXigHDRrG8ZFNpeXwMgaYwBmACMhG5qo+YLDBxalm6NX6nVjV7PR2V3rlVk2ZF27lV5fKjuOk4oC+CQXleN45UuVSPuugaHWtBUdrpZZORKmSqIBmC3RVkngURBkLKRaE-JR1ENj9dEYQiKbdThMB4QRYyfbFJFkX9V4AyhQPoQxTHeH4-heCg6CxPESQ43jTm+FgYlCmBDSNNIkYCZGHSRj0vTyaoikjP9SHoCDOkWQGAA8HPIRUL28wi+Vdg0dn2KTjnCaTLlqG5O6edSi0MjATJgHtB0IZzaDzsFuWnSKMDhc+l3yHKCqC1zCUahduvITAqUZVljHHYKnUeQ0JX9srb21JNlEzU1LUoHGinc51Ynpk4A0w0NI2FuM43QEHtVQA1TWNujHsBzZe1vtdG2LmORgoNwJ5XjrVF6wbm1G8KDSZLMEA0A715XV2N2iw0JNno9z2vV7lXgVpo9YeDYC4fh2bzYxniYwEqIbv42ASpqAnojAADiyoaOTVlSTv9NM-Yyrs0jetR1Ut0NALV9CyLcKYctrq2eie+5o5n-7wrpL+xLj5DWzJtY231kFBuJ0m6mwlObV82grYmkfrbNUmo9rI2dmlGAmUfALUNkuN+hVfZlWLkfMs1Vg6Z1mmHCOCYOpg1KNPXqcdBoCiTmNEsacpRTQSCHOaTZ3Zq09kQhohcu7WV3KrAhZdNZBgQF-NQmJ66lwfCbHezIjCIMyrAU0CBd7700DoDsHtGTKk9HnEeEsDHzCLt3Tqd9BK-1zIPBAQFRYBz0uMc+uZCyNAmD4lAABJaQhZQRbASHqFAyUoJLB+CkUA6oYmGXGD8QJRVlRxMuDALo493qTyYTPaGAT95+JKfMEJYSVgRKick0aqTQQJJAEk4i9S0nKgyfMLJOT54YxYv4DgbhIhOBQE4WIkZghwG4gANngDtGxhhihTwpqPKmbROhnwvrw5G2Z0nKjyUmW+vcYAP0dugCo6xykoE6fU9Cw9xaSMKkeOQKBFGYjgDtRR-8lbrWkVA2RoDq7gJUQeNRDQzYdyitbFBRQ7boJhVg12eChEyPzu-EhgDyHp2mtQ0OMZw5tSjownq-U2F5gLJwiaPCqFZwEbnYRaLCriPkIAv5pcGjPIxG8xRR0GWhTEedHl25LGiI1sqEJmKjkvzLB848rzlSuPcdKzxH1AmVJggc1+xKIZXPVWjZsi9+mWArnZLY+MkCJDAMa-sEAzUACkIASgWbEJp6ollMJWe9NZLJZK9ECZfM5SkxjYAQMAY1UA4AQDslAdYarpCarFo8aV99wEXJgLq0JUJzLKtFQAK0dWgN5DqJRfJQESRW7li5suAXIoFMKQUhWNuCuBkLEHRXAfFNBHdMEuxwW7SBqirGPJ9qVSVnZA7UozrS2hhKGFVBjiw+OOZ2EUqLFwihvD+E53wf8xlYiLbAFZTIYRvkwBvLjQ2xuK5YGSkUUguNnbErOt7bgndg7RW9j9utLFMAABCoZvkzsjnOrqhSWHFPGInClKd10NH-Rub526UW7qHTZQJno4XwCjdAKs5pawRnamQh5AYgx4fDMhID9CJ7RynrHLMCcV31NTjK7DujqwWhgHWBsvTfnHpkQ0PwWguXKg5NgITTUhXyEvdA69LIxMvIWWOxxxbC0KoAm45+34VWvATQUnqoxemGqxl4MN5rLUmYVIgEMujsAhsIPkQoMB3XmE9RO6mtN6aMz6MYG+Sbvw6c05ZB5NkQDcDwO8sLUBvmVvsdWraMBQvWeUQO0F-KZAV2ZIYPRYrcwpcbTA6QGWMRVn0e8O0fKm3pcrll-syDA0aG-VK-zXVrOKsC1q4jY8iXzto5DWeYxelAA
