cd ../
mvn package -D"maven.test.skip=true"
docker build -f . -t dearmann/api-gateway:1.0 ./api-gateway
docker build -f . -t dearmann/bet-service:1.0 ./bet-service
docker build -f . -t dearmann/comment-service:1.0 ./comment-service
docker build -f . -t dearmann/discovery-server:1.0 ./discovery-server
docker build -f . -t dearmann/match-service:1.0 ./match-service
docker build -f . -t dearmann/rate-service:1.0 ./rate-service
docker build -f . -t dearmann/user-service:1.0 ./user-service