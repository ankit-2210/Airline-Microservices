
pipeline {
    agent any

    options {
        timestamps()
        disableConcurrentBuilds()
    }

    environment {
        IMAGE_TAG = 'local'
    }

    stages {
        stage('Checkout') {
            steps {
                checkout scm
            }
        }

        stage('Verify Tools') {
            steps {
                sh '''
                    java -version
                    mvn -version
                    docker --version
                '''
            }
        }

        stage('Build and Test Backend') {
            steps {
                sh 'mvn -B clean verify -DskipTests'
            }
        }

        stage('Build Cloud Images') {
            steps {
                script {
                    def cloudServices = [
                        [
                            name: 'eureka-server',
                            jar: 'cloud/eurekaserver/target/eurekaserver-0.0.1-SNAPSHOT.jar'
                        ],
                        [
                            name: 'config-server',
                            jar: 'cloud/configserver/target/configserver-0.0.1-SNAPSHOT.jar'
                        ],
                        [
                            name: 'api-gateway',
                            jar: 'cloud/apigateway/target/apigateway-0.0.1-SNAPSHOT.jar'
                        ]
                    ]

                    cloudServices.each { svc ->
                        sh """
                            test -f '${svc.jar}'
                            docker build \
                              --build-arg JAR_FILE='${svc.jar}' \
                              -t '${svc.name}:${IMAGE_TAG}' .
                        """
                    }
                }
            }
        }

        stage('Build Business Service Images') {
            steps {
                script {
                    def services = [
                        'AdminService',
                        'AirlineService',
                        'AIService',
                        'BaggageService',
                        'BookingService',
                        'CheckInService',
                        'FareService',
                        'FlightService',
                        'LocationService',
                        'LoyaltyService',
                        'NotificationService',
                        'PaymentService',
                        'RefundService',
                        'SeatService',
                        'UserService'
                    ]

                    services.each { serviceName ->
                        def artifactId = serviceName.toLowerCase()

                        def jarPath =
                            "services/${serviceName}/target/${artifactId}-0.0.1-SNAPSHOT.jar"

                        def imageName =
                            serviceName.replaceAll(
                                /([a-z])([A-Z])/,
                                '$1-$2'
                            ).toLowerCase()

                        sh """
                            test -f '${jarPath}'
                            docker build \
                              --build-arg JAR_FILE='${jarPath}' \
                              -t '${imageName}:${IMAGE_TAG}' .
                        """
                    }
                }
            }
        }

        stage('Build Frontend Image') {
            when {
                expression {
                    fileExists('airlines/Dockerfile')
                }
            }
            steps {
                sh '''
                    docker build \
                      -t airline-frontend:${IMAGE_TAG} \
                      ./airlines
                '''
            }
        }

        stage('List Images') {
            steps {
                sh 'docker image ls'
            }
        }
    }

    post {
        success {
            echo 'Backend build and Docker image creation completed.'
        }

        failure {
            echo 'Pipeline failed. Check the stage logs.'
        }
    }
}
