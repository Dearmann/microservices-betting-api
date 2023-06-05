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
- Start k8s components: `kubectl apply -f .\kubernetes --recursive` + `kubectl apply -f .\.aws-secret.yaml`

To get access to Minikube cluster from localhost: 
`kubectl port-forward --address 127.0.0.1 service/betting-app-ui 8888:80` (HOST_PORT:POD_PORT)

`kubectl port-forward --address 192.168.1.111 service/betting-app-ui 30080:80`

`kubectl port-forward --address 192.168.1.111 service/keycloak 30088:8080`

# Run with Docker
 - Add entry to hosts file `'127.0.0.1 keycloak'`
 - Run: `docker-compose up -d`