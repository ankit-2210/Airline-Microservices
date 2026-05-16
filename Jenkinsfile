pipeline {

    agent any

    environment {
        USER_IMAGE = "user-service"
        LOCATION_IMAGE = "location-service"
    }

    stages {

        stage('Checkout Code') {
            steps {
                checkout scm
            }
        }

        stage('Build Maven Project') {
            steps {
                sh 'mvn clean package -DskipTests'
            }
        }

        // =========================
        // Build User Service Image
        // =========================
        stage('Build UserService Image') {
            steps {
                sh """
                    docker rm -f user-service || true
                    docker rmi $USER_IMAGE || true

                    docker build --no-cache \
                    --build-arg JAR_FILE=services/UserService/target/userservice-0.0.1-SNAPSHOT.jar \
                    -t $USER_IMAGE .
                """
            }
        }

        // =========================
        // Build Location Service Image
        // =========================
        stage('Build LocationService Image') {
            steps {
                sh """
                    docker rm -f location-service || true
                    docker rmi $LOCATION_IMAGE || true

                    docker build --no-cache \
                    --build-arg JAR_FILE=services/LocationService/target/locationservice-0.0.1-SNAPSHOT.jar \
                    -t $LOCATION_IMAGE .
                """
            }
        }

        // =========================
        // Run UserService
        // =========================
        stage('Run UserService') {
            steps {
                sh """
                    docker run -d \
                    --restart unless-stopped \
                    --name user-service \
                    -p 5005:5005 \
                    $USER_IMAGE
                """
            }
        }

        // =========================
        // Run LocationService
        // =========================
        stage('Run LocationService') {
            steps {
                sh """
                    docker run -d \
                    --restart unless-stopped \
                    --name location-service \
                    -p 5004:5004 \
                    $LOCATION_IMAGE
                """
            }
        }

        // =========================
        // Verify
        // =========================
        stage('Verify Running Containers') {
            steps {
                sh 'docker ps'
            }
        }
    }

    post {
        success {
            echo 'Deployment Successful!'
        }

        failure {
            echo 'Deployment Failed!'
        }
    }
}