# microservices-betting-api
Betting API for e-sports games made with Spring Boot in microservices architecture.

# Run with Kubernetes
To run with Kubernetes locally you need Minikube:
 - Start Minikube cluster: `minikube start --cpus 4 --memory 8000 --vm-driver=hyperv`
 - Get Minikube IP: `minikube ip`
 - Add entries to hosts file for Ingress configuration as follows:
    ```
    <MINIKUBE_IP> auth.me
    <MINIKUBE_IP> betting-esport.com
    ```
- Enable ingress-nginx Ingress controller implementation: `minikube addons enable ingress`
- Start k8s components: `kubectl apply -f .\kubernetes --recursive`

To access k8s component from local IP address: `kubectl port-forward --address <LOCAL_IP> <COMPONENT/NAME> <HOST_PORT>:<COMPONENT_PORT>`

Example:
- `kubectl port-forward --address 192.168.1.111 service/keycloak 8080:80`
- `kubectl port-forward --address 192.168.1.111 service/betting-app-ui 80:80`

# Run with Docker
 - Add entry to hosts file `'127.0.0.1 keycloak'`
 - Run: `docker-compose up -d`