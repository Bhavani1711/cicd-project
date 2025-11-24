/*pipeline {
    agent any

    // These variables will be used in the stages
    environment {
        // ---!!! YOU MUST EDIT THESE VALUES !!!---
        AWS_ACCOUNT_ID    = "904233105350"       // Find this in your AWS console
        AWS_REGION        = "ap-south-1"                 // The region for your ECR/EC2
        ECR_REPO_NAME     = "asb/dockerized-my-app"                // The ECR repo name you will create
        TARGET_EC2_IP     = "15.206.224.9" // The IP of your deployment server
        TARGET_EC2_USER   = "ubuntu"                    // We are using an Ubuntu server
        // ---!!! -----------------------------!!!---

        // These are calculated automatically
        ECR_REPO_URL      = "${AWS_ACCOUNT_ID}.dkr.ecr.${AWS_REGION}.amazonaws.com"
        IMAGE_NAME        = "${ECR_REPO_NAME}:${env.BUILD_NUMBER}"
        IMAGE_NAME_LATEST = "${ECR_REPO_NAME}:latest"
    }

    stages {
        stage('1. Checkout Code') {
            steps {
                echo 'Checking out code from GitHub...'
                checkout scm
            }
        }
		

        stage('2. Build Application') {
		    
            steps {
                echo 'Installing Node.js dependencies...'
                sh 'npm install'
            }
        }

        stage('3. Run Tests') {
            steps {
                echo 'Running tests...'
                sh 'npm test'
            }
        }

        stage('4. Build Docker Image') {
		    
            steps {
                echo "Building Docker image: ${IMAGE_NAME}"
				sh 'mkdir -p /tmp/.docker'  
				dockerImage = docker.build(repoUri + ":$IMAGE_NAME")
                #sh "docker build -t ${IMAGE_NAME} ."
                #sh "docker tag ${IMAGE_NAME} ${IMAGE_NAME_LATEST}"
            }
        }

        stage('5. Push to AWS ECR') {
            steps {
                echo 'Logging in to AWS ECR...'
                // Jenkins uses its attached IAM Role for credentials
                sh "aws ecr get-login-password --region ${AWS_REGION} | docker login --username AWS --password-stdin ${ECR_REPO_URL}"

                echo "Pushing image ${ECR_REPO_URL}/${IMAGE_NAME}..."
                sh "docker tag ${IMAGE_NAME} ${ECR_REPO_URL}/${IMAGE_NAME}"
                sh "docker push ${ECR_REPO_URL}/${IMAGE_NAME}"

                echo "Pushing image ${ECR_REPO_URL}/${IMAGE_NAME_LATEST}..."
                sh "docker tag ${IMAGE_NAME_LATEST} ${ECR_REPO_URL}/${IMAGE_NAME_LATEST}"
                sh "docker push ${ECR_REPO_URL}/${IMAGE_NAME_LATEST}"
            }
        }

        stage('6. Deploy to EC2') {
            steps {
                echo "Deploying application to ${TARGET_EC2_IP}..."
                // 'target-ec2-ssh' is the Credential ID we will create in Jenkins
                sshagent(credentials: ['target-ec2-ssh']) {
                    sh """
                        ssh -o StrictHostKeyChecking=no ${TARGET_EC2_USER}@${TARGET_EC2_IP} '

                            # Log in to ECR on the target machine (uses its own IAM Role)
                            aws ecr get-login-password --region ${AWS_REGION} | docker login --username AWS --password-stdin ${ECR_REPO_URL}

                            # Stop and remove the old container, if it exists
                            docker stop my-web-app || true
                            docker rm my-web-app || true

                            # Pull the new 'latest' image from ECR
                            docker pull ${ECR_REPO_URL}/${IMAGE_NAME_LATEST}

                            # Run the new container, mapping port 80 (public) to 8080 (app)
                            docker run -d -p 80:8080 --name my-web-app ${ECR_REPO_URL}/${IMAGE_NAME_LATEST}
                        '
                    """
                }
            }
        }
    }

    // This block runs after all stages
    post {
        always {
            echo 'Cleaning up the Jenkins workspace...'
            // Removes the built docker images from the Jenkins server to save space
            sh "docker rmi ${IMAGE_NAME} || true"
            sh "docker rmi ${IMAGE_NAME_LATEST} || true"
        }
        success {
            // You can configure the 'Mailer' plugin to send an email
            echo "Pipeline Succeeded! App is live at http://${TARGET_EC2_IP}"
        }
        failure {
            echo 'Pipeline Failed.'
        }
    }
}

pipeline {

     environment {
        // ---!!! YOU MUST EDIT THESE VALUES !!!---
        AWS_ACCOUNT_ID    = "904233105350"       // Find this in your AWS console
        AWS_REGION        = "ap-south-1"                 // The region for your ECR/EC2
        ECR_REPO_NAME     = "asb/dockerized-my-app"                // The ECR repo name you will create
        TARGET_EC2_IP     = "15.206.224.9" // The IP of your deployment server
        TARGET_EC2_USER   = "ubuntu"                    // We are using an Ubuntu server
        // ---!!! -----------------------------!!!---

        // These are calculated automatically
        ECR_REPO_URL      = "${AWS_ACCOUNT_ID}.dkr.ecr.${AWS_REGION}.amazonaws.com"
        IMAGE_NAME        = "${ECR_REPO_NAME}:${env.BUILD_NUMBER}"
        IMAGE_NAME_LATEST = "${ECR_REPO_NAME}:latest"
    }
    
	agent{
	  docker{
	       image 'node:24.11.1-alpine3.22'
		   args '-u root:root'
	  }
	}
	
	
	stages {
        stage('1.Installing dependencies') {		    
            steps {
                sh '''
                     #set -x  # Print each command
                     export npm_config_cache="${WORKSPACE}/.npm-cache"
                     npm config list
                     #npm install --verbose --loglevel=silly 2>&1 | tee npm-install.log
					 npm install
                 '''
            }
        }
		stage('2. Run Tests') {
            steps {
                echo 'Running tests...'
                sh 'npm test'
            }
        }
		stage('3. Build Docker Image') {
		    agent {
                docker {
                    image 'docker:latest'
                    args '-v /var/run/docker.sock:/var/run/docker.sock'  // Mount Docker socket
                }
            }
            steps {                
				script {
                    echo 'Building Docker Image from Dockerfile...'
                    sh 'mkdir -p /tmp/.docker'  
                    dockerImage = docker.build(repoUri + ":$IMAGE_NAME")
                }
            }
        }
	}
}*/

