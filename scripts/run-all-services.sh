git-bash -c 'cd ../discovery-server;mvn spring-boot:run -Dmaven.test.skip=true' &
git-bash -c 'cd ../bet-service;mvn spring-boot:run -Dmaven.test.skip=true' &
git-bash -c 'cd ../comment-service;mvn spring-boot:run -Dmaven.test.skip=true' &
git-bash -c 'cd ../rate-service;mvn spring-boot:run -Dmaven.test.skip=true' &
git-bash -c 'cd ../match-service;mvn spring-boot:run -Dmaven.test.skip=true' &
git-bash -c 'cd ../user-service;mvn spring-boot:run -Dmaven.test.skip=true' &
git-bash -c 'cd ../api-gateway;mvn spring-boot:run -Dmaven.test.skip=true' &