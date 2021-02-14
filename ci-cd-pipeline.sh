
echo "Checking scala format correctness."
sbt scalafmtCheckAll

echo "Runs unit tests for proxy"
sbt proxy/test

echo "Running unit tests for prime numbers server"
sbt primeNumbersServer/test

echo "Building docker images."
sbt 'primeNumbersServer/docker:publishLocal'
sbt 'proxy/docker:publishLocal'

echo "Spinning up docker services."
docker-compose -f ./docker-compose.yml up -d proxy prime-numbers-server

echo "Running integration tests..."
sleep 15
sbt 'integrationTests/test'