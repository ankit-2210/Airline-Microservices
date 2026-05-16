pipeline {

    agent any

    environment {
        USER_IMAGE = "user-service"
        LOCATION_IMAGE = "location-service"
    }

    stages {

        // =========================
        // Checkout Source
        // =========================
        stage('Checkout Code') {
            steps {
                checkout scm
            }
        }

        // =========================
        // Maven Build
        // =========================
        stage('Build Maven Project') {
            steps {
                sh 'mvn clean package -DskipTests'
            }
        }

        // =========================
        // Build UserService Image
        // =========================
        stage('Build UserService Docker Image') {
            steps {

                sh '''
                docker rmi $USER_IMAGE || true

                docker build \
                --no-cache \
                --build-arg JAR_FILE=services/UserService/target/userservice-0.0.1-SNAPSHOT.jar \
                -t $USER_IMAGE .
                '''
            }
        }

        // =========================
        // Build LocationService Image
        // =========================
        stage('Build LocationService Docker Image') {
            steps {

                sh '''
                docker rmi $LOCATION_IMAGE || true

                docker build \
                --no-cache \
                --build-arg JAR_FILE=services/LocationService/target/locationservice-0.0.1-SNAPSHOT.jar \
                -t $LOCATION_IMAGE .
                '''
            }
        }

        // =========================
        // Stop & Remove Old Containers
        // =========================
        stage('Cleanup Old Containers') {
            steps {

                sh 'docker rm -f user-service || true'
                sh 'docker rm -f location-service || true'
            }
        }

        // =========================
        // Run UserService
        // =========================
        stage('Run UserService') {
            steps {

                sh '''
                docker run -d \
                --restart unless-stopped \
                --name user-service \
                -p 5004:5004 \
                $USER_IMAGE
                '''
            }
        }

        // =========================
        // Run LocationService
        // =========================
        stage('Run LocationService') {
            steps {

                sh '''
                docker run -d \
                --restart unless-stopped \
                --name location-service \
                -p 5005:5005 \
                $LOCATION_IMAGE
                '''
            }
        }

        // =========================
        // Verify Containers
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