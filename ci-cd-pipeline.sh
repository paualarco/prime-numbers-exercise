
echo "Checking scala format."
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

sleep 35

echo "Proxy docker container logs:"
docker logs proxy

echo "Prime numbers docker container logs:"
docker logs prime-numbers-server

echo "Running integration tests..."
sbt 'integrationTests/test'

echo "Tests finished! Releasing docker resources..."
docker rm $(docker stop $(docker ps -aq))