cd ../
mvn package -D"maven.test.skip=true"
docker build -f . -t api-gateway:1.0 ./api-gateway
docker build -f . -t bet-service:1.0 ./bet-service
docker build -f . -t comment-service:1.0 ./comment-service
docker build -f . -t discovery-server:1.0 ./discovery-server
docker build -f . -t match-service:1.0 ./match-service
docker build -f . -t rate-service:1.0 ./rate-service
docker build -f . -t user-service:1.0 ./user-service