pipeline {
    agent any  // No global agent, each stage will define its own
	
    environment {
        DOCKER_CONFIG = '/tmp/.docker'  // Set to a directory with write access		
		AWS_ACCOUNT_ID    = "904233105350"       // Find this in your AWS console
        AWS_REGION        = "ap-south-1"                 // The region for your ECR/EC2
        ECR_REPO_NAME     = "asb/dockerized-my-app"                // The ECR repo name you will create
        TARGET_EC2_IP     = "15.206.224.9" // The IP of your deployment server
        TARGET_EC2_USER   = "ubuntu"    
        repoUri = "${AWS_ACCOUNT_ID}.dkr.ecr.${AWS_REGION}.amazonaws.com/${ECR_REPO_NAME}"
        repoRegistryUrl = "https://${AWS_ACCOUNT_ID}.dkr.ecr.${AWS_REGION}.amazonaws.com"
        registryCreds = 'ecr:ap-south-1:awscreds'
		ECR_REPO_URL      = "${AWS_ACCOUNT_ID}.dkr.ecr.${AWS_REGION}.amazonaws.com"
        IMAGE_NAME        = "${ECR_REPO_NAME}:${env.BUILD_NUMBER}"
        IMAGE_NAME_LATEST = "${ECR_REPO_NAME}:latest"   
        CONTAINER_NAME = 'my-web-app'
        HOST_PORT = '80'
        CONTAINER_PORT = '9090'	
        SSH_CREDENTIALS_ID='ec2-ssh-key'		
    }
	
    stages {	   
        stage('Docker Test') {
            agent {
                docker {
                    image 'docker:latest'
                    args '-v /var/run/docker.sock:/var/run/docker.sock'  // Mount Docker socket
                }
            }
            steps {
                script {
                    sh 'docker ps'
                }
            }
        }

        stage('Build Docker Image') {
            agent {
                docker {
                    image 'docker:latest'
                    args '-v /var/run/docker.sock:/var/run/docker.sock'  // Mount Docker socket
                }
            }
            steps {
                script {
                    echo 'Building Docker Image from Dockerfile...'
                    sh 'mkdir -p /tmp/.docker'  // Ensure the directory exists
                    dockerImage = docker.build(repoUri + ":$BUILD_NUMBER")
                }
            }
        }

        stage('Push Docker Image to ECR') {
            agent {
                docker {
                    image 'docker:latest'
                    args '-v /var/run/docker.sock:/var/run/docker.sock'  // Mount Docker socket
                }
            }
            steps {
                script {
                    echo "Pushing Docker Image to ECR..."
                    docker.withRegistry(repoRegistryUrl, registryCreds) {
                        dockerImage.push("$BUILD_NUMBER")
                        dockerImage.push('latest')
                    }
                }
            }
        }          
        
	stage('Deploy to EC2') {
            steps {
                script {
                    // Use SSH credentials from Jenkins
                    sshagent(credentials: [SSH_CREDENTIALS_ID]) {
                        sh """
                            ssh -o StrictHostKeyChecking=no ${TARGET_EC2_USER}@${TARGET_EC2_IP} '
                                set -e
                                
                                echo "========================================="
                                echo "Step 1: AWS ECR Login"
                                echo "========================================="
                                aws ecr get-login-password --region ${AWS_REGION} | docker login --username AWS --password-stdin ${ECR_REPO_URL}
                                
                                echo "========================================="
                                echo "Step 2: Stop and Remove Old Container"
                                echo "========================================="
                                docker stop ${CONTAINER_NAME} 2>/dev/null || true
                                docker rm ${CONTAINER_NAME} 2>/dev/null || true
                                
                                echo "========================================="
                                echo "Step 3: Pull Latest Image from ECR"
                                echo "========================================="
                                docker pull ${IMAGE_LATEST}
                                
                                echo "========================================="
                                echo "Step 4: Run New Container"
                                echo "========================================="
                                docker run -d \
                                    --name ${CONTAINER_NAME} \
                                    -p ${HOST_PORT}:${CONTAINER_PORT} \
                                    --restart unless-stopped \
                                    ${IMAGE_LATEST}
                                
                                echo "========================================="
                                echo "Step 5: Verify Deployment"
                                echo "========================================="
                                docker ps | grep ${CONTAINER_NAME}
                                
                                echo "========================================="
                                echo "Step 6: Cleanup Old Images"
                                echo "========================================="
                                docker image prune -f
                                
                                echo "Deployment completed successfully!"
                            '
                        """
                    }
                }
            }       
       }  
    }	   
    
}