pipeline{
    agent any

     environment {
        USER_IMAGE = "user-service"
        LOCATION_IMAGE = "location-service"
     }

     stages{
        // =========================
        // Pull Latest Code
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
                    docker build \
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
                    docker build \
                    --build-arg JAR_FILE=services/LocationService/target/locationservice-0.0.1-SNAPSHOT.jar \
                    -t $LOCATION_IMAGE .
                    '''
            }
        }

        // =========================
        // Stop Old Containers
        // =========================
        stage('Stop Old Containers') {
            steps {
                sh 'docker stop user-service || true'
                sh 'docker rm user-service || true'

                sh 'docker stop location-service || true'
                sh 'docker rm location-service || true'
            }
        }

        // =========================
        // Run UserService
        // =========================
        stage('Run UserService') {
            steps {
                sh '''
                    docker run -d \
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
                    --name location-service \
                    -p 5005:5005 \
                    $LOCATION_IMAGE
                    '''
            }
        }
     }

     post{
        success{
            echo 'Deployment Successful!'
        }
        failure{
            echo 'Deployment Failed!'
        }
     }

}