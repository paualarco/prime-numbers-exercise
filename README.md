## Scala Exercise

[![ci-cd](https://github.com/paualarco/prime-numbers-exercise/workflows/build/badge.svg)](https://github.com/paualarco/prime-numbers-exercise/actions)

Hi **Dixaterians**! **Welcome** to my implementation of the `Prime Numbers` scala exercise!

Below you can find a summary of the personal process followed throughout the implementation of the exercise,
some _instructions_ to execute and test locally and finally a list of _future improvements_.

### Design choices

Initially, I was planning to use _ScalaPb_, _Monix_ for grpc communication between the two microservices, and _http4s_ for the Proxy's _REST API_.
Then I realized that the exercise required to be designed in a streaming request fashion on the server side, 
since this way we ensure a good performance of the application in cases where for example the request for prime numbers has to return an extensive list.
Therefore, I found an example of a _streaming http response_ in the _akka_ documentation which would 
fit very well to the use case, so I ended up using **akka-http**, **akka-grpc** and **akka-streams*.

### Project structure and implementation 

The project structure has been internally divided in **4** different **sbt submodules**:

- **Proxy**: The microservices that implements a minimal REST API that exposes a **Get /prime/{n}** that returns all the list of subsequent prime numbers.
  The _http_ port as well as the grpc endpoint for requesting the prime numbers are configurable from the _HOCOON_ [application.conf](/proxy/src/main/resources/application.conf).
  Its _unit tests_ are declared under the _test_ folder and can be executed with following sbt task: `sbt 'primeNumbersServer/test'`.
  
- **Prime Numbers Server**: The service that implements the gRPC server that given a single limit number, it returns in streaming all its subsequents 
  prime numbers. I decided to use the _Monix_ **Observable** data type instead of _Akka's_ **Source** for implementing the logic of emitting only its prime numbers. 
  The both reactive implementations can be easily converted.
  Finally, you can run its unit tests with the following sbt task: `sbt 'primeNumbersServer/test'`.
  
- **Common**: A module with the purpose to share code and utilities between the two microservices, in this case used for defining the protobuf protocol/schema.

- **Integration Test**: This module runs a minimal **end to end** tests that checks the communication between the two microservices. 
  Thus, it requires the microservices to be running before executing the tests with: `sbt 'integrationTests/test'`. 

### Test instructions
As described in previous section, there are unit tests defined on the two microservices and integration tests on a separate module. 
In addition, I have put all those steps in a [single shell script](ci-cd-pipeline.sh) so it can be executed all at once, nevertheless, 
the script is also running on the [ci pipeline with github actions](https://github.com/paualarco/prime-numbers-exercise/actions).

### Future improvements
- Improve unit tests with property based testing.
- Create a proper UI.
- Include project subdomain in the package name.
