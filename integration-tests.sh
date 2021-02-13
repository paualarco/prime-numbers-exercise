

sbt 'primeNumbersServer/docker:publishLocal'
sbt 'proxy/docker:publishLocal'

docker-compose -f ./docker-compose.yml up proxy prime-numbers-server

sleep(5)