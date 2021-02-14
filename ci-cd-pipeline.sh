
# checks that the formatting is correct
sbt scalafmtCheckAll

# runs unit tests for proxy
sbt proxy/test

# runs unit tests for prime numbers server
sbt primeNumbersServer/test

# Builds docker images
sbt 'primeNumbersServer/docker:publishLocal'
sbt 'proxy/docker:publishLocal'
# Spin up docker services
docker-compose -f ./docker-compose.yml up -d proxy prime-numbers-server
# Runs integration tests
sleep 5
sbt 'integrationTests/test